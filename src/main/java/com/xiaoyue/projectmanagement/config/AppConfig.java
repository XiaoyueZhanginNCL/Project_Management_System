package com.xiaoyue.projectmanagement.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
//@EnableWebSecurity
public class AppConfig {
    @Bean//表示该方法会产生一个对象被spring的ioc容器所管理，该对象可以在整个应用中被其他地方引用和使用
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{//所有的HTTP请求都会经过这个过滤器链来进行安全检查。throws Exception意味着方法可能会抛出异常，需要调用者处理这些异常
        http.authorizeHttpRequests(configurer->
                configurer.requestMatchers("/api/**").authenticated()// 以/api/开头的所有请求需要进行用户认证，即需要用户登录后才能访问
                        .anyRequest().permitAll()) //除了/api/**路径以外的所有其他请求都允许未经认证的访问，即不用登录即可访问
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)//添加自定义的JWT令牌验证过滤器，用于在基本认证过滤器之前验证JWT令牌的有效性
                .csrf(csrf->csrf.disable())
                .cors(cors->cors.configurationSource(configurationSource()));//配置跨域资源共享（CORS），使得应用可以允许来自不同域的请求

        return http.build();//构建并返回一个SecurityFilterChain对象，返回一个包含上述安全配置的SecurityFilterChain实例，Spring Security会使用这个实例来处理HTTP请求的安全性
    }

    private CorsConfigurationSource configurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg =new CorsConfiguration();
                cfg.setAllowedOrigins(Arrays.asList(
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "http://localhost:4200"
                ));//允许从指定的域（即这些localhost地址）发起跨域请求。例如，前端开发时，常见的端口3000、5173和4200都被允许访问后端API
                cfg.setAllowedMethods(Collections.singletonList("*"));//允许使用所有HTTP方法进行跨域请求
                cfg.setAllowCredentials(true);//是否允许跨域请求携带认证信息（如Cookies、HTTP认证头）,适用于需要身份验证的场景。
                cfg.setAllowedHeaders(Collections.singletonList("*"));//许客户端在跨域请求中使用任意的HTTP头信息，例如自定义的头信息。
                cfg.setExposedHeaders(Arrays.asList("Authorization"));//指定哪些头信息在响应时可以暴露给客户端，这里是“Authorization”头。允许客户端在处理响应时读取特定的头信息，比如 Authorization（通常用来传递JWT令牌）
                cfg.setMaxAge(3600L);//设置浏览器缓存预检请求的最长时间，这里为3600秒（1小时）作用： 减少浏览器发送预检请求的次数，从而提高性能。预检请求是浏览器在发出实际跨域请求之前所做的一次检查请求(一小时检查一次)。
                return cfg;
            }
        };

    }

    @Bean//在将用户信息存储进数据库之前bcrypt password
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
