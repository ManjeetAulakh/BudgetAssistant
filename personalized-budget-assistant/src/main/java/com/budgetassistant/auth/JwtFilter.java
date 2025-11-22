package com.budgetassistant.auth;

import com.budgetassistant.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- NEW IMPORT
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// NEW: Added Logger Import for the fix
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class); // Define logger

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = parseJwt(request);

        if (jwt != null) {
            String username = null;
            try {
                // 1. Get username from JWT
                username = jwtUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                // Log and ignore parsing errors (malformed, expired token, etc.)
                logger.warn("JWT parsing failed for request.", e); 
                filterChain.doFilter(request, response);
                return;
            }

            if (username != null) {
                UserDetails userDetails = null;
                try {
                    // 2. Load UserDetails from database
                    userDetails = userService.loadUserByUsername(username);
                    
                    // 3. Full Validation (Signature + Username Match + Expiration)
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        
                        // 4. Create and set authentication object
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 5. Set user as authenticated in the SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (UsernameNotFoundException e) {
                    // Log but allow filter chain to proceed; SecurityConfig will deny access later.
                    logger.warn("User '{}' found in token but not in database.", username);
                } catch (Exception e) {
                    // Catch any other unexpected error during authentication setup
                    logger.error("Error setting security context for user: {}", username, e);
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    // ... (parseJwt method remains unchanged)
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}