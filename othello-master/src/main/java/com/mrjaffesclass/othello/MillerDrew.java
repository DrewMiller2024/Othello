package com.mrjaffesclass.othello;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Test player
 */
public class MillerDrew extends Player {

  private static int SEARCH_DEPTH = 4;
  /**
   * Constructor
   * @param color Player color: one of Constants.BLACK or Constants.WHITE
   */
  public MillerDrew(int color) {
    super(color);
  }

  /**
   *
   * @param board
   * @return The player's next move
   */
  @Override
  public Position getNextMove(Board board) {
     /* Your code goes here */
    //call minimax here
  }

  /**
   * Is this a legal move?
   * @param player Player asking
   * @param positionToCheck Position of the move being checked
   * @return True if this space is a legal move
   */
  private boolean isLegalMove(Board board, Position positionToCheck) {
    for (String direction : Directions.getDirections()) {
      Position directionVector = Directions.getVector(direction);
      if (step(board, positionToCheck, directionVector, 0)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Traverses the board in the provided direction. Checks the status of
   * each space: 
   * a. If it's the opposing player then we'll move to the next
   *    space to see if there's a blank space
   * b. If it's the same player then this direction doesn't represent
   *    a legal move
   * c. If it's a blank AND if it's not the adjacent square then this
   *    direction is a legal move. Otherwise, it's not.
   * 
   * @param player  Player making the request
   * @param position Position being checked
   * @param direction Direction to move
   * @param count Number of steps we've made so far
   * @return True if we find a legal move
   */
  private boolean step(Board board, Position position, Position direction, int count) {
    Position newPosition = position.translate(direction);
    int color = this.getColor();
    if (newPosition.isOffBoard()) {
      return false;
    } else if (board.getSquare(newPosition).getStatus() == -color) {
      return this.step(board, newPosition, direction, count+1);
    } else if (board.getSquare(newPosition).getStatus() == color) {
      return count > 0;
    } else {
      return false;
    }
  }

  /**
   * Get the legal moves for this player on the board
   * @param board
   * @return True if this is a legal move for the player
   */
  public ArrayList<Position> getLegalMoves(Board board) {
    int color = this.getColor();
    ArrayList list = new ArrayList<>();
    for (int row = 0; row < Constants.SIZE; row++) {
      for (int col = 0; col < Constants.SIZE; col++) {
        if (board.getSquare(this, row, col).getStatus() == Constants.EMPTY) {
          Position testPosition = new Position(row, col);
          if (this.isLegalMove(board, testPosition)) {
            list.add(testPosition);
          }
        }        
      }
    }
    return list;
  }

  public Board cloneBoard(Board originalBoard) {
    return originalBoard;
  }

  public int minimax(Board gameBoard, int depth, boolean maximizingPlayer) {
    if (depth == SEARCH_DEPTH) {
      bestCoordinate = move;
    }

    if (maximizingPlayer) {
      int bestValue = Integer.MIN_VALUE;
        ArrayList<Position> moves = this.getLegalMoves(gameBoard);
        for (Position move : moves) {
          Board newBoard = cloneBoard(gameBoard);
          processMove(newBoard, newBoard[move.getxCoordinate()][move.getyCoordinate()]);
          int v = minimax(newBoard, depth - 1, !maximizingPlayer);
          if (v > bestValue) {
            bestValue = v;
            bestCoordinate = move;
          }
        }
        return bestValue;
    }
    else {
      int bestValue = Integer.MAX_VALUE;
      LinkedList<Coordinate> moves = generateMoves(gameBoard);
      for (Coordinate move : moves) {
        MyButton[][] newBoard = cloneBoard(gameBoard);
        processMove(newBoard, newBoard[move.getxCoordinate()][move.getyCoordinate()]);
        int v = minimax(newBoard, depth - 1, !maximizingPlayer);
        if (v < bestValue) {
            bestValue = v;
            bestCoordinate = move;
          }
      }
      return bestValue;
    }
  }

  public int evaluateBoard(Board gameBoard) {
    int blackPieces = 0;
    int whitePieces = 0;

    for (int i = 0; i < Constants.SIZE; i++) {
      for (int j = 0; j < Constants.SIZE; j++) {
        Position temp = new Position(i, j);
        Square getSColor = gameBoard.getSquare(temp);
        if (getSColor.equals(Constants.BLACK)) {
            blackPieces++;
        } else if (getSColor.equals(Constants.WHITE)) {
            whitePieces++;
        }
      }
    }

    int cornerBonus = 10;
    if (gameBoard.getSquare(new Position(0, 0)).getStatus() == Constants.BLACK) {
      blackPieces += cornerBonus;
    }
    if (gameBoard.getSquare(new Position(0, Constants.SIZE)).getStatus() == Constants.BLACK) {
      blackPieces += cornerBonus;
    }
    if (gameBoard.getSquare(new Position(Constants.SIZE, 0)).getStatus() == Constants.BLACK) {
      blackPieces += cornerBonus;
    }
    if (gameBoard.getSquare(new Position(Constants.SIZE, Constants.SIZE)).getStatus() == Constants.BLACK) {
      blackPieces += cornerBonus;
    }
    if (gameBoard.getSquare(new Position(0, 0)).getStatus() == Constants.WHITE) {
      whitePieces += cornerBonus;
    }
    if (gameBoard.getSquare(new Position(0, Constants.SIZE)).getStatus() == Constants.WHITE) {
      whitePieces += cornerBonus;
    }
    if (gameBoard.getSquare(new Position(Constants.SIZE, 0)).getStatus() == Constants.WHITE) {
      whitePieces += cornerBonus;
    }
    if (gameBoard.getSquare(new Position(Constants.SIZE, Constants.SIZE)).getStatus() == Constants.WHITE) {
      whitePieces += cornerBonus;
    }
    return whitePieces - blackPieces;
  }
}
