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
class BlogpostDaoDBTest {
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
    void createBlogpostAndReadById() {
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

        //blogpost
        Blogpost blogpost = new Blogpost();
        blogpost.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        blogpost.setTitle("my blogpost");
        blogpost.setContent("lorum ipsum..........");
        blogpost.setType("post");
        blogpost.setStatus("public");
        blogpost.setUser(user);
        blogpost.setPhotoFileName("name");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        Blogpost fromDao = blogpostDao.readBlogpostById(blogpost.getBlogpostId());
        assertEquals(blogpost, fromDao);
    }

    @Test
    void readAllBlogposts() {
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
        blogpost.setPhotoFileName("name");
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
        blogpost2.setPhotoFileName("name");
        blogpost2.setHashtags(hashtags);
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        List<Blogpost> fromDao = blogpostDao.readAllBlogposts();
        assertEquals(2, fromDao.size());

    }

    @Test
    void updateBlogpost() {
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
        blogpost.setPhotoFileName("name");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        Blogpost fromDao = blogpostDao.readBlogpostById(blogpost.getBlogpostId());

        assertEquals(blogpost, fromDao);

        blogpost.setTitle("new title");
        blogpost.setContent("something different");
        blogpost.setType("new");

        blogpostDao.updateBlogpost(blogpost);

        assertNotEquals(fromDao, blogpost);
    }

    @Test
    void deleteBlogpost() {
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
        blogpost.setPhotoFileName("name");
        blogpost.setUser(user);
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        //blogpost 2
        Blogpost blogpost2 = new Blogpost();
        blogpost2.setTimePosted(LocalDateTime.parse("2020-01-01T12:40:30"));
        blogpost2.setTitle("my blogpost 2");
        blogpost2.setContent("lorum ipsum 2..........");
        blogpost2.setType("post");
        blogpost2.setPhotoFileName("name");
        blogpost2.setStatus("public");
        blogpost2.setUser(user);
        blogpost2.setHashtags(hashtags);
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost2);
        comment = commentDao.createComment(comment);

        blogpostDao.deleteBlogpost(blogpost2.getBlogpostId());

        List<Blogpost> blogposts = blogpostDao.readAllBlogposts();

        assertEquals(1, blogposts.size());
        assertTrue(blogposts.contains(blogpost));
        assertFalse(blogposts.contains(blogpost2));
        //also check that the comments get deleted
        List<Comment> comments = commentDao.readAllComments();
        assertEquals(0, comments.size());
        assertFalse(comments.contains(comment));
    }

    @Test
    void getUserForBlogpost() {
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
        blogpost.setPhotoFileName("name");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        User userForBlogpost = blogpostDao.getUserForBlogpost(blogpost.getBlogpostId());

        assertEquals(user, userForBlogpost);
    }

    @Test
    void getTagsForBlogpost() {
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
        blogpost.setPhotoFileName("name");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        List<Hashtag> tagsForBlogpost = blogpostDao.getTagsForBlogpost(blogpost.getBlogpostId());

        assertEquals(1, tagsForBlogpost.size());
        assertTrue(tagsForBlogpost.contains(hashtag));
    }

    @Test
    void getBlogpostByType() {
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
        blogpost.setPhotoFileName("name");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        List<Blogpost> blogpostsByType = blogpostDao.getBlogpostByType("post");
        assertEquals(1, blogpostsByType.size());
        assertTrue(blogpostsByType.contains(blogpost));
    }

    @Test
    void getBlogpostByTag() {
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
        blogpost.setPhotoFileName("name");
        blogpost.setHashtags(hashtags);
        blogpost = blogpostDao.createBlogpost(blogpost);

        List<Blogpost> blogpostsByTag = blogpostDao.getBlogpostByTag(hashtag.getHashtagId());

        assertEquals(1, blogpostsByTag.size());

    }

    @Test
    void getBlogpostBySearchTitle() {
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
        blogpost.setPhotoFileName("name");
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
        blogpost2.setPhotoFileName("name");
        blogpost2.setHashtags(hashtags);
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        List<Blogpost> blogpostsByTitle = blogpostDao.getBlogpostBySearchTitle("my");
        assertEquals(2, blogpostsByTitle.size());
        assertTrue(blogpostsByTitle.contains(blogpost));
        assertTrue(blogpostsByTitle.contains(blogpost2));

        List<Blogpost> blogpostsByTitle2 = blogpostDao.getBlogpostBySearchTitle("2");
        assertEquals(1, blogpostsByTitle2.size());
        assertFalse(blogpostsByTitle2.contains(blogpost));
        assertTrue(blogpostsByTitle2.contains(blogpost2));
    }
}