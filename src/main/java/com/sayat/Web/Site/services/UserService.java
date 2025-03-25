package com.sayat.Web.Site.services;

import com.sayat.Web.Site.models.User;
import com.sayat.Web.Site.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String number, String username, String password) {
        if (userExists(email)) {
            throw new RuntimeException("Email уже зарегистрирован!");
        }

        User user = new User();
        user.setEmail(email);
        user.setNumber(number);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findByUsername(String username) {  // Исправлено: теперь метод возвращает User, а не Optional<User>
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username); // Теперь метод возвращает User, а не Optional<User>

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
