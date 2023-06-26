package org.example.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.example.POJO.domainmodel.User;
import org.example.common.annotation.logTest;
import org.example.common.ResultResponse;
import org.example.common.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class testController {

    @GetMapping("/test")
    @logTest
    @RequiresPermissions(value={"read"})
    public ResultResponse test(@RequestParam("test") String test) {
        Subject subject = SecurityUtils.getSubject();
        log.info("running test==== "+ JwtUtil.getUserName((String) subject.getPrincipal()));
        return ResultResponse.success(test);
    }

    @RequestMapping("/init")
    @logTest
    public String index() {
        log.info("running test====");
        return JSON.toJSONString("=== 未登录");
    }
}
