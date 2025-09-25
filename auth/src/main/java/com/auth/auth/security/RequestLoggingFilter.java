package com.auth.auth.security;

import com.auth.auth.utils.UserUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private final UserUtils userUtils;

    public RequestLoggingFilter(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userEmail = userUtils.getCurrentUserEmailId();
        if (userEmail == null) {
            userEmail = "UNKNOWN";
        }
        LOGGER.info("API Called: {}, URL: {}, Method: {}", userEmail, request.getRequestURI(), request.getMethod());
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
