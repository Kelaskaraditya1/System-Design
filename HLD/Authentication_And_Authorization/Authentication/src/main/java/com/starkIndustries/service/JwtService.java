package com.starkIndustries.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.starkIndustries.exceptions.CustomException;
import com.starkIndustries.keys.Keys;
import com.starkIndustries.models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

  @Value("${jwt.secret.key}")
  public String secretKey;

  @Value("${jwt.expiry.time}")
  public Long jwtExpirationTime;

  public String generateJwtToken(Users users){

    Map<String,Object> claims =null;

    try{

      claims = new HashMap<>();
      claims.put(Keys.USER_ID,users.getUserId());
      claims.put(Keys.ROLE,"USER");

      return Jwts.builder()
        .claims()
        .add(claims)
        .subject(users.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis()+jwtExpirationTime))
        .and()
        .signWith(getSecretKey())
        .compact();

    }catch(Exception e){
      log.error("JwtService :: generateJwtToken() : Error while generating Jwt Token: {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "JwtService :: generateJwtToken() : Error while generating Jwt Token: "+e.getMessage());
    }
  }

      public SecretKey getSecretKey(){
      byte [] keyRawBytes = Base64.getDecoder().decode(secretKey);
      return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyRawBytes);
    }

        public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);

    }

    public <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        final boolean isExpired = isTokenExpired(token);

        log.info("Validating token for user...");
        log.info("→ Extracted username from token : {}", userName);
        log.info("→ UserDetails username         : {}", userDetails.getUsername());
        log.info("→ Token expired?                : {}", isExpired);

        boolean isValid = userName.equals(userDetails.getUsername()) && !isExpired;
        log.info("→ Final token validity result   : {}", isValid);

        return isValid;
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

  
}
