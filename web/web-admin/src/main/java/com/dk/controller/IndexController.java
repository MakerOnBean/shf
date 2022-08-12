package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Admin;
import com.dk.entity.Permission;
import com.dk.service.AdminService;
import com.dk.service.PermissionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    /**
     * 去首页
     */
    //@RequestMapping("/")
    //public String index(){
    //    return "frame/index";
    //}

    @RequestMapping("/")
    public String index(Model model){
        //设置默认用户id
        //Long userId = 1L;
        //根据用户id查询用户
        //Admin admin = adminService.getById(userId);


        //通过Spring Security获取User对象
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据用户名获取admin对象
        Admin admin = adminService.getAdminByUsername(user.getUsername());
        //根据用户id调用PermissionService中获取用户权限的方法
        List<Permission> permissionList = permissionService.getMenuPermissionByAdminId(admin.getId());
        //将用户和权限菜单放入request中
        model.addAttribute("admin",admin);
        model.addAttribute("permissionList",permissionList);
        return "frame/index";
    }

    /**
     * 去主页面
     */
    @RequestMapping("/main")
    public String main(){
        return "frame/main";
    }

    /**
     * 去登陆页面
     */
    @RequestMapping("/login")
    public String login(){
        return "frame/login";
    }

    /**
     * 去没有权限的提示页面
     */
    @RequestMapping("/auth")
    public String auth(){
        return "frame/auth";
    }
}
