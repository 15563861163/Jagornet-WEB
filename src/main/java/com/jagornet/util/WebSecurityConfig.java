package com.jagornet.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;

/**
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 15:01 2018/3/1
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter{

    public final static String SESSION_KEY = "name";
    @Bean
    public SecurityInterceptor getSecurityInterceptor() {return new SecurityInterceptor();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(getSecurityInterceptor());

        //删除配置
        registration.excludePathPatterns("/error");
        //拦截配置
        registration.addPathPatterns("/Jargornet-redis/*");
        super.addInterceptors(registry);
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_KEY) != null) {
                return true;
            }

            //跳转指定
            String url = "/Jargornet-redis";
            response.sendRedirect(url);
            return false;
        }
    }
}
