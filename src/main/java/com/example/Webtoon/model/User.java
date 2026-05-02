package com.example.Webtoon.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role;

    // Fields for profile
    private String name;   // full name of the user
    private String bio;    // short bio
    private String image;  // path to uploaded profile image
}
