package com.starkIndustries.restApi.speciifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.starkIndustries.restApi.model.Users;

public class UsersSpecification {

  public static Specification<Users> hasName(String name){
    return (root,query,criteriaBuilder)-> criteriaBuilder.equal(root.get("name"),name);
  }

  public static Specification<Users> hasDateOfBirth(LocalDate dateOfBirth){

    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth);

  }

    public static Specification<Users> hasContact(String contact){

    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("contact"), contact);

  }

    public static Specification<Users> hasEmail(String email){

    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);

  }

    public static Specification<Users> hasUsername(String username){

    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);

  }

    public static Specification<Users> hasPassword(String password){

    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("password"), password);

  }
  
}

