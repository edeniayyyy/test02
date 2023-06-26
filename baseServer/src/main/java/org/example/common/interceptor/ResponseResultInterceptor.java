package org.example.common.interceptor;

import org.example.common.ResultResponse;
import org.example.common.annotation.logTest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ResponseResultInterceptor implements HandlerInterceptor {


    public static final String RESPONSE_RESULT = "RESPONSE-RESULT";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            System.out.println(handlerMethod);
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if (clazz.isAnnotationPresent(logTest.class)) {
                System.out.println("clazz======");
                request.setAttribute(RESPONSE_RESULT, clazz.getAnnotation(logTest.class));
            } else if (method.isAnnotationPresent(logTest.class)) {
                System.out.println("method======");
                request.setAttribute(RESPONSE_RESULT, method.getAnnotation(logTest.class));
            }
            System.out.println("=========RESPONSE-RESULT ======= " + clazz.getAnnotation(logTest.class));
            System.out.println(response.getHeaderNames());
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // nothing to do
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // nothing to do
    }
}
