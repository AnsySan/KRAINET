package com.ansysan.project.authentication_service.security;

import com.ansysan.project.authentication_service.config.security.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final JwtConfig jwtConfig;


    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getRefreshSecretKey())
                    .verifyWith(getAccessSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getAccessSecretKey())
                .verifyWith(getAccessSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(UserDetails user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("authorities", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtConfig.getAccess_expiration() * 1000L))
                .signWith(getAccessSecretKey())
                .compact();
    }
    public String generateRefreshToken(UserDetails user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtConfig.getRefresh_expiration() * 1000L))
                .signWith(getRefreshSecretKey())
                .compact();
    }

    private SecretKey getAccessSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getAccess_secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private SecretKey getRefreshSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getRefresh_secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}