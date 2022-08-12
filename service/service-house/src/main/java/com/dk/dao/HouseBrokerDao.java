package com.dk.dao;

import com.dk.entity.HouseBroker;

import java.util.List;

public interface HouseBrokerDao extends BaseDao<HouseBroker> {
    /**
     * 根据房源Id查询经纪人
     */
    List<HouseBroker> getHouseBrokerByHouseId(Long houseId);
}
