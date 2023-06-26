package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.example.POJO.domainmodel.User;
import org.example.common.BizException;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * users for system 服务实现类
 * </p>
 *
 * @author edenia
 * @since 2023-06-05
 */
@Service
@Slf4j
@Cacheable(value = "userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public boolean register(String username, String password){
        User user = new User(username, password);
        user.codePassword(password);
        log.info("user========" + user);
        User exsit = getUserByName(username);
        if (exsit != null) {
            log.info("用户已存在" + exsit);
            return false;
        }
        this.save(user);
        return true;
    }

    public User login(User user) {
        User exist = this.getUserByName(user.getUsername());
        if (exist == null) throw new BizException("用户不存在");
        System.out.println("用户" + user.getUsername() + "认证-----ShiroRealm.doGetAuthenticationInfo");
        // 通过用户名到数据库查询用户信息
//
//        if (user == null) {
//            throw new UnknownAccountException("用户不存在！");
//        }
        if (!User.codePassword(user.getPassword(), exist.getSalt()).equals(exist.getPassword())) {
            log.info("密码不相等{} ==== {}", User.codePassword(user.getPassword(), exist.getSalt()), exist.getPassword());
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        if (exist.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }
        return exist;
//        return new SimpleAuthenticationInfo(user, password, ByteSource.Util.bytes(user.getSalt()), "");

    }



    public User getUserByName(String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, name);
        return this.getOne(wrapper);
    }

    public String getRole(String userName) {
        return "test_role";
    }

    public String getPermission(String userName) {
        return "read";
    }

}
