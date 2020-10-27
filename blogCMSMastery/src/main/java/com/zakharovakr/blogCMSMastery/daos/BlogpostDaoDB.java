package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import com.zakharovakr.blogCMSMastery.dtos.Hashtag;
import com.zakharovakr.blogCMSMastery.dtos.Role;
import com.zakharovakr.blogCMSMastery.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BlogpostDaoDB implements BlogpostDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Blogpost createBlogpost(Blogpost blogpost) {
        final String INSERT_BLOGPOST = "INSERT INTO blogpost (timePosted, title, type, status, content, photoFileName, userId) VALUES (?,?,?,?,?,?,?)";
        jdbc.update(INSERT_BLOGPOST, blogpost.getTimePosted(), blogpost.getTitle(), blogpost.getType(), blogpost.getStatus(), blogpost.getContent(), blogpost.getPhotoFileName(), blogpost.getUser().getUserId());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        blogpost.setBlogpostId(newId);

        insertIntoBlogpostTag(blogpost);

        return blogpost;
    }

    @Override
    public List<Blogpost> readAllBlogposts() {
        final String SELECT_ALL_BLOGPOSTS = "SELECT * FROM blogpost";
        List<Blogpost> blogpostList = jdbc.query(SELECT_ALL_BLOGPOSTS, new BlogpostMapper());
        associateUserBlogpost(blogpostList);
        associateBlogpostTag(blogpostList);

        return blogpostList;
    }

    @Override
    public Blogpost readBlogpostById(int id) {
        final String SELECT_BLOGPOST_BY_ID = "SELECT * FROM blogpost WHERE blogpostId = ?";
        Blogpost blogpost = jdbc.queryForObject(SELECT_BLOGPOST_BY_ID, new BlogpostMapper(), id);

        blogpost.setUser(getUserForBlogpost(blogpost.getBlogpostId()));
        blogpost.setHashtags(getTagsForBlogpost(blogpost.getBlogpostId()));

        return blogpost;
    }

    @Override
    @Transactional
    public void updateBlogpost(Blogpost blogpost) {
        final String UPDATE_BLOGPOST = "UPDATE blogpost SET " +
                "timePosted = ?, " +
                "title = ?, " +
                "type = ?, " +
                "status = ?, " +
                "PhotoFileName = ?, " +
                "content = ?, " +
                "userId = ? " +
                "WHERE blogpostId = ?";
        jdbc.update(UPDATE_BLOGPOST, blogpost.getTimePosted(), blogpost.getTitle(), blogpost.getType(), blogpost.getStatus(), blogpost.getPhotoFileName(), blogpost.getContent(), blogpost.getUser().getUserId(), blogpost.getBlogpostId());

        final String DELETE_BLOGPOST_TAG = "DELETE FROM blogpost_hashtag WHERE blogpostId = ?";
        jdbc.update(DELETE_BLOGPOST_TAG, blogpost.getBlogpostId());

        insertIntoBlogpostTag(blogpost);

    }

    @Override
    @Transactional
    public void deleteBlogpost(int id) {
        final String DELETE_BLOGPOST_TAG = "DELETE FROM blogpost_hashtag WHERE blogpostId = ?";
        jdbc.update(DELETE_BLOGPOST_TAG, id);

        final String DELETE_COMMENT = "DELETE FROM comment WHERE blogpostId = ?";
        jdbc.update(DELETE_COMMENT, id);

        final String DELETE_BLOGPOST = "DELETE FROM blogpost WHERE blogpostId = ?";
        jdbc.update(DELETE_BLOGPOST, id);
    }

    private void insertIntoBlogpostTag (Blogpost blogpost) {
        for(Hashtag tag : blogpost.getHashtags()) {
            final String INSERT_BLOGPOST_TAG = "INSERT INTO blogpost_hashtag (blogpostId, hashtagId) VALUES (?,?)";
            jdbc.update(INSERT_BLOGPOST_TAG, blogpost.getBlogpostId(), tag.getHashtagId());
        }
    }

    @Override
    public User getUserForBlogpost(int blogpostId) {
        final String SELECT_USER_BY_BLOGPOST_ID = "SELECT u.* FROM user u " +
                "JOIN blogpost b ON u.userId = b.userId " +
                "WHERE b.blogpostId = ?";
        User user = jdbc.queryForObject(SELECT_USER_BY_BLOGPOST_ID, new UserDaoDB.UserMapper(), blogpostId);
        user.setRoles(getRoleForUser(user.getUserId()));
        return user;
    }

    private void associateUserBlogpost(List<Blogpost> blogpostList) {
        for (Blogpost blogpost : blogpostList) {
            User user = getUserForBlogpost(blogpost.getBlogpostId());
            blogpost.setUser(user);
        }
    }

    @Override
    public List<Hashtag> getTagsForBlogpost(int blogpostId) {
        final String SELECT_TAG_BY_BLOGPOST_ID = "SELECT h.* FROM hashtag h " +
                "JOIN blogpost_hashtag bh ON h.hashtagId = bh.hashtagId " +
                "WHERE bh.blogpostId = ?";
        return jdbc.query(SELECT_TAG_BY_BLOGPOST_ID, new HashtagDaoDB.HashtagMapper(), blogpostId);
    }

    private void associateBlogpostTag(List<Blogpost> blogpostList) {
        for (Blogpost blogpost : blogpostList) {
            List<Hashtag> tagList = getTagsForBlogpost(blogpost.getBlogpostId());
            blogpost.setHashtags(tagList);
        }
    }

    public List<Role> getRoleForUser(int userId) {
        final String SELECT_ROLE_BY_USER_ID = "SELECT r.* FROM role r " +
                "JOIN user_role ur ON r.roleId = ur.roleId " +
                "WHERE ur.userId = ?";
        return jdbc.query(SELECT_ROLE_BY_USER_ID, new RoleDaoDB.RoleMapper(), userId);
    }

    @Override
    public List<Blogpost> getBlogpostByType(String type) {
        List<Blogpost> blogpostList = this.readAllBlogposts();
//        contentList = filterScheduleExpiredDate(contentList);
        List<Blogpost> typeList = new ArrayList();
        for (Blogpost blogpost : blogpostList) {
//
            if (blogpost.getType().equals(type) && blogpost.getStatus().equals("public")) {
                typeList.add(blogpost);
            }
        }
        return typeList;
    }

    @Override
    public List<Blogpost> getBlogpostByTag(int tagId) {
        final String SELECT_BLOGPOST_BY_TAG = "SELECT b.* FROM blogpost b " +
                "JOIN blogpost_hashtag bh ON bh.blogpostId = b.blogpostId " +
                "WHERE bh.hashtagId = ?";
        List<Blogpost> blogpostList =  jdbc.query(SELECT_BLOGPOST_BY_TAG, new BlogpostMapper(), tagId);
        associateBlogpostTag(blogpostList);
        associateUserBlogpost(blogpostList);

        //sorting out by status (only public)
        List<Blogpost> sortedPublicList = new ArrayList();
        for (Blogpost blogpost : blogpostList) {
            if (blogpost.getStatus().equals("public")) {
                sortedPublicList.add(blogpost);
            }
        }
        return sortedPublicList;
    }

    @Override
    public List<Blogpost> getBlogpostBySearchTitle(String searchText) {
        searchText = "%" + searchText + "%";
        final String SELECT_BLOGPOST_BY_SEARCH_TITLE = "SELECT * FROM blogpost WHERE title LIKE ?";
        List<Blogpost> blogpostList = jdbc.query(SELECT_BLOGPOST_BY_SEARCH_TITLE, new BlogpostMapper(), searchText);
        associateBlogpostTag(blogpostList);
        associateUserBlogpost(blogpostList);

        //sorting out by status (only public)
        List<Blogpost> sortedPublicList = new ArrayList();
        for (Blogpost blogpost : blogpostList) {
            if (blogpost.getStatus().equals("public")) {
                sortedPublicList.add(blogpost);
            }
        }
        return sortedPublicList;
    }

    public static class BlogpostMapper implements RowMapper<Blogpost> {
        @Override
        public Blogpost mapRow(ResultSet resultSet, int i) throws SQLException {
            Blogpost blogpost = new Blogpost();

            blogpost.setBlogpostId(resultSet.getInt("blogpostId"));
            blogpost.setTimePosted(resultSet.getTimestamp("timePosted").toLocalDateTime());
            blogpost.setTitle(resultSet.getString("title"));
            blogpost.setType(resultSet.getString("type"));
            blogpost.setStatus(resultSet.getString("status"));
            blogpost.setPhotoFileName(resultSet.getString("photoFileName"));
            blogpost.setContent(resultSet.getString("content"));

            return blogpost;
        }
    }
}
