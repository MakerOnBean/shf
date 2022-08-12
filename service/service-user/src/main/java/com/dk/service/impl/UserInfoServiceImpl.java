package com.dk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dk.dao.BaseDao;
import com.dk.dao.UserInfoDao;
import com.dk.entity.UserInfo;
import com.dk.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserInfoService.class)
@Transactional
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Override
    protected BaseDao<UserInfo> getEntityDao() {
        return this.userInfoDao;
    }

    /**
     * 判断手机号是否被注册
     */
    @Override
    public UserInfo getUserInfoByPhone(String phone) {
        return userInfoDao.getUserInfoByPhone(phone);
    }
}
