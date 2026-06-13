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
import org.springframework.http.ProblemDetail;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
@Tag(name = "Users Controller", description = "This represents the operations realted to Users")
public class UsersController {

  @Autowired
  public Utility utility;

  @Autowired
  public UsersRepository usersRepository;



  @PostMapping
  @Operation(summary ="POST: /api/v1/users", description = "This endpoint is to add User")
  public ResponseEntity<ApiResponse<UserResponse>> addUser(
    @Valid @RequestBody UserRequest userRequest
  ){
    Users users = UserRequest.toUsers(userRequest);
    UserResponse userResponse = UserResponse.toUsers(users);

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(userResponse));
  }


  @GetMapping("/{userId}")
  @Operation(summary = "GET: /api/v1/users/{userId}", description = "To get a single user using User Id")
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
  @Operation(summary = "GET: /api/v1/users", description = "This endpoint return a paginated annd sorted list of users and accepts 3 optional parameter Curson(userId of last object), sortingParameter and direction of sorting")
  public ResponseEntity<?> getAllUsers(
    @Parameter(description = "Cursro means the Primary key of the last object of the list, so in next iteration it will start from the next object") @RequestParam(name = "cursor", required = false) String cursor,
    @Parameter(description = "sortingParameter means on the basis of whic parameter we have to perform sorting") @RequestParam(name="sortingParameter", required = false, defaultValue = "userId") String sortingParameter,
    @Parameter(description = "Whether to sort in Ascending or Decending order") @RequestParam(name = "direction", required = false, defaultValue = Keys.ASCENDING) String direction
  ){

    log.info("Sorting Paramere: {}", sortingParameter);
    log.info("Sorting Direction: {}", direction);

    Direction direction2;
    Sort sort;
    if(direction !=null && direction.equalsIgnoreCase(Keys.DECENDING))
      direction2=Direction.DESC;
    else 
      direction2 = Direction.ASC;


    sort = sortingParameter!=null ? Sort.by(direction2, sortingParameter): Sort.by("userId");

    if(cursor==null)
      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(this.usersRepository.findTop10By(sort)));

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.sucessWithData(this.usersRepository.findTop10ByUserIdGreaterThan(cursor, sort)));

  }

  @GetMapping("/specification")
  @Operation(summary = "GET: /api/v1/users/specification", description = "This endpoint is used to filter the user on the basis of multiple parameters, basically dynamic filtering")
  @ApiResponses(
    {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="400", description="Client side error"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server side error")

    }
  )
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
  @Operation(summary = "GET: /api/v1/users/multiple-sort", description = "This returns a sorted list which is being sorted on the basis of multiple parameters.")
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

  // @GetMapping(produces = "application/vnd.starkindustries.v1+json")
  // public ResponseEntity<?> getUsersV1(){
  //   return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successWithDataAndMessage(null,"Response from Version 1"));
  // }

  //   @GetMapping(produces = "application/vnd.starkindustries.v2+json")
  // public ResponseEntity<?> getUsersV2(){
  //   return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successWithDataAndMessage(null,"Response from Version 2"));

  // }


  
}
