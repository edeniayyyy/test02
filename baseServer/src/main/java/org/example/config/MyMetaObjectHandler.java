package org.example.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.example.common.utils.JwtUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author edenia
 * @version 1.0
 * @date 2023/6/21 15:03
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    public String getUser() {
        Subject subject = SecurityUtils.getSubject();
        return JwtUtil.getUserName((String) subject.getPrincipal());
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }
}
