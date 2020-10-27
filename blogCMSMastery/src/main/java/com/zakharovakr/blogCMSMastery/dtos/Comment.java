package com.zakharovakr.blogCMSMastery.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private int commentId;
    private LocalDateTime timePosted;
    private String content;
    private User user;
    private Blogpost blogpost;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public LocalDateTime getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(LocalDateTime timePosted) {
        this.timePosted = timePosted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Blogpost getBlogpost() {
        return blogpost;
    }

    public void setBlogpost(Blogpost blogpost) {
        this.blogpost = blogpost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment1 = (Comment) o;
        return getCommentId() == comment1.getCommentId() &&
                Objects.equals(getTimePosted(), comment1.getTimePosted()) &&
                Objects.equals(getContent(), comment1.getContent()) &&
                Objects.equals(getUser(), comment1.getUser()) &&
                Objects.equals(getBlogpost(), comment1.getBlogpost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId(), getTimePosted(), getContent(), getUser(), getBlogpost());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", timePosted=" + timePosted +
                ", comment='" + content + '\'' +
                ", user=" + user +
                ", blogpost=" + blogpost +
                '}';
    }
}
