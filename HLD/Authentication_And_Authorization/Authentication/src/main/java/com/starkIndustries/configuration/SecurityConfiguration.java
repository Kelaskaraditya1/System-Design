package com.starkIndustries.configuration;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.starkIndustries.exceptions.CustomException;
import com.starkIndustries.filter.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

  private UserDetailsService userDetailsService;
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfiguration(
    UserDetailsService userDetailsService,
    JwtAuthenticationFilter  jwtAuthenticationFilter
  ){
    this.userDetailsService = userDetailsService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public BCryptPasswordEncoder getBCryptPasswordEncoder(){

    try{
          return new BCryptPasswordEncoder(12);
    }catch(Exception e){
      log.error("SecurityConfiguration :: getBCryptPasswordEncoder() : Error in Bcrypt Passowrd Encoder: {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"SecurityConfiguration :: getBCryptPasswordEncoder() : Error in Bcrypt Passowrd Encoder: "+e.getMessage());
    }

  }

  @Bean
  public AuthenticationProvider getAuthenticationProvider(){

    try{

          DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(getBCryptPasswordEncoder());

    return daoAuthenticationProvider;

    }catch(Exception e){
      log.error("SecurityConfiguration :: getAuthenticationProvider() : Error occured in Authentication Provider: {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"SecurityConfiguration :: getAuthenticationProvider() : Error occured in Authentication Provider: "+e.getMessage());
    }

  }

  @Bean
  public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration){
  
    try{
      return authenticationConfiguration.getAuthenticationManager();
    }catch(Exception e){
            log.error("SecurityConfiguration :: gAuthenticationManager() : Error occured in Authentication Manager: {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"SecurityConfiguration :: gAuthenticationManager() : Error occured in Authentication Manager: "+e.getMessage());
    }
  }

  @Bean
  public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity){

    SecurityFilterChain securityFilterChain = null;

    try{

      securityFilterChain = httpSecurity.csrf(csrf->csrf.disable())
        .cors(cors->cors.configurationSource(corsConfigurationSource()))
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(getAuthenticationProvider())
        .authorizeHttpRequests(
          request->request.requestMatchers("/auth/signup","/auth/login", "/auth/refresh-token")
            .permitAll()
            .anyRequest()
            .authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
        .build();

      return securityFilterChain;

    }catch(Exception e){
      log.error("SecurityConfiguration :: getSecurityFilterChain() : Error occured in SecurityFilterChain {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"SecurityConfiguration :: getSecurityFilterChain() : Error occured in SecurityFilterChain "+e.getMessage());
    }

  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(){

    try{
      
    CorsConfiguration corsConfiguration = new CorsConfiguration();

    corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://192.168.0.105:3000", "http://192.168.96.1:3000"));
    corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
    corsConfiguration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

    return urlBasedCorsConfigurationSource;

    }catch(Exception e){
            log.error("SecurityConfiguration :: corsConfigurationSource() : Error occured in Cors Configuration {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"SecurityConfiguration :: corsConfigurationSource() : Error occured in Cors Configuration "+e.getMessage());
    }

  }
  
}
