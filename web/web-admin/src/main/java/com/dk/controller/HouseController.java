package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.*;
import com.dk.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController{
    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseUserService houseUserService;

    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE="house/create";
    private static final String PAGE_EDIT="house/edit";
    private static final String PAGE_SHOW="house/show";

    /**
     * 分页以及带条件查询
     */
    @RequestMapping
    public String index(Model model, HttpServletRequest request){
        Map<String, Object> filters = getFilters(request);
        model.addAttribute("filters",filters);
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("page",page);
        setRequestAttribute(model);
        return PAGE_INDEX;
    }

    /**
     * 去添加房源的页面
     */
    @RequestMapping("/create")
    public String goCreatePage(Model model){
        setRequestAttribute(model);
        return PAGE_CREATE;
    }

    /**
     * 保存添加的数据
     */
    @RequestMapping("/save")
    public String save(House house){
        houseService.insert(house);
        return SUCCESS_PAGE;
    }

    /**
     * 去修改的页面
     */
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id")Long id,Model model){
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        setRequestAttribute(model);
        return PAGE_EDIT;
    }

    /**
     * 保存修改的数据
     */
    @RequestMapping("/update")
    public String update(House house){
        houseService.update(house);
        return SUCCESS_PAGE;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        houseService.delete(id);
        return "redirect:/house";
    }

    /**
     * 将所有小区以及数据字典中的数据放入model对象中
     */
    private void setRequestAttribute(Model model){
        //获取所有小区
        List<Community> communityList = communityService.findAll();
        model.addAttribute("communityList",communityList);
        //获取所有户型
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        model.addAttribute("houseTypeList",houseTypeList);
        //获取楼层
        List<Dict> floorList = dictService.findListByDictCode("floor");
        model.addAttribute("floorList",floorList);
        //获取建筑结构
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        model.addAttribute("buildStructureList",buildStructureList);
        //获取朝向
        List<Dict> directionList = dictService.findListByDictCode("direction");
        model.addAttribute("directionList",directionList);
        //获取装修情况
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        model.addAttribute("decorationList",decorationList);
        //获取房屋用途
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        model.addAttribute("houseUseList",houseUseList);
    }

    /**
     * 发布和取消发布
     */
    @RequestMapping("/publish/{houseId}/{status}")
    public String publish(@PathVariable("houseId")Long houseId,@PathVariable("status")Integer status){
        //调用service发布或取消发布方法
        houseService.publish(houseId,status);
        //重定向到房源首页
        return "redirect:/house";
    }

    /**
     * 查看房源详情
     */
    @RequestMapping("/{houseId}")
    public String show(@PathVariable("houseId")Long houseId,Model model){
        //调用houseService根据id查询房源
        House house = houseService.getById(houseId);
        model.addAttribute("house",house);
        //调用communityService中根据小区id查询小区的方法
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("community",community);

        //查询房源图片
        List<HouseImage> houseImage1List = houseImageService.getHouseImagesByHouseIdAndType(houseId, 1);
        model.addAttribute("houseImage1List",houseImage1List);
        //查看房产图片
        List<HouseImage> houseImage2List = houseImageService.getHouseImagesByHouseIdAndType(houseId, 2);
        model.addAttribute("houseImage2List",houseImage2List);
        //查询经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokerByHouseId(houseId);
        model.addAttribute("houseBrokerList",houseBrokerList);
        //查询房东
        List<HouseUser> houseUserList = houseUserService.getHouseUserByHouseId(houseId);
        model.addAttribute("houseUserList",houseUserList);
        return PAGE_SHOW;
    }
}
