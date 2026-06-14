package com.starkIndustries.dateTime;

import java.time.LocalDateTime;

public class LocalDateTimeClass {

  public static void main(String[] args) {

    LocalDateTime localDateTime = LocalDateTime.now();  // gives current LocalDate +"T" + LocalTime
    System.out.println(localDateTime);

    System.out.println(localDateTime.toLocalDate()); // to convert LocalDateTime to LocalDate
    System.out.println(localDateTime.toLocalTime()); // to convert LocalDateTime to LocalTime
  }
  
}
