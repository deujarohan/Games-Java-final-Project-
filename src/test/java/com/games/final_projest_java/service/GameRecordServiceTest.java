package com.games.final_projest_java.service;

import com.games.final_projest_java.dto.GameRecordDto;
import com.games.final_projest_java.model.GameRecord;
import com.games.final_projest_java.repository.GameRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameRecordServiceTest {

    @Mock
    private GameRecordRepository gameRecordRepository;

    @InjectMocks
    private GameRecordService gameRecordService;

    private GameRecordDto dto;

    @BeforeEach
    void setUp() {
        dto = new GameRecordDto();
        dto.setGameType("TIC_TAC_TOE");
        dto.setResult("WIN");
        dto.setOpponentType("AI");
        dto.setNotes("Good game");
        dto.setDurationSeconds(45);
    }

    @Test
    void saveShouldPersistRecordWithCorrectFields() {
        GameRecord saved = GameRecord.builder()
                .id("abc123")
                .username("testuser")
                .gameType("TIC_TAC_TOE")
                .result("WIN")
                .opponentType("AI")
                .notes("Good game")
                .durationSeconds(45)
                .playedAt(LocalDateTime.now())
                .build();

        when(gameRecordRepository.save(any(GameRecord.class))).thenReturn(saved);

        GameRecord result = gameRecordService.save(dto, "testuser");

        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getGameType()).isEqualTo("TIC_TAC_TOE");
        assertThat(result.getResult()).isEqualTo("WIN");
        verify(gameRecordRepository, times(1)).save(any(GameRecord.class));
    }

    @Test
    void findFilteredWithNoFiltersShouldReturnAll() {
        List<GameRecord> all = List.of(new GameRecord(), new GameRecord());
        when(gameRecordRepository.findAllByOrderByPlayedAtDesc()).thenReturn(all);

        List<GameRecord> result = gameRecordService.findFiltered(null, null);

        assertThat(result).hasSize(2);
        verify(gameRecordRepository).findAllByOrderByPlayedAtDesc();
    }

    @Test
    void findFilteredWithGameTypeOnlyShouldFilterByGameType() {
        List<GameRecord> filtered = List.of(new GameRecord());
        when(gameRecordRepository.findByGameType("TIC_TAC_TOE")).thenReturn(filtered);

        List<GameRecord> result = gameRecordService.findFiltered("TIC_TAC_TOE", null);

        assertThat(result).hasSize(1);
        verify(gameRecordRepository).findByGameType("TIC_TAC_TOE");
    }

    @Test
    void findFilteredWithResultOnlyShouldFilterByResult() {
        List<GameRecord> filtered = List.of(new GameRecord(), new GameRecord());
        when(gameRecordRepository.findByResult("WIN")).thenReturn(filtered);

        List<GameRecord> result = gameRecordService.findFiltered(null, "WIN");

        assertThat(result).hasSize(2);
        verify(gameRecordRepository).findByResult("WIN");
    }

    @Test
    void findFilteredWithBothFiltersShouldFilterByBoth() {
        List<GameRecord> filtered = List.of(new GameRecord());
        when(gameRecordRepository.findByGameTypeAndResult("ROCK_PAPER_SCISSORS", "LOSS"))
                .thenReturn(filtered);

        List<GameRecord> result = gameRecordService.findFiltered("ROCK_PAPER_SCISSORS", "LOSS");

        assertThat(result).hasSize(1);
        verify(gameRecordRepository).findByGameTypeAndResult("ROCK_PAPER_SCISSORS", "LOSS");
    }

    @Test
    void findFilteredWithBlankStringsShouldReturnAll() {
        List<GameRecord> all = List.of(new GameRecord());
        when(gameRecordRepository.findAllByOrderByPlayedAtDesc()).thenReturn(all);

        List<GameRecord> result = gameRecordService.findFiltered("", "  ");

        assertThat(result).hasSize(1);
        verify(gameRecordRepository).findAllByOrderByPlayedAtDesc();
    }

    @Test
    void findByUsernameShouldDelegateToRepository() {
        List<GameRecord> records = List.of(new GameRecord());
        when(gameRecordRepository.findByUsername("alice")).thenReturn(records);

        List<GameRecord> result = gameRecordService.findByUsername("alice");

        assertThat(result).hasSize(1);
        verify(gameRecordRepository).findByUsername("alice");
    }
}
