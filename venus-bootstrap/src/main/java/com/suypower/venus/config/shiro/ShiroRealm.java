package com.suypower.venus.config.shiro;

import com.suypower.venus.entity.User;
import com.suypower.venus.service.IUserService;
import com.suypower.venus.utils.UserUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IUserService userService;

    /**
     * 这里做权限控制
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 这里做登录控制
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("验证当前Subject时获取到token为：" + token.toString());
        //查出是否有此用户
        String username = token.getUsername();
        User user = new User(username);
        User find = userService.getUser(user);
        String userPassword = userService.getUserPassword(user);
        return new SimpleAuthenticationInfo(find, userPassword, getName());


    }






}
