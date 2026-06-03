package com.rf.requestportal.controller;

import com.rf.requestportal.service.RegistrationService;
import com.rf.requestportal.service.UsernameAlreadyExistsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {

        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("error", "Заполните все поля.");
            model.addAttribute("username", username);
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают.");
            model.addAttribute("username", username);
            return "register";
        }

        try {
            registrationService.register(username.trim(), password);
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("error", "Имя пользователя уже занято.");
            model.addAttribute("username", username);
            return "register";
        }

        return "redirect:/login?registered";
    }
}
