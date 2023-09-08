package ru.nicetu.online_shop.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.nicetu.online_shop.models.PersonDetails;

import java.util.Date;
import java.util.UUID;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${online_shop.jwtSecret}")
    private String jwtSecret;

    @Value("${online_shop.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        PersonDetails personPrincipal = (PersonDetails) authentication.getPrincipal();

        return JWT.create()
                .withClaim("email", personPrincipal.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String getEmailFromJwt(String token) {
        return JWT.decode(token).getClaims().get("email").toString();
    }

    public boolean validateJwtToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        }
        return false;
    }
}

