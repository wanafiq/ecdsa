package com.wanafiq.ecdsa.config.filter;

import com.wanafiq.ecdsa.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("In JWT Filter");

        try {
            String jwt = extractJwtFromRequest(request);
            if (jwt != null && jwtService.isValidToken(jwt)) {
                log.debug("JWT is valid");
                setAuthenticationContext(jwt);
            } else {
                log.debug("JWT is invalid");
                SecurityContextHolder.clearContext();
                handleAuthenticationError(response);
                return;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error in JWT Filter: {}", e.getMessage());
            handleAuthenticationError(response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        List<String> excludedPaths = List.of(
                "/h2",
                "/api/login"
        );

        return excludedPaths.stream()
                .anyMatch(path::startsWith);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            String jwt  = bearerToken.substring(BEARER_PREFIX.length());
            log.debug("Received JWT: {}", jwt);
            return jwt;
        }
        return null;
    }

    private void setAuthenticationContext(String token) {
        try {
            String userId = jwtService.extractSubject(token);
            List<String> roles = jwtService.extractRoles(token);

            List<SimpleGrantedAuthority> authorities = roles != null
                    ? roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList())
                    : List.of();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            log.debug("AuthenticationToken: {}", authToken);

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            log.error("Failed to set authentication context: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
    }

    private void handleAuthenticationError(HttpServletResponse response)  {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print("{\"error\": \"Unauthorized - Invalid or missing token.\"}");
            out.flush();
        } catch (IOException e) {
            log.error("Failed to write error response: {}", e.getMessage());
        }
    }

}
