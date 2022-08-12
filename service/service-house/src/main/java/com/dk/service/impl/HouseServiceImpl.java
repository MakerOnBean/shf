package com.dk.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.dk.dao.BaseDao;
import com.dk.dao.DictDao;
import com.dk.dao.HouseDao;
import com.dk.entity.House;
import com.dk.service.HouseService;
import com.dk.util.CastUtil;
import com.dk.vo.HouseQueryVo;
import com.dk.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseDao houseDao;

    @Autowired
    private DictDao dictDao;

    @Override
    protected BaseDao<House> getEntityDao() {
        return this.houseDao;
    }

    @Override
    public void publish(Long houseId, Integer status) {
        House house = new House();
        house.setId(houseId);
        house.setStatus(status);
        houseDao.update(house);
    }

    /**
     * houseService中前端分页及带条件查询的方法
     */
    @Override
    public PageInfo<HouseVo> findPageList(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        //调用houseDao中前端分页及带条件查询的方法
        Page<HouseVo> page = houseDao.findPageList(houseQueryVo);
        //查询dict字典，为HouseVo中的name属性赋值
        page.forEach(houseVo -> {
            //获取房屋类型
            houseVo.setHouseTypeName(dictDao.getNameById(houseVo.getHouseTypeId()));
            //获取楼层
            houseVo.setFloorName(dictDao.getNameById(houseVo.getFloorId()));
            //获取朝向
            houseVo.setDirectionName(dictDao.getNameById(houseVo.getDirectionId()));
        });
        return new PageInfo<>(page,5);
    }

    /**
     * 为了展示房源详情，需要重写方法在dict中查询数据
     */
    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);
        //获取户型
        String houseTypeName = dictDao.getNameById(house.getHouseTypeId());
        house.setHouseTypeName(houseTypeName);
        //获取楼层
        String floorName = dictDao.getNameById(house.getFloorId());
        house.setFloorName(floorName);
        //获取朝向
        String directionName = dictDao.getNameById(house.getDirectionId());
        house.setDirectionName(directionName);
        //获取建筑结构
        String buildStructureName = dictDao.getNameById(house.getBuildStructureId());
        house.setBuildStructureName(buildStructureName);
        //获取装修情况
        String decorationName = dictDao.getNameById(house.getDecorationId());
        house.setDecorationName(decorationName);
        //获取房屋用途
        String houseUseName = dictDao.getNameById(house.getHouseUseId());
        house.setHouseUseName(houseUseName);

        return house;
    }
}
