package com.starkIndustries.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starkIndustries.keys.Keys;
import com.starkIndustries.models.Gender;
import com.starkIndustries.models.Users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {

  @NotBlank(message = "Name should not be null or blank")
  public String name;

  @Past(message = "Date Of Birth should not be greater than current date")
  @JsonFormat(pattern = "dd-MM-yyyy")
  public LocalDate dateOfBirth;

  public Gender gender;

  @NotBlank(message = "Contact Number should not be null")
  @Pattern(regexp = "^[7-9]\\d{9}$", message = "Contact should be 10 digits")
  public String contactNumber;

  @NotBlank(message = "Email Id should not be null or blank")
  @Email(message = "Enter valid Email Id")
  public String emailId;

  @NotBlank(message = "username should not be null or blank")
  public String username;

  @NotBlank(message = "Password should not be null or blank")
  public String password;

  public static Users mapSignupRequestToUser(String userId, SignupRequest signupRequest){

    return Users.builder()
      .userId(userId)
      .name(signupRequest.getName())
      .dateOfBirth(signupRequest.getDateOfBirth())
      .gender(signupRequest.getGender())
      .contactNumber(signupRequest.getContactNumber())
      .emailId(signupRequest.getEmailId())
      .username(signupRequest.getUsername())
      .password(signupRequest.getPassword())
      .build();

  }

  
}
