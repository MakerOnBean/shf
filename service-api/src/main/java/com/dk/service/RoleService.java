package com.dk.service;

import com.dk.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {
    List<Role> findAll();

    /**
     * 根据用户id查询用户角色
     */
    Map<String ,Object> findRolesByAdminId(Long adminId);

    /**
     * 为用户修改分配的角色
     */
    void assignRole(Long adminId, List<Long> roleIds);
}
