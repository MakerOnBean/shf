package com.dk.interceptor;


import com.dk.entity.UserInfo;
import com.dk.result.Result;
import com.dk.result.ResultCodeEnum;
import com.dk.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session中的userInfo对象
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo)session.getAttribute("user");
        if (userInfo == null) {
            //尚未登录
            Result<String> result = Result.build("尚未登陆", ResultCodeEnum.LOGIN_AUTH);
            //将result对象响应到前端
            WebUtil.writeJSON(response,result);
            return false;
        }
        return true;
    }
}
