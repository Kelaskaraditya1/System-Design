package com.starkIndustries.configuration;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import com.starkIndustries.keys.Keys;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;

@EnableWs // enables web service, it enables spring to detect the @Endpoint classes, enables the MessageDispatcherServlet
@Configuration
public class WebServiceConfig {


    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {

              /* 
              This method is to configure the MessageDispatcherServlet,
              
              1) MessageDispatcherServlet: It is the first, who recives the incoming request from the client and than decides to which @Endpoint method, should the request be routed.
                          It is similar to Dispatcher Servlet which is in Rest
              
              2) ServletRegisterationBean<> is a generic class which takes the MessageDispatcherServlet class as a input paramenter, and it is used to configur the MessageDispatcherServlet

              3) ApplicationContext it represents the IOC container. now the servlet can discover the @Endpoint classes, .xsd etc

               */

        MessageDispatcherServlet servlet = new MessageDispatcherServlet(); // ServletRegisterationBean constructor requires 2 parameters, applicationContext and urlMapping 
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true); // when running locally client has to hit, localhost:8080/ws/... but when hosted using domain, it will automatically take the domain or shift to the new host

        return new ServletRegistrationBean<>(servlet, "/ws/*"); // this creates a ServletRegisterationBean with a MessageDispatcherServlet type of servlet and it's base url will be : localhost:8080/ws/, basically client has to reach the servlet on this url and if we want to get a particular .wsdl we have to use this base url for that, localhost:8080/ws/customer.wsdl
    }


    @Bean(name = "customer") // we have given name to the Bean, since the name of the bean is mapped to the wsdl which is hosted, if we want to access the customer wsdl than, localhost:8080/ws/customer, which is the name of the bean
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema customerXsdSchema){

      // DefaultWsdlDefinition is used to generate wsdl.

      DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();

      defaultWsdl11Definition.setPortTypeName(Keys.CUSTOMER_PORT_TYPE_NAME); // gives name to a particular .xsd
      defaultWsdl11Definition.setLocationUri(Keys.DEFAULT_LOCATION_PATH_URI); // we have to send the soap request here, "/ws" and this should match the MessageServlet path, for eg: localhost:8080/ws/customer.wsdl
      defaultWsdl11Definition.setTargetNamespace(Keys.CUSTOMER_TARGET_NAME_SPACE); // this should match with the incoming request 
      defaultWsdl11Definition.setSchema(customerXsdSchema);

      return defaultWsdl11Definition;


    }


    @Bean
    public XsdSchema createCustomerXsdSchema(){

      // XsdSchema is used to create a java object out of the .xsd

      return new SimpleXsdSchema(
        new ClassPathResource(Keys.CUSTOMER_XSD_PATH) // gets the customer.xsd file from the res/
      );

    }
} 