package com.starkIndustries.restApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.starkIndustries.restApi.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,String> {

  List<Users> findTop10ByUserIdGreaterThanOrderByUserIdAsc(String userId);
  List<Users> findTop10ByOrderByUserIdAsc();
  
}
