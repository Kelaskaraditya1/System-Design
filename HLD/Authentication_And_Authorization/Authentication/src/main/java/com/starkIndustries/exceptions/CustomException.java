package com.starkIndustries.exceptions;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException {

  public HttpStatus httpStatus;
  public String message;
  public LocalDateTime localDateTime;

  public CustomException(HttpStatus httpStatus, String message){
    super(message);
    this.httpStatus=httpStatus;
    this.message=message;
    this.localDateTime=LocalDateTime.now();
  }

  
}