package com.examly.springapp.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class implements the AuthenticationEntryPoint interface and is used to handle
 * unauthorized access attempts by sending a 403 Forbidden error response.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * This method is called whenever an exception is thrown due to an unauthorized access attempt.
     * It sends a 403 Forbidden error response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
    }
}
