package com.debug.kill.server.controller;/**
 * Created by Administrator on 2019/7/2.
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户controller
 * @Author:debug (SteadyJack)
 * @Date: 2019/7/2 17:45
 **/
@Controller
public class UserController {

    private static final Logger log= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private Environment env;

    /**
     * 跳到登录页
     * @return
     */
    @RequestMapping(value = {"/to/login","/unauth"})
    public String toLogin(){
        return "login";
    }

    /**
     * 登录认证
     * @param userName
     * @param password
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam String userName, @RequestParam String password, ModelMap modelMap){
        String errorMsg="";
        try {
            if (!SecurityUtils.getSubject().isAuthenticated()){ // 判断是否已认证通过
                String newPsd=new Md5Hash(password,env.getProperty("shiro.encrypt.password.salt")).toString();
                UsernamePasswordToken token=new UsernamePasswordToken(userName,newPsd); // 封装成token
                SecurityUtils.getSubject().login(token); // 登陆
            }
        }catch (UnknownAccountException e){
            errorMsg=e.getMessage();
            modelMap.addAttribute("userName",userName);
        }catch (DisabledAccountException e){
            errorMsg=e.getMessage();
            modelMap.addAttribute("userName",userName);
        }catch (IncorrectCredentialsException e){
            errorMsg=e.getMessage();
            modelMap.addAttribute("userName",userName);
        }catch (Exception e){
            errorMsg="用户登录异常，请联系管理员!";
            e.printStackTrace();
        }
        if (StringUtils.isBlank(errorMsg)){ // 如果没有错误，就重定向到index页面
            return "redirect:/index";
        }else{
            modelMap.addAttribute("errorMsg",errorMsg);
            return "login";
        }
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "login";
    }

    /**
     * todo:用户注册模块
     */
    //git study test1

//    git test3
//    git test4
//    git test6
//    git test7:a=2
//    git test8: push in another place, see what happen
}
