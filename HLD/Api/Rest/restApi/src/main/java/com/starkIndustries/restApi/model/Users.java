package com.starkIndustries.restApi.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {

  public String userId;
  public String name;
  public LocalDate dateOfBirth;
  public String contact;
  public String email;
  public String username;
  public String password;
  
}
