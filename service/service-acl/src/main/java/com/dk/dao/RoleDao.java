package com.dk.dao;

import com.dk.entity.Role;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface RoleDao extends BaseDao<Role>{
    /**
     * 查询所有角色
     */
    List<Role> findAll();


}
