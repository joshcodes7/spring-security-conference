package com.example.EmployeeManagementApplication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtIssuer {
    private final JwtProperties jwtProperties;
    public String issue(String userName, String password){
        //System.out.println("Role Names: " + roles);
        return JWT.create()
                //.withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                //.withClaim("i",userId)
                .withClaim("u",userName)
                .withClaim("p",password)
                //.withClaim("r",roles)
                //.withArrayClaim("r", roles.toArray(new String[0]))
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));

    }
}