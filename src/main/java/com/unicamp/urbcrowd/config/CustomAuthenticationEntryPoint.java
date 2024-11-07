package com.unicamp.urbcrowd.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicamp.urbcrowd.exceptions.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED, "Unauthorized. Access token is not valid.");

        String responseBody = objectMapper.writeValueAsString(errorDTO);
        response.getWriter().write(responseBody);
    }
}
