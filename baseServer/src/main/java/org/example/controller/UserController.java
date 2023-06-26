package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.example.POJO.domainmodel.User;
import org.example.common.BizException;
import org.example.common.ResultResponse;
import org.example.common.annotation.logTest;
import org.example.common.filter.JwtFilter;
import org.example.common.utils.JwtToken;
import org.example.common.utils.JwtUtil;
import org.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * users for system 前端控制器
 * </p>
 *
 * @author edenia
 * @since 2023-06-05
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @PostMapping("/registry")
    @logTest
    public ResultResponse registry(@RequestBody @Valid User user) {
        log.info("user ======" + user);
        if(userService.register(user.getUsername(), user.getPassword())) {
            return ResultResponse.success(user.getUsername());
        }
        else return ResultResponse.error("用户已存在" + user.getUsername());
    }

    @RequestMapping("/login")
    @logTest
//    @Cacheable(value = "test")
    public ResultResponse login(@RequestBody(required = false) @Valid User user) {
        log.info("登录用户信息" + user);
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword(), true);
        // 获取Subject对象
        Subject subject = SecurityUtils.getSubject();
        try {
            // 执行登录
            user = userService.login(user);
            log.info("数据库用户信息" + user);
            String sign = JwtUtil.sign(user);
//            subject.login(new JwtToken(sign));
            return ResultResponse.success(sign);
        } catch (UnknownAccountException e) {
            return ResultResponse.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return ResultResponse.error("IncorrectCredentialsException " + e.getMessage());
        } catch (LockedAccountException e) {
            return ResultResponse.error("LockedAccountException " + e.getMessage());
        } catch (AuthenticationException e) {
            return  ResultResponse.error("认证失败！");
        } catch (BizException e) {
            return ResultResponse.error("用户不存在！");
        }
    }

}

