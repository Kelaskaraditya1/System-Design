package com.starkIndustries.restApi.service;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.starkIndustries.restApi.exception.CustomException;
import com.starkIndustries.restApi.keys.Keys;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    @Value("${app.password}")
    public String appPassword;

  @Async
  public void sendEmail(String toEmail, String username){

    String body = """
        Hello %s,

        Welcome to Stark Industries!

        Your account has been created successfully, and you're now ready to get started.

        We're excited to have you with us and look forward to providing you with a great experience.

        If you have any questions or need assistance, feel free to reach out to our support team.

        Best Regards,
        Stark Industries Team
        """.formatted(username);

    try{

      Properties properties = new Properties(); // Hashmap to store SMTP configurations in a key value pair, like which host to connect, on which port, is authentication required or not. 

        properties.put(Keys.HOST, "smtp.gmail.com"); // which email server should recive and forward my mail.
        properties.put(Keys.PORT, "465"); // on which port should i connect to.
        properties.put(Keys.AUTH, "true"); // is Authentication required for that, so Email and it's corrosponding App password will be supplied.
        properties.put(Keys.SSL_ENABLED, "true"); // excrypt this credentials while travelling , without this they will travel as plain text.
        properties.put(Keys.SOCKET_FACTORY_PORT, "465"); // This and below properties allow to use Socket factory
        properties.put(Keys.SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory");
        properties.put(Keys.SOCKET_FACTORY_FALLBACK, "false");

        Session session = Session.getDefaultInstance(properties,new Authenticator() {

          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(Keys.APP_PASSWORD_EMAIL,appPassword); // before sending email, it asks forr authentication so this method is called which gives the email and App password.
          }
          
        });

        MimeMessage mimeMessage = new MimeMessage(session); // This represents the actual email , like a dto.

        mimeMessage.setFrom(Keys.APP_PASSWORD_EMAIL);
        mimeMessage.addRecipient(MimeMessage.RecipientType.TO,new InternetAddress(toEmail)); // InternetAddress is used to show actual email address
        mimeMessage.setSubject("Greetings "+username+", Welcome to Stark Industries");
        mimeMessage.setText(body);

        Transport.send(mimeMessage);
        log.info("Email sent succesfully to :{}", toEmail);


    }catch(Exception e){

      log.error("Failed to send Signup Email: {}", e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to send Signup email: "+e.getMessage());
    }


  }
  
}
