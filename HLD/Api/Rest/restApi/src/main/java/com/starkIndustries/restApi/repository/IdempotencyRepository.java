package com.starkIndustries.restApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.starkIndustries.restApi.model.Idempotency;


@Repository
public interface IdempotencyRepository extends JpaRepository<Idempotency,String>{
  
}
