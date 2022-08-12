package com.dk.service;

import com.dk.entity.HouseImage;

import java.util.List;

public interface HouseImageService extends BaseService<HouseImage> {
    /**
     * 根据房源id和类型（type）查询房源或房产图片
     */
    List<HouseImage> getHouseImagesByHouseIdAndType(Long houseId, Integer type);
}
