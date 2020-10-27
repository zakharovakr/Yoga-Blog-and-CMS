package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Role;
import com.zakharovakr.blogCMSMastery.dtos.User;

import java.util.List;

public interface UserDao {
    public User createUser (User user);
    public List<User> readAllUsers ();
    public User readUserById (int id);
    public void updateUser (User user);
    public void deleteUser (int id);

    List<Role> getRoleForUser(int userId);

    User getUserByUsername(String username);
}
