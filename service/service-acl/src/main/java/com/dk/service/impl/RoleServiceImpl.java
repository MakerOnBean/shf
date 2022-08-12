package com.dk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dk.dao.AdminRoleDao;
import com.dk.dao.BaseDao;
import com.dk.dao.RoleDao;
import com.dk.service.RoleService;
import com.dk.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    protected BaseDao<Role> getEntityDao() {
        return this.roleDao;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Map<String, Object> findRolesByAdminId(Long adminId) {
        //获取所有角色
        List<Role> roleList = roleDao.findAll();
        //获取用户已拥有的角色id
        List<Long> roleIds = adminRoleDao.findRoleIdByAdminId(adminId);
        //创建两个List，一个存放为选中的角色，一个存放选中的角色
        List<Role> noAssginRoleList = new ArrayList<>();
        List<Role> assginRoleList = new ArrayList<>();
        //遍历所有角色，分别存放到list中
        roleList.forEach(role -> {
            if (roleIds.contains(role.getId())) {
                assginRoleList.add(role);
            } else {
                noAssginRoleList.add(role);
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("noAssginRoleList", noAssginRoleList);
        map.put("assginRoleList", assginRoleList);
        return map;
    }

    @Override
    public void assignRole(Long adminId, List<Long> roleIds) {
        //先删除
        adminRoleDao.deleteRoleIdsByAdminId(adminId);
        roleIds.forEach(roleId -> {
            if (roleId != null) {
                adminRoleDao.addRoleIdAndAdminId(adminId, roleId);
            }
        });
    }


}
