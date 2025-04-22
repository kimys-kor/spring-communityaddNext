package com.community.api.common.ipauthentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminPathIntercepter()).addPathPatterns("/admin/**");
        registry.addInterceptor(userPathIntercepter()).addPathPatterns("/user/**");
//        registry.addInterceptor(guestPathIntercepter()).addPathPatterns("/guest/**");
    }

    @Bean
    public AdminPathIntercepter adminPathIntercepter() {
        return new AdminPathIntercepter();
    }

    @Bean
    UserPathIntercepter userPathIntercepter() {
        return new UserPathIntercepter();
    }

    @Bean
    GuestPathIntercepter guestPathIntercepter() {
        return new GuestPathIntercepter();
    }
}
