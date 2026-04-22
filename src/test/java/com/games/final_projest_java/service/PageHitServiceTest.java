package com.games.final_projest_java.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PageHitServiceTest {

    private PageHitService pageHitService;

    @BeforeEach
    void setUp() {
        pageHitService = new PageHitService();
    }

    @Test
    void initialCountShouldBeZero() {
        assertThat(pageHitService.getCount()).isEqualTo(0L);
    }

    @Test
    void incrementShouldIncreaseCountByOne() {
        long first = pageHitService.increment();
        assertThat(first).isEqualTo(1L);
        assertThat(pageHitService.getCount()).isEqualTo(1L);
    }

    @Test
    void multipleIncrementsAccumulate() {
        pageHitService.increment();
        pageHitService.increment();
        long third = pageHitService.increment();
        assertThat(third).isEqualTo(3L);
        assertThat(pageHitService.getCount()).isEqualTo(3L);
    }

    @Test
    void resetShouldReturnCountToZero() {
        pageHitService.increment();
        pageHitService.increment();
        pageHitService.reset();
        assertThat(pageHitService.getCount()).isEqualTo(0L);
    }

    @Test
    void incrementAfterResetStartsFromOne() {
        pageHitService.increment();
        pageHitService.reset();
        long val = pageHitService.increment();
        assertThat(val).isEqualTo(1L);
    }
}
