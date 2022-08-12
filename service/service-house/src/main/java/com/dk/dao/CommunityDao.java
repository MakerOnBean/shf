package com.dk.dao;

import com.dk.entity.Community;
import com.github.pagehelper.Page;

import java.util.List;

public interface CommunityDao extends BaseDao<Community>{
    List<Community> findAll();
}
