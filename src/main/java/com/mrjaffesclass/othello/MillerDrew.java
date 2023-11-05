package com.mrjaffesclass.othello;

import java.util.ArrayList;

/**
 * Test player
 */
public class MillerDrew extends Player {

    private final static int MAX_DEPTH = 8;
    private int chosenScore;
    private Position chosenMove;

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
        int miniMax = MiniMax(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true, null, this);
        return chosenMove;
    }

    public int MiniMax(Board board, int depth, int alpha, int beta, boolean isMaxPlayer, Position theMove, Player player) {
        if (depth == MAX_DEPTH) {
            return evaluate(board, player);
        } else {
            ArrayList<Position> moves = getMoves(board, player);
            if (isMaxPlayer) {
                int bestScore = Integer.MIN_VALUE;
                for (Position move : moves) {
                    Board board2 = new Board();
                    for (int i = 0; i < Constants.SIZE; i++) {
                        for (int j = 0; j < Constants.SIZE; j++) {
                            Position pos = new Position(i, j);
                            Square square = board.getSquare(pos);
                            board2.setSquare(new Player(square.getStatus()), pos);
                        }
                    }
                    board2.makeMove(player, move);
                    int score = MiniMax(board2, depth + 1, alpha, beta, false, theMove, player);
                    if (score > bestScore) {
                        bestScore = score;
                        if (depth == 0) {
                            chosenMove = move;
                        }
                    }
                    alpha = Math.max(alpha, score);
                    if (beta <= alpha) {
                        break;  // Alpha-Beta Pruning
                    }
                }
                return bestScore;
            } else {
                int bestScore = Integer.MAX_VALUE;
                for (Position move : moves) {
                    Board board2 = new Board();
                    for (int i = 0; i < Constants.SIZE; i++) {
                        for (int j = 0; j < Constants.SIZE; j++) {
                            Position pos = new Position(i, j);
                            Square square = board.getSquare(pos);
                            board2.setSquare(new Player(square.getStatus()), pos);
                        }
                    }
                    board2.makeMove(player, move);
                    int score = MiniMax(board2, depth + 1, alpha, beta, false, theMove, player);
                    if (score < bestScore) {
                        bestScore = score;
                        if (depth == 0) {
                            chosenMove = move;
                        }
                    }
                    beta = Math.min(beta, score);
                    if (beta <= alpha) {
                        break;  // Alpha-Beta Pruning
                    }
                }
                return bestScore;
            }
        }
    }

    public int evaluate(Board gameBoard, Player player) {
        boolean corners = cornersTaken(gameBoard, player);
        boolean lateGame = cornersTaken(gameBoard, player) || gameBoard.countSquares(0) <= 30;
        int newEval = 0;
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                Square tempSquare = gameBoard.getSquare(new Position(i, j));
                int checkColor = tempSquare.getStatus();
                if (checkColor != 0) {
                    if (!corners) {
                        if (isCorner(i, j)) {
                            newEval += 400 * checkColor;
                        }
                    } else {
                        if (isStable(i, j, gameBoard, checkColor, player)) {
                            newEval += 20 * checkColor;
                        }
                    }
                }
            }
        }
        newEval = newEval * player.getColor();
        return newEval;
    }

    public boolean isCorner(int r, int c) {
        return (r == 0 && c == 0
                || r == 0 && c == Constants.SIZE - 1
                || r == Constants.SIZE - 1 && c == 0
                || r == Constants.SIZE - 1 && c == Constants.SIZE - 1);
    }

    public boolean cornersTaken(Board gameBoard, Player player) {
        return (gameBoard.getSquare(player, 0, 0).getStatus() != 0
                && gameBoard.getSquare(player, 0, Constants.SIZE - 1).getStatus() != 0
                && gameBoard.getSquare(player, Constants.SIZE - 1, 0).getStatus() != 0
                && gameBoard.getSquare(player, Constants.SIZE - 1, Constants.SIZE - 1).getStatus() != 0);
    }

    public boolean isStable(int r, int c, Board gameBoard, int color, Player player) {
        boolean ud = upDown(r, c, gameBoard, color, player);
        boolean lr = leftRight(r, c, gameBoard, color, player);
        boolean tlbr = topLeftBottomRight(r, c, gameBoard, color, player);
        boolean bltr = bottomLeftTopRight(r, c, gameBoard, color, player);
        return (ud && lr && tlbr && bltr);
    }

    public boolean upDown(int r, int c, Board gameBoard, int color, Player player) {
        for (int i = r; i >= 0; i--) {
            if (gameBoard.getSquare(player, i, c).getStatus() != color) {
                break;
            }
            if (i <= 0) {
                return true;
            }
        }
        for (int i = r; i < Constants.SIZE; i++) {
            if (gameBoard.getSquare(player, i, c).getStatus() != color) {
                break;
            }
            if (i >= Constants.SIZE - 1) {
                return true;
            }
        }
        return false;
    }

    public boolean leftRight(int r, int c, Board gameBoard, int color, Player player) {
        for (int i = c; i >= 0; i--) {
            if (gameBoard.getSquare(player, r, i).getStatus() != color) {
                break;
            }
            if (i <= 0) {
                return true;
            }
        }
        for (int i = c; i < Constants.SIZE; i++) {
            if (gameBoard.getSquare(player, r, i).getStatus() != color) {
                break;
            }
            if (i >= Constants.SIZE - 1) {
                return true;
            }
        }
        return false;
    }

    public boolean topLeftBottomRight(int r, int c, Board gameBoard, int color, Player player) {
        int j = c;
        for (int i = r; i >= 0; i--) {
            if (gameBoard.getSquare(player, i, j).getStatus() != color) {
                break;
            }
            if (i <= 0 || j <= 0) {
                return true;
            }
            j--;
        }
        int j2 = c;
        for (int i = r; i < Constants.SIZE; i++) {
            if (gameBoard.getSquare(player, i, j2).getStatus() != color) {
                break;
            }
            if (i >= Constants.SIZE - 1 || j2 >= Constants.SIZE - 1) {
                return true;
            }
            j2++;
        }
        return false;
    }

    public boolean bottomLeftTopRight(int r, int c, Board gameBoard, int color, Player player) {
        int j = c;
        for (int i = r; i >= 0; i++) {
            if (gameBoard.getSquare(player, i, j).getStatus() != color) {
                break;
            }
            if (i >= Constants.SIZE - 1 || j <= 0) {
                return true;
            }
            j--;
        }
        int j2 = c;
        for (int i = r; i < Constants.SIZE; i--) {
            if (gameBoard.getSquare(player, i, j2).getStatus() != color) {
                break;
            }
            if (i <= 0 || j2 >= Constants.SIZE - 1) {
                return true;
            }
            j2++;
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
}

