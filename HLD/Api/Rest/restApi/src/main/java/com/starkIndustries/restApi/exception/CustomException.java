package com.starkIndustries.restApi.exception;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException{  

  public HttpStatus httpStatus;
  public String message;
  public long timeStamp;

  public CustomException(HttpStatus httpStatus, String message){
    super(message);
    this.httpStatus = httpStatus;
    this.message = message;
    this.timeStamp = System.currentTimeMillis();
  }
  
}

/* 
  We extend CustomException with RunTime Exception:
    1) we have to throw this at any stage , in Service or in Controller and we can only throw that objects which extends Throwable interface.
    2) Since the exception which we will trow will be a Un checked exception therefore we have to use Runtime Exception which shows that this is a unchecked runtime exception.
 */