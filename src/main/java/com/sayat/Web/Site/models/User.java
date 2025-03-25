package com.sayat.Web.Site.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, unique = true, name = "number")
    private String number;

    @Column(nullable = false, unique = true, length = 15, name = "username")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;
}
