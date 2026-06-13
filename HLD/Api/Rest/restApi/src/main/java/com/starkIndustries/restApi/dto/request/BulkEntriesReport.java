package com.starkIndustries.restApi.dto.request;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkEntriesReport {

  private int totalEntries;
  private int successEntriesCount;
  private List<UsersBulkResponse> failedEntries;
  
}
