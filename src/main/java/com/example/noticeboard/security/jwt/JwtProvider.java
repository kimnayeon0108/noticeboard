package com.example.noticeboard.security.jwt;

import com.example.noticeboard.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private long SEVEN_DAYS_IN_SECONDS = 604_800;
    private long FIFTEEN_DAYS_IN_SECONDS = 1_296_000;

    public String generateAccessToken(User user) {
        return generateJwtToken(user, createExpireDate(SEVEN_DAYS_IN_SECONDS));
    }

    public String generateRefreshToken(User user) {
        return generateJwtToken(user, createExpireDate(FIFTEEN_DAYS_IN_SECONDS));
    }

    private String generateJwtToken(User user, Date expiration) {
        String token = Jwts.builder()
                           .setSubject(user.getEmail())
                           .setHeader(createHeader())
                           .setClaims(createClaims(user))
                           .setExpiration(expiration)
                           .setIssuedAt(new Date(System.currentTimeMillis()))
                           .signWith(SignatureAlgorithm.HS256, createSigningKey())
                           .compact();

        log.info("Token generated: {}", token);

        return token;
    }

    private Key createSigningKey() {
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private Date createExpireDate(long duration) {
        return new Date(System.currentTimeMillis() + duration * 1000);
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        return claims;
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return header;
    }
}
