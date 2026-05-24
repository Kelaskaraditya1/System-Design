package com.starkIndustries.restApi.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername,  String>

{

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    if(value==null)
      return false;

    return value.startsWith("STARK");

  }
  
}
