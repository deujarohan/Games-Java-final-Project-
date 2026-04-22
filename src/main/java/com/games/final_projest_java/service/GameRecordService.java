package com.games.final_projest_java.service;

import com.games.final_projest_java.dto.GameRecordDto;
import com.games.final_projest_java.model.GameRecord;
import com.games.final_projest_java.repository.GameRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRecordService {

    private final GameRecordRepository gameRecordRepository;

    public GameRecord save(GameRecordDto dto, String username) {
        GameRecord record = GameRecord.builder()
                .username(username)
                .gameType(dto.getGameType())
                .result(dto.getResult())
                .opponentType(dto.getOpponentType())
                .notes(dto.getNotes())
                .durationSeconds(dto.getDurationSeconds())
                .playedAt(LocalDateTime.now())
                .build();

        return gameRecordRepository.save(record);
    }

    /**
     * Filtered list supporting 0, 1, or 2 optional filter params.
     */
    public List<GameRecord> findFiltered(String gameType, String result) {
        boolean hasGameType = gameType != null && !gameType.isBlank();
        boolean hasResult   = result   != null && !result.isBlank();

        if (hasGameType && hasResult) {
            return gameRecordRepository.findByGameTypeAndResult(gameType, result);
        } else if (hasGameType) {
            return gameRecordRepository.findByGameType(gameType);
        } else if (hasResult) {
            return gameRecordRepository.findByResult(result);
        } else {
            return gameRecordRepository.findAllByOrderByPlayedAtDesc();
        }
    }

    public List<GameRecord> findByUsername(String username) {
        return gameRecordRepository.findByUsername(username);
    }

    public List<GameRecord> findAll() {
        return gameRecordRepository.findAllByOrderByPlayedAtDesc();
    }
}
