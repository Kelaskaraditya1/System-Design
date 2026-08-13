package com.starkIndustries.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.starkIndustries.keys.Keys;
import com.starkindustries.soap.generated.Customer;
import com.starkindustries.soap.generated.GetCustomerRequest;
import com.starkindustries.soap.generated.GetCustomerResponse;
import com.starkindustries.soap.generated.ObjectFactory;

import jakarta.xml.bind.JAXBElement;
import lombok.extern.slf4j.Slf4j;

@Endpoint
@Slf4j
public class CustomerEndpoint {

  @PayloadRoot(localPart = Keys.CUSTOMER_REQUEST_LOCAL_PART, namespace = Keys.CUSTOMER_TARGET_NAME_SPACE)
  @ResponsePayload
  public JAXBElement<GetCustomerResponse> getCustomer(
    @RequestPayload JAXBElement<GetCustomerRequest> getCustomerRequest
  ){

      GetCustomerRequest getCustomerRequest1 = getCustomerRequest.getValue();
      log.info("CustomerID:{}",getCustomerRequest1.getCustomerId());
    
      Customer customer = new Customer();
      customer.setCustomerId(1);
      customer.setCustomerName("Aditya Kelaskar");
      customer.setEmail("kelaskaraditya1@gmail.com");

      GetCustomerResponse getCustomerResponse = new GetCustomerResponse();
      getCustomerResponse.setCustomer(customer);

      return new ObjectFactory().createGetCustomerResponse(getCustomerResponse);

  }
  
}
