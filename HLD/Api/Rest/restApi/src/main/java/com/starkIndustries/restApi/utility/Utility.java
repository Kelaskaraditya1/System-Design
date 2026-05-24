package com.starkIndustries.restApi.utility;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import com.starkIndustries.restApi.model.Users;

@Component
public class Utility {

  public List<Users> getDummyUsers(){

List<Users> users = List.of(

    new Users(
        "550e8400-e29b-41d4-a716-446655440001",
        "Aditya Kelaskar",
        LocalDate.of(2002, 5, 14),
        "9876543210",
        "aditya@gmail.com",
        "aditya_01",
        "Aditya@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440002",
        "Rahul Sharma",
        LocalDate.of(1999, 8, 21),
        "9123456780",
        "rahul@gmail.com",
        "rahul_99",
        "Rahul@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440003",
        "Sneha Patil",
        LocalDate.of(2001, 3, 11),
        "9988776655",
        "sneha@gmail.com",
        "sneha_11",
        "Sneha@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440004",
        "Aman Verma",
        LocalDate.of(1998, 12, 5),
        "9765432109",
        "aman@gmail.com",
        "aman_verma",
        "Aman@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440005",
        "Priya Singh",
        LocalDate.of(2000, 7, 19),
        "9345678901",
        "priya@gmail.com",
        "priya_singh",
        "Priya@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440006",
        "Karan Mehta",
        LocalDate.of(1997, 11, 28),
        "9234567810",
        "karan@gmail.com",
        "karan_mehta",
        "Karan@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440007",
        "Neha Joshi",
        LocalDate.of(2003, 1, 9),
        "9871203456",
        "neha@gmail.com",
        "neha_j",
        "Neha@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440008",
        "Rohit Deshmukh",
        LocalDate.of(1996, 4, 17),
        "9012345678",
        "rohit@gmail.com",
        "rohit_d",
        "Rohit@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440009",
        "Anjali Kulkarni",
        LocalDate.of(2002, 9, 25),
        "9456781230",
        "anjali@gmail.com",
        "anjali_k",
        "Anjali@123"
    ),

    new Users(
        "550e8400-e29b-41d4-a716-446655440010",
        "Vikas Yadav",
        LocalDate.of(1995, 6, 30),
        "9321456780",
        "vikas@gmail.com",
        "vikas_y",
        "Vikas@123"
    )
);
  return users;


  }

  
  
}
