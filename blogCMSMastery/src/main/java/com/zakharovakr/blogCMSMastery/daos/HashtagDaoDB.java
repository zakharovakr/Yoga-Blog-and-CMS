package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Hashtag;
import com.zakharovakr.blogCMSMastery.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HashtagDaoDB implements HashtagDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Hashtag createHashtag(Hashtag hashtag) {
        final String INSERT_TAG = "INSERT INTO hashtag (name) VALUES (?)";
        jdbc.update(INSERT_TAG, hashtag.getName());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hashtag.setHashtagId(newId);

        return hashtag;
    }

    @Override
    public List<Hashtag> readAllHashtags() {
        final String SELECT_ALL_TAG = "SELECT * FROM hashtag";
        return jdbc.query(SELECT_ALL_TAG, new HashtagMapper());
    }

    @Override
    public Hashtag readHashtagById(int id) {
        try {
            final String SELECT_TAG_BY_ID = "SELECT * FROM hashtag WHERE hashtagId = ?";
            return jdbc.queryForObject(SELECT_TAG_BY_ID, new HashtagMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateHashtag(Hashtag hashtag) {
        final String UPDATE_TAG = "UPDATE hashtag SET name = ? WHERE hashtagId = ?";
        jdbc.update(UPDATE_TAG, hashtag.getName(), hashtag.getHashtagId());
    }

    @Override
    @Transactional
    public void deleteHashtag(int id) {
        final String DELETE_BLOGPOST_TAG = "DELETE FROM blogpost_hashtag WHERE hashtagId = ?";
        jdbc.update(DELETE_BLOGPOST_TAG, id);

        final String DELETE_TAG = "DELETE FROM hashtag WHERE hashtagId = ?";
        jdbc.update(DELETE_TAG, id);
    }

    public static class HashtagMapper implements RowMapper<Hashtag> {
        @Override
        public Hashtag mapRow(ResultSet resultSet, int i) throws SQLException {

            Hashtag tag = new Hashtag();

            tag.setHashtagId(resultSet.getInt("hashtagId"));
            tag.setName(resultSet.getString("name"));

            return tag;
        }
    }
}
