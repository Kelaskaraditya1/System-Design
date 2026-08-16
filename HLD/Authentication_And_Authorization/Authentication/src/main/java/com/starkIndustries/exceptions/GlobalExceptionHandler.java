package com.starkIndustries.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.starkIndustries.dto.response.ApiResponse;
import com.starkIndustries.keys.Keys;

@RestControllerAdvice
@Component
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Map<String,Object>>> globalExceptionHandler(CustomException customException){

    Map<String,Object> response = new LinkedHashMap<>();

    response.put(Keys.STATUS_CODE,customException.getHttpStatus().value());
    response.put(Keys.STATUS, HttpStatus.valueOf(customException.getHttpStatus().value()).name());
    response.put(Keys.ERROR,customException.getMessage());
    response.put(Keys.TIME_STAMP,LocalDateTime.now());

    return ResponseEntity.status(customException.getHttpStatus()).body(ApiResponse.failureResponse(customException.httpStatus,customException.getMessage(), response));

  }

  @Override
  protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String,Object> response = new LinkedHashMap<>();

        response.put(Keys.STATUS_CODE,status.value());
        response.put(Keys.STATUS, HttpStatus.valueOf(status.value()).name());

        Map<String,Object> errors = ex.getBindingResult()
          .getFieldErrors()
          .stream()
          .collect(
            Collectors.toMap(
              fieldError->fieldError.getField(),
              fieldError-> fieldError.getDefaultMessage()
            )
          );

          response.put(Keys.ERRORS, errors);
          response.put(Keys.TIME_STAMP,LocalDateTime.now());

          return ResponseEntity.status(status).body(response);

  }
  
}
