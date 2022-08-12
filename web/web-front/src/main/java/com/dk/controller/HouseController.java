package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.*;
import com.dk.result.Result;
import com.dk.service.*;
import com.dk.vo.HouseQueryVo;
import com.dk.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/house")
public class HouseController {
    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private UserFollowService userFollowService;

    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findPageList(@PathVariable("pageNum")Integer pageNum, @PathVariable("pageSize")Integer pageSize,
                               @RequestBody HouseQueryVo houseQueryVo){
        //调用houseService中前端分页及带条件查询的方法
        PageInfo<HouseVo> pageInfo=houseService.findPageList(pageNum,pageSize,houseQueryVo);
        return Result.ok(pageInfo);
    }

    /**
     * 查看房源详情页
     */
    @RequestMapping("/info/{id}")
    public Result info(@PathVariable("id")Long houseId,HttpSession session){
        //查询房源
        House house = houseService.getById(houseId);
        //查询小区信息
        Community community = communityService.getById(house.getCommunityId());
        //查询图片
        List<HouseImage> houseImage1List = houseImageService.getHouseImagesByHouseIdAndType(houseId, 1);
        //获取房源经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokerByHouseId(houseId);
        Map<String, Object> map=new HashMap<>();
        map.put("house",house);
        map.put("community",community);
        map.put("houseImage1List",houseImage1List);
        map.put("houseBrokerList",houseBrokerList);
        //默认没有关注该房源，要改
        Boolean isFollow = false;
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        if (userInfo != null) {
            isFollow = userFollowService.isFollow(houseId,userInfo.getId());
        }
        map.put("isFollow",isFollow);
        return Result.ok(map);
    }
}
