package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class RoleDaoDBTest {

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
    void createRoleAndReadById() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);

        Role fromDao = roleDao.readRoleById(role.getRoleId());
        assertEquals(role, fromDao);
    }

    @Test
    void readAllRoles() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        List<Role> fromDao = roleDao.readAllRoles();
        assertEquals(2, fromDao.size());
    }

    @Test
    void updateRole() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);

        role.setRole("Changed");
        roleDao.updateRole(role);

        Role fromDao = roleDao.readRoleById(role.getRoleId());
        assertEquals(role, fromDao);
    }

    @Test
    void deleteRole() {
        Role role = new Role();
        role.setRole("role1");
        role = roleDao.createRole(role);
        Role role2 = new Role();
        role2.setRole("role2");
        role2 = roleDao.createRole(role2);

        roleDao.deleteRole(role2.getRoleId());

        List<Role> fromDao = roleDao.readAllRoles();
        assertEquals(1, fromDao.size());
    }
}