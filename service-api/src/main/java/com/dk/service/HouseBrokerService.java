package com.dk.service;


import com.dk.entity.HouseBroker;

import java.util.List;

public interface HouseBrokerService extends BaseService<HouseBroker> {
    /**
     * 根据房源Id查询经纪人
     */
    List<HouseBroker> getHouseBrokerByHouseId(Long houseId);
}
