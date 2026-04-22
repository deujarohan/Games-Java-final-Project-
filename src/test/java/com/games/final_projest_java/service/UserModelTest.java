package com.games.final_projest_java.service;

import com.games.final_projest_java.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserModelTest {

    @Test
    void getTotalGamesPlayedShouldSumAllStats() {
        User user = User.builder()
                .tttWins(3).tttLosses(2).tttDraws(1)
                .rpsWins(5).rpsLosses(4).rpsDraws(2)
                .build();

        assertThat(user.getTotalGamesPlayed()).isEqualTo(17);
    }

    @Test
    void getTotalWinsShouldSumTTTAndRPSWins() {
        User user = User.builder()
                .tttWins(4).rpsWins(6)
                .build();

        assertThat(user.getTotalWins()).isEqualTo(10);
    }

    @Test
    void getTotalLossesShouldSumTTTAndRPSLosses() {
        User user = User.builder()
                .tttLosses(2).rpsLosses(3)
                .build();

        assertThat(user.getTotalLosses()).isEqualTo(5);
    }

    @Test
    void newUserShouldHaveZeroStats() {
        User user = User.builder().username("newbie").build();

        assertThat(user.getTotalGamesPlayed()).isEqualTo(0);
        assertThat(user.getTotalWins()).isEqualTo(0);
        assertThat(user.getTotalLosses()).isEqualTo(0);
    }

    @Test
    void lombokBuilderShouldSetAllFields() {
        User user = User.builder()
                .username("alice")
                .email("alice@example.com")
                .role("ROLE_USER")
                .tttWins(10)
                .build();

        assertThat(user.getUsername()).isEqualTo("alice");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getRole()).isEqualTo("ROLE_USER");
        assertThat(user.getTttWins()).isEqualTo(10);
    }
}
