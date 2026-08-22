package com.starkIndustries.dto.response;

import lombok.Data;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.starkIndustries.models.Gender;
import com.starkIndustries.models.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenResponse {

  public String userId;
  public String name;

  @JsonFormat(pattern = "dd-MM-yyyy")
  public LocalDate dateOfBirth;
  public Gender gender;
  public String contactNumber;
  public String emailId;
  public String username;
  public String jwtToken;
  public String refreshToken;
  public String tokenType;

  public static RefreshTokenResponse mapUsersToRefreshTokenResponse(Users users, String jwtToken, String refreshToken,  String tokenType){

    return RefreshTokenResponse.builder()
      .userId(users.getUserId())
      .name(users.getName())
      .dateOfBirth(users.getDateOfBirth())
      .gender(users.getGender())
      .contactNumber(users.getContactNumber())
      .emailId(users.getEmailId())
      .username(users.getUsername())
      .jwtToken(jwtToken)
      .refreshToken(refreshToken)
      .tokenType(tokenType)
      .build();

  }
  
}
