package com.zakharovakr.blogCMSMastery.daos;

import com.zakharovakr.blogCMSMastery.dtos.Role;

import java.util.List;

public interface RoleDao {
    public Role createRole (Role role);
    public List<Role> readAllRoles ();
    public Role readRoleById (int id);
    public void updateRole (Role role);
    public void deleteRole (int id);
}
