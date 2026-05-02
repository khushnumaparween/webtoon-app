package com.example.Webtoon.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        boolean isReader = false;
        boolean isCreator = false;

        for (GrantedAuthority authority : authentication.getAuthorities()) {

            if (authority.getAuthority().equals("ROLE_CREATOR")) {
                isCreator = true;
            }

            if (authority.getAuthority().equals("ROLE_READER")) {
                isReader = true;
            }
        }

        if (isCreator) {
            response.sendRedirect("/series");
        } else if (isReader) {
            response.sendRedirect("/reader_home");
        } else {
            response.sendRedirect("/");
        }
    }
}