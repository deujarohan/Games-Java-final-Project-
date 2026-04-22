package com.games.final_projest_java.controller;

import com.games.final_projest_java.model.GameRecord;
import com.games.final_projest_java.service.GameRecordService;
import com.games.final_projest_java.service.PageHitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameRecordController.class)
@Import(com.games.final_projest_java.config.SecurityConfig.class)
class GameRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameRecordService gameRecordService;

    @MockBean
    private PageHitService pageHitService;

    @MockBean
    private com.games.final_projest_java.service.UserService userService;

    @Test
    @WithMockUser
    void listRecordsShouldReturn200AndShowRecords() throws Exception {
        GameRecord r = GameRecord.builder()
                .id("1")
                .username("alice")
                .gameType("TIC_TAC_TOE")
                .result("WIN")
                .opponentType("AI")
                .durationSeconds(30)
                .playedAt(LocalDateTime.now())
                .build();

        when(gameRecordService.findFiltered(null, null)).thenReturn(List.of(r));

        mockMvc.perform(get("/records"))
                .andExpect(status().isOk())
                .andExpect(view().name("records/list"))
                .andExpect(model().attributeExists("records"));
    }

    @Test
    @WithMockUser
    void listRecordsWithGameTypeFilterShouldPassFilterToService() throws Exception {
        when(gameRecordService.findFiltered("TIC_TAC_TOE", null)).thenReturn(List.of());

        mockMvc.perform(get("/records").param("gameType", "TIC_TAC_TOE"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("filterGameType", "TIC_TAC_TOE"));
    }

    @Test
    @WithMockUser
    void newRecordFormShouldReturn200() throws Exception {
        mockMvc.perform(get("/records/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("records/form"))
                .andExpect(model().attributeExists("gameRecordDto"));
    }

    @Test
    @WithMockUser
    void submitValidRecordShouldRedirectToRecordsList() throws Exception {
        when(gameRecordService.save(any(), anyString()))
                .thenReturn(new GameRecord());

        mockMvc.perform(post("/records/new").with(csrf())
                .param("gameType", "TIC_TAC_TOE")
                .param("result", "WIN")
                .param("opponentType", "AI")
                .param("durationSeconds", "60")
                .param("notes", "Great game"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/records?saved=true"));
    }

    @Test
    @WithMockUser
    void submitRecordWithMissingGameTypeShouldShowValidationErrors() throws Exception {
        mockMvc.perform(post("/records/new").with(csrf())
                .param("gameType", "")
                .param("result", "WIN")
                .param("opponentType", "AI")
                .param("durationSeconds", "60"))
                .andExpect(status().isOk())
                .andExpect(view().name("records/form"))
                .andExpect(model().attributeHasFieldErrors("gameRecordDto", "gameType"));
    }

    @Test
    @WithMockUser
    void submitRecordWithInvalidResultShouldShowValidationErrors() throws Exception {
        mockMvc.perform(post("/records/new").with(csrf())
                .param("gameType", "TIC_TAC_TOE")
                .param("result", "INVALID_RESULT")
                .param("opponentType", "AI")
                .param("durationSeconds", "60"))
                .andExpect(status().isOk())
                .andExpect(view().name("records/form"))
                .andExpect(model().attributeHasFieldErrors("gameRecordDto", "result"));
    }

    @Test
    @WithMockUser
    void submitRecordWithZeroDurationShouldShowValidationErrors() throws Exception {
        mockMvc.perform(post("/records/new").with(csrf())
                .param("gameType", "TIC_TAC_TOE")
                .param("result", "WIN")
                .param("opponentType", "AI")
                .param("durationSeconds", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("records/form"))
                .andExpect(model().attributeHasFieldErrors("gameRecordDto", "durationSeconds"));
    }

    @Test
    void listRecordsWithoutAuthShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/records"))
                .andExpect(status().is3xxRedirection());
    }
}
