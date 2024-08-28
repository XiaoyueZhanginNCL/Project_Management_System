package com.xiaoyue.projectmanagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {//确保每次 HTTP 请求仅被过滤一次
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt=request.getHeader(JwtConstant.JWT_HEADER);//Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.OeZ5KNL1MnmwR8fn3kVQfZsIHtPA5LLlZIz1NmFCJbU

        if(jwt!=null){

            jwt=jwt.substring(7);//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.OeZ5KNL1MnmwR8fn3kVQfZsIHtPA5LLlZIz1NmFCJbU

            try {
                SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());//使用预定义密钥字符串创建的密钥

                Claims claims= Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = claims.get("email", String.class);
                String authorities = claims.get("authorities", String.class);

                List<GrantedAuthority> auths= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication=new UsernamePasswordAuthenticationToken(email,null,auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                throw new BadCredentialsException("invalid token............");
            }

        }


    }
}
