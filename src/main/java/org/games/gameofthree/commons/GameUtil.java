package org.games.gameofthree.commons;

import java.util.Random;

public interface GameUtil {

    static boolean isDivisibleByThree(int inputNumber) {
        return inputNumber % 3 == 0;
    }

    static String getRandomNumber() {
        return new Random().nextInt(100) + "";
    }
}
