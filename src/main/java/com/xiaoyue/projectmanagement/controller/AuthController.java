package com.xiaoyue.projectmanagement.controller;

import com.xiaoyue.projectmanagement.config.JwtProvider;
import com.xiaoyue.projectmanagement.model.User;
import com.xiaoyue.projectmanagement.repository.UserRepository;
import com.xiaoyue.projectmanagement.request.LoginRequest;
import com.xiaoyue.projectmanagement.response.AuthResponse;
import com.xiaoyue.projectmanagement.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody User user) throws Exception{

        User isExist = userRepository.findByEmail(user.getEmail());
        if(isExist!=null){
            throw new Exception("email already exist with another account"+user.getEmail());
        }

//        User createdUser = new User();
//        createdUser.setEmail(user.getEmail());
//        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
//        createdUser.setFullName(user.getFullName());

//        User savedUser = userRepository.save(createdUser);
//
        User savedUser = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt= JwtProvider.generateJwtToken(authentication);
        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("sign up success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);


    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication=autheticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateJwtToken(authentication);

        AuthResponse res=new AuthResponse();
        res.setMessage("signup success");
        res.setJwt(jwt);

        return new ResponseEntity<>(res,HttpStatus.CREATED);

    }

    private Authentication autheticate(String userName, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        if(userDetails==null){
            throw new BadCredentialsException("invalid username !");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("invalid password, doesn't match with the username !");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
