package com.debug.kill.server.service;/**
 * Created by Administrator on 2019/7/2.
 */

import com.debug.kill.model.entity.User;
import com.debug.kill.model.mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * 用户自定义的realm-用于shiro的认证、授权 (这个是真正用于认证、授权的)
 * @Author:debug (SteadyJack)
 * @Date: 2019/7/2 17:55
 **/
public class CustomRealm extends AuthorizingRealm{

    private static final Logger log= LoggerFactory.getLogger(CustomRealm.class);

    private static final Long sessionKeyTimeOut=3600_000L; // 60min

    @Autowired
    private UserMapper userMapper;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证-登录
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override  //这个方法会获取到登陆时的信息，即UsernamePasswordToken
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        String userName=token.getUsername();
        String password=String.valueOf(token.getPassword());
        log.info("Current User={} PassWord={} ",userName,password);

        User user=userMapper.selectByUserName(userName);
        if (user==null){
            throw new UnknownAccountException("User Not Exist!");
        }
        if (!Objects.equals(1,user.getIsActive().intValue())){
            throw new DisabledAccountException("Current User Is Forbidden!");
        }
        if (!user.getPassword().equals(password)){
            throw new IncorrectCredentialsException("Username and Password is not Match!");
        }

        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user.getUserName(),password,getName());// 主体 密码 当前realm
        setSession("uid",user.getId());
        return info;
    }

    /**
     * 将key与对应的value塞入shiro的session中-最终交给HttpSession进行管理(如果是分布式session配置，那么就是交给redis管理)
     * @param key
     * @param value
     */
    private void setSession(String key,Object value){
        Session session=SecurityUtils.getSubject().getSession();
        if (session!=null){
            session.setAttribute(key,value);
            session.setTimeout(sessionKeyTimeOut);
        }
    }
}



















