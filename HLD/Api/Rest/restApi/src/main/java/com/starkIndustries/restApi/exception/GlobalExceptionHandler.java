package com.starkIndustries.restApi.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.starkIndustries.restApi.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice // => This will handle exception by any controller , instead of wrtiing try-catch
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{  // we extend it with ResponseEntityExceptionHandler sicne we want to overide the methodArguementNotValid method for requestDto validation

  @ExceptionHandler(CustomException.class) // any time some one throws CustomException object, it's implementation is here.
  public ResponseEntity<ApiResponse<CustomException>> globalExceptionHandler(CustomException customException){  // This is for Custom Exceptions which we trhow from service layer.

    log.error("StatusCode: {}",customException.httpStatus.name());
    log.error("Message {}", customException.message);

    return ResponseEntity.status(customException.httpStatus).body(ApiResponse.failureWithMessage(customException.httpStatus,customException.message));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleUncheckedException(Exception exception){ // This methods handle generic exceptions , nullPointer, indexOutOFBound etc.

    log.error("Error Message: {}", exception.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failureWithMessage(HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage()));

  }

  @Override
  protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) { // This method handles any error which occur at Request Dto validation layer.

        Map<String,Object> errors = ex.getBindingResult()
          .getFieldErrors()
          .stream()
          .collect(
            Collectors.toMap(
              fieldError->fieldError.getField(),
              fieldError-> fieldError.getDefaultMessage()
            )
          );

          return ResponseEntity.status(status).body(ApiResponse.failureWithErrorAndMessages(HttpStatus.valueOf(status.value()), errors, null));

  }
  
}

/* 

1) @RestControllerAdvice:
      we are throwing exception from service layer , but handeling it using try-catch in the controller layer.
      So what does RestControllerAdvice does is that it's a global Handler at controller layer which handles the exception without writing try catch block.
      It is applicable for all Controllers.

2) why extend with ResponseEntityExceptionHandler and overide handleMethodArguementNotValid() ?
      It provides a overiden method, handleMethodArguementNotValid() 
      this method is called when any thing failes at dto layer, when client provides a invalid information, this method is being called.

3) Why ExceptionHandler(CustomException.class)
      It says that whenever any time CustomException is being thrown than we have to follow this method for implementation.

4)     Map<String, String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(
            Collectors.toMap(
                fieldError -> fieldError.getField(),
                fieldError -> fieldError.getDefaultMessage()
                  )
                );

        We want the errors in a Map format where , key => Field and value => error

        In this peice of code, getBindindResults() it is like a report card , which contains a list of which field failed from which request
        than we take the getFieldErrors which is basically a List of (field, message)
        than we stream and transform it using stream().collect()
        than we convert it in the form of a Map (Key,Value) 
        where fieldError -> fieldError.getField() => Key 
              fieldError -> fieldError.getDefaultMessage() => Error
*/
