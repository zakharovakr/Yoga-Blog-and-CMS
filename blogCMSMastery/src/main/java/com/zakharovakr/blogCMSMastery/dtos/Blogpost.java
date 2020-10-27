package com.zakharovakr.blogCMSMastery.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Blogpost {
    private int blogpostId;

    private LocalDateTime timePosted;

    @NotBlank(message = "Please enter title")
    @Size(max = 70, message= "Invalid title: Please enter title between 1-70 characters")
    private String title;

    @NotBlank(message = "Please enter content")
    private String content;

    @NotBlank(message = "Please enter type")
    private String type;

    @NotBlank(message = "Please enter status")
    private String status;

    private String photoFileName;

    private User user;

    @NotNull(message = "Please choose at least one category")
    private List<Hashtag> hashtags;

    public int getBlogpostId() {
        return blogpostId;
    }

    public void setBlogpostId(int blogpostId) {
        this.blogpostId = blogpostId;
    }

    public LocalDateTime getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(LocalDateTime timePosted) {
        this.timePosted = timePosted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blogpost)) return false;
        Blogpost blogpost = (Blogpost) o;
        return getBlogpostId() == blogpost.getBlogpostId() &&
                Objects.equals(getTimePosted(), blogpost.getTimePosted()) &&
                Objects.equals(getTitle(), blogpost.getTitle()) &&
                Objects.equals(getContent(), blogpost.getContent()) &&
                Objects.equals(getType(), blogpost.getType()) &&
                Objects.equals(getStatus(), blogpost.getStatus()) &&
                Objects.equals(getPhotoFileName(), blogpost.getPhotoFileName()) &&
                Objects.equals(getUser(), blogpost.getUser()) &&
                Objects.equals(getHashtags(), blogpost.getHashtags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBlogpostId(), getTimePosted(), getTitle(), getContent(), getType(), getStatus(), getPhotoFileName(), getUser(), getHashtags());
    }

    @Override
    public String toString() {
        return "Blogpost{" +
                "blogpostId=" + blogpostId +
                ", timePosted=" + timePosted +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", photoFileName='" + photoFileName + '\'' +
                ", user=" + user +
                ", hashtags=" + hashtags +
                '}';
    }
}
