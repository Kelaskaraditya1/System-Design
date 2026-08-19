package com.starkIndustries.dto.response;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starkIndustries.models.Gender;
import com.starkIndustries.models.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtValidationResponse {

  public String userId;
  public String name;

  @JsonFormat(pattern = "dd-MM-yyyy")
  public LocalDate dateOfBirth;
  public Gender gender;
  public String contactNumber;
  public String emailId;
  public String username;

  public static JwtValidationResponse mapUsersToJwtValidationResponse(Users users){

    return JwtValidationResponse.builder()
      .userId(users.getUserId())
      .name(users.getName())
      .dateOfBirth(users.getDateOfBirth())
      .gender(users.getGender())
      .contactNumber(users.getContactNumber())
      .emailId(users.getEmailId())
      .username(users.getUsername())
      .build();

  }
  
}
