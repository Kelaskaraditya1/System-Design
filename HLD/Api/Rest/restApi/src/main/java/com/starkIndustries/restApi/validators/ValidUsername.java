package com.starkIndustries.restApi.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)  // => Tells that this annotation can be applied on which components, method, fields, constructor
@Retention(RetentionPolicy.RUNTIME) // => Tells when will this annotation will start working, in our case RUN_TIME since we require when the app is running
@Constraint(validatedBy=UsernameValidator.class) // => Tells where the validation logic is present , for validation of the field
public @interface ValidUsername { // => @interface tells that create a annotation of this 

  String message() default "username should start with  STARK"; // => default message attribute , when no message is passed this is thrown

  Class<?>[] groups() default {}; // => default list of groups , list of groups can be added 

  Class<? extends Payload>[] payload() default {};
  
}
