package edu.utep.cs.cs4330.hw4.model;

import java.util.Random;

/**
 * Created by juanrazo on 4/5/16.
 */
public class StrategyRandom implements Strategy {
    @Override
    public Coordinates findCoordinates(char[][]board) {
        Random random = new Random();
        int x = random.nextInt(board.length);
        int y = random.nextInt(board.length);
        while(board[x][y]!=' ') {
            x = random.nextInt(board.length);
            y = random.nextInt(board.length);
        }
        return new Coordinates(x,y);
    }
}
