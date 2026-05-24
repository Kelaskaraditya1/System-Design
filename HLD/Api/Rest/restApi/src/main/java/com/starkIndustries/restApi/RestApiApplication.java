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


	
	
	*/

}
