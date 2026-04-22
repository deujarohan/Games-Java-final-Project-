package com.games.final_projest_java.controller;

import com.games.final_projest_java.service.PageHitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST API for page hit counter.
 * Called asynchronously every 3 seconds from all pages.
 *
 * Dependency injection location #2: PageHitService injected here.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PageHitController {

    private final PageHitService pageHitService;

    @GetMapping("/hits")
    public Map<String, Long> getHits() {
        return Map.of("hits", pageHitService.getCount());
    }
}
