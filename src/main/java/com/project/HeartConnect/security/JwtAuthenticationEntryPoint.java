package com.project.HeartConnect.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.HeartConnect.utils.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Log4j2
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        log.info("Inside @class JwtAuthenticationEntryPoint @method commence");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(GenericResponse.error(authException.getMessage()));

        PrintWriter printWriter = response.getWriter();
        printWriter.write(jsonResponse);
    }
}
