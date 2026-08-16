package com.starkIndustries.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.starkIndustries.models.Users;

@Repository
public interface AuthenticationRepository extends JpaRepository<Users,String>{

  public boolean existsByUsername(String username);
  public boolean existsByEmailId(String emailId);
  public boolean existsByContactNumber(String contactNumber);
  public Optional<Users> findByUsernameOrEmailId(String username, String emailId);

  
}
