package com.games.final_projest_java.controller;

import com.games.final_projest_java.service.PageHitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageHitController.class)
@Import(com.games.final_projest_java.config.SecurityConfig.class)
class PageHitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageHitService pageHitService;

    // Required by SecurityConfig
    @MockBean
    private com.games.final_projest_java.service.UserService userService;

    @Test
    @WithMockUser
    void getHitsShouldReturnJsonWithHitsKey() throws Exception {
        when(pageHitService.getCount()).thenReturn(42L);

        mockMvc.perform(get("/api/hits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.hits").value(42));
    }

    @Test
    void getHitsShouldBeAccessibleWithoutAuthentication() throws Exception {
        when(pageHitService.getCount()).thenReturn(0L);

        mockMvc.perform(get("/api/hits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hits").value(0));
    }
}
