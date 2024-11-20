package com.trainAi.backend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTTokenUtil {

    // key
    private static final String SECRET_KEY = "TrainAI-SecretKey";

    // JWT Validity set to 1 hour
    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour in milliseconds

    /**
     * create JWT Token
     * @param address
     * @return  JWT token
     */
    public static String generateToken(String address) {

        long now = System.currentTimeMillis();

        // create JWT token
        return Jwts.builder()
                .setSubject(address)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * parse JWT Token
     * @param token JWT token
     * @return address
     */
    public static String parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
