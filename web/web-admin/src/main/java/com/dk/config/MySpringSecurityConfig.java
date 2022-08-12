package com.dk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 当前配置类需要被扫描
 */
@Configuration
@EnableWebSecurity//开启Security的自动配置，会生成一个登陆页面
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启controller中的方法权限控制
public class MySpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 在内存中设置一个管理员用户和密码
     * 不再在内存中设置密码，在数据库中查询
     */
    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth.inMemoryAuthentication()
    //            .withUser("admin")
    //            .password(new BCryptPasswordEncoder().encode("111111"))
    //            .roles("");
    //}

    /**
     * 创建一个密码加密器放到ioc中
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 允许iframe标签显示
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //必须调用父类方法，否则认证过程失效
        //super.configure(http);
        //配置允许iframe标签中的内容显示
        http.headers().frameOptions().sameOrigin();


        //自定义认证授权，不再调用父类认证方式
        //可以匿名访问的资源
        http.authorizeRequests().antMatchers("/static/**","/login").permitAll().anyRequest().authenticated();
        //配置自定义登陆页面
        http.formLogin().loginPage("/login")//配置去自定义页面访问路径
                .defaultSuccessUrl("/");//配置登陆成功后前往的地址
        //配置登出的地址以及登出成功以后去往的地址，不用配置"/logout"请求
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        //关闭跨域请求伪造
        http.csrf().disable();

        //配置自定义的无权限访问处理器
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
    }
}