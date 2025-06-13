package org.example.myvault.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.User;
import org.example.myvault.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        // nickname 중복 체크
        Optional<User> existingUser = userService.findByNickname(user.getNickname());
        if (existingUser.isPresent()) {
            model.addAttribute("registerError", true);
            return "users/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String nickname,
                        @RequestParam String password,
                        HttpServletRequest request,
                        Model model) {
        Optional<User> optionalUser = userService.findByNickname(nickname);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                request.getSession().setAttribute("userId", user.getId());
                return "redirect:/items";  // 로그인 성공 시 item list로 이동
            }
        }
        model.addAttribute("loginError", true);
        return "users/login";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/items";
    }
}
