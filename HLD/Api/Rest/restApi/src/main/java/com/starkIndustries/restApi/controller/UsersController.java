package com.starkIndustries.restApi.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.starkIndustries.restApi.dto.request.UserRequest;
import com.starkIndustries.restApi.dto.response.ApiResponse;
import com.starkIndustries.restApi.dto.response.UserResponse;
import com.starkIndustries.restApi.keys.Keys;
import com.starkIndustries.restApi.model.Users;
import com.starkIndustries.restApi.repository.UsersRepository;
import com.starkIndustries.restApi.speciifications.UsersSpecification;
import com.starkIndustries.restApi.utility.Utility;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

  @Autowired
  public Utility utility;

  @Autowired
  public UsersRepository usersRepository;


  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> addUser(
    @Valid @RequestBody UserRequest userRequest
  ){
    Users users = UserRequest.toUsers(userRequest);
    UserResponse userResponse = UserResponse.toUsers(users);

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(userResponse));
  }


  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@PathVariable("userId") String userId){

    var users =  utility.getDummyUsers()
      .stream()
      .filter(user->user.userId==userId)
      .findFirst();

      if(users.isPresent())
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(users));
      else{
        log.error("user with userId {} not found", userId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failureWithMessage(HttpStatus.BAD_REQUEST,"user with userId "+userId+" not found"));
      }
    
  }

  // @GetMapping
  // public ResponseEntity<?> getUsers(
  //   @RequestParam(name = "pageNo", defaultValue = "0", required = false)int pageNo ,
  //   @RequestParam(name = "size", defaultValue = "10", required = false)int size
  // ){

  //   PageRequest pageRequest = PageRequest.of(pageNo, size);
  //   Page<Users> usersPage = this.usersRepository.findAll(pageRequest);
  //   return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(usersPage));
  // }

  @GetMapping
  public ResponseEntity<?> getAllUsers(
    @RequestParam(name = "cursor", required = false) String cursor,
    @RequestParam(name="sortingParameter", required = false) String sortingParameter,
    @RequestParam(name = "direction", required = false, defaultValue = Keys.ASCENDING) String direction
  ){

    log.info("Sorting Paramere: {}", sortingParameter);
    log.info("Sorting Direction: {}", direction);

    Direction direction2;
    if(direction !=null && direction.equalsIgnoreCase(Keys.DECENDING))
      direction2=Direction.DESC;
    else 
      direction2 = Direction.ASC;


    Sort sort = Sort.by(direction2, sortingParameter);

    if(cursor==null)
      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(this.usersRepository.findTop10By(sort)));

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(this.usersRepository.findTop10ByUserIdGreaterThan(cursor, sort)));

  }

  @GetMapping("/specification")
  public ResponseEntity<?> specification(
    @RequestParam(name = "name",required = false) String name,
    @RequestParam(name = "dateOfBirth",required = false) LocalDate dateOfBirth,
    @RequestParam(name = "contact",required = false) String contact,
    @RequestParam(name = "email",required = false) String email,
    @RequestParam(name = "username",required = false) String username
    
  ){

    Specification<Users> specification = Specification.allOf();

    if(name!=null)
      specification = specification.or(UsersSpecification.hasName(name));
    
    if(dateOfBirth!=null)
      specification = specification.or(UsersSpecification.hasDateOfBirth(dateOfBirth));

    if(contact!=null)
      specification = specification.or(UsersSpecification.hasContact(contact));

    if(email!=null)
      specification = specification.or(UsersSpecification.hasEmail(email));

    if(username!=null)
      specification = specification.or(UsersSpecification.hasUsername(username));


    List<Users> users = this.usersRepository.findAll(specification);

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(users));
  }

  @GetMapping("/multiple-sort")
  public ResponseEntity<?> getMultipleSortedUsers(
    @RequestParam(required = false) List<String> fieldAndOrders,
    @RequestParam(name = "cursor", required = false) String cursorField
  ){

      Sort sort;
    if(fieldAndOrders!=null){

      List<Sort.Order> orderList = new ArrayList<>();

      for(String orderItem: fieldAndOrders){
        String field = orderItem.split(",")[0];
        String order = orderItem.split(",")[1];

        if(order.equalsIgnoreCase(Keys.DECENDING))
          orderList.add(Sort.Order.desc(field));
        else
          orderList.add(Sort.Order.asc(field));
      }

      sort = Sort.by(orderList);


      if(cursorField!=null)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(this.usersRepository.findTop10ByUserIdGreaterThan(cursorField, sort)));

      return ResponseEntity.status(HttpStatus.OK).body(this.usersRepository.findTop10By(sort));
      
    }
          sort = Sort.by(Direction.ASC,"userId");
          return ResponseEntity.status(HttpStatus.OK).body(this.usersRepository.findTop10By(sort));

  }
  
}
