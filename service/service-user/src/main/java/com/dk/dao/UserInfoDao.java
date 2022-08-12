package com.dk.dao;

import com.dk.entity.UserInfo;

public interface UserInfoDao extends BaseDao<UserInfo> {
    UserInfo getUserInfoByPhone(String phone);
}
