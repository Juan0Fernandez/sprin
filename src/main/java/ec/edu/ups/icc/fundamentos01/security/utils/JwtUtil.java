package ec.edu.ups.icc.fundamentos01.security.utils;

import ec.edu.ups.icc.fundamentos01.security.config.JwtProperties;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;

// IMPORTS EXACTOS PARA JJWT 0.12.3
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return buildToken(userPrincipal.getId(), userPrincipal.getEmail(), userPrincipal.getName(), userPrincipal.getAuthorities());
    }

    public String generateTokenFromUserDetails(UserDetailsImpl userDetails) {
        return buildToken(userDetails.getId(), userDetails.getEmail(), userDetails.getName(), userDetails.getAuthorities());
    }

    private String buildToken(Long id, String email, String name, java.util.Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());
        String roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        return Jwts.builder()
            .subject(String.valueOf(id))
            .claim("email", email)
            .claim("name", name)
            .claim("roles", roles)
            .issuer(jwtProperties.getIssuer())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key, Jwts.SIG.HS256)
            .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) { logger.error("Firma JWT inválida: {}", e.getMessage()); }
          catch (MalformedJwtException e) { logger.error("Token JWT malformado: {}", e.getMessage()); }
          catch (ExpiredJwtException e) { logger.error("Token JWT expirado: {}", e.getMessage()); }
          catch (UnsupportedJwtException e) { logger.error("Token JWT no soportado: {}", e.getMessage()); }
          catch (IllegalArgumentException e) { logger.error("JWT claims vacío: {}", e.getMessage()); }
        return false;
    }
}