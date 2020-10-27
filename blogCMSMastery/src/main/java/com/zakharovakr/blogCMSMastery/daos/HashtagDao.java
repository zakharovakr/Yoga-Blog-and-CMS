package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Hashtag;

import java.util.List;

public interface HashtagDao {
    public Hashtag createHashtag (Hashtag hashtag);
    public List<Hashtag> readAllHashtags ();
    public Hashtag readHashtagById (int id);
    public void updateHashtag (Hashtag hashtag);
    public void deleteHashtag(int id);
}
