package org.example.myvault.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroController {
    @GetMapping("/intro")
    public String intro() {
        return "intro";
    }
}
