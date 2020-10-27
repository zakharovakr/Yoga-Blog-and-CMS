package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Role;
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
public class RoleDaoDB implements RoleDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Role createRole(Role role) {
        final String INSERT_ROLE = "INSERT INTO role (role) values (?)";
        jdbc.update(INSERT_ROLE, role.getRole());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        role.setRoleId(newId);

        return role;
    }

    @Override
    public List<Role> readAllRoles() {
        final String SELECT_ALL_ROLES = "SELECT * FROM role";
        return jdbc.query(SELECT_ALL_ROLES, new RoleMapper());
    }

    @Override
    public Role readRoleById(int id) {
        try {
            final String SELECT_ROLE_BY_ID = "SELECT * FROM role WHERE roleId = ?";
            return jdbc.queryForObject(SELECT_ROLE_BY_ID, new RoleMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        final String UPDATE_ROLE = "UPDATE role SET role = ? WHERE roleId = ?";
        jdbc.update(UPDATE_ROLE, role.getRole(), role.getRoleId());
    }

    @Override
    @Transactional
    public void deleteRole(int id) {
        final String DELETE_USER_ROLE = "DELETE FROM user_role WHERE roleId = ?";
        jdbc.update(DELETE_USER_ROLE, id);

        final String DELETE_ROLE = "DELETE FROM role WHERE roleId = ?";
        jdbc.update(DELETE_ROLE, id);
    }

    public static class RoleMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet resultSet, int i) throws SQLException {
            Role role = new Role();

            role.setRoleId(resultSet.getInt("roleId"));
            role.setRole(resultSet.getString("role"));

            return role;
        }
    }
}
