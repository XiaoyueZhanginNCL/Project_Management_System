package com.xiaoyue.projectmanagement.service;

import com.xiaoyue.projectmanagement.config.JwtProvider;
import com.xiaoyue.projectmanagement.model.User;
import com.xiaoyue.projectmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email=JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("user doesn't exist");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("user doesn't exist");
        }
        return user;
    }

    @Override
    public User findUserById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new Exception("user doesn't exist");
        }
        return user.get();
    }

    @Override
    public User updateUserProjectSize(User user, int num) throws Exception {
        user.setProjectSize(user.getProjectSize()+num);
        return userRepository.save(user);
    }
}
