package com.dk.service;

import com.dk.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findPermissionByRoleId(Long roleId);

    void assignPermission(Long roleId, List<Long> permissionIds);

    List<Permission> getMenuPermissionByAdminId(Long userId);

    List<Permission> findAll();

    List<String> getPermissionCodesByAdminId(Long id);
}
