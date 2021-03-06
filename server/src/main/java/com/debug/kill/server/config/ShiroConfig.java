package com.debug.kill.server.config;/**
 * Created by Administrator on 2019/7/2.
 */

import com.debug.kill.server.service.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * shiro的通用化配置
 * @Author:debug (SteadyJack)
 * @Date: 2019/7/2 17:54
 **/
@Configuration
public class ShiroConfig {

    @Bean
    public CustomRealm customRealm(){
        return new CustomRealm();
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm()); // 设置 用户自定义的 realm 用于shiro的认证、授权
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl("/to/login");
        bean.setUnauthorizedUrl("/unauth"); // 没有授权的  TODO 但有什么区别呢？

        Map<String, String> filterChainDefinitionMap=new HashMap<>();
        filterChainDefinitionMap.put("/to/login","anon");

        filterChainDefinitionMap.put("/**","anon");

        filterChainDefinitionMap.put("/kill/execute/*","authc"); // 这个目录下的请求都需要认证
        filterChainDefinitionMap.put("/item/detail/*","authc"); // 同上

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap); // 设置过滤器链
        return bean;
    }

}




























