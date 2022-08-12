package com.dk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dk.dao.BaseDao;
import com.dk.dao.PermissionDao;
import com.dk.dao.RolePermissionDao;
import com.dk.entity.Permission;
import com.dk.helper.PermissionHelper;
import com.dk.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    protected BaseDao<Permission> getEntityDao() {
        return this.permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        //获取所有权限
        List<Permission> permissionList = permissionDao.findAll();
        //根据角色id返回已分配的权限id
        List<Long> permissionIds = rolePermissionDao.findPermissionIdByRoleId(roleId);

        List<Map<String, Object>> returnList = new ArrayList<>();
        //遍历所有权限
        permissionList.forEach(permission -> {
            Map<String, Object> map = new HashMap<>();
            //id
            map.put("id", permission.getId());
            //pId
            map.put("pId", permission.getParentId());
            //name
            map.put("name", permission.getName());
            //判断权限id在不在permissionIds中
            if (permissionIds.contains(permission.getId())) {
                map.put("checked", true);
            } else {
                map.put("checked", false);
            }
            returnList.add(map);
        });
        return returnList;
    }

    @Override
    public void assignPermission(Long roleId, List<Long> permissionIds) {
        //调用rolePermissionDao删除已分配权限的方法
        rolePermissionDao.deletePermissionIdsByRoleId(roleId);
        //遍历所有的权限id
        permissionIds.forEach(permissionId -> {
            if (permissionId != null) {
                //调用保存权限id和角色id的方法
                rolePermissionDao.addRoleIdAndPermissionId(roleId, permissionId);
            }
        });
    }

    @Override
    public List<Permission> getMenuPermissionByAdminId(Long userId) {
        List<Permission> permissionList = null;
        //判断是否是系统管理员
        if (userId==1){
            //获取所有权限
            permissionList = permissionDao.findAll();
        } else {
            //根据用户id查询权限菜单
            permissionList = permissionDao.getMenuPermissionsByAdminId(userId);
        }
        //通过PermissionHelper工具类将List转换为树形结构
        return PermissionHelper.build(permissionList);
    }

    /**
     * 获取所有菜单数据
     */
    @Override
    public List<Permission> findAll() {
        List<Permission> permissionList = permissionDao.findAll();
        if (permissionList != null) {
            return PermissionHelper.build(permissionList);
        }
        return null;
    }

    @Override
    public List<String> getPermissionCodesByAdminId(Long adminId) {
        List<String> permissionCodes = null;
        if (adminId==1){
            //系统管理员
            permissionCodes = permissionDao.getAllPermissionCodes();
        } else {
            permissionCodes = permissionDao.getPermissionCodesByAdminId(adminId);
        }
        return permissionCodes;
    }
}
