package com.dk.dao;

import com.dk.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionDao extends BaseDao<RolePermission> {
    List<Long> findPermissionIdByRoleId(Long roleId);

    void deletePermissionIdsByRoleId(Long roleId);

    void addRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("permissionId") Long permissionId);
}
