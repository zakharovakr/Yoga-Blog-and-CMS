package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import com.zakharovakr.blogCMSMastery.dtos.Comment;
import com.zakharovakr.blogCMSMastery.dtos.User;

import java.util.List;

public interface CommentDao {
    public Comment createComment (Comment comment);
    public List<Comment> readAllComments ();
    public Comment readCommentById (int id);
    public void updateComment (Comment comment);
    public void deleteComment (int id);

    User getUserForComment(int commentId);

    Blogpost getBlogpostForComment(int commentId);

    List<Comment> getCommentByBlogpostId(int blogpostId);
}
