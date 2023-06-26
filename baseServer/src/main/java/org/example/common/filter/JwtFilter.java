package org.example.common.filter;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.example.common.ResultResponse;
import org.example.common.utils.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author edenia
 * @version 1.0
 * @date 2023/6/15 16:44
 */

public class JwtFilter extends BasicHttpAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String TOKEN = "Authorization";

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        log.info("jwt === mappedValue: " + mappedValue);
        log.info("path======:" + ((HttpServletRequest) request).getRequestURI());
        // 这里大家可以处理白名单逻辑，这里就不实现了 比如 /login 我们需要放行
        if (((HttpServletRequest) request).getRequestURI().equals("/user/login")) {
            return true;
        }

        if (isLoginAttempt(request, response)) {
            try {
                return executeLogin(request, response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.error("未传token {}", httpServletRequest.getRequestURI());
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(JSON.toJSONString(ResultResponse.error("未传token " + httpServletRequest.getRequestURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    @Override
    public boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(ResultResponse.error(request.getAttribute("fail").toString())));
        return super.onAccessDenied(request, response);

    }


    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(TOKEN);
        String parameter = req.getParameter("token");
        log.info("========parameter---" + parameter);
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(TOKEN);
        JwtToken jwtToken = new JwtToken(token);
        log.info("filter get token===" + token);
        try {
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (Exception e) {
            request.setAttribute("fail", e.getMessage());
            log.error("executeLogin {}", e.getMessage());
            return false;
        }
    }

    /**
     * 对跨域提供支持(注意生产)
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}

