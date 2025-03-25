package com.sayat.Web.Site.controllers;

import com.sayat.Web.Site.models.User;
import com.sayat.Web.Site.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // 📌 Логин
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }

        User user = userOptional.get();

        if (passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("user", user.getUsername()); // Авторизуем пользователя
            return ResponseEntity.ok("Вход выполнен успешно!");
        } else {
            return ResponseEntity.badRequest().body("Неверный пароль");
        }
    }
}
