package com.dk.dao;

import com.dk.entity.HouseUser;

import java.util.List;

public interface HouseUserDao extends BaseDao<HouseUser> {
    /**
     * 根据房源id查询房东信息
     */
    List<HouseUser> getHouseUserByHouseId(Long houseId);
}
