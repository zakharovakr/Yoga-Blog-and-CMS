package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CommentDaoDBTest {
    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    BlogpostDao blogpostDao;

    @Autowired
    HashtagDao hashtagDao;

    @Autowired
    CommentDao commentDao;

    @BeforeEach
    void setUp() {
        List<Role> roleList = roleDao.readAllRoles();
        for (Role role : roleList) {
            roleDao.deleteRole(role.getRoleId());
        }
        List<User> userList = userDao.readAllUsers();
        for (User user : userList) {
            userDao.deleteUser(user.getUserId());
        }
        List<Blogpost> blogpostList = blogpostDao.readAllBlogposts();
        for (Blogpost blogpost : blogpostList) {
            blogpostDao.deleteBlogpost(blogpost.getBlogpostId());
        }
        List<Comment> commentList = commentDao.readAllComments();
        for (Comment comment : commentList) {
            commentDao.deleteComment(comment.getCommentId());
        }
        List<Hashtag> hashtagList = hashtagDao.readAllHashtags();
        for (Hashtag hashtag : hashtagList) {
            hashtagDao.deleteHashtag(hashtag.getHashtagId());
        }
    }

    @Test
    void createCommentAndReadById() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(role2);

        User user = new User();
        user.setUsername("username1");
        user.setPassword("password");
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setEnable(true);
        user.setEmail("test@gmail.com");
        user.setRoles(roleList);
        user = userDao.createUser(user);

        //hashtag
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = hashtagDao.readAllHashtags();

        //blogpost 1
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        blogpost.setTitle("my blogpost");
        blogpost.setContent("lorum ipsum..........");
        blogpost.setType("post");
        blogpost.setStatus("public");
        blogpost.setUser(user);
        blogpost.setPhotoFileName("filename");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost);
        comment = commentDao.createComment(comment);

        Comment fromDao = commentDao.readCommentById(comment.getCommentId());

        assertEquals(comment, fromDao);
    }

    @Test
    void readAllComments() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(role2);

        User user = new User();
        user.setUsername("username1");
        user.setPassword("password");
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setEnable(true);
        user.setEmail("test@gmail.com");
        user.setRoles(roleList);
        user = userDao.createUser(user);

        //hashtag
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = hashtagDao.readAllHashtags();

        //blogpost 1
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        blogpost.setTitle("my blogpost");
        blogpost.setContent("lorum ipsum..........");
        blogpost.setType("post");
        blogpost.setStatus("public");
        blogpost.setUser(user);
        blogpost.setPhotoFileName("filename");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        //blogpost 2
        Blogpost blogpost2 = new Blogpost();
        blogpost2.setTimePosted(LocalDateTime.parse("2020-01-01T12:40:30"));
        blogpost2.setTitle("my blogpost 2");
        blogpost2.setContent("lorum ipsum 2..........");
        blogpost2.setType("post");
        blogpost2.setStatus("public");
        blogpost2.setUser(user);
        blogpost2.setPhotoFileName("filename");
        blogpost2.setHashtags(hashtags);
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost);
        comment = commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment2.setContent("another comment");
        comment2.setUser(user);
        comment2.setBlogpost(blogpost2);
        comment2 = commentDao.createComment(comment2);

        List<Comment> fromDao = commentDao.readAllComments();
        assertEquals(2, fromDao.size());
    }

    @Test
    void updateComment() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(role2);

        User user = new User();
        user.setUsername("username1");
        user.setPassword("password");
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setEnable(true);
        user.setEmail("test@gmail.com");
        user.setRoles(roleList);
        user = userDao.createUser(user);

        //hashtag
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = hashtagDao.readAllHashtags();

        //blogpost 1
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        blogpost.setTitle("my blogpost");
        blogpost.setContent("lorum ipsum..........");
        blogpost.setType("post");
        blogpost.setStatus("public");
        blogpost.setUser(user);
        blogpost.setPhotoFileName("filename");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        //blogpost 2
        Blogpost blogpost2 = new Blogpost();
        blogpost2.setTimePosted(LocalDateTime.parse("2020-01-01T12:40:30"));
        blogpost2.setTitle("my blogpost 2");
        blogpost2.setContent("lorum ipsum 2..........");
        blogpost2.setType("post");
        blogpost2.setStatus("public");
        blogpost2.setUser(user);
        blogpost2.setPhotoFileName("filename");
        blogpost2.setHashtags(hashtags);
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost);
        comment = commentDao.createComment(comment);

        Comment fromDao = commentDao.readCommentById(comment.getCommentId());

        assertEquals(comment, fromDao);

        comment.setContent("something new");

        commentDao.updateComment(comment);

        assertNotEquals(fromDao, comment);
    }

    @Test
    void deleteComment() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(role2);

        User user = new User();
        user.setUsername("username1");
        user.setPassword("password");
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setEnable(true);
        user.setEmail("test@gmail.com");
        user.setRoles(roleList);
        user = userDao.createUser(user);

        //hashtag
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = hashtagDao.readAllHashtags();

        //blogpost 1
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        blogpost.setTitle("my blogpost");
        blogpost.setContent("lorum ipsum..........");
        blogpost.setType("post");
        blogpost.setStatus("public");
        blogpost.setUser(user);
        blogpost.setPhotoFileName("filename");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        //blogpost 2
        Blogpost blogpost2 = new Blogpost();
        blogpost2.setTimePosted(LocalDateTime.parse("2020-01-01T12:40:30"));
        blogpost2.setTitle("my blogpost 2");
        blogpost2.setContent("lorum ipsum 2..........");
        blogpost2.setType("post");
        blogpost2.setStatus("public");
        blogpost2.setUser(user);
        blogpost2.setPhotoFileName("filename");
        blogpost2.setHashtags(hashtags);
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost);
        comment = commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment2.setContent("another comment");
        comment2.setUser(user);
        comment2.setBlogpost(blogpost2);
        comment2 = commentDao.createComment(comment2);

        commentDao.deleteComment(comment2.getCommentId());

        List<Comment> fromDao = commentDao.readAllComments();
        assertEquals(1, fromDao.size());
        assertFalse(fromDao.contains(comment2));
    }

    @Test
    void getUserForComment() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(role2);

        User user = new User();
        user.setUsername("username1");
        user.setPassword("password");
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setEnable(true);
        user.setEmail("test@gmail.com");
        user.setRoles(roleList);
        user = userDao.createUser(user);

        //hashtag
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = hashtagDao.readAllHashtags();

        //blogpost 1
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        blogpost.setTitle("my blogpost");
        blogpost.setContent("lorum ipsum..........");
        blogpost.setType("post");
        blogpost.setStatus("public");
        blogpost.setUser(user);
        blogpost.setPhotoFileName("filename");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        //blogpost 2
        Blogpost blogpost2 = new Blogpost();
        blogpost2.setTimePosted(LocalDateTime.parse("2020-01-01T12:40:30"));
        blogpost2.setTitle("my blogpost 2");
        blogpost2.setContent("lorum ipsum 2..........");
        blogpost2.setType("post");
        blogpost2.setStatus("public");
        blogpost2.setUser(user);
        blogpost2.setHashtags(hashtags);
        blogpost2.setPhotoFileName("filename");
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost);
        comment = commentDao.createComment(comment);

        User userForComment = commentDao.getUserForComment(comment.getCommentId());

        assertEquals(user, userForComment);
    }

    @Test
    void getBlogpostForComment() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(role2);

        User user = new User();
        user.setUsername("username1");
        user.setPassword("password");
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setEnable(true);
        user.setEmail("test@gmail.com");
        user.setRoles(roleList);
        user = userDao.createUser(user);

        //hashtag
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = hashtagDao.readAllHashtags();

        //blogpost 1
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        blogpost.setTitle("my blogpost");
        blogpost.setContent("lorum ipsum..........");
        blogpost.setType("post");
        blogpost.setStatus("public");
        blogpost.setUser(user);
        blogpost.setPhotoFileName("filename");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        //blogpost 2
        Blogpost blogpost2 = new Blogpost();
        blogpost2.setTimePosted(LocalDateTime.parse("2020-01-01T12:40:30"));
        blogpost2.setTitle("my blogpost 2");
        blogpost2.setContent("lorum ipsum 2..........");
        blogpost2.setType("post");
        blogpost2.setStatus("public");
        blogpost2.setUser(user);
        blogpost2.setHashtags(hashtags);
        blogpost2.setPhotoFileName("filename");
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost);
        comment = commentDao.createComment(comment);

        Blogpost blogpostForComment = commentDao.getBlogpostForComment(comment.getCommentId());

        assertEquals(blogpost, blogpostForComment);
    }
}