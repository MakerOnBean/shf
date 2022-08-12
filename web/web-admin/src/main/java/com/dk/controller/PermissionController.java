package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Permission;
import com.dk.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController{

    @Reference
    private PermissionService permissionService;

    private final static String LIST_ACTION = "redirect:/permission";
    private final static String PAGE_INDEX = "permission/index";
    private final static String PAGE_CREATE = "permission/create";
    private final static String PAGE_EDIT = "permission/edit";

    /**
     * 获取菜单
     */
    @RequestMapping
    public String index(Model model){
        List<Permission> permissionList = permissionService.findAll();
        model.addAttribute("list",permissionList);
        return PAGE_INDEX;
    }

    /**
     * 去添加页面
     */
    @RequestMapping("/create")
    public String goCreatePage(Model model,Permission permission){
        model.addAttribute("permission",permission);
        return PAGE_CREATE;
    }

    /**
     * 保存添加的数据
     */
    @RequestMapping("/save")
    public String save(Permission permission){
        permissionService.insert(permission);
        return SUCCESS_PAGE;
    }

    /**
     * 去修改页面
     */
    @RequestMapping("/edit/{id}")
    public String goEditPage(@PathVariable("id")Long id,Model model){
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    /**
     * 保存修改
     */
    @RequestMapping("/update")
    public String update(Permission permission){
        permissionService.update(permission);
        return SUCCESS_PAGE;
    }

    /**
     * 删除权限
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        permissionService.delete(id);
        return LIST_ACTION;
    }

}
