package com.dk.dao;

import com.dk.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageDao extends BaseDao<HouseImage> {
    /**
     * 根据房源id和类型（type）查询房源或房产图片
     */
    List<HouseImage> getHouseImagesByHouseIdAndType(@Param("houseId") Long houseId,@Param("type") Integer type);
}
