package com.examly.springapp.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
    @Value("${SECRET_KEY}")// in property add SECRET_KEY=java
     private String SECRET_KEY;
    public String genrateToken(Authentication authentication) {
          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          return Jwts.builder()
          .setSubject(userDetails.getUsername())
          .setIssuedAt(new Date())
          .setExpiration(new Date(System.currentTimeMillis()+(60*60*1000)))
          .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
          .compact();

    }
    public String extractUsername(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
    public Date extractExperation(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    }
    public boolean isTokenExpired(String token){
        Date expire = extractExperation(token);
        return expire.before(new Date());
    }
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header!=null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
    public boolean validateToken(String token) {
         return !isTokenExpired(token);
    }
}


