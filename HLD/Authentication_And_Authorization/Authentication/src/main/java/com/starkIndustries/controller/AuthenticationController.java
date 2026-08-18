package com.starkIndustries.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starkIndustries.dto.request.LoginRequest;
import com.starkIndustries.dto.request.SignupRequest;
import com.starkIndustries.dto.response.ApiResponse;
import com.starkIndustries.dto.response.LoginResponse;
import com.starkIndustries.dto.response.SignupResponse;
import com.starkIndustries.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private AuthenticationService  authenticationService;

  public AuthenticationController(
    AuthenticationService authenticationService
  ){
    this.authenticationService = authenticationService;
  }

  @PostMapping(value = "/signup")
  public ResponseEntity<ApiResponse<SignupResponse>> signup(
    @Valid @RequestBody SignupRequest signupRequest
  ){

    ApiResponse<SignupResponse> signupResponse = this.authenticationService.signup(signupRequest);
    return ResponseEntity.status(signupResponse.getStatusCode()).body(signupResponse);

  }

  @PostMapping(value = "/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
    @Valid @RequestBody LoginRequest loginRequest
  ){

    ApiResponse<LoginResponse> loginResponse = this.authenticationService.login(loginRequest);
    return ResponseEntity.status(loginResponse.getStatusCode()).body(loginResponse);

  }

  @GetMapping("/greetings")
  public ResponseEntity<Map<String,String>> greetings(){

    Map<String, String> response = new HashMap<>();

    response.put("status","Up");
    response.put("message","Greetings, I am Optimus Prime!!");

    return ResponseEntity.status(HttpStatus.OK).body(response);

  }
  
}
