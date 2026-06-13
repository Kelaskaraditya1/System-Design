package com.starkIndustries.restApi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.starkIndustries.restApi.dto.request.BulkEntriesReport;
import com.starkIndustries.restApi.dto.request.UserRequest;
import com.starkIndustries.restApi.dto.request.UsersBulkResponse;
import com.starkIndustries.restApi.dto.response.ApiResponse;
import com.starkIndustries.restApi.exception.CustomException;
import com.starkIndustries.restApi.keys.Keys;
import com.starkIndustries.restApi.model.Idempotency;
import com.starkIndustries.restApi.model.Users;
import com.starkIndustries.restApi.repository.IdempotencyRepository;
import com.starkIndustries.restApi.repository.UsersRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsersService {

  @Autowired
  private EmailService emailService;

  @Autowired
  private IdempotencyRepository idempotencyRepository;

  @Autowired
  public UsersRepository usersRepository;

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

  public BulkEntriesReport performBulkUserSaving(List<UserRequest> userRequestList){

    int successEntriesCount = 0;
    List<UsersBulkResponse> failedEntries = new ArrayList<>();

    for(UserRequest userRequest: userRequestList){
      UsersBulkResponse usersBulkResponse = saveIndivisualUser(userRequest);

      if(usersBulkResponse.getSuccess())
        successEntriesCount++;
      else
        failedEntries.add(usersBulkResponse);
    }

    return BulkEntriesReport.builder()
      .totalEntries(userRequestList.size())
      .successEntriesCount(successEntriesCount)
      .failedEntries(failedEntries)
      .build();

  }

  public UsersBulkResponse saveIndivisualUser(UserRequest userRequest){

    UsersBulkResponse usersBulkResponse = UsersBulkResponse.builder()
      .build();

    if(this.usersRepository.existsByContact(userRequest.getContact())){

      usersBulkResponse.setSuccess(false);
      usersBulkResponse.setMessage("Contact "+userRequest.getContact()+" already taken");

      return usersBulkResponse;

    }

    if(this.usersRepository.existsByEmail(userRequest.getEmail())){

      usersBulkResponse.setSuccess(false);
      usersBulkResponse.setMessage("Email "+userRequest.getEmail()+" already taken");

      return usersBulkResponse;

    }

        if(this.usersRepository.existsByUsername(userRequest.getUsername())){

      usersBulkResponse.setSuccess(false);
      usersBulkResponse.setMessage("Username "+userRequest.getUsername()+" already taken");

      return usersBulkResponse;

    }

    Users users = Users.builder()
      .userId(UUID.randomUUID().toString())
      .name(userRequest.getName())
      .contact(userRequest.getContact())
      .email(userRequest.getEmail())
      .dateOfBirth(userRequest.getDateOfBirth())
      .username(userRequest.getUsername())
      .password(userRequest.getPassword())
      .build();

      this.usersRepository.save(users);

      usersBulkResponse.setSuccess(true);
      usersBulkResponse.setMessage(null);

      return usersBulkResponse;

      
  }

  
  
}
