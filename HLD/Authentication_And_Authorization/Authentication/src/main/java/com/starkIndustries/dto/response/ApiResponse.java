package com.starkIndustries.dto.response;

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
public class ApiResponse<T> {

  public int statusCode;
  public String status;
  public String message;
  public T data;
  public Object errors;
  public boolean success;
  public LocalDateTime timeStamp;

  public static <T> ApiResponse<T> successResponse(String message, T data){

    return ApiResponse.<T>builder()
    .statusCode(HttpStatus.OK.value())
    .status(HttpStatus.valueOf(HttpStatus.OK.value()).name())
    .message(message)
    .data(data)
    .success(true)
    .timeStamp(LocalDateTime.now())
    .build();

  }

  public  static <T> ApiResponse<T> failureResponse(HttpStatus statusCode, String message, Object errors){

    return ApiResponse.<T>builder()
    .statusCode(statusCode.value())
    .status(HttpStatus.valueOf(statusCode.value()).name())
    .message(message)
    .errors(errors)
    .success(false)
    .timeStamp(LocalDateTime.now())
    .build();

  }
  
}
