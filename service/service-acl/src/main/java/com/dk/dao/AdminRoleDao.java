package com.dk.dao;

import com.dk.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * admin与role中间表操作对象
 */
public interface AdminRoleDao extends BaseDao<AdminRole> {
    List<Long> findRoleIdByAdminId(Long adminId);

    void deleteRoleIdsByAdminId(Long adminId);

    void addRoleIdAndAdminId(@Param("adminId") Long adminId,@Param("roleId") Long roleId);
}
