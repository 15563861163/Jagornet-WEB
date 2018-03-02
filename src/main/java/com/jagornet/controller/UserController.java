package com.jagornet.controller;

import com.jagornet.util.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 14:23 2018/3/1
 */
@Controller
@Configuration
@EnableAutoConfiguration
public class UserController {

    @Value("${admin.name}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @RequestMapping("/Jargornet-redis")
    public String index() {

        return "Sign";
    }

    @RequestMapping(value = "/Jargornet-redisLogin",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("password") String password, HttpSession session) {
        if (!password.equals(getName())) {
            session.removeAttribute(WebSecurityConfig.SESSION_KEY);
            return "密码不符";
        }
        session.setAttribute(WebSecurityConfig.SESSION_KEY,password);
        return "success";
    }


}
