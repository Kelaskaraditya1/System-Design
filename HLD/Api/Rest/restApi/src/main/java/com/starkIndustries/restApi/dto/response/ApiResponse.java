  package com.starkIndustries.restApi.dto.response;

  import org.springframework.http.HttpStatus;

  import lombok.*;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public class ApiResponse<T> {

    public int statusCode;
    public String status;
    public T data;
    public String message;
    public Object errors;
    public boolean success;
    public long timeStamp;

    public static <T> ApiResponse<T> successWithDataAndMessage(T data, String message){

    return ApiResponse.<T>builder()
      .statusCode(HttpStatus.OK.value())
      .status(HttpStatus.valueOf(HttpStatus.OK.value()).name())
      .data(data)
      .message(message)
      .success(true)
      .timeStamp(System.currentTimeMillis())
      .build();

  }

  public static <T> ApiResponse<T> sucessWithData(T data){

    return ApiResponse.<T>builder()
    .statusCode(HttpStatus.OK.value())
    .status(HttpStatus.valueOf(HttpStatus.OK.value()).name())
    .data(data)
    .success(true)
    .timeStamp(System.currentTimeMillis())
    .build();

  }

  public static <T> ApiResponse<T> failureWithMessage(HttpStatus httpStatus, String message){

    return ApiResponse.<T>builder()
    .statusCode(httpStatus.value())
    .status(HttpStatus.valueOf(httpStatus.value()).name())
    .message(message)
    .success(false)
    .timeStamp(System.currentTimeMillis())
    .build();

  }

  public static <T> ApiResponse<T> failureWithErrorAndMessageis(HttpStatus httpStatus, Object errors, String message){

    return ApiResponse.<T>builder()
    .statusCode(httpStatus.value())
    .status(HttpStatus.valueOf(httpStatus.value()).name())
    .message(message)
    .errors(errors)
    .success(false)
    .timeStamp(System.currentTimeMillis())
    .build();
  }
    
  }


