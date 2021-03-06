package io.assignment.kalah.exception;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.assignment.kalah.dto.KalahGameDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameExceptionHandlerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGameNotFoundException() throws Exception {
        final MockHttpServletRequestBuilder playGameRequest =
                MockMvcRequestBuilders.put("/games/123/pits/7");
        this.mockMvc.perform(playGameRequest).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Could not find game 123\"}")).andReturn();
    }

    @Test
    public void testIllegalMoveException() throws Exception {
        final MockHttpServletRequestBuilder initGameRequest = MockMvcRequestBuilders.post("/games");
        final String responseString =
                this.mockMvc.perform(initGameRequest).andReturn().getResponse().getContentAsString();
        final ObjectMapper objectMapper = new ObjectMapper();
        final KalahGameDTO game = objectMapper.readValue(responseString, KalahGameDTO.class);

        final MockHttpServletRequestBuilder playGameRequest =
                MockMvcRequestBuilders.put("/games/" + game.getId() + "/pits/7");
        this.mockMvc.perform(playGameRequest).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"Illegal move: Can not start from house\"}"))
                .andReturn();
    }
}
