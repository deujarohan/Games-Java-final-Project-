package com.games.final_projest_java.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks page hits since server startup.
 * This service is dependency-injected into both HomeController and PageHitController.
 */
@Service
public class PageHitService {

    private final AtomicLong hitCount = new AtomicLong(0);

    public long increment() {
        return hitCount.incrementAndGet();
    }

    public long getCount() {
        return hitCount.get();
    }

    public void reset() {
        hitCount.set(0);
    }
}
