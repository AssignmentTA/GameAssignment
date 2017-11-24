package org.games.gameofthree.services;

import org.games.gameofthree.domain.models.Game;
import org.games.gameofthree.domain.models.Move;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.games.gameofthree.commons.Constants.PLAYER_1;
import static org.games.gameofthree.commons.Constants.PLAYER_2;

public class GameOfThreeApplicationServiceTest {

    private GameOfThreeApplicationService subject = new GameOfThreeApplicationService();

    @Test
    public void playWithUserInputAsOne() throws Exception {
        List<Move> moves = subject.play("1").getMoves();
        assertThat(moves.size()).isEqualTo(1);

        Move firstMove = moves.get(0);
        assertThat(firstMove.getPlayer().getName()).isEqualTo(PLAYER_1);
        assertThat(firstMove.getOutput()).isEqualTo(1);
        assertThat(firstMove.getAdditive()).isEqualTo(0);
    }

    @Test
    public void playWithUserInputAsFour() throws Exception {
        Game game = subject.play("4");
        List<Move> moves = game.getMoves();
        assertThat(moves.size()).isEqualTo(2);

        Move firstMove = moves.get(0);
        assertThat(firstMove.getPlayer().getName()).isEqualTo(PLAYER_1);
        assertThat(firstMove.getOutput()).isEqualTo(4);
        assertThat(firstMove.getAdditive()).isEqualTo(0);

        Move secondMove = moves.get(1);
        assertThat(secondMove.getPlayer().getName()).isEqualTo(PLAYER_2);
        assertThat(secondMove.getOutput()).isEqualTo(1);
        assertThat(secondMove.getAdditive()).isEqualTo(-1);
    }

    @Test
    public void playWithoutUserInputNumber() {
        List<Move> moves = subject.play("").getMoves();

        assertThat(moves.size()).isGreaterThan(1);
        Move lastMove = moves.get(moves.size() - 1);
        assertThat(lastMove.getPlayer().getName()).contains("Player");
        assertThat(lastMove.getOutput()).isEqualTo(1);
        assertThat(lastMove.getAdditive()).isBetween(-1,1);
    }

    @Test
    public void playWithUserInputNumber() {
        /**
         Player 1  11    0
         Player 2  4     1
         Player 1  1     -1
         */
        List<Move> moves = subject.play("11").getMoves();
        assertThat(moves.size()).isEqualTo(3);

        Move firstMove = moves.get(0);
        assertThat(firstMove.getPlayer().getName()).isEqualTo(PLAYER_1);
        assertThat(firstMove.getOutput()).isEqualTo(11);
        assertThat(firstMove.getAdditive()).isEqualTo(0);

        Move lastMove = moves.get(moves.size() - 1);
        assertThat(lastMove.getPlayer().getName()).isEqualTo(PLAYER_1);
        assertThat(lastMove.getOutput()).isEqualTo(1);
        assertThat(lastMove.getAdditive()).isEqualTo(-1);
    }


    @Test
    public void playWithUserInputNumberAndReturnsWinner() {
        Game game = subject.play("11");
        List<Move> moves = game.getMoves();

        Move lastMove = moves.get(moves.size() - 1);
        assertThat(game.getWinner().getName()).isEqualTo(PLAYER_1);
    }

    @Test
    public void playWithAnotherUserInputNumber() {
        /**
         Player 1  15    0
         Player 2  5     0
         Player 1  2     1
         Player 2  1     1
         */
        List<Move> moves = subject.play("15").getMoves();
        assertThat(moves.size()).isEqualTo(4);

        Move firstMove = moves.get(0);
        assertThat(firstMove.getPlayer().getName()).isEqualTo(PLAYER_1);
        assertThat(firstMove.getOutput()).isEqualTo(15);
        assertThat(firstMove.getAdditive()).isEqualTo(0);

        Move lastMove = moves.get(moves.size() - 1);
        assertThat(lastMove.getPlayer().getName()).isEqualTo(PLAYER_2);
        assertThat(lastMove.getOutput()).isEqualTo(1);
        assertThat(lastMove.getAdditive()).isEqualTo(1);
    }


}