package com.starkIndustries.restApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(RestApiApplication.class, args);
	}

	/* Notes:

	<-----------------------------------------------------------------------------------------------@Valid Annotaions----------------------------------------------------------------------------------->

	1) Different annotations related to @Valid annotation:
			most of the annotation take the 'message' parameter to display the error message.

			1) @NotNull(message="")
			it accepts "" and " " but not null.
			This has to be used on Objects and not on Strings.

			2) @NotEmpty(message="")
			it does not accepts null and "" but accepts "  ".

			3) @NotBlan(message="")
			cannot be null, cannot be "" and cannot be " " or empty.

			4) @Size(message="", min=, max=)
			checks the min and max size of string, collection.
			'min' defines minimum and 'max' defines the maximum length.

			5) @Min() means minimum integer value which can be passed.

			6) @Max() means maximum integer value can be passed.

			7) @Email(message="")

			8) @Past(message="") which means the date must be in Past.

			9) @Future(message="") which means the date must be in future.

			10) @Pattern(regexp="", message) , validates the field using regex expression.

	<-----------------------------------------------------------------------------------------------Regex Expressions----------------------------------------------------------------------------------->


	2) Regex Fundamentals:

			Regex stands for Regular Expression.
				1) '^':  means the String should start with what is written after '^'
						for eg: '^abc' so every string should contain 'abc' at the beginning.

				2) '$': means String should end with '$'.
						for eg: 'abc$' so every string should end with 'abc' 

				3) '.': means any charecter
						for eg: 'a.b' means it will take abc, abbbghc etc..

				4) []: means the String should contain the charecters which are written in the bracket.
						for eg: '[a-z]' so it can take any String, 'abc', 'hgjf' etc...
						
				5) [^]: means any charecter accept which are mentioned in the bracket. basically not operation

				6) \s: means white space, \d means: digit, \D means non digit, \W means word char

				7) {n}: no of charecters
						for eg: "^[0-9]{10}" means should contain 10 digits.

				8) {min,max}: means can take minimum min digit charecters and also can take maximum max digit of charecters.
						for  eg: "^\d[3,5]$" this can take 3 digit char , 4 digit char and 5 digit char

			
			Important validations:

			1) Mobile number: "^[7-9]\d{9}$" 
					means 1) first digit should start between [7-9]
								2) than next 9 digits can be any digits which is represented by \d and {9}

	<-----------------------------------------------------------------------------------------------Valid Groups---------------------------------------------------------------------------------------->

		This concepts is used when we have to use a single dto request object for different purposes.
		for eg: in one request we require passwor , but in another request we donot require password but all the mandatory filed which the first request has.
		so in this case use the concept of group.

		steps: 
			scenario there are 2 cases in one case we have to create the user and in another request we have to update the user.
			and in create user case we require password and in uppdate user case we donot require password.

		the general dto request would look like: 

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			@Builder
			public class UserRequest {

				@NotBlank(message = "Name is required")
				public String name;

				@NotBlank(message = "Contact is required")
				@Pattern(regexp = "^[7-9]\\d{9}$", message = "Contact should be 10 digits")
				public String contact;

				@NotBlank(message = "Email is required")
				@Email
				public String email;

				@NotBlank(message = "Username is required")
				public String username;

				@NotBlank(message = "Password is required")
				public String password;

				@NotNull(message = "DateOfBirth is required")
				@Past(message = "Date of birth should not be greater than current date")
				public LocalDate dateOfBirth;
				
			}

			now to solve this follow the below steps:

			1) create an empty interface with the name of the operation (not compulsory ooperation name any thing would do).

			for eg: interface createUserGroup {} and interface updateUserGroup {}

			2) now in the dto request object in @NotNull, @NotEmpty or @NotBlank annotations we get a groups attribute , so we have to add the name of the interfae in which that particular attribute is required.

			for eg: {

			@NotNull(groups={createUserGroup.class, updateUserGroup.class})
			public String userID;

			@NotNull(groups = {createUserGroup.class})
			public String password;

			}


			3) now in controller layer , accept the same request object with @Validated(interfaceGroup.class) instead of @Valid.

			public ResponseEntity<?> createUser(
			@Validated(createUserGroup.class) @RequestBody UserRequest userRequest
			){
			..implementation
			}


	<-----------------------------------------------------------------------------------------------Custom Validator/Annotations------------------------------------------------------------------------>

Step 1: create Custom Annotation


											@Target(ElementType.FIELD)
											@Retention(RetentionPolicy.RUNTIME)
											@Constraint(validatedBy = UsernameValidator.class)

											public @interface ValidUsername { 

												String message()
														default "Invalid Username";

												Class<?>[] groups() default {};

												Class<? extends Payload>[] payload() default{};

											}

							1) '@interface' tells that create a annotation of the name ValidUsername

							2) '@Target' tells on what component we can apply this fields, for eg: field, method, constructor
									so in our case we have selected ElementType.FIELD.

							3) '@Retention' this annotation tells that which class will contain the logic for validation of the incoming field, 
															basically this annotation tells to go to the class which contains the validation logic.

							4) String message()
										default "message"

									when we write a annotation it has a message="" attribute, when we donot give any message the default message is printed

							5) class<?>[] groups() default{}

									this means the groups which we studied earlier , which all groups are to be applied and by default there are no groups 
									class<?> [] this means it taks a array of different .class which could be interface/class.

							the  default shows the default value that the attribute holds , in case of message "message" and in case of groups {}.



step 2: Create a class which has a validation logic.

								create a class implements it with ConstraintValidator<{Name of the annotation},{Data type of attribute}>

								for eg: class UsernameValidator implements ConstraintValidator<UsernameValidator,String>{

								.. override the method and give the logic for validation.

								} 

<-----------------------------------------------------------------------------------------------Pagination-------------------------------------------------------------------------------------------->

<-----------------------------------------------------------------------------------------------Offset Pagination------------------------------------------------------------------------------------->

		Pagination uses 2 parameters size and page no, which are to be taken as Request Param.
		we use Pageable interface and PageRequest which implements Pageable for implementing Pagination.
		all the request params should have some default values.

		Steps:
			1) in controller layer , accept 2 request params , for size and page no.

	@GetMapping
  public ResponseEntity<?> method(
    @RequestParam(name = "pageNo", defaultValue = "0", required = false)int pageNo ,
    @RequestParam(name = "size", defaultValue = "10", required = false)int size
  ){ ... }

	2) than form a PageRequest object using page no and size. and if there is a service layer pass it forward since the repository method requires it.
	
	PageRequest pageRequest = PageREquest.of(pageNo, size);

	3) than pass this pageRequst object to the repository method , findAll(), since it gives a list of objects.	

	response to return:
			1) if we want additional information than return the entire page as it is like, total items, pages etc.
			2) for getting stream , use .get() this returns stream of list
			3) for getting the actual list, .getContent() since in the page response 'content' defines the actual data.
			4) for getting pageNumber: getNumber()
			5) for getting number of items in current response: getNumberOfElements() or getSize()	
			6) for getting total number of elements: .getTotalElements()
			7) for getting total number of pages: .getTotalPages()

			extra functions: hasContent(), hasPrevious(), hasNext(), isEmpty(), isFirst(), isLast()

<-----------------------------------------------------------------------------------------------Cursor Pagination------------------------------------------------------------------------------------->

		In Cursor pagination, instead of pageNo and size we have to pass , cursor which is the primary key of the last object.

	@GetMapping
  public ResponseEntity<?> getAllUsers(
    @RequestParam(name = "cursor", required = false) String cursor
  ){ ... }

	we have to pass the primary key of the last object in the cursror.

	now we havev to write 2 queries/ repository methods 
		1) which runs initially first time and returns first 10 records.
		2) which takes the primary key off the last object and returns next 10 records.

		Repository methods:
			1) List<Users> findTop10ByOrderByUserIdAsc(); => returns 1st 10 records
  		2)   List<Users> findTop10ByUserIdGreaterThanOrderByUserIdAsc(String userId); => returns next 10 records next to userID (last object). 

	now first time, cursor == null than use , List<Users> findTop10ByOrderByUserIdAsc();
	than when we get the primary key of the last object than use, List<Users> findTop10ByOrderByUserIdAsc();

	the above logic has to be implemented in Controller layer.

<----------------------------------------------------------------------------------------------Filtering---------------------------------------------------------------------------------------------->

	Ways to implement Filtering in Spring boot:

	1) Custom Query methods: forming query methods in repository level.
	we have to use specific keywords for performing the operations.

					Keyword 								||  Operation
		1)		find by 								=>	finding exact match
		2) 		exists by								=>  whether data exists or not
		3)		And/Or									=>  Logical Operation
		4) 		Greater than/Less Than 	=> 	>/<
		5) 		Like										=> 	Sql like operation
		6) 		Starting/Ending with		=> 	starts/ ends with
		7) 		OrderBy									=> 	sorting in ascennding/decending order
		
		The main issue with this is that this is fine for static or fixed queries. but filtering demands dynamic filtering, dynamic parameters etc 


		2) Specification: specification is a dynamic way of filtering by passing multiple search parameters.

		Steps: 
			1) extend the repository interface with JpaSpecificationExecutor<Entity>

			2) create a EntitySpecification class and in that create static methods which returns Specification<Entity>

			class EntitySpecification{

			public Specification<Entity> hasAttribute1(Type attribute){

					return (root, query, queryBuilder)-> queryBuilder.equals(root.get("name of attribute"), attribute)
				}
			}

			public Specification<Entity> hasAttribute2(Type attribute){

					return (root, query, queryBuilder)-> queryBuilder.equals(root.get("name of attribute"), attribute)
				}
			}

			in this we return a lambda function which takes 3 parameters:
				1) root: this refers to the Entity.
				2) query: this refers to the entire query that has been formed.
				3) criteriaBuilder: this refers to the where clause, defines the condition. where attribute1=attribute.
				
				
			3) Than create a seprate controller which will take all the parameters which are defined in the Entity as requestParam and make them required=false.

			  @GetMapping("/specification")
  			public ResponseEntity<?> specification(
    				@RequestParam(name = "name",required = false) String name,
    				@RequestParam(name = "dateOfBirth",required = false) LocalDate dateOfBirth,
    				@RequestParam(name = "contact",required = false) String contact,
    				@RequestParam(name = "email",required = false) String email,
    				@RequestParam(name = "username",required = false) String username
  						){
						....
						}


						now in controller create a Specification object,

						Specification specification = Specification.allOf() => this defines a empty query with no conditions 
						now on the basis of how many parameters recived, by checking whether each queryParam is null or not add it to the condition,

						if(attribute !=null){

								specification.or(EntitiySpecification.hasAttribute(attribute)); => we can use or/and and thiis method comes from EntitySpecification.
						}

						repeat this for all queryParams which are present.

<----------------------------------------------------------------------------------------------Sorting------------------------------------------------------------------------------------------------>

		1) Normal sorting using one field.
		
		In the controller layer take the parameter on the basis of which sorting is to be done and the direction of Sorting.and make them optional.

		Then form a Sorting oobject,
				Sort sort = Sort.by(direction, sortingParameter);
						direction = Direction.ASC/Direction.DESC

		than pass this sort object ot service -> repository in the custom method which finds the data.

		when we implement, Pagination with cursor, 
						we make 2 custom queirs , first which returns first n records.
						2nd which takes the id of the last item and returns a list of n objects next to that.
						so let that methods take Sort object so that it will return the Sorted list as per the direction passed.

		2) Sorting using multiple parameters:

		when there are multiple parameters on the basis of which we have to perform sorting than basically the complete list is being sorted on the basis of the first parameter.
				than when there are equal cases than it is sorted on the basis of the second parameter. this is how sorting on multiple parameters work.

		now in controller layer:
		1) take List<String> sortingParameter as an input and while passing as request pass it like 
					[
						"userId,ASCENDING",
						"name,DECENDING",
						....
						]

			so in the controller , Declare the List<Sort.Order> orders;
			now traverse the List<String> and take single String which contains "parameter,direction" and split them using split(",") which gives a array[].
			
			array[0] => field, array[1] => direction.

			now form an object on the basis of direction

			if(direction.equalsIgnoreCase(ASCENDING))
				orders.add(Sort.Order.asc(array[0]))
			else
				orders.add(Sort.Order.desc(array[0]))

			Sort.Order.asc(property) , Sort.Order.desc(property) gives a Order object.

			than form a general Sort Object which takes the orders List.

			Sort sort = Sort.by(orders);

			than pass it to any Repository method.



						
	
	*/

}
