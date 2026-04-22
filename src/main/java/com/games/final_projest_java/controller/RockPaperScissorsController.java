package com.games.final_projest_java.controller;

import com.games.final_projest_java.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/games/rps")
@RequiredArgsConstructor
public class RockPaperScissorsController {

    private final UserService userService;

    @GetMapping
    public String rpsPage(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "games/rps";
    }

    /**
     * Called by JS when game ends to persist stats to user profile.
     */
    @PostMapping("/result")
    @ResponseBody
    public String recordResult(@RequestParam String result, Authentication auth) {
        userService.updateRPSStats(auth.getName(), result);
        return "ok";
    }
}
