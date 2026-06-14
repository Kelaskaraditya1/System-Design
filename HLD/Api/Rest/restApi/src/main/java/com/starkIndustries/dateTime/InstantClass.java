package com.starkIndustries.dateTime;

import java.time.Duration;
import java.time.Instant;

public class InstantClass {

  public static void main(String[] args) {

    Instant instant = Instant.now(); 
    System.out.println(instant);  // creates a Instant of current moment.

    Instant oneHour = Instant.now().plus(Duration.ofHours(1)); // adding some Time to the Isntant, expects a Duration object,
    System.out.println(oneHour);

    Instant i1 = Instant.now();

    Instant i2 = Instant.now().plus(Duration.ofMinutes(5)); 

    if(i1.isAfter(i2)) // comparision operator
      System.out.println("Otp expired");
    else
      System.out.println("Otp valid");

    Instant start = instant.minus(Duration.ofMinutes(2));

    if(Duration.between(start, instant).toMinutes() > Duration.ofMinutes(5).toMinutes())
      System.out.println("Otp expired");
    else
      System.out.println("valid  otp");


    
  }
  
}
