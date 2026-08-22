package com.starkIndustries.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.starkIndustries.exceptions.CustomException;
import com.starkIndustries.keys.Keys;
import com.starkIndustries.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{

  private JwtService jwtService;
  private UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(
    JwtService jwtService,
    UserDetailsService userDetailsService
  ){
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


    String endPoint  = request.getServletPath();
    return endPoint.equals("/auth/signup") ||
    endPoint.equals("/auth/login") ||
    endPoint.equals("/auth/refresh-token");
    
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

        String bearerToken = null;
        String authToken = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;

        try{
            // Step 1: get Bearer token from "Authorization" Header from request
        bearerToken = request.getHeader(Keys.AUTHORIZATION);
        if(bearerToken==null || !bearerToken.startsWith("Bearer ")){
          log.error("JwtAuthenticationFilter :: doFilterInternal() :Auth Token is null or doesnot starts with Bearer ");
          filterChain.doFilter(request, response);
          return;
        }

        // Step 2: extract the Auth Token from BearerToken which is "Bearer <token>" <token> is auth token

        authToken = bearerToken.substring(7);
        username = this.jwtService.extractUserName(authToken);

        if(username==null){
          log.error("JwtAuthenticationFilter :: doFilterInternal() : Username is null");
          filterChain.doFilter(request, response);
          return;
        }

        // Step 3: Check if the user is already Authenticated or not

        if(SecurityContextHolder.getContext().getAuthentication() ==null){

          // Step 4: Extract UserDetails using UserDetailsService

          userDetails = this.userDetailsService.loadUserByUsername(username);
          if(userDetails!=null){

            // Step 5: Validate the JwtToken

            if(this.jwtService.isTokenValid(authToken, userDetails)){
              
            // Step 6: Generate the UsernameAndPasswordToken 
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

            // Adding the extra information  required using the request.
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Step 7: declaring that user is now Authenticated
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }else
              log.error("JwtAuthenticationFilter :: doFilterInternal() : Invalid JwtToken could not be validated");

          }else
            log.error("JwtAuthenticationFilter :: doFilterInternal() : No user found with the username {}",username);


        }else
          log.info("User with username {} is already authenticated",username);

          filterChain.doFilter(request, response);

        }catch(Exception e){
          log.error("JwtAuthenticationFilter :: doFilterInternal() : Error while verifying JwtToken: {}",e.getMessage());
          throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"JwtAuthenticationFilter :: doFilterInternal() : Error while verifying JwtToken: "+e.getMessage());
        }



  }
  
}
