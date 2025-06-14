package org.example.myvault.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.myvault.domain.User;
import org.example.myvault.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUserId")
    public Long currentUserId(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("userId");
    }

    @ModelAttribute("currentUserNickname")
    public String currentUserNickname(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId != null) {
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isPresent()) {
                return userOpt.get().getNickname();
            }
        }
        return null;
    }
}
