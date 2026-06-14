package com.starkIndustries.dateTime;

import java.time.LocalTime;

public class LocalTimeClass {

  public static void main(String[] args) {
    LocalTime localTime = LocalTime.now();
    System.out.println(localTime);  // .now() gives the current time 

    LocalTime localTime2 = LocalTime.of(15, 30);
    System.out.println(localTime2); // .of() gives LocalTime object which takes hour and minutes as input, Hour should be less than 24

    System.out.println(localTime2.getHour()); // getHour() gives hour of LocalTime 
    System.out.println(localTime2.getMinute()); // getMinute() gives minute of LocalTime
    System.out.println(localTime2.getSecond());  // getSeconds() gives Seconds of LocalTime

    // Arithmatic operations:

    System.out.println(localTime2.plusHours(1)); // plusHours() we can add hour to LocalTime
    System.out.println(localTime2.plusMinutes(1)); // plusMinute() we can add minute to LocalTime
    System.out.println(localTime2.plusSeconds(1)); // plusSeconds() we can add seconds to LocalTime

    // same we can perform , miinus oeprations.

    // comparision operators: .isAfter(), .isBefore(), .equals()

    if(localTime.isAfter(localTime2))
      System.out.println("now is after LocalTime2");
    else
      System.out.println("LocalTime2 is after now");

    LocalTime localTime3 = LocalTime.parse("07:30");
    System.out.println("Parsed time:"+localTime3);


  }
  
}
