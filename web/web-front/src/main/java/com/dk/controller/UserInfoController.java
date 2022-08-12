package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.UserInfo;
import com.dk.result.Result;
import com.dk.result.ResultCodeEnum;
import com.dk.service.UserInfoService;
import com.dk.util.MD5;
import com.dk.util.SMSUtils;
import com.dk.vo.LoginVo;
import com.dk.vo.RegisterVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;

    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone")String phone, HttpSession session){
        //设置验证码为8888
        String code="8888";

        try {
            SMSUtils.sendMessage(phone,code);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }

        session.setAttribute("code",code);
        return Result.ok(code);
    }

    /**
     * 注册
     */
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo,HttpSession session){
        //获取手机号、昵称、密码
        String phone = registerVo.getPhone();
        String password = registerVo.getPassword();
        String nickName = registerVo.getNickName();
        String code = registerVo.getCode();
        //验空
        if (StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)||StringUtils.isEmpty(nickName)||StringUtils.isEmpty(code)){
            //返回参数错误的消息
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        //判断验证码
        if (!code.equals(session.getAttribute("code"))){
            //返回验证码错误的消息
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //查询手机号是否被注册
        UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);

        if (userInfo != null) {
            //手机号已经注册
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }



        //创建一个UserInfo对象，放入数据库中
        UserInfo userInfo1 = new UserInfo();
        //使用MD5加密密码
        userInfo1.setPassword(MD5.encrypt(password));
        userInfo1.setPhone(phone);
        userInfo1.setNickName(nickName);
        userInfo1.setStatus(1);
        userInfoService.insert(userInfo1);
        return Result.ok();
    }

    /**
     * 登陆
     */
    @RequestMapping("/login")
    public Result login(@RequestBody LoginVo loginVo,HttpSession session){
        String phone = loginVo.getPhone();
        String password = loginVo.getPassword();
        //验空
        if (StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
            return Result.build(null,ResultCodeEnum.PARAM_ERROR);
        }
        //根据手机号查询用户信息
        UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);
        //账号不正确
        if (userInfo == null) {
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (!MD5.encrypt(password).equals(userInfo.getPassword())){
            //密码不正确
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }
        //判断用户是否锁定
        if (userInfo.getStatus() == 0){
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //登陆成功
        //将用户信息放入session中，用于后台判断用户是否登陆
        session.setAttribute("user",userInfo);

        //map中必须存放nickname
        Map<String,Object> map=new HashMap<>();
        map.put("nickName",userInfo.getNickName());
        map.put("phone",userInfo.getPhone());
        return Result.ok(map);
    }

    /**
     * 登出
     */
    @RequestMapping("/logout")
    public Result logout(HttpSession session){
        //移除userInfo对象
        session.removeAttribute("user");
        return Result.ok();
    }
}
