package com.starkIndustries.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Users")
public class Users {

  @Id
  public String userId;

  public String name;

  public LocalDate dateOfBirth;

  @Enumerated(EnumType.STRING)
  public Gender gender;

  public String contactNumber;

  public String emailId;

  public String username;
  
  public String password;

  @CreationTimestamp
  public LocalDateTime createdAt;

}
