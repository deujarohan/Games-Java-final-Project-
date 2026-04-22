package com.games.final_projest_java.controller;

import com.games.final_projest_java.model.User;
import com.games.final_projest_java.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String profile(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);

        // Win rates
        int tttTotal = user.getTttWins() + user.getTttLosses() + user.getTttDraws();
        int rpsTotal = user.getRpsWins() + user.getRpsLosses() + user.getRpsDraws();
        double tttWinRate = tttTotal > 0 ? (user.getTttWins() * 100.0 / tttTotal) : 0;
        double rpsWinRate = rpsTotal > 0 ? (user.getRpsWins() * 100.0 / rpsTotal) : 0;

        model.addAttribute("tttTotal", tttTotal);
        model.addAttribute("rpsTotal", rpsTotal);
        model.addAttribute("tttWinRate", String.format("%.1f", tttWinRate));
        model.addAttribute("rpsWinRate", String.format("%.1f", rpsWinRate));
        return "user/profile";
    }
}
