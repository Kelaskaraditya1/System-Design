package com.starkIndustries.restApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.starkIndustries.restApi.dto.request.UserRequest;
import com.starkIndustries.restApi.dto.response.ApiResponse;
import com.starkIndustries.restApi.model.Users;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsersService {

  @Autowired
  private EmailService emailService;

  public Users saveUser(UserRequest userRequest){

    Users users = UserRequest.toUsers(userRequest);
    log.info("User with userId {}",users.userId);

    return users;
  }

  public ApiResponse<Object> signup(UserRequest userRequest){

    saveUser(userRequest);
    this.emailService.sendEmail(userRequest.email,userRequest.username);

    return ApiResponse.successWithDataAndMessage(null,"Signup Success");

  }
  
}
