package org.games.gameofthree.domain.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.games.gameofthree.commons.Constants.PLAYER_1;
import static org.games.gameofthree.commons.Constants.PLAYER_2;

public class Game {

    private long id;
    private Player winner;
    private Player currentPlayer;
    private List<Move> moves = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    public Game() {
        setPlayers();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public void play(String userInput) {
    	int userInputNumber = Integer.valueOf(userInput);

        moves.add(firstMove(userInputNumber));

        addNextMoves(userInputNumber);

        setWinner(getCurrentPlayer());
    }

    private void addNextMoves(int userInputNumber) {
        int startingNumber = userInputNumber;
        while (startingNumber != 1) {
            currentPlayer = getOtherPlayer();
            Move move = new Move();
            move = move.nextMove(startingNumber, currentPlayer);
            moves.add(move);
            startingNumber = move.getOutput();
        }
    }

    private void setPlayers() {
        players =  Arrays.asList(new Player(1, PLAYER_1),new Player(2, PLAYER_2));
    }

    private Move firstMove(int inputNumber) {
        return new Move(getCurrentPlayer(), 0, inputNumber);
    }

    private Player getOtherPlayer() {
        return players.stream().filter(res -> !res.equals(getCurrentPlayer())).findFirst().get();
    }

    public Player getCurrentPlayer() {
        if(currentPlayer == null){
            currentPlayer = players.get(0);
        }
        return currentPlayer;
    }

}
