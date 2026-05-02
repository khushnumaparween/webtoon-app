//package com.example.Webtoon.controller;
//import com.example.Webtoon.model.User;
//import com.example.Webtoon.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest({AuthController.class, PageController.class})
//@AutoConfigureMockMvc
//class ControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    void register_shouldReturnSavedUser() throws Exception {
//        User mockUser = new User();
//        mockUser.setUsername("testuser");
//        mockUser.setPassword("encodedPassword");
//
//        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(mockUser);
//
//        String userJson = "{\"username\":\"testuser\",\"password\":\"plainPassword\"}";
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType("application/json")
//                        .content(userJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.password").value("encodedPassword"));
//    }
//
//    @Test
//    void getLoginPage_shouldReturnLoginView() throws Exception {
//        mockMvc.perform(get("/login"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getRegisterPage_shouldReturnRegisterView() throws Exception {
//        mockMvc.perform(get("/register"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("register"));
//    }
//
//
//    @Test
//    @WithMockUser(roles = "READER")
//    void root_shouldRedirectToReaderHome() throws Exception {
//        mockMvc.perform(get("/"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/reader_home"));
//    }
//
//    @Test
//    @WithMockUser(roles = "CREATOR")
//    void root_shouldRedirectToSeries() throws Exception {
//        mockMvc.perform(get("/"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/series"));
//    }
//}

package com.example.Webtoon.controller;

import com.example.Webtoon.model.User;
import com.example.Webtoon.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class, PageController.class})
@AutoConfigureMockMvc
@Import(ControllerIntegrationTest.TestSecurityConfig.class) // import test security
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // Test Security configuration to allow all requests and disable CSRF
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf().disable() // disables CSRF for tests
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    void register_shouldReturnSavedUser() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        String userJson = "{\"username\":\"testuser\",\"password\":\"plainPassword\"}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(userJson)
                        .with(csrf())) // add CSRF token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").value("encodedPassword"));
    }

    @Test
    void getLoginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void getRegisterPage_shouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser(roles = "READER")
    void root_shouldRedirectToReaderHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reader_home"));
    }

    @Test
    @WithMockUser(roles = "CREATOR")
    void root_shouldRedirectToSeries() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/series"));
    }
}