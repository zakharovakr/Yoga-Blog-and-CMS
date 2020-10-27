package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Blogpost;
import com.zakharovakr.blogCMSMastery.dtos.Role;
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
public class UserDaoDB implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public User createUser(User user) {
        final String INSERT_USER = "INSERT INTO user (username, password, firstName, lastName, email, enable) VALUES (?,?,?,?,?,?)";
        jdbc.update(INSERT_USER, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnable());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        user.setUserId(newId);
        insertIntoUserRole(user);
        return user;
    }

    @Override
    public List<User> readAllUsers() {
        final String SELECT_ALL_USERS = "SELECT * FROM user";
        List<User> userList = jdbc.query(SELECT_ALL_USERS, new UserMapper());
        associateUserRole(userList);

        return userList;
    }

    @Override
    public User readUserById(int id) {
        try {
            final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE userId = ?";
            User user = jdbc.queryForObject(SELECT_USER_BY_ID, new UserMapper(), id);
            user.setRoles(getRoleForUser(user.getUserId()));
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE user SET " +
                "username = ?, " +
                "password = ?, " +
                "firstName = ?, " +
                "lastName = ?, " +
                "email = ?, " +
                "enable = ? " +
                "WHERE userId = ?";
        jdbc.update(UPDATE_USER, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnable(), user.getUserId());

        final String DELETE_USER_ROLE = "DELETE FROM user_role WHERE userId = ?";
        jdbc.update(DELETE_USER_ROLE, user.getUserId());

        insertIntoUserRole(user);

    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        final String SELECT_BLOGPOST_BY_USER_ID = "SELECT * FROM blogpost WHERE userId = ?";
        List<Blogpost> blogpostList = jdbc.query(SELECT_BLOGPOST_BY_USER_ID, new BlogpostDaoDB.BlogpostMapper(), id);
        for (Blogpost blogpost : blogpostList) {
            final String DELETE_BLOGPOST_HASHTAG = "DELETE FROM blogpost_hashtag WHERE blogpostId = ?";
            jdbc.update(DELETE_BLOGPOST_HASHTAG, blogpost.getBlogpostId());

            final String DELETE_COMMENT = "DELETE FROM comment WHERE blogpostId = ?";
            jdbc.update(DELETE_COMMENT, blogpost.getBlogpostId());

            final String DELETE_BLOGPOST = "DELETE FROM blogpost WHERE blogpostId = ?";
            jdbc.update(DELETE_BLOGPOST, blogpost.getBlogpostId());
        }


        final String DELETE_COMMENT = "DELETE FROM comment WHERE userId = ?";
        jdbc.update(DELETE_COMMENT, id);

        final String DELETE_USER_ROLE = "DELETE FROM user_role WHERE userId = ?";
        jdbc.update(DELETE_USER_ROLE, id);

        final String DELETE_USER = "DELETE FROM user WHERE userId = ?";
        jdbc.update(DELETE_USER, id);

    }

    private void insertIntoUserRole(User user) {
        final String INSERT_USER_ROLE = "INSERT INTO user_role (userId, roleId) VALUES (?,?)";
        for (Role role : user.getRoles()) {
            jdbc.update(INSERT_USER_ROLE, user.getUserId(), role.getRoleId());
        }
    }

    @Override
    public List<Role> getRoleForUser(int userId) {
        final String SELECT_ROLE_BY_USER_ID = "SELECT r.* FROM role r " +
                "JOIN user_role ur ON r.roleId = ur.roleId " +
                "WHERE ur.userId = ?";
        return jdbc.query(SELECT_ROLE_BY_USER_ID, new RoleDaoDB.RoleMapper(), userId);
    }

    private void associateUserRole(List<User> userList) {
        for (User user : userList) {
            List<Role> roleList = getRoleForUser(user.getUserId());
            user.setRoles(roleList);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            final String SELECT_USER_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
            User user = jdbc.queryForObject(SELECT_USER_BY_USERNAME, new UserMapper(), username);
            user.setRoles(getRoleForUser(user.getUserId()));
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    public static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();

            user.setUserId(resultSet.getInt("userId"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setLastName(resultSet.getString("lastName"));
            user.setEmail(resultSet.getString("email"));
            user.setEnable(resultSet.getBoolean("enable"));

            return user;
        }
    }
}
