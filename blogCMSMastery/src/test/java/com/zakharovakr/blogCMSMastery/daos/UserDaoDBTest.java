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
class UserDaoDBTest {

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
    void createUserAndReadById() {
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

        User fromDao = userDao.readUserById(user.getUserId());
        assertEquals(user, fromDao);
    }

    @Test
    void readAllUsers() {
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

        User user2 = new User();
        user2.setUsername("username2");
        user2.setPassword("password1");
        user2.setFirstName("firstname2");
        user2.setLastName("lastname2");
        user2.setEnable(true);
        user2.setEmail("test2@gmail.com");
        user2.setRoles(roleList);
        user2 = userDao.createUser(user);

        List<User> fromDao = userDao.readAllUsers();
        assertEquals(2, fromDao.size());
    }


    @Test
    void updateUser() {
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

        User fromDao = userDao.readUserById(user.getUserId());

        user.setFirstName("new first name");
        user.setPassword("12345");

        userDao.updateUser(user);

        assertNotEquals(user, fromDao);

        fromDao = userDao.readUserById(user.getUserId());
        assertEquals(user, fromDao);
    }

    @Test
    void deleteUser() {
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

        User user2 = new User();
        user2.setUsername("username2");
        user2.setPassword("password1");
        user2.setFirstName("firstname2");
        user2.setLastName("lastname2");
        user2.setEnable(true);
        user2.setEmail("test2@gmail.com");
        user2.setRoles(roleList);
        user2 = userDao.createUser(user);

        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = hashtagDao.readAllHashtags();

        Blogpost blogpost2 = new Blogpost();
        blogpost2.setTimePosted(LocalDateTime.parse("2020-01-01T12:40:30"));
        blogpost2.setTitle("my blogpost 2");
        blogpost2.setContent("lorum ipsum 2..........");
        blogpost2.setType("post");
        blogpost2.setStatus("public");
        blogpost2.setUser(user2);
        blogpost2.setHashtags(hashtags);
        blogpost2.setPhotoFileName("filename");
        blogpost2 = blogpostDao.createBlogpost(blogpost2);

        Comment comment = new Comment();
        comment.setTimePosted(LocalDateTime.parse("2020-01-01T12:45:30"));
        comment.setContent("comment");
        comment.setUser(user);
        comment.setBlogpost(blogpost2);
        comment = commentDao.createComment(comment);

        userDao.deleteUser(user2.getUserId());

        List<User> fromDao = userDao.readAllUsers();
        assertEquals(1, fromDao.size());
        //also check that the blogpost gets deleted
        List<Blogpost> blogposts = blogpostDao.readAllBlogposts();
        assertEquals(0, blogposts.size());
        assertFalse(blogposts.contains(comment));
        //also check that the comments get deleted
        List<Comment> comments = commentDao.readAllComments();
        assertEquals(0, comments.size());
        assertFalse(comments.contains(comment));
    }

    @Test
    void getRoleForUser() {
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

        List<Role> listOfRolesForUser = new ArrayList<>();
        listOfRolesForUser = userDao.getRoleForUser(user.getUserId());

        assertEquals(2, listOfRolesForUser.size());
        assertTrue(listOfRolesForUser.contains(role));
        assertTrue(listOfRolesForUser.contains(role2));
    }

    @Test
    void getUserByUsername() {
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

        User fromDao = userDao.getUserByUsername(user.getUsername());
        assertEquals(fromDao, user);
    }
}