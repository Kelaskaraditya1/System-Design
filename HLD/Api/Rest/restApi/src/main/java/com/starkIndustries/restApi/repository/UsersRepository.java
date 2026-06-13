package com.starkIndustries.restApi.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.starkIndustries.restApi.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,String>, JpaSpecificationExecutor<Users> {

  List<Users> findTop10ByUserIdGreaterThan(String userId, Sort sort);
  List<Users> findTop10By(Sort sort);

  List<Users> findByName(String name);

  public boolean existsByContact(String contact);
  public boolean existsByEmail(String email);
  public boolean existsByUsername(String username);
  
}
