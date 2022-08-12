package com.dk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dk.dao.BaseDao;
import com.dk.dao.HouseBrokerDao;
import com.dk.entity.HouseBroker;
import com.dk.service.HouseBrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = HouseBrokerService.class)
@Transactional
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {
    @Autowired
    private HouseBrokerDao houseBrokerDao;

    @Override
    protected BaseDao<HouseBroker> getEntityDao() {
        return this.houseBrokerDao;
    }

    @Override
    public List<HouseBroker> getHouseBrokerByHouseId(Long houseId) {
        return houseBrokerDao.getHouseBrokerByHouseId(houseId);
    }
}
