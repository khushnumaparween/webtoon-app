package com.example.Webtoon.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageControllerTest {

    private PageController pageController;

    @BeforeEach
    void setUp() {
        pageController = new PageController();
    }

    @Test
    void root_shouldRedirectToReaderHome_forReaderRole() {
        Authentication auth = new MockAuthentication(List.of(new SimpleGrantedAuthority("ROLE_READER")));
        String result = pageController.root(auth);
        assertEquals("redirect:/reader_home", result);
    }

    @Test
    void root_shouldRedirectToSeries_forCreatorRole() {
        Authentication auth = new MockAuthentication(List.of(new SimpleGrantedAuthority("ROLE_CREATOR")));
        String result = pageController.root(auth);
        assertEquals("redirect:/series", result);
    }

    @Test
    void root_shouldReturnLogin_forUnauthenticatedUser() {
        String result = pageController.root(null);
        assertEquals("login", result);
    }

    // Simple mock Authentication for testing
    static class MockAuthentication implements Authentication {

        private final List<SimpleGrantedAuthority> authorities;

        MockAuthentication(List<SimpleGrantedAuthority> authorities) {
            this.authorities = authorities;
        }

        @Override
        public List<SimpleGrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public Object getCredentials() { return null; }
        @Override
        public Object getDetails() { return null; }
        @Override
        public Object getPrincipal() { return null; }
        @Override
        public boolean isAuthenticated() { return true; }
        @Override
        public void setAuthenticated(boolean isAuthenticated) {}
        @Override
        public String getName() { return "mockUser"; }
    }
}