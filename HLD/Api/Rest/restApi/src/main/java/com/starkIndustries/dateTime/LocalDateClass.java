package com.starkIndustries.dateTime;

import java.time.LocalDate;

public class LocalDateClass {

  public static void main(String[] args) {

    LocalDate localDate = LocalDate.now(); // gives the current date
    // System.out.println(localDate);

    LocalDate localDate2 = LocalDate.of(2003, 9, 15); // gives the date whose year, month and date has been entered
    System.out.println(localDate2);
    System.out.println(localDate2.getYear()); // get Year of the date.
    System.out.println(localDate2.getMonth()); // get Month of the date.
    System.out.println(localDate2.getDayOfMonth()); // gives Date for LocalDate.

    // here the complete (day,month,year) is called a Date. not only the day. therefore we use dayOfMonth() for getting the day.

    System.out.println(localDate.getDayOfYear()); //  means out of 365 days what is the number of that day like 120th day, 123th day etc 
    System.out.println(localDate2.getDayOfMonth()); // means out of 30 days , what is the number of that day, this actually represents the day 
    System.out.println(localDate2.getMonthValue()); // means out of 12 months which month
    System.out.println(localDate2.getDayOfWeek()); // means which day out the 7 days SUNDAY,MONDAY,...

    // Date Arithmatic:

    LocalDate localDate3 = LocalDate.of(2011, 8, 29);

    System.out.println(localDate3.plusDays(1));  // when we have to add day 
    System.out.println(localDate3.plusMonths(1)); // when we have to add Month
    System.out.println(localDate3.plusYears(1)); // when we have to add Year

    /* Similarly there are methods by which we can perform Subtraction operation
      .minusDays(no of days)
      .minusWeek(no of week)
      .minusYears(no of years)

     */

      if(localDate3.isAfter(localDate2))  // when we have to compare 2 dates we use , .isAfter(LocalDate), .isBefore(LocalDate), .equals(LocalDate) 
        System.out.println("Maithili is younger than Aditya");
      else
        System.out.println("Maithili is older than Aditya");

      LocalDate localDate4 = LocalDate.parse(localDate3.toString());
      System.out.println("LocalDate 4:  "+localDate4);

  }
  
}
