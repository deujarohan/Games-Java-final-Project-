package com.games.final_projest_java.controller;

import com.games.final_projest_java.dto.GameRecordDto;
import com.games.final_projest_java.model.GameRecord;
import com.games.final_projest_java.service.GameRecordService;
import com.games.final_projest_java.service.PageHitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/records")
@RequiredArgsConstructor
public class GameRecordController {

    private final GameRecordService gameRecordService;
    private final PageHitService pageHitService;

    /**
     * List all game records with optional GET param filters.
     * Supports filtering by gameType and/or result (0 to 2 filters).
     */
    @GetMapping
    public String listRecords(@RequestParam(required = false) String gameType,
                              @RequestParam(required = false) String result,
                              Model model) {
        pageHitService.increment();
        List<GameRecord> records = gameRecordService.findFiltered(gameType, result);
        model.addAttribute("records", records);
        model.addAttribute("filterGameType", gameType);
        model.addAttribute("filterResult", result);
        return "records/list";
    }

    /**
     * Show form to log a game manually.
     */
    @GetMapping("/new")
    public String newRecordForm(Model model) {
        pageHitService.increment();
        model.addAttribute("gameRecordDto", new GameRecordDto());
        return "records/form";
    }

    /**
     * Submit form - validated server-side, persisted to MongoDB.
     */
    @PostMapping("/new")
    public String submitRecord(@Valid @ModelAttribute("gameRecordDto") GameRecordDto dto,
                               BindingResult result,
                               Authentication auth,
                               Model model) {
        if (result.hasErrors()) {
            return "records/form";
        }

        gameRecordService.save(dto, auth.getName());
        return "redirect:/records?saved=true";
    }
}
