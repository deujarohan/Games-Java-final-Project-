package com.games.final_projest_java.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "game_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRecord {

    @Id
    private String id;

    private String username;

    private String gameType;       // "TIC_TAC_TOE" or "ROCK_PAPER_SCISSORS"

    private String result;         // "WIN", "LOSS", "DRAW"

    private String opponentType;   // "AI" or "HUMAN"

    private String notes;          // optional notes about the game

    private int durationSeconds;

    private LocalDateTime playedAt;
}
