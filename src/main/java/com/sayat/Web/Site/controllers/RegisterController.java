package com.sayat.Web.Site.controllers;

import com.sayat.Web.Site.models.User;
import com.sayat.Web.Site.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Возвращает страницу register.html
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email,
                               @RequestParam String number,
                               @RequestParam String username,
                               @RequestParam String password,
                               Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(email, number, username, password);
            model.addAttribute("message", "Пользователь " + username + " успешно зарегистрирован!");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Ошибка регистрации
        }
    }
}
