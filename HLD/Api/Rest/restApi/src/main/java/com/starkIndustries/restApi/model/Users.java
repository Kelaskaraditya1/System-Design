package com.starkIndustries.restApi.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {

  @Id
  public String userId;
  public String name;
  public LocalDate dateOfBirth;
  public String contact;
  public String email;
  public String username;
  public String password;
  
}
