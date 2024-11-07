package com.unicamp.urbcrowd.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicamp.urbcrowd.exceptions.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.FORBIDDEN, "Access denied.");
        String responseBody = objectMapper.writeValueAsString(errorDTO);
        response.getWriter().write(responseBody);
    }
}
