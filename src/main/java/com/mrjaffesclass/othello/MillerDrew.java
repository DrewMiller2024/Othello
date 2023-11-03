package com.mrjaffesclass.othello;

import java.util.ArrayList;

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

    @Override
    public Position getNextMove(Board board) {
        /* Your code goes here */
        //call minimax here 
        MiniMax mini = new MiniMax(board, 0, true, null, this);
        return mini.getMove();
    }

    class MiniMax {

        private final static int MAX_DEPTH = 8;
        private int chosenScore;
        private Position chosenMove;

        public MiniMax(Board board, int depth, boolean isMaxPlayer, Position theMove, Player player) {
            if (depth == MAX_DEPTH) {
                chosenScore = evaluate(board, player);
            } else {
                Player tempPlayer = isMaxPlayer ? (player) : (new Player(player.getColor() * -1));
                ArrayList<Position> moves = getMoves(board, tempPlayer);
                if (moves.isEmpty()) {
                    chosenScore = evaluate(board, player);
                } else {
                    int bestScore = Integer.MIN_VALUE;
                    Position bestMove = new Position(0, 0);
                    for (Position move : moves) {
                        Board board2 = new Board();
                        for (int i = 0; i < Constants.SIZE; i++) {
                            for (int j = 0; j < Constants.SIZE; j++) {
                                Position pos = new Position(i, j);
                                Square square = board.getSquare(pos);
                                board2.setSquare(new Player(square.getStatus()), bestMove);
                            }
                        }
                        board2.makeMove(tempPlayer, move);
                        MiniMax child = new MiniMax(board2, depth + 1, !isMaxPlayer, theMove, player);
                        int score = child.getEval();
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = move;
                        }
                    }
                    chosenScore = bestScore;
                    if (depth == 0) {
                        chosenMove = bestMove;
                    }
                }
            }
        }

        public int evaluate(Board gameBoard, Player player) {
            int newEval = 0;
            for (int i = 0; i < Constants.SIZE; i++) {
                for (int j = 0; j < Constants.SIZE; j++) {
                    Square tempSquare = gameBoard.getSquare(new Position(i, j));
                    int checkColor = tempSquare.getStatus();
                    if (checkColor != 0) {
                        if (isCorner(i, j)) {
                            newEval += 100 * checkColor;
                        } else {
                            //newEval += 1 * checkColor;
                        }
                    }
                }
            }
            newEval = newEval * player.getColor();
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

        public ArrayList<Position> getMoves(Board board, Player playerToCheck) {
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

        public int getEval() {
            return this.chosenScore;
        }

        public Position getMove() {
            return this.chosenMove;
        }
    }
}
