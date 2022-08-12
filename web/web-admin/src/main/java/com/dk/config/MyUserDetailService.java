package com.dk.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.Admin;
import com.dk.service.AdminService;
import com.dk.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MyUserDetailService implements UserDetailsService {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    /**
     * 用户登陆时Spring Security会自动调用该方法，并将用户名传入到该方法中
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名获取admin对象
        Admin admin = adminService.getAdminByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }

        //调用permissionService中获取当前用户权限码的方法
        List<String> permissionCodes = permissionService.getPermissionCodesByAdminId(admin.getId());
        //创建一个List，用于授权的集合
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        //遍历得到每一个权限码
        permissionCodes.forEach(permissionCode -> {
            if (permissionCode != null) {
                //创建GrantedAuthority对象
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permissionCode);
                //将对象放入权限集合中
                grantedAuthoritySet.add(simpleGrantedAuthority);
            }
        });

        /*
            给用户授权
            两种标识方式
                1）听过角色的方式标识，例如：ROLE_ADMIN
                2）直接设置权限，例如：Delete,Query,update...
        */
        return new User(username, admin.getPassword(), grantedAuthoritySet);
    }
}
