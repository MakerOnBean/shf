package com.dk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dk.entity.UserFollow;
import com.dk.entity.UserInfo;
import com.dk.result.Result;
import com.dk.service.UserFollowService;
import com.dk.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

    @Reference
    private UserFollowService userFollowService;

    /**
     * 关注房源
     */
    @RequestMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId")Long houseId, HttpSession session){
        UserInfo userInfo= (UserInfo) session.getAttribute("user");
        //调用关注房源的方法
        userFollowService.follow(houseId,userInfo.getId());
        return Result.ok();
    }

    /**
     * 查询关注的房源
     */
    @RequestMapping("/auth/list/{pageNum}/{pageSize}")
    public Result myFollowed(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize")Integer pageSize,HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("user");
        //调用分页查询的方法
        PageInfo<UserFollowVo> pageInfo = userFollowService.findPageList(pageNum,pageSize,userInfo.getId());
        return Result.ok(pageInfo);
    }

    /**
     * 取消更新
     */
    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id")Long id){
        //取消关注的方法
        userFollowService.cancelFollowed(id);
        return Result.ok();
    }
}
