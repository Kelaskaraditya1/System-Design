package com.starkIndustries.restApi.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.starkIndustries.restApi.dto.request.UserRequest;
import com.starkIndustries.restApi.dto.response.ApiResponse;
import com.starkIndustries.restApi.exception.CustomException;
import com.starkIndustries.restApi.model.Idempotency;
import com.starkIndustries.restApi.model.Users;
import com.starkIndustries.restApi.repository.IdempotencyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsersService {

  @Autowired
  private EmailService emailService;

  @Autowired
  private IdempotencyRepository idempotencyRepository;

  public Users saveUser(UserRequest userRequest){

    Users users = UserRequest.toUsers(userRequest);
    log.info("User with userId {}",users.userId);

    return users;
  }

  public ApiResponse<Object> signup(UserRequest userRequest, String idempotencyKey){

    if(idempotencyKey!=null){

      if(this.idempotencyRepository.existsById(idempotencyKey)){
        log.info("Request with idempotency Id {} already procesed",idempotencyKey);
        return ApiResponse.successWithDataAndMessage(null,"Duplicate request, user already added");
      }
      
    log.info("Processing a new request with Idempotency id {}.",idempotencyKey);

    saveUser(userRequest);

    Idempotency idempotency = Idempotency.builder()
        .idempotencyKey(idempotencyKey)
        .createdAt(LocalDateTime.now())
      .build();

    this.idempotencyRepository.save(idempotency);
    this.emailService.sendEmail(userRequest.email,userRequest.username);

    return ApiResponse.successWithDataAndMessage(null,"Signup Success");

    }else{
      log.error("Idempotency key is null!!");
      throw new CustomException(HttpStatus.BAD_REQUEST,"Idempotency key is null!!");
    }


  }
  
}
