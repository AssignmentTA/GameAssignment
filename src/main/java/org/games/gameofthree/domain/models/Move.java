package org.games.gameofthree.domain.models;

import org.games.gameofthree.commons.GameUtil;

public class Move {

    private Player player;

    private int input;

    private int output;

    private int additive;

    public Move() {

    }

    public Move(Player player, int additive, int output) {
        this.player = player;
        this.additive = additive;
        this.output = output;
    }

    public Player getPlayer() {
        return player;
    }

    public int getInput() {
        return input;
    }


    public int getOutput() {
        return output;
    }

    public int getAdditive() {
        return additive;
    }

    public int calculateOutput() {
        return (input+additive)/3;
    }

    public int findAdditive() {
        if (GameUtil.isDivisibleByThree(input)) {
            additive = 0;
        } else if (GameUtil.isDivisibleByThree(input + 1)) {
            additive = 1;
        } else {
            additive = -1;
        }
        return additive;
    }

    public Move nextMove(int inputNumber, Player currentPlayer) {
        this.input = inputNumber;

        return new Move(currentPlayer, findAdditive(), calculateOutput());
    }
}
