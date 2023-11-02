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
        MiniMax mini = new MiniMax(board, 0, Integer.MIN_VALUE, null, this.getColor(), this);
        return mini.getMove();
    }

    class MiniMax {

        private final static int MAX_DEPTH = 4;
        private Board board;
        private ArrayList<Position> legalMoves = new ArrayList<>();
        private Position move;
        private int eval;
        private boolean isMaxPlayer;
        private Player player;

        public MiniMax(Board board, int depth, int chosenScore, Position chosenMove, int color, Player player) {
            this.move = chosenMove;
            this.board = board;
            this.isMaxPlayer = isMaxPlayer;
            this.player = player;
                    
            if (depth == MAX_DEPTH) {
                eval = evaluate(board);
            } else {
                getMoves();
            } //endelse
        }

        public void getMoves() {
           Player tempPlayer = isMaxPlayer ? (player) : (new Player(player.getColor() * -1));
           legalMoves = getLegalMoves(board, tempPlayer);
        }
        
        public int evaluate(Board gameBoard) {
            int newEval = 0;
            for (int i = 0; i < Constants.SIZE; i++) {
                for (int j = 0; j < Constants.SIZE; j++) {
                    Square tempSquare = gameBoard.getSquare(new Position(i, j));
                    int checkColor = tempSquare.getStatus();
                    if (checkColor != 0) {
                        if (isCorner(i, j)) {
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

        public boolean isCorner(int r, int c) {
            if (r == 0 && c == 0 || r == 0 && c == Constants.SIZE - 1) {
                return true;
            } else if (r == Constants.SIZE - 1 && c == 0 || r == Constants.SIZE - 1 && c == Constants.SIZE - 1) {
                return true;
            }
            return false;
        }

        public Position getMove() {
            return this.move;
        }
    }
}
