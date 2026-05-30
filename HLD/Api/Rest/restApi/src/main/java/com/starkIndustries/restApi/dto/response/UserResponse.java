package com.starkIndustries.restApi.dto.response;

import java.time.LocalDate;

import com.starkIndustries.restApi.model.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
@Builder
public class UserResponse {

  public String userId;
  public String name;

  @Schema(description = "Should start with 7,8,9", example = "8591059220")
  public String contact;

  @Schema(description = "domain should be @gmail.com", example = "kelaskaraditya1@gmail.com")
  public String email;
  public String username;
  public LocalDate dateOfBirth;

  public static UserResponse toUsers(Users users){
    return UserResponse.builder()
      .userId(users.userId)
      .name(users.name)
      .contact(users.contact)
      .email(users.email)
      .username(users.username)
      .dateOfBirth(users.dateOfBirth)
      .build();
  }
  
}
