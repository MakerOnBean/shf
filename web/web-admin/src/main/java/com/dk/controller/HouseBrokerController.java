package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Admin;
import com.dk.entity.HouseBroker;
import com.dk.service.AdminService;
import com.dk.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private AdminService adminService;
    private static final String PAGE_SUCCESS="common/successPage";
    private static final String PAGE_CREATE="houseBroker/create";
    private static final String PAGE_EDIT="houseBroker/edit";

    /**
     * 去添加紧急人页面
     */
    @RequestMapping("/create")
    public String goCreatePage(@RequestParam("houseId")Long houseId, Model model){
        model.addAttribute("houseId",houseId);
        //调用AdminService获取所有用户的方法
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList",adminList);
        return PAGE_CREATE;
    }

    /**
     * 保存经纪人
     */
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker){
        //调用AdminService查询经纪人完整信息
        setHouseBroker(houseBroker);
        //保存数据
        houseBrokerService.insert(houseBroker);
        return PAGE_SUCCESS;
    }

    /**
     * 去修改页面
     */
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id")Long id,Model model){
        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("houseBroker",houseBroker);
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList",adminList);
        return PAGE_EDIT;
    }

    /**
     * 保存修改数据
     */
    @RequestMapping("/update")
    public String update(HouseBroker houseBroker){
        setHouseBroker(houseBroker);
        houseBrokerService.update(houseBroker);
        return PAGE_SUCCESS;
    }

    /**
     * 删除经纪人数据
     */
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId")Long houseId,@PathVariable("id")Long brokerId){
        houseBrokerService.delete(brokerId);
        return "redirect:/house/"+houseId;
    }

    /**
     * 获取经纪人完整数据
     */
    private void setHouseBroker(HouseBroker houseBroker){
        //调用AdminService查询经纪人完整信息
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
    }
}
