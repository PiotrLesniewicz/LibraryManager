package org.library.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class LibraryAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private static final UUID ERROR_ID = UUID.randomUUID();
    private static final String REALM_NAME = "LibraryApp";
    private final ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() {
        setRealmName(REALM_NAME);
        super.afterPropertiesSet();
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=/" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String,Object> body = new LinkedHashMap<>();

        body.put("timestamp", Instant.now());
        body.put("status",401);
        body.put("error","Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path",request.getRequestURI());
        body.put("errorId",ERROR_ID);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
