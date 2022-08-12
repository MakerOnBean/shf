package com.dk.dao;

import com.dk.entity.Permission;

import java.util.List;

public interface PermissionDao extends BaseDao<Permission> {
    /**
     * 获取所有权限
     */
    List<Permission> findAll();

    List<Permission> getMenuPermissionsByAdminId(Long userId);

    List<String> getAllPermissionCodes();

    List<String> getPermissionCodesByAdminId(Long adminId);
}
