package com.starkIndustries.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.starkIndustries.dto.request.LoginRequest;
import com.starkIndustries.dto.request.SignupRequest;
import com.starkIndustries.dto.response.ApiResponse;
import com.starkIndustries.dto.response.LoginResponse;
import com.starkIndustries.dto.response.SignupResponse;
import com.starkIndustries.exceptions.CustomException;
import com.starkIndustries.models.Users;
import com.starkIndustries.repository.AuthenticationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {

  public AuthenticationRepository authenticationRepository;
  public BCryptPasswordEncoder bCryptPasswordEncoder;
  public AuthenticationManager authenticationManager;

  public AuthenticationService(
    AuthenticationRepository authenticationRepository,
    BCryptPasswordEncoder bCryptPasswordEncoder,
    AuthenticationManager authenticationManager
  ){
    this.authenticationRepository = authenticationRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.authenticationManager = authenticationManager;
  }

  @Value("${date.of.birth.format}")
  public String dateOfBirthFormat;

    public void checkIfSignupCredentialsExist(SignupRequest signupRequest){

    if(this.authenticationRepository.existsByContactNumber(signupRequest.getContactNumber())){
      log.info("AuthenticationService :: checkIfSignupCredentialsExist() : Contact Number {} already exists",signupRequest.getContactNumber());
      throw new CustomException(HttpStatus.BAD_REQUEST,"AuthenticationService :: checkIfSignupCredentialsExist() : Contact Number "+signupRequest.getContactNumber()+" already exists");
    }

      if(this.authenticationRepository.existsByEmailId(signupRequest.getEmailId())){
      log.info("AuthenticationService :: checkIfSignupCredentialsExist() : EmailId {} already exists",signupRequest.getEmailId());
      throw new CustomException(HttpStatus.BAD_REQUEST,"AuthenticationService :: checkIfSignupCredentialsExist() : EmailId "+signupRequest.getEmailId()+" already exists");
    }

        if(this.authenticationRepository.existsByUsername(signupRequest.getUsername())){
      log.info("AuthenticationService :: checkIfSignupCredentialsExist() : Username {} already exists",signupRequest.getUsername());
      throw new CustomException(HttpStatus.BAD_REQUEST,"AuthenticationService :: checkIfSignupCredentialsExist() : Username "+signupRequest.getUsername()+" already exists");
    }

    
  }
  
  public ApiResponse<SignupResponse> signup(SignupRequest signupRequest){

    if(signupRequest==null){
      log.error("AuthenticationService :: signup() : Signup Request is null");
      throw new CustomException(HttpStatus.BAD_REQUEST,"AuthenticationService :: signup() : Signup Request is null");
    }

    String contactNumber = new StringBuffer().append("+91").append(signupRequest.getContactNumber()).toString();
    signupRequest.setContactNumber(contactNumber);
    signupRequest.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
    checkIfSignupCredentialsExist(signupRequest);
    Users users = SignupRequest.mapSignupRequestToUser(UUID.randomUUID().toString(), signupRequest);
    this.authenticationRepository.save(users);
    SignupResponse signupResponse = SignupResponse.mapUserToSignupResponse(users);
    return ApiResponse.successResponse("Signup Success for UserId "+users.getUserId(), signupResponse);

  }

  public ApiResponse<LoginResponse> login(LoginRequest loginRequest){

    if(loginRequest == null){
      log.error("AuthenticationService :: login() : Login Request is null");
      throw new CustomException(HttpStatus.BAD_REQUEST,"AuthenticationService :: login() : Login Request is null");
    }

    Optional<Users> optionalUsers = this.authenticationRepository.findByUsernameOrEmailId(loginRequest.getUsername(), loginRequest.getUsername());
    if(optionalUsers.isPresent()){
      Users users = optionalUsers.get();

      Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

      if(authentication.isAuthenticated()){
        log.info("Login Successful for {}",loginRequest.getUsername());
        LoginResponse loginResponse = LoginResponse.mapUsersToLoginResponse(users);
        return ApiResponse.successResponse("Login Successful for "+loginRequest.getUsername(), loginResponse);
      }else{
        log.error("AuthenticationService :: login() : Invalid credentials");
        throw new CustomException(HttpStatus.UNAUTHORIZED,"AuthenticationService :: login() : Invalid credentials");
      }

    }else{
      log.error("AuthenticationService :: login() : Username {} doesnot exists",loginRequest.getUsername());
      throw new CustomException(HttpStatus.UNAUTHORIZED,"AuthenticationService :: login() : Username "+loginRequest.getUsername()+" doesnot exists");
    }

  }
}
