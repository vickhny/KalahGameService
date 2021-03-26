package io.assignment.kalah.controller;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.assignment.kalah.dto.KalahGameDTO;
import io.assignment.kalah.service.GameService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration
public class KalahGameControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GameService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testNewGame() throws Exception {
        final MockHttpServletRequestBuilder playGameRequest = MockMvcRequestBuilders.post("/games");
        this.mockMvc.perform(playGameRequest).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uri").isNotEmpty()).andReturn();
    }

    @Test
    public void testPlay() throws Exception {
        final KalahGameDTO game = this.service.createGame();
        final MockHttpServletRequestBuilder playGameRequest =
                MockMvcRequestBuilders.put("/games/" + game.getId() + "/pits/1");
        this.mockMvc.perform(playGameRequest).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(game.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uri").value(Matchers.endsWith(game.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.1").value("0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.2").value("7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.3").value("7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.4").value("7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.5").value("7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.6").value("7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.7").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.8").value("6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.9").value("6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.10").value("6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.11").value("6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.12").value("6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.13").value("6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status.14").value("0")).andReturn();
    }

}
