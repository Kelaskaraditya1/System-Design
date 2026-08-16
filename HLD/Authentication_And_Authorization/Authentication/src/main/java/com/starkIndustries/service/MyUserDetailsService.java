package com.starkIndustries.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.starkIndustries.exceptions.CustomException;
import com.starkIndustries.models.UserPrincipal;
import com.starkIndustries.models.Users;
import com.starkIndustries.repository.AuthenticationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService{

  public AuthenticationRepository  authenticationRepository;

  public MyUserDetailsService(
    AuthenticationRepository authenticationRepository
  ){
    this.authenticationRepository=authenticationRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    try{
      Optional<Users> optionalUser = this.authenticationRepository.findByUsernameOrEmailId(username, username);
      if(optionalUser.isPresent())
        return new UserPrincipal(optionalUser.get());
      else{
        log.error("MyUserDetailsService :: loadUserByUsername() : User with username {} not found",username);
        throw new CustomException(HttpStatus.NOT_FOUND,"MyUserDetailsService :: loadUserByUsername() : User with username "+username+" not found");
      }

    }catch(Exception e){
      log.error("MyUserDetailsService :: loadUserByUsername() : Error in finding User Principal: {}",e.getMessage());
      throw new CustomException(HttpStatus.NOT_FOUND, "MyUserDetailsService :: loadUserByUsername() : Error in finding User Principal: "+e.getMessage());
    }
  }
  
}
