package com.starkIndustries.dateTime;

import java.time.Duration;
import java.time.Instant;

public class DurationClass {

  public static void main(String[] args) {

    Duration oneHour = Duration.ofHours(1); // represents Duration of 1 hour
    System.out.println(oneHour);

    Duration oneMinute = Duration.ofMinutes(1); // represents Duration of 1 minute
    System.out.println(oneMinute);

    Duration oneSecond = Duration.ofSeconds(1); // represents Duration of 1 second
    System.out.println(oneSecond);

    System.out.println(oneHour.toSeconds()); // to get Duration in seconds
    System.out.println(oneHour.toMinutes()); // to get Duration in minutes
    System.out.println(oneHour.toHours()); // to get Duration in Hours

    Instant now = Instant.now();
    Instant fiveMinEarlier = Instant.now().minus(Duration.ofMinutes(7));


    System.out.println(Duration.between( fiveMinEarlier, now).toMinutes());
    System.out.println(Duration.ofMinutes(5).toMinutes());

    if(Duration.between(fiveMinEarlier,now).toMinutes()>Duration.ofMinutes(5).toMinutes()) // for calculating the difference between 2 entities
      System.out.println("Otp expired");
    else
      System.out.println("valid otp");


    
  }
  
}
