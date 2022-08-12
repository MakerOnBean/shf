package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Admin;
import com.dk.service.AdminService;
import com.dk.service.RoleService;
import com.dk.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{
    @Reference
    private AdminService adminService;

    @Reference
    private RoleService roleService;

    //注入密码加密器
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 分页及带条件查询
     */
    @RequestMapping
    public String index(Model model, HttpServletRequest request){
        Map<String, Object> filters = getFilters(request);
        model.addAttribute("filters",filters);
        PageInfo<Admin> page = adminService.findPage(filters);
        model.addAttribute("page",page);
        return "admin/index";
    }

    /**
     * 添加页面
     */
    @RequestMapping("/create")
    public String goCreatePage(){
        return "admin/create";
    }

    /**
     * 添加用户
     */
    @RequestMapping("/save")
    public String save(Admin admin){
        //对密码加密
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.insert(admin);
        return SUCCESS_PAGE;
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete/{adminId}")
    public String delete(@PathVariable("adminId")Long adminId){
        adminService.delete(adminId);
        return "redirect:/admin";
    }

    /**
     * 根据id获取要修改的用户数据
     * 并跳转到修改页回显
     */
    @RequestMapping("/edit/{adminId}")
    public String goEditPage(@PathVariable("adminId")Long adminId,Model model){
        Admin admin = adminService.getById(adminId);
        model.addAttribute("admin",admin);
        return "admin/edit";
    }

    /**
     * 删除用户
     */
    @RequestMapping("/update")
    public String update(Admin admin){
        adminService.update(admin);
        return SUCCESS_PAGE;
    }

    /**
     * 去上传头像页面
     */
    @RequestMapping("/uploadShow/{id}")
    public String goUploadPage(Model model,@PathVariable("id")Long id){
        model.addAttribute("id",id);
        return "admin/upload";
    }

    /**
     * 保存上传的头像
     */
    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable("id")Long id, @RequestParam("file")MultipartFile file){
        try {
            // 根据id查询用户
            Admin admin = adminService.getById(id);
            //获取字节数组
            byte[] bytes = file.getBytes();
            //通过UUID随机生成文件名
            String fileName = UUID.randomUUID().toString();
            //上传到云端
            QiniuUtils.upload2Qiniu(bytes,fileName);
            //给用户设置头像url
            admin.setHeadUrl("http://rfyz3qaei.hn-bkt.clouddn.com/"+fileName);
            adminService.update(admin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SUCCESS_PAGE;
    }

    /**
     * 去分配角色页面
     */
    @RequestMapping("/assignShow/{adminId}")
    public String goAssignShowPage(@PathVariable("adminId")Long adminId,ModelMap modelMap){
        modelMap.addAttribute("adminId",adminId);
        //根据用户id查询角色
        Map<String, Object> roles = roleService.findRolesByAdminId(adminId);
        modelMap.addAllAttributes(roles);
        return "admin/assignShow";
    }

    /**
     * 修改用户角色
     * 先删除，再添加
     */
    @RequestMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,@RequestParam("roleIds") List<Long> roleIds){
        roleService.assignRole(adminId,roleIds);
        return SUCCESS_PAGE;
    }
}
