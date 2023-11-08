package com.mrjaffesclass.othello;

import java.util.ArrayList;

/**
 * Test player
 */
public class MillerDrew1 extends Player {

    private final static int MAX_DEPTH = 6;
    private Position chosenMove;

    /**
     * Constructor
     *
     * @param color Player color: one of Constants.BLACK or Constants.WHITE
     */
    public MillerDrew1(int color) {
        super(color);
    }

    @Override
    public Position getNextMove(Board board) {
        /* Your code goes here */
        //call minimax here 
        int miniMax = MiniMax(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true, this);
        return chosenMove;
    }

    public int MiniMax(Board board, int depth, int alpha, int beta, boolean isMaxPlayer, Player player) {
        if (depth == 0 && board.noMovesAvailable(isMaxPlayer ? player : new Player(player.getColor() * -1))) {
            chosenMove = null;
            return evaluate(board, isMaxPlayer, player);
        }
        if (depth == MAX_DEPTH) {
            return evaluate(board, isMaxPlayer, player);
        } else {
            if (isMaxPlayer) {
                ArrayList<Position> moves = getMoves(board, player);
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
                    int score = MiniMax(board2, depth + 1, alpha, beta, false, player);
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
                ArrayList<Position> moves = getMoves(board, new Player(player.getColor() * -1));
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
                    int score = MiniMax(board2, depth + 1, alpha, beta, false, player);
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

    public int evaluate(Board gameBoard, boolean isMaxPlayer, Player player) {
        boolean cornersTaken = cornersTaken(gameBoard, player);
        boolean lateGame = cornersTaken(gameBoard, player) || gameBoard.countSquares(0) <= 30;
        Player tempPlayer = (isMaxPlayer ? (new Player(player.getColor())) : (new Player(player.getColor() * -1)));
        ArrayList<Position> moves = getMoves(gameBoard, tempPlayer);
        int newEval = 0;
        for (int i = 0; i < Constants.SIZE; i++) {
            for (int j = 0; j < Constants.SIZE; j++) {
                Square tempSquare = gameBoard.getSquare(new Position(i, j));
                int checkColor = tempSquare.getStatus();
                if (checkColor != 0) {
                    /*
                    if (isCorner(i, j)) {
                        newEval += 200 * checkColor;
                    } else if (isStable(i, j, gameBoard, checkColor, player)) {
                        newEval += 200 * checkColor;
                    }*/
                    if (!lateGame) {
                        if (isCorner(i, j)) {
                            newEval += 2000 * checkColor;
                        } else if (isCSquare(i, j)) {
                            newEval += 10 * checkColor;
                        } else if (isDiagonalToCornerAndWithoutCorner(i, j, gameBoard, player)) {
                            newEval -= 100 * checkColor;
                        }
                        if (isStable(i, j, gameBoard, checkColor, player)) {
                            newEval += 200 * checkColor;
                        } else {
                            if (isNextToCorner(i, j)) {
                                newEval -= 10 * checkColor;
                            }
                            if (isEdge(i, j)) {
                                newEval -= 10 * checkColor;
                            }
                        }
                    } else {
                        /*
                        if (!cornersTaken) {
                            if (isCorner(i, j)) {
                                newEval += 100;
                            } else if (isDiagonalToCornerAndWithoutCorner(i, j, gameBoard, player)) {
                                newEval -= 10 * checkColor;
                            }
                        }*/
                        if (isStable(i, j, gameBoard, checkColor, player)) {
                            newEval += 200 * checkColor;
                        }
                    }
                }else {
                    if (isCorner(i, j)) {
                        if (givesUpCorner(i, j, tempPlayer, gameBoard)) {
                            if (!lateGame) {
                                newEval -= 1000 * tempPlayer.getColor();
                            } else {
                                newEval -= 100 * tempPlayer.getColor();
                            }
                        }
                    }
                }
            }
        }
        newEval += moves.size() * 10 * tempPlayer.getColor();
        newEval = newEval * player.getColor();
        return newEval;
    }

    public boolean isCorner(int r, int c) {
        return (r == 0 && c == 0
                || r == 0 && c == Constants.SIZE - 1
                || r == Constants.SIZE - 1 && c == 0
                || r == Constants.SIZE - 1 && c == Constants.SIZE - 1);
    }

    public boolean isNextToCorner(int r, int c) {
        boolean below = (r == 1 && c == 0) || (r == 1 && c == Constants.SIZE - 1);
        boolean above = (r == Constants.SIZE - 2 && c == 0) || (r == Constants.SIZE - 2 && c == Constants.SIZE - 1);
        boolean right = (r == 0 && c == 1) || (r == Constants.SIZE - 1 && c == 1);
        boolean left = (r == 0 && c == Constants.SIZE - 2) || (r == Constants.SIZE - 1 && c == Constants.SIZE - 2);
        return below
                || above
                || left
                || right;
    }

    //use givesUpCorner if depth is even, where the last move made in the recursion is made by the original player
    public boolean givesUpCorner(int r, int c, Player tempPlayer, Board gameBoard) {
        if (r == 0 && c == 0) {
            if (gameBoard.getSquare(tempPlayer, 1, 1).getStatus() == tempPlayer.getColor()) {
                int j = 2;
                for (int i = 2; i < Constants.SIZE; i++) {
                    if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == tempPlayer.getColor() * -1) {
                        return true;
                    } else if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == 0) {
                        break;
                    }
                    j++;
                }
            }
        } else if (r == 0 && c == Constants.SIZE - 1) {
            if (gameBoard.getSquare(tempPlayer, 1, Constants.SIZE - 2).getStatus() == tempPlayer.getColor()) {
                int j = 5;
                for (int i = 2; i < Constants.SIZE; i++) {
                    if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == tempPlayer.getColor() * -1) {
                        return true;
                    } else if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == 0) {
                        break;
                    }
                    j--;
                }
            }
        } else if (r == Constants.SIZE - 1 && c == 0) {
            if (gameBoard.getSquare(tempPlayer, Constants.SIZE - 2, 1).getStatus() == tempPlayer.getColor()) {
                int j = 2;
                for (int i = 5; i < Constants.SIZE; i--) {
                    if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == tempPlayer.getColor() * -1) {
                        return true;
                    } else if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == 0) {
                        break;
                    }
                    j++;
                }
            }
        } else {
            if (gameBoard.getSquare(tempPlayer, Constants.SIZE - 2, Constants.SIZE - 2).getStatus() == tempPlayer.getColor()) {
                int j = 5;
                for (int i = 5; i < Constants.SIZE; i--) {
                    if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == tempPlayer.getColor() * -1) {
                        return true;
                    } else if (gameBoard.getSquare(tempPlayer, i, j).getStatus() == 0) {
                        break;
                    }
                    j--;
                }
            }
        }
        return false;
    }

    public boolean isDiagonalToCornerAndWithoutCorner(int r, int c, Board gameBoard, Player player) {
        return (r == 1 && c == 1 && gameBoard.getSquare(player, 1, 1).getStatus() != gameBoard.getSquare(player, 0, 0).getStatus())
                || (r == 1 && c == Constants.SIZE - 2 && gameBoard.getSquare(player, 1, Constants.SIZE - 2).getStatus() != gameBoard.getSquare(player, 0, Constants.SIZE - 1).getStatus())
                || (r == Constants.SIZE - 2 && c == 1 && gameBoard.getSquare(player, Constants.SIZE - 2, 1).getStatus() != gameBoard.getSquare(player, Constants.SIZE - 1, 0).getStatus())
                || (r == Constants.SIZE - 2 && c == Constants.SIZE - 2 && gameBoard.getSquare(player, Constants.SIZE - 2, Constants.SIZE - 2).getStatus() != gameBoard.getSquare(player, Constants.SIZE - 1, Constants.SIZE - 1).getStatus());
    }

    public boolean cornersTaken(Board gameBoard, Player player) {
        return (gameBoard.getSquare(player, 0, 0).getStatus() != 0
                && gameBoard.getSquare(player, 0, Constants.SIZE - 1).getStatus() != 0
                && gameBoard.getSquare(player, Constants.SIZE - 1, 0).getStatus() != 0
                && gameBoard.getSquare(player, Constants.SIZE - 1, Constants.SIZE - 1).getStatus() != 0);
    }

    public boolean isCSquare(int r, int c) {
        return (r == 2 && c == 2)
                || (r == 2 && c == 5)
                || (r == 5 && c == 2)
                || (r == 5 && c == 5);
    }

    public boolean isEdge(int r, int c) {
        return (r <= 6 && r >= 1 && c == 0)
                || (r <= 6 && r >= 1 && c == Constants.SIZE - 1)
                || (r == 0 && 1 <= c && c <= 6)
                || (r == Constants.SIZE - 1 && 1 <= c && c <= 6);
    }

    public boolean isStable(int r, int c, Board gameBoard, int color, Player player) {
        boolean ud = upDown(r, c, gameBoard, color, player);
        boolean lr = leftRight(r, c, gameBoard, color, player);
        boolean tlbr = topLeftBottomRight(r, c, gameBoard, color, player);
        boolean bltr = bottomLeftTopRight(r, c, gameBoard, color, player);
        return (ud && lr && tlbr && bltr);
    }

    public boolean upDown(int r, int c, Board gameBoard, int color, Player player) {
        boolean upOpp = false;
        for (int i = r; i >= 0; i--) {
            int squareColor = gameBoard.getSquare(player, i, c).getStatus();
            if (squareColor == 0) {
                break;
            } else if (squareColor == color * -1) {
                upOpp = true;
                break;
            }
            if (i <= 0) {
                return true;
            }
        }
        for (int i = r; i < Constants.SIZE; i++) {
            int squareColor = gameBoard.getSquare(player, i, c).getStatus();
            if (squareColor == 0) {
                break;
            }
            if (squareColor == color * -1) {
                if (upOpp) {
                    if (upDownIsFull(i, c, gameBoard, player)) {
                        return true;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (i >= Constants.SIZE - 1) {
                return true;
            }
        }
        return false;
    }

    public boolean upDownIsFull(int r, int c, Board gameBoard, Player player) {
        for (int i = 0; i < Constants.SIZE; i++) {
            if (gameBoard.getSquare(player, i, c).getStatus() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean leftRight(int r, int c, Board gameBoard, int color, Player player) {
        boolean leftOpp = false;
        for (int i = c; i >= 0; i--) {
            int squareColor = gameBoard.getSquare(player, r, i).getStatus();
            if (squareColor == 0) {
                break;
            }
            if (squareColor == color * -1) {
                leftOpp = true;
                break;
            }
            if (i <= 0) {
                return true;
            }
        }
        for (int i = c; i < Constants.SIZE; i++) {
            int squareColor = gameBoard.getSquare(player, r, i).getStatus();
            if (squareColor == 0) {
                break;

            }
            if (squareColor == color * -1) {
                if (leftOpp) {
                    if (leftRightIsFull(r, i, gameBoard, player)) {
                        return true;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (i >= Constants.SIZE - 1) {
                return true;
            }
        }
        return false;
    }
    
    public boolean leftRightIsFull(int r, int c, Board gameBoard, Player player) {
        for (int i = 0; i < Constants.SIZE; i++) {
            if (gameBoard.getSquare(player, r, i).getStatus() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean topLeftBottomRight(int r, int c, Board gameBoard, int color, Player player) {
        boolean topLeftOpp = false;
        int j = c;
        for (int i = r; i >= 0; i--) {
            int squareColor = gameBoard.getSquare(player, i, j).getStatus();
            if (squareColor == 0) {
                break;
            }
            if (squareColor == color * -1) {
                topLeftOpp = true;
                break;
            }
            if (i <= 0 || j <= 0) {
                return true;
            }
            j--;
        }
        int j2 = c;
        for (int i = r; i < Constants.SIZE; i++) {
            int squareColor = gameBoard.getSquare(player, i, j2).getStatus();
            if (squareColor == 0) {
                break;
            }
            if (squareColor == color * -1) {
                if (topLeftOpp) {
                    if (tlbrIsFull(i, j2, gameBoard, player)) {
                        return true;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (i >= Constants.SIZE - 1 || j2 >= Constants.SIZE - 1) {
                return true;
            }
            j2++;
        }
        return false;
    }
    
    public boolean tlbrIsFull(int r, int c, Board gameBoard, Player player) {
        while (r > 0 && c > 0) {
            r--;
            c--;
        }
        int j = c;
        for (int i = r; i < Constants.SIZE; i++) {
            if (gameBoard.getSquare(player, i, j).getStatus() == 0) {
                return false;
            }
            if (j >= Constants.SIZE - 1) {
               return false;
            }
        }
        return true;
    }

    public boolean bottomLeftTopRight(int r, int c, Board gameBoard, int color, Player player) {
        boolean bottomLeftOpp = false;
        int j = c;
        for (int i = r; i <= Constants.SIZE - 1; i++) {
            int squareColor = gameBoard.getSquare(player, i, j).getStatus();
            if (squareColor == 0) {
                break;
            }
            if (squareColor == color * -1) {
                bottomLeftOpp = true;
                break;
            }
            if (i >= Constants.SIZE - 1 || j <= 0) {
                return true;
            }
            j--;
        }
        int j2 = c;
        for (int i = r; i >= 0; i--) {
            int squareColor = gameBoard.getSquare(player, i, j2).getStatus();
            if (squareColor == 0) {
                break;
            }
            if (squareColor == color * -1) {
                if (bottomLeftOpp) {
                    if (bltrIsFull(i, j2, gameBoard, player)) {
                        return true;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (i <= 0 || j2 >= Constants.SIZE - 1) {
                return true;
            }
            j2++;
        }
        return false;
    }
    
    public boolean bltrIsFull(int r, int c, Board gameBoard, Player player) {
        while (r < Constants.SIZE - 1 && c > 0) {
            r++;
            c--;
        }
        int j = c;
        for (int i = r; i >= 0; i--) {
            if (gameBoard.getSquare(player, i, j).getStatus() == 0) {
                return false;
            }
            if (j >= Constants.SIZE - 1) {
               return false;
            }
        }
        return true;
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
