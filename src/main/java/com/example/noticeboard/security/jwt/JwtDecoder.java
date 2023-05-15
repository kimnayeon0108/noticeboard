package com.example.noticeboard.security.jwt;

import com.example.noticeboard.domain.User;
import com.example.noticeboard.security.dto.UserDetailsDto;
import com.example.noticeboard.type.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Collections;

@Log4j2
@Component
public class JwtDecoder {

    @Value("${jwt.secret}")
    private String secretKey;

    public boolean isValid(String token) {
        try {
            Claims claims = getClaimsFromToken(token);

            log.info("email: {}", claims.get("email"));

            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired");
            return false;
        } catch (JwtException e) {
            log.error("Invalid token");
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                   .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                   .parseClaimsJws(token)
                   .getBody();
    }

    public UserDetailsDto decode(String token) {
        Claims claims = getClaimsFromToken(token);

        String email = (String) claims.get("email");
        String role = (String) claims.get("role");

        log.info("email from token: {}", email);
        log.info("role from token: {}", role);

        User user = User.builder()
                        .email(email)
                        .role(UserRole.getUserRole(role))
                        .build();

        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole()
                                                                                 .name());

        return new UserDetailsDto(user, Collections.singleton(grantedAuthority));
    }
}
