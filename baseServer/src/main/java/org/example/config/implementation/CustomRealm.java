package org.example.config.implementation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.example.POJO.domainmodel.User;
import org.example.common.filter.JwtFilter;
import org.example.common.utils.JwtUtil;
import org.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;


/**
 * @author edenia
 * @version 1.0
 * @date 2023/6/8 17:01
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserServiceImpl userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        //权限
        User user = userService.getById(JwtUtil.getUserId(token.toString()));
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
        log.info("当前用户=========" + user);
        String userName = user.getUsername();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色
        Set<String> roleSet = new HashSet<>();
        String role = userService.getRole(userName);
        roleSet.add(role);
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限
        String permission = userService.getPermission(userName);
        Set<String> permissionSet = new HashSet<String>();
        permissionSet.add(permission);
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

//      获取用户输入的用户名和密码
//        String userName = (String) token.getPrincipal();
//        String password = new String((char[]) token.getCredentials());

        String token = (String) authenticationToken.getCredentials();
        String userId = JwtUtil.getUserId(token);


        User user = userService.getById(userId);
        System.out.println("用户" + user.getUsername() + "认证-----ShiroRealm.doGetAuthenticationInfo");
//        log.info("数据库用户信息" + user);
        // 通过用户名到数据库查询用户信息
//
//        if (user == null) {
//            throw new UnknownAccountException("用户不存在！");
//        }
//        if (!User.codePassword(password, user.getSalt()).equals(user.getPassword())) {
//            log.info("密码不相等{} ==== {}", User.codePassword(password, user.getSalt()), user.getPassword());
//            throw new IncorrectCredentialsException("用户名或密码错误！");
//        }
//        if (user.getStatus() == 0) {
//            throw new LockedAccountException("账号已被锁定,请联系管理员！");
//        }
//        return new SimpleAuthenticationInfo(user, password, ByteSource.Util.bytes(user.getSalt()), "");
//
        if (StringUtils.isBlank(userId)) {
            throw new AuthenticationException("验证失败");
        }
        User userBean = new User();
        userBean.setId(userId);
        userBean.setRoleId(user.getRoleId());
        log.info("the token =====" + token);
        if (!JwtUtil.verify(token, user)) {
            throw new AuthenticationException("token失效");
        }
        return new SimpleAuthenticationInfo(token, token, "shiroJwtRealm");


    }
}
