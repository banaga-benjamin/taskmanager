package com.example.taskmanager.security;

import java.util.Date;
import io.jsonwebtoken.*;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    
    private final String secret = "mysupersecretkeymysupersecretkey"; // must be base 64  or long enough; todo: move to env
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes( ));
    private final long EXPIRATION = 1000 * 60 * 60;

    public String generateToken(String username) {
        return Jwts.builder( )
                .setSubject(username)
                .setIssuedAt(new Date( ))
                .setExpiration(new Date(System.currentTimeMillis( ) + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact( );
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder( ).setSigningKey(key).build( )
                .parseClaimsJws(token).getBody( ).getSubject( );
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder( ).setSigningKey(key).build( ).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
