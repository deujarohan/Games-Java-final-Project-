package com.games.final_projest_java.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GameRecordDto {

    @NotBlank(message = "Game type is required")
    private String gameType;

    @NotBlank(message = "Result is required")
    @Pattern(regexp = "WIN|LOSS|DRAW", message = "Result must be WIN, LOSS, or DRAW")
    private String result;

    @NotBlank(message = "Opponent type is required")
    private String opponentType;

    @Size(max = 200, message = "Notes cannot exceed 200 characters")
    private String notes;

    @Min(value = 1, message = "Duration must be at least 1 second")
    @Max(value = 3600, message = "Duration cannot exceed 1 hour")
    private int durationSeconds;
}
