package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//not really using this - only displaying comments under some posts
@Repository
public class CommentDaoDB implements CommentDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        final String INSERT_COMMENT = "INSERT INTO comment(timePosted, content, userId, blogpostId) VALUES (?,?,?,?)";
        jdbc.update(INSERT_COMMENT, comment.getTimePosted(), comment.getContent(), comment.getUser().getUserId(), comment.getBlogpost().getBlogpostId());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        comment.setCommentId(newId);

        return comment;
    }

    @Override
    public List<Comment> readAllComments() {
        final String SELECT_ALL_COMMENT = "SELECT * FROM comment";
        List<Comment> commentList = jdbc.query(SELECT_ALL_COMMENT, new CommentMapper());

        associateUserComment(commentList);
        associateBlogpostComment(commentList);

        return commentList;
    }

    @Override
    public Comment readCommentById(int id) {
        final String SELECT_COMMENT_BY_ID = "SELECT * FROM comment WHERE commentId = ?";
        Comment comment = jdbc.queryForObject(SELECT_COMMENT_BY_ID, new CommentMapper(), id);

        comment.setUser(getUserForComment(comment.getCommentId()));
        comment.setBlogpost(getBlogpostForComment(comment.getCommentId()));

        return comment;
    }

    @Override
    @Transactional
    public void updateComment(Comment comment) {
        final String UPDATE_COMMENT = "UPDATE comment SET " +
                "timePosted = ?, " +
                "content = ?, " +
                "userId = ?, " +
                "blogpostId = ? " +
                "WHERE commentId = ?";
        jdbc.update(UPDATE_COMMENT,  comment.getTimePosted(), comment.getContent(), comment.getUser().getUserId(), comment.getBlogpost().getBlogpostId(), comment.getCommentId());
    }

    @Override
    @Transactional
    public void deleteComment(int id) {
        final String DELETE_COMMENT = "DELETE FROM comment WHERE commentId = ?";
        jdbc.update(DELETE_COMMENT, id);
    }

    @Override
    public User getUserForComment(int commentId) {
        final String SELECT_USER_BY_COMMENT_ID = "SELECT u.* FROM user u " +
                "JOIN comment c ON u.userId = c.userId " +
                "WHERE c.commentId = ?";
        User user = jdbc.queryForObject(SELECT_USER_BY_COMMENT_ID, new UserDaoDB.UserMapper(), commentId);
        user.setRoles(getRoleForUser(user.getUserId()));
        return user;
    }

    private void associateUserComment(List<Comment> commentList) {
        for(Comment comment : commentList) {
            User user = getUserForComment(comment.getCommentId());
            comment.setUser(user);
        }
    }

    @Override
    public Blogpost getBlogpostForComment(int commentId) {
        final String SELECT_BLOGPOST_BY_COMMENT_ID = "SELECT b.* FROM blogpost b " +
                "JOIN comment cm ON b.blogpostId = cm.blogpostId " +
                "WHERE cm.commentId = ?";
        Blogpost blogpost = jdbc.queryForObject(SELECT_BLOGPOST_BY_COMMENT_ID, new BlogpostDaoDB.BlogpostMapper(), commentId);
        blogpost.setUser(getUserForBlogpost(blogpost.getBlogpostId()));
        blogpost.setHashtags(getTagsForBlogpost(blogpost.getBlogpostId()));
        return blogpost;
    }

    private void associateBlogpostComment(List<Comment> commentList) {
        for(Comment comment : commentList) {
            Blogpost blogpost = getBlogpostForComment(comment.getCommentId());
        }
    }

    public List<Role> getRoleForUser(int userId) {
        final String SELECT_ROLE_BY_USER_ID = "SELECT r.* FROM role r " +
                "JOIN user_role ur ON r.roleId = ur.roleId " +
                "WHERE ur.userId = ?";
        return jdbc.query(SELECT_ROLE_BY_USER_ID, new RoleDaoDB.RoleMapper(), userId);
    }

    public User getUserForBlogpost(int blogpostId) {
        final String SELECT_USER_BY_BLOGPOST_ID = "SELECT u.* FROM user u " +
                "JOIN blogpost b ON u.userId = b.userId " +
                "WHERE b.blogpostId = ?";
        User user = jdbc.queryForObject(SELECT_USER_BY_BLOGPOST_ID, new UserDaoDB.UserMapper(), blogpostId);
        user.setRoles(getRoleForUser(user.getUserId()));
        return user;
    }

    public List<Hashtag> getTagsForBlogpost(int blogpostId) {
        final String SELECT_TAG_BY_BLOGPOST_ID = "SELECT h.* FROM hashtag h " +
                "JOIN blogpost_hashtag bh ON h.hashtagId = bh.hashtagId " +
                "WHERE bh.blogpostId = ?";
        return jdbc.query(SELECT_TAG_BY_BLOGPOST_ID, new HashtagDaoDB.HashtagMapper(), blogpostId);
    }

    @Override
    public List<Comment> getCommentByBlogpostId(int blogpostId) {
        final String SELECT_COMMENT_BY_BLOGPOST = "SELECT * FROM comment WHERE blogpostId = ?";
        List<Comment> commentList =  jdbc.query(SELECT_COMMENT_BY_BLOGPOST, new CommentMapper(), blogpostId);
        associateBlogpostComment(commentList);
        associateUserComment(commentList);

        return commentList;
    }

    public class CommentMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
            Comment comment = new Comment();

            comment.setCommentId(resultSet.getInt("commentId"));
            comment.setTimePosted(resultSet.getTimestamp("timePosted").toLocalDateTime());
            comment.setContent(resultSet.getString("content"));

            return comment;
        }

    }
}
