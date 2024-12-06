package by.zemich.userms.security.utils;

import by.zemich.userms.security.properties.JWTProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
public class JWTHandler {
    private final JWTProperty jwtProperty;

    private SecretKey secretKey;
    @Value("${spring.application.name}")
    private String applicationName;

    public String getUserName(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public String getUserIssuer(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getIssuer();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT signature", e);
        }
    }
    public String generate(UserDetails details) {
        SecretKey signingKey = Keys.hmacShaKeyFor(jwtProperty.getKey().getBytes(StandardCharsets.UTF_8));


        return Jwts.builder()
                .subject(details.getUsername())
                .claim("id", details.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(jwtProperty.getDuration())))
                .issuer(applicationName)
                .signWith(signingKey)
                .compact();
    }

    @PostConstruct
    private void init(){
        secretKey = Keys.hmacShaKeyFor(jwtProperty.getKey().getBytes(StandardCharsets.UTF_8));
    }
}
