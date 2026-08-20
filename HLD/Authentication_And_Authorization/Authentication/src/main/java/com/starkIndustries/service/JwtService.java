package com.starkIndustries.service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.starkIndustries.exceptions.CustomException;
import com.starkIndustries.keys.Keys;
import com.starkIndustries.models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

  @Value("${jwt.secret.key}")
  public String secretKey;

  @Value("${jwt.expiry.time}")
  public Long jwtExpirationTime;


//   The below 2 are for fetching public key and private key which are stored in res/

  @Value("classpath:private_key.pem")
    private Resource privateKeyResource;

    @Value("classpath:public_key.pem")
    private Resource publicKeyResource;

    // This generates JwtToken using HMACSha 256 
//   public String generateJwtToken(Users users){

//     Map<String,Object> claims =null;

//     try{

//       claims = new HashMap<>();
//       claims.put(Keys.USER_ID,users.getUserId());
//       claims.put(Keys.ROLE,"USER");

//       return Jwts.builder()
//         .claims()
//         .add(claims)
//         .subject(users.getUsername())
//         .issuedAt(new Date(System.currentTimeMillis()))
//         .expiration(new Date(System.currentTimeMillis()+jwtExpirationTime))
//         .and()
//         .signWith(getSecretKey())
//         .compact();

//     }catch(Exception e){
//       log.error("JwtService :: generateJwtToken() : Error while generating Jwt Token: {}",e.getMessage());
//       throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "JwtService :: generateJwtToken() : Error while generating Jwt Token: "+e.getMessage());
//     }
//   }

  public String generateJwtToken(Users users){

    Map<String,Object> claims = null;

    try{

        claims = new HashMap<>();

      return Jwts.builder()
        .claims()
        .add(claims)
        .subject(users.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis()+jwtExpirationTime))
        .and()
        .signWith(getPrivateKey(), Jwts.SIG.RS256)
        .compact();

    }catch(Exception e){
      log.error("JwtService :: generateJwtToken() : Error while generating Jwt Token: {}",e.getMessage());
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "JwtService :: generateJwtToken() : Error while generating Jwt Token: "+e.getMessage());
    }

  }

//   This is for getting Secret key for signing the token 

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

    // This is used for verifiying Jwt Token using HMAC Sha 256

    // public Claims extractClaims(String token) {
    //     return Jwts
    //             .parser()
    //             .verifyWith(getSecretKey())
    //             .build()
    //             .parseSignedClaims(token)
    //             .getPayload();
    // }

    // This is used for verifiying the JwtToken using RS256 using Public Key which was signed by Private key

    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getPublicKey())
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

    // This provides the private key which is used for signing the token in RS256

    private PrivateKey getPrivateKey() {

    try {

        String key = new String(
                privateKeyResource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);

    } catch (Exception e) {

        log.error(
                "JwtService :: getPrivateKey() : Error while reading private key: {}",
                e.getMessage()
        );

        throw new CustomException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error while reading private key: " + e.getMessage()
        );
    }
}

    // This provides the public key which is used for verifiying the token in RS256

private PublicKey getPublicKey() {

    try {

        String key = new String(
                publicKeyResource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);

    } catch (Exception e) {

        log.error(
                "JwtService :: getPublicKey() : Error while reading public key: {}",
                e.getMessage()
        );

        throw new CustomException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error while reading public key: " + e.getMessage()
        );
    }
}

  
}
