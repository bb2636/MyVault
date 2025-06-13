package org.example.myvault.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUserId")
    public Long currentUserId(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("userId");
    }
}
