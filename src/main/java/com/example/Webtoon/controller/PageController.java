//package com.example.Webtoon.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class PageController {
//
//
//    @PostMapping("/register")
//    public String register(@RequestParam String email,
//                           @RequestParam String password,
//                           @RequestParam String role) {
//
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole(role);
//
//        userRepository.save(user);
//
//        return "redirect:/login";
//    }
//
//
//
//
//    @GetMapping("/")
//    public String root(Authentication authentication) {
//        if (authentication == null) {
//            return "login";
//        }
//
//        boolean isReader = authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_READER"));
//
//        boolean isCreator = authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));
//
//        if (isReader) {
//            return "redirect:/reader_home";
//        }
//
//        if (isCreator) {
//            return "redirect:/series";
//        }
//
//        return "login";
//    }
//
//    // Login page
//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "login"; // login.html
//    }
//
//    // Register page
//    @GetMapping("/register")
//    public String showRegisterForm() {
//        return "register"; // register.html
//    }
//}
//
//
//

package com.example.Webtoon.controller;

import com.example.Webtoon.model.User;
import com.example.Webtoon.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PageController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //  REGISTER (FIXED)
    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);

        return "redirect:/login";
    }

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication == null) {
            return "login";
        }

        boolean isReader = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_READER"));

        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isReader) return "redirect:/reader_home";
        if (isCreator) return "redirect:/series";

        return "login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }
}