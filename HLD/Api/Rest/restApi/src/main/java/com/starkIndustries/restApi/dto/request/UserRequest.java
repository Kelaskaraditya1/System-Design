package com.starkIndustries.restApi.dto.request;

import java.time.LocalDate;
import java.util.UUID;
import com.starkIndustries.restApi.model.Users;
import com.starkIndustries.restApi.validators.ValidUsername;
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
public class UserRequest {

  @NotBlank(message = "Name is required")
  public String name;

  @NotBlank(message = "Contact is required")
  @Pattern(regexp = "^[7-9]\\d{9}$", message = "Contact should be 10 digits")
  public String contact;

  @NotBlank(message = "Email is required")
  @Email
  public String email;

  @NotBlank(message = "Username is required")
  @ValidUsername
  public String username;

  @NotBlank(message = "Password is required")
  public String password;

  @NotNull(message = "DateOfBirth is required")
  @Past(message = "Date of birth should not be greater than current date")
  public LocalDate dateOfBirth;

  public static Users toUsers(UserRequest userRequest){
    return Users.builder()
      .userId(UUID.randomUUID().toString())
      .name(userRequest.name)
      .dateOfBirth(userRequest.dateOfBirth)
      .contact(userRequest.contact)
      .email(userRequest.email)
      .username(userRequest.username)
      .password(userRequest.password)
      .build();
  }
  
}


