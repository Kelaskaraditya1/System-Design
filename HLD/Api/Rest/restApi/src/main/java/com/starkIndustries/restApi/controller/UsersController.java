package com.starkIndustries.restApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.starkIndustries.restApi.dto.request.UserRequest;
import com.starkIndustries.restApi.dto.response.ApiResponse;
import com.starkIndustries.restApi.dto.response.UserResponse;
import com.starkIndustries.restApi.model.Users;
import com.starkIndustries.restApi.utility.Utility;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

  @Autowired
  public Utility utility;


  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> addUser(
    @Valid @RequestBody UserRequest userRequest
  ){
    Users users = UserRequest.toUsers(userRequest);
    UserResponse userResponse = UserResponse.toUsers(users);

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(userResponse));
  }


  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@PathVariable("userId") String userId){

    var users =  utility.getDummyUsers()
      .stream()
      .filter(user->user.userId==userId)
      .findFirst();

      if(users.isPresent())
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(users));
      else{
        log.error("user with userId {} not found", userId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureWithMessage(HttpStatus.BAD_REQUEST,"user with userId "+userId+" not found"));
      }
    
  }

  @GetMapping
  public ResponseEntity<?> getUsers(){
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(utility.getDummyUsers()));
  }
  
}
