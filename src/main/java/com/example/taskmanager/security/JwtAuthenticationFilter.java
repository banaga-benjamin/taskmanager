package com.example.taskmanager.security;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtutil;
    private final CustomUserDetailsService userservice;

    public JwtAuthenticationFilter(JwtUtil jwtutil, CustomUserDetailsService userservice) {
        this.userservice = userservice;
        this.jwtutil = jwtutil;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException{
        String token = parseJwt(request);
        if (token != null && jwtutil.validateToken(token)) {
            String username = jwtutil.extractUsername(token);
            var userdetails = userservice.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities( ));
            SecurityContextHolder.getContext( ).setAuthentication(auth);
        }

        filter.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerauth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerauth) && headerauth.startsWith("Bearer ")) {
            return headerauth.substring(7);
        }
        return null;
    }
}
