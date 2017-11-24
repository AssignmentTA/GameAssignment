package org.games.gameofthree.services;

import org.apache.commons.lang3.StringUtils;
import org.games.gameofthree.domain.models.Game;
import org.games.gameofthree.commons.GameUtil;
import org.springframework.stereotype.Service;

@Service
public class GameOfThreeApplicationService {

    public Game play(String userInputNumber) {
        if (StringUtils.isBlank(userInputNumber)) {
            userInputNumber = GameUtil.getRandomNumber();
        }

        Game game = new Game();
        game.play(userInputNumber);
        return game;
    }
}
