package com.starkIndustries.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.starkIndustries.dto.request.LoginRequest;
import com.starkIndustries.dto.request.SignupRequest;
import com.starkIndustries.dto.response.ApiResponse;
import com.starkIndustries.dto.response.JwtValidationResponse;
import com.starkIndustries.dto.response.LoginResponse;
import com.starkIndustries.dto.response.RefreshTokenResponse;
import com.starkIndustries.dto.response.SignupResponse;
import com.starkIndustries.exceptions.CustomException;
import com.starkIndustries.keys.Keys;
import com.starkIndustries.models.Users;
import com.starkIndustries.repository.AuthenticationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {

  private AuthenticationRepository authenticationRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private AuthenticationManager authenticationManager;
  private JwtService jwtService;
  private UserDetailsService userDetailsService;

  public AuthenticationService(
    AuthenticationRepository authenticationRepository,
    BCryptPasswordEncoder bCryptPasswordEncoder,
    AuthenticationManager authenticationManager,
    JwtService jwtService,
    UserDetailsService userDetailsService
  ){
    this.authenticationRepository = authenticationRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userDetailsService=userDetailsService;
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

    String contactNumber = null;
    Users users = null;
    SignupResponse signupResponse = null;

    try{

    contactNumber = new StringBuffer().append("+91").append(signupRequest.getContactNumber()).toString();
    signupRequest.setContactNumber(contactNumber);
    signupRequest.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
    checkIfSignupCredentialsExist(signupRequest);
    users = SignupRequest.mapSignupRequestToUser(UUID.randomUUID().toString(), signupRequest);
    this.authenticationRepository.save(users);
    signupResponse = SignupResponse.mapUserToSignupResponse(users,this.jwtService.generateJwtToken(users), this.jwtService.generateRefreshToken(users), Keys.BEARER_TOKEN);
    return ApiResponse.successResponse("Signup Success for UserId "+users.getUserId(), signupResponse);

    }catch(Exception e){
      log.error("AuthenticationService :: signup() : Error while signup : {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "AuthenticationService :: signup() : Error while signup :"+e.getMessage());
    }



  }

  public ApiResponse<LoginResponse> login(LoginRequest loginRequest){

    if(loginRequest == null){
      log.error("AuthenticationService :: login() : Login Request is null");
      throw new CustomException(HttpStatus.BAD_REQUEST,"AuthenticationService :: login() : Login Request is null");
    }

    LoginResponse loginResponse = null;
    Optional<Users> optionalUsers = null;
    Authentication authentication = null;

    try{

          optionalUsers = this.authenticationRepository.findByUsernameOrEmailId(loginRequest.getUsername(), loginRequest.getUsername());
    if(optionalUsers.isPresent()){
      Users users = optionalUsers.get();

      authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      if(authentication.isAuthenticated()){
        log.info("Login Successful for {}",loginRequest.getUsername());
        loginResponse = LoginResponse.mapUsersToLoginResponse(users,this.jwtService.generateJwtToken(users), this.jwtService.generateRefreshToken(users), Keys.BEARER_TOKEN);
        return ApiResponse.successResponse("Login Successful for "+loginRequest.getUsername(), loginResponse);
      }else{
        log.error("AuthenticationService :: login() : Invalid credentials");
        throw new CustomException(HttpStatus.UNAUTHORIZED,"AuthenticationService :: login() : Invalid credentials");
      }

    }else{
      log.error("AuthenticationService :: login() : Username {} doesnot exists",loginRequest.getUsername());
      throw new CustomException(HttpStatus.UNAUTHORIZED,"AuthenticationService :: login() : Username "+loginRequest.getUsername()+" doesnot exists");
    }

    }catch(Exception e){
      log.error("AuthenticationService :: login() : Error occured while login: {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "AuthenticationService :: login() : Error occured while login: "+e.getMessage());
    }

  }

  public ApiResponse<JwtValidationResponse> validateJwtToken(String authToken){

    String username = null;

    try{

      username = this.jwtService.extractUserName(authToken);
      if(username==null || username.isBlank()){
        log.error("AuthenticationService :: validateJwtToken() : Username not found in Jwt Token");
        throw new CustomException(HttpStatus.UNAUTHORIZED, "AuthenticationService :: validateJwtToken() : Username not found in Jwt Token");
      }

      Optional<Users> users = this.authenticationRepository.findByUsername(username);
      if(users.isPresent()){

        UserDetails userDetails = new com.starkIndustries.models.UserPrincipal(users.get());
        if(this.jwtService.isTokenValid(authToken, userDetails)){
          Map<String,Object> claims = this.jwtService.extractClaims(authToken);
          log.info("User Id: {}",claims.get(Keys.USER_ID));
          log.info("Role: {}",claims.get(Keys.ROLE));
          return ApiResponse.successResponse(username,JwtValidationResponse.mapUsersToJwtValidationResponse(users.get()));
        }
        else
          return ApiResponse.failureResponse(HttpStatus.UNAUTHORIZED,"Unable to validate JWT Authentication",null);

      }else{
        log.error("AuthenticationService :: validateJwtToken() : User with username {} doesnot exists in the database",username);
        throw new CustomException(HttpStatus.UNAUTHORIZED,"AuthenticationService :: validateJwtToken() : User with username "+username+" doesnot exists in the database");
      }


    }catch(Exception e){
      log.error("AuthenticationService :: validateJwtToken() :Error while validating Jwt Token :{}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"AuthenticationService :: validateJwtToken() : Error while validating Jwt Token: "+e.getMessage());
    }

  } 

  public ApiResponse<RefreshTokenResponse> regenerateJwtTokenUsingRefereshToken(String refreshToken){
    String username = null;
    UserDetails userDetails = null;
    Optional<Users> optionalUsers = null;

    try{
        username = this.jwtService.extractUserName(refreshToken);
        optionalUsers = this.authenticationRepository.findByUsername(username);

        if(optionalUsers.isPresent()){
          Users users = optionalUsers.get();
          userDetails = new com.starkIndustries.models.UserPrincipal(users);
          if(this.jwtService.isTokenValid(refreshToken, userDetails)){

            RefreshTokenResponse refreshTokenResponse = RefreshTokenResponse.mapUsersToRefreshTokenResponse(users, this.jwtService.generateJwtToken(users), refreshToken, Keys.BEARER_TOKEN);
            return ApiResponse.successResponse(null, refreshTokenResponse);

          }else{
            log.error("AuthenticationService :: regenerateJwtTokenUsingRefereshToken() : Token is invalid or expired");
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"AuthenticationService :: regenerateJwtTokenUsingRefereshToken() : Token is invalid or expired");
          }

        }else{ 
        log.error("AuthenticationService :: regenerateJwtTokenUsingRefereshToken() : User with username {} does not exists",username);
        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"AuthenticationService :: regenerateJwtTokenUsingRefereshToken() : User with username "+username+" does not exists");
        }

    }catch(Exception e){
      log.error("AuthenticationService :: regenerateJwtTokenUsingRefereshToken() : error while regerating Jwt Token: {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "AuthenticationService :: regenerateJwtTokenUsingRefereshToken() : error while regerating Jwt Token: "+e.getMessage());
    }


  }
}
