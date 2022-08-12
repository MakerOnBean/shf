package com.dk.service;

import com.dk.entity.Admin;

import java.util.List;

public interface AdminService extends BaseService<Admin> {
    List<Admin> findAll();

    Admin getAdminByUsername(String username);
}
