package com.starkIndustries.utility;

import org.springframework.http.HttpStatus;

import com.starkIndustries.exceptions.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationUtility {

  public static void validateFields(String caller, Field... fields){

    for(Field field: fields){
      if(field.fieldValue()==null){
        log.error("{} : {} is null",caller,field.fieldName());
        throw new CustomException(HttpStatus.BAD_REQUEST,caller+" : "+field.fieldName()+" is null");
      }
      if((field.fieldValue() instanceof String s) && (s.isBlank())){
        log.error("{} : {} is blank",caller,field.fieldName());
        throw new CustomException(HttpStatus.BAD_REQUEST,caller+" : "+field.fieldName()+" is blank");
      }
    }

  }
  
}
