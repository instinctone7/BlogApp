package com.InstinctOne.BlogApp.security;

import com.InstinctOne.BlogApp.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {

    private static final SecretKey secretKey = Keys.hmacShaKeyFor(
            ("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6" +
                    "MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30").getBytes()
    );
    private static final Date expirationTime = Date.from(Instant.now().plus(30,ChronoUnit.DAYS));

    public static String generateToken(User user){
        return
                Jwts.builder()
                        .subject(user.getEmail())
                        .signWith(secretKey)
                        .expiration(expirationTime)
                        .claim("name",user.getName())
                        .claim("expiration",expirationTime)
                        .issuedAt(Date.from(Instant.now()))
                        .claim("player","Cris")
                        .compact();
    }

    public static Claims validateToken(String token){

        Claims claims = Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();

        if (claims.getSubject().isEmpty() || claims.getSubject() == null){
            throw  new RuntimeException("Invalid Subject of the token");
        }
        return claims;
    }
}
