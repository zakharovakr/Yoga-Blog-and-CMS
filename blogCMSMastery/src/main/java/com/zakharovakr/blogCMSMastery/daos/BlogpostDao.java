package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import com.zakharovakr.blogCMSMastery.dtos.Hashtag;
import com.zakharovakr.blogCMSMastery.dtos.User;

import java.util.List;

public interface BlogpostDao {
    public Blogpost createBlogpost (Blogpost blogpost);
    public List<Blogpost> readAllBlogposts ();
    public Blogpost readBlogpostById (int id);
    public void updateBlogpost (Blogpost blogpost);
    public void deleteBlogpost (int id);

    User getUserForBlogpost(int blogpostId);

    List<Hashtag> getTagsForBlogpost(int blogpostId);

    List<Blogpost> getBlogpostByType(String type);

    List<Blogpost> getBlogpostByTag(int tagId);

    List<Blogpost> getBlogpostBySearchTitle(String searchText);
}
