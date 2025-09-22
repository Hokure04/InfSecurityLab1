package com.example.infsecuritylab1.filter;

import com.example.infsecuritylab1.exception.AuthorizeException;
import com.example.infsecuritylab1.exception.BlockedUserException;
import com.example.infsecuritylab1.exception.JwtTokenExpiredException;
import com.example.infsecuritylab1.service.JwtService;
import com.example.infsecuritylab1.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final Supplier<UserService> userServiceSupplier;



    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    )throws ServletException, IOException {
        try{
            var authHeader = request.getHeader(HEADER_NAME);
            if(StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)){
                filterChain.doFilter(request, response);
                return;
            }
            UserService userService = userServiceSupplier.get();
            var jwt = authHeader.substring(BEARER_PREFIX.length());
            var username = jwtService.extractUserName(jwt);
            if(StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService
                        .getUserDetailsService()
                        .loadUserByUsername(username);
                if(!userDetails.isEnabled()){
                    throw new BlockedUserException("Ваш аккаунт заблокирован");
                }

                if (!jwtService.isTokenValid(jwt, userDetails)){
                    throw new JwtTokenExpiredException("JWT token is invalid or expired");
                }



                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        }catch (BlockedUserException e) {
            log.warn("Blocked user: {}", e.getMessage());
            writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, e.getMessage(), request.getRequestURI());
        } catch (JwtTokenExpiredException e) {
            log.warn("Expired JWT: {}", e.getMessage());
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), request.getRequestURI());
        } catch (AuthorizeException | UsernameNotFoundException e) {
            log.warn("Authorization error: {}", e.getMessage());
            writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, e.getMessage(), request.getRequestURI());
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message, String path) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String body = String.format(
                "{ \"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"path\": \"%s\" }",
                LocalDateTime.now(),
                status,
                message,
                path
        );

        response.getWriter().write(body);
    }
}
