package com.dk.service;

import com.dk.entity.Community;

import java.util.List;

public interface CommunityService extends BaseService<Community> {

    List<Community> findAll();
}
