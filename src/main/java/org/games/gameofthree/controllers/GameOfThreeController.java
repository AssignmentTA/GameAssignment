package org.games.gameofthree.controllers;

import org.games.gameofthree.domain.models.Game;
import org.games.gameofthree.services.GameOfThreeApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameOfThreeController {

    @Autowired
    private GameOfThreeApplicationService gameOfThreeApplicationService;

    @SendTo("/topic/game")
    @MessageMapping("/play")
    @GetMapping("/play")
    public Game playGame(@RequestParam String userInputNumber) {
        return gameOfThreeApplicationService.play(userInputNumber);
    }

    @SendTo("/topic/game")
    @MessageMapping("/autoPlay")
    @GetMapping("/autoPlay")
    public Game autoPlay() {
        return gameOfThreeApplicationService.play("");
    }
}
