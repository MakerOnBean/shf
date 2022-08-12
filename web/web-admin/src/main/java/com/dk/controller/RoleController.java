package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Role;
import com.dk.service.PermissionService;
import com.dk.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{

    @Reference
    private RoleService roleService;

    @Reference
    private PermissionService permissionService;

    /**
     * 角色管理首页表格页面
     * 该用分页插件后，已弃用该方法
     */
//    @RequestMapping
    @Deprecated
    public String index(Model model) {
        List<Role> roleList = roleService.findAll();
        model.addAttribute("list", roleList);
        return "role/index";
    }

    /**
     * 新增页面
     */
    @PreAuthorize("hasAuthority('role.create')")
    @GetMapping("/create")
    public String goCreatePage() {
        return "role/create";
    }

    /**
     * 添加角色
     */
    @PreAuthorize("hasAuthority('role.create')")
    @PostMapping("/save")
    public String save(Role role) {
        roleService.insert(role);
        return SUCCESS_PAGE;
    }

    /**
     * 删除角色
     */
    @PreAuthorize("hasAuthority('role.delete')") //此时只有 有delete权限才能调用下面的方法
    @GetMapping("/delete/{roleId}")
    public String delete(@PathVariable("roleId") Long roleId) {
        roleService.delete(roleId);
        return "redirect:/role";
    }

    /**
     * 查询修改的角色
     * 去修改页面
     */
    @PreAuthorize("hasAuthority('role.edit')")
    @GetMapping("/edit/{roleId}")
    public String goEditPage(@PathVariable("roleId") Integer roleId, Model model) {
        Role role = roleService.getById(roleId);
        model.addAttribute("role", role);
        return "role/edit";
    }

    /**
     * 修改角色
     */
    @PreAuthorize("hasAuthority('role.edit')")
    @PostMapping("/update")
    public String update(Role role) {
        roleService.update(role);
        return SUCCESS_PAGE;
    }

    /**
     * 分页及待条件查询
     */
    @PreAuthorize("hasAuthority('role.show')")
    @RequestMapping
    public String index(Model model, HttpServletRequest request){
        //通过该方法获取分页的数据，并放入map中
        Map<String, Object> filters = getFilters(request);
        //将该集合放入request域对象中
        model.addAttribute("filters",filters);

        //通过分页查询获取PageInfo对象
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        //将PageInfo对象放入request域对象中
        model.addAttribute("page",pageInfo);
        return "role/index";
    }

    /**
     * 去权限管理页面
     */
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignShow/{roleId}")
    public String goAssignShow(@PathVariable("roleId")Long roleId,Model model){
        model.addAttribute("roleId",roleId);
        //调用PermissionService中根据角色ID获取权限方法
        List<Map<String,Object>> zNodes = permissionService.findPermissionByRoleId(roleId);
        model.addAttribute("zNodes",zNodes);
        return "role/assignShow";
    }

    /**
     * 分配权限
     */
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId")Long roleId,@RequestParam("permissionIds")List<Long> permissionIds){
        //调用permissionService分配权限
        permissionService.assignPermission(roleId,permissionIds);
        return SUCCESS_PAGE;
    }
}