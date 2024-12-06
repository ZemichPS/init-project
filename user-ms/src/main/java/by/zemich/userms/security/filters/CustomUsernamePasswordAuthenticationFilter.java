package by.zemich.userms.security.filters;

import by.zemich.userms.controller.dto.request.LoginRequest;
import by.zemich.userms.security.utils.JWTHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JWTHandler jwtHandler;
    private final AuthenticationManager authenticationManager;

    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper,
                                                      JWTHandler jwtHandler,
                                                      AuthenticationManager authenticationManager) {
        this.objectMapper = objectMapper;
        this.jwtHandler = jwtHandler;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/v1/login");

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            super.attemptAuthentication(request, response);
        }

        LoginRequest loginRequest = null;
        try {
            loginRequest = getLoginRequest(request);
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), Collections.emptyList());

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails details = (UserDetails) authResult.getDetails();
        String token = jwtHandler.generate(details);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new BadCredentialsException(failed.getMessage());
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        byte[] bytes = new byte[request.getContentLength()];
        int bytesRead = 0;
        while (bytesRead < bytes.length) {
            bytesRead += inputStream.read(bytes, bytesRead, bytes.length - bytesRead);
        }
        return objectMapper.readValue(bytes, LoginRequest.class);
    }
}
