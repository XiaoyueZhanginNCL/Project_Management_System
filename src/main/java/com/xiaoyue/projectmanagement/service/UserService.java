package com.xiaoyue.projectmanagement.service;

import com.xiaoyue.projectmanagement.model.User;

public interface UserService {

    User findUserProfileByJwt(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;

    User findUserById(Long id) throws Exception;

    User updateUserProjectSize(User user , int num) throws Exception;
}
