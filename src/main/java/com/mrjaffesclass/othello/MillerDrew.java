package com.mrjaffesclass.othello;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Test player
 */
public class MillerDrew extends Player {

    /**
     * Constructor
     *
     * @param color Player color: one of Constants.BLACK or Constants.WHITE
     */
    public MillerDrew(int color) {
        super(color);
        clonePlayer = new Player(color);
        if (color == 1) {
            color = -1;
        } else {
            color = 1;
        }
        invClonePlayer = new Player(color);
    }

    public ArrayList<Position> getLegalMoves(Board board, Player playerToCheck) {
        ArrayList<Position> list = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position testPosition = new Position(row, col);
                if (board.isLegalMove(playerToCheck, testPosition)) {
                    list.add(testPosition);
                }
            }
        }
        return list;
    }

    @Override
    public Position getNextMove(Board board) {
        /* Your code goes here */
        //call minimax here 
        MiniMax mini = new MiniMax(board, 0, Integer.MIN_VALUE, null, this.getColor());
        return mini.getMove();
    }

    class MiniMax {

        private final static int MAX_DEPTH = 4;
        private Board cloneBoard;
        private Position moveMade;
        private final int color;
        private int eval;

        MiniMax(Board board, int depth, int chosenScore, Position chosenMove, int color) {
            this.color = color;
            this.moveMade = chosenMove;
            this.cloneBoard = board;
            
            if (depth == MAX_DEPTH) {
                eval = evaluate(board);
            }
        }

        public int evaluate(Board gameBoard) {
            int newEval;
            for (int i = 0; i < Constants.SIZE; i++) {
                for (int j = 0; j < Constants.SIZE; j++) {
                    Square tempSquare = gameBoard.getSquare(new Position(i, j));
                    int checkColor = tempSquare.getStatus();
                    if (checkColor != 0) {
                        if (isCorner(tempSquare)) {
                            newEval += 10 * checkColor;
                        } else {
                        newEval += 1 * checkColor;
                        }
                    }
                }
            }
            //change newEval to be positive/negative in terms of player color
            newEval = newEval * color;
            return newEval;
        }
        
        public boolean isCorner(Position pos) {
            if ()
        }
    }
}
