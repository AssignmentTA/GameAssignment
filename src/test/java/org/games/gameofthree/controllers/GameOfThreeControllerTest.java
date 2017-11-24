package org.games.gameofthree.controllers;

import org.games.gameofthree.domain.models.Game;
import org.games.gameofthree.domain.models.Move;
import org.games.gameofthree.domain.models.Player;
import org.games.gameofthree.services.GameOfThreeApplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.games.gameofthree.commons.Constants.PLAYER_1;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GameOfThreeController.class)
public class GameOfThreeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private Move move;
    private Game game;

    @Before
    public void setUp() throws Exception {
        Player player = new Player(1, PLAYER_1);
        move = new Move(player, 1, 19);

        game = new Game();
        game.getMoves().add(move);
    }

    @MockBean
    private GameOfThreeApplicationService gameOfThreeApplicationService;

    @Test
    public void playGameWithUserInputNumber() throws Exception {
        String userInputNumber = "56";
        given(gameOfThreeApplicationService.play(userInputNumber))
                .willReturn(game);

        mockMvc.perform(get("/play")
                .param("userInputNumber", userInputNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("moves[0].player.name", is(move.getPlayer().getName())))
                .andExpect(jsonPath("moves[0].output", is(move.getOutput())))
                .andExpect(jsonPath("moves[0].additive", is(move.getAdditive())));

    }

    @Test
    public void playGameWithoutUserInputNumber() throws Exception {
        given(gameOfThreeApplicationService.play(""))
                .willReturn(game);

        mockMvc.perform(get("/autoPlay")
                .param("userInputNumber", ""))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("moves[0].player.name", is(move.getPlayer().getName())))
                .andExpect(jsonPath("moves[0].output", is(move.getOutput())))
                .andExpect(jsonPath("moves[0].additive", is(move.getAdditive())));

    }

}