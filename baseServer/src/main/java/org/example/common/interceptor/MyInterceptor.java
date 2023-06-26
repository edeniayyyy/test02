package org.example.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        try {
//            RequestWrapper requestWrapper = new RequestWrapper(request);
            System.out.println("========     package org.example.common.interceptor MyInterceptor    ===================");
            HttpServletRequestWrapper httpRequestWrapper = new HttpServletRequestWrapper(request);
            log.info("request====" + request.toString());
            System.out.println(httpRequestWrapper.getPathInfo());
            System.out.println(httpRequestWrapper.getServerPort());
        } catch (Exception e) {
            //
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //
    }
}
