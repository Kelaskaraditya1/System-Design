package com.starkIndustries.restApi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersBulkResponse {

  private String userId;
  private Boolean success;
  private String message;
  
}
