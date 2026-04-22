package com.games.final_projest_java.controller;

import com.games.final_projest_java.service.PageHitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dependency injection location #1: PageHitService injected here.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PageHitService pageHitService;

    @GetMapping("/")
    public String home(Model model) {
        pageHitService.increment();
        model.addAttribute("pageTitle", "GameHub - Play & Compete");
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            org.springframework.security.core.Authentication auth) {
        pageHitService.increment();
        model.addAttribute("username", auth.getName());
        return "dashboard";
    }
}
