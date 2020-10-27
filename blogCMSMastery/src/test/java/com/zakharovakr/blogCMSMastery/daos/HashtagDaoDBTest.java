package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class HashtagDaoDBTest {

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
    void createHashtagAndReadById() {
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag fromDao = hashtagDao.readHashtagById(hashtag.getHashtagId());
        assertEquals(hashtag, fromDao);

    }

    @Test
    void readAllHashtags() {
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setName("tag2");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> fromDao = hashtagDao.readAllHashtags();
        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(hashtag));
        assertTrue(fromDao.contains(hashtag2));
    }


    @Test
    void updateHashtag() {
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag fromDao = hashtagDao.readHashtagById(hashtag.getHashtagId());

        hashtag.setName("new tag");
        hashtagDao.updateHashtag(hashtag);

        assertNotEquals(hashtag, fromDao);

        fromDao = hashtagDao.readHashtagById(hashtag.getHashtagId());

        assertEquals(hashtag, fromDao);
    }

    @Test
    void deleteHashtag() {
        Hashtag hashtag = new Hashtag();
        hashtag.setName("tag1");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setName("tag2");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setName("tag2");
        hashtag3 = hashtagDao.createHashtag(hashtag3);

        hashtagDao.deleteHashtag(hashtag2.getHashtagId());

        List<Hashtag> fromDao = hashtagDao.readAllHashtags();

        assertEquals(2, fromDao.size());
        assertTrue(fromDao.contains(hashtag));
        assertTrue(fromDao.contains(hashtag3));
        assertFalse(fromDao.contains(hashtag2));

    }
}