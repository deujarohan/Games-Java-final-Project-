package com.games.final_projest_java.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String role;

    // Tic-Tac-Toe stats
    private int tttWins;
    private int tttLosses;
    private int tttDraws;

    // Rock Paper Scissors stats
    private int rpsWins;
    private int rpsLosses;
    private int rpsDraws;

    private LocalDateTime createdAt;

    public int getTotalGamesPlayed() {
        return tttWins + tttLosses + tttDraws + rpsWins + rpsLosses + rpsDraws;
    }

    public int getTotalWins() {
        return tttWins + rpsWins;
    }

    public int getTotalLosses() {
        return tttLosses + rpsLosses;
    }
}
