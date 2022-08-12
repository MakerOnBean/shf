package com.dk.dao;

import com.dk.entity.House;
import com.dk.vo.HouseQueryVo;
import com.dk.vo.HouseVo;
import com.github.pagehelper.Page;

public interface HouseDao extends BaseDao<House> {
    /**
     * houseDao中前端分页及带条件查询的方法
     */
    Page<HouseVo> findPageList(HouseQueryVo houseQueryVo);
}
