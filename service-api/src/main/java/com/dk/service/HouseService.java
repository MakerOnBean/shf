package com.dk.service;

import com.dk.entity.House;
import com.dk.vo.HouseQueryVo;
import com.dk.vo.HouseVo;
import com.github.pagehelper.PageInfo;

public interface HouseService extends BaseService<House> {
    void publish(Long houseId, Integer status);

    /**
     * houseService中前端分页及带条件查询的方法
     */
    PageInfo<HouseVo> findPageList(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo);
}
