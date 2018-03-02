package com.jagornet.util;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;


/**w
 * @Author:LiuXinJian
 * @Description:
 * @Date:Created in 17:27 2018/2/28
 */
@Configuration
@EnableAutoConfiguration
public class FileUploadConfiguration {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        // 设置文件大小限制 ,超出设置页面会抛出异常信息，
        // 这样在文件上传的地方就需要进行异常信息的处理了;
        multipartConfigFactory.setMaxFileSize("1MB");
        // 设置总上传数据总大小
        multipartConfigFactory.setMaxRequestSize("2MB");
        // Sets the directory location where files will be stored.
        // factory.setLocation("路径地址");
        //multipartConfigFactory.setLocation("路径地址");
        return multipartConfigFactory.createMultipartConfig();
    }
}
