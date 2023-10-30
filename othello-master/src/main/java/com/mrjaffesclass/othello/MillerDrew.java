package com.mrjaffesclass.othello;

import java.util.LinkedList;

/**
 * Test player
 */
public class MillerDrew extends Player {

  private int SEARCH_DEPTH = 4;
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

  public int minimax(MyButton[][] gameBoard, int depth, boolean maximizingPlayer) {
    if (depth == SEARCH_DEPTH) {
      bestCoordinate = move;
    }

    if (maximizingPlayer) {
      int bestValue = Integer.MIN_VALUE;
        LinkedList<Coordinate> moves = generateMoves(gameBoard);
        for (Coordinate move : moves) {
          MyButton[][] newBoard = cloneBoard(gameBoard);
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

  public int evaluateBoard(MyButton[][] gameBoard) {
    int blackPieces = 0;
    int whitePiecess = 0;

    for (MyButton[] array : gameBoard) {
      for (MyButton button : array) {
        if (button.getBackground().equals(Constants.BLACK)) {
            blackPieces++;
        } else if (button.getBackground().equals(Constants.WHITE)) {
            whitePiecess++;
        }
      }
    }

    int cornerBonus = 10;
    if (gameBoard[0][0].getBackground().equals(Color.BLACK)) {
      blackPieces += cornerBonus;
    }
    if (gameBoard[0][getBoardWidth() - 1].getBackground().equals(Color.BLACK)) {
      blackPieces += cornerBonus;
    }
    if (gameBoard[getBoardHeight() - 1][0].getBackground().equals(Color.BLACK)) {
      blackPieces += cornerBonus;
    }
    if (gameBoard[getBoardHeight() - 1][getBoardWidth() - 1].getBackground().equals(Color.BLACK)) {
      blackPieces += cornerBonus;
    }
    if (gameBoard[0][0].getBackground().equals(Color.WHITE)) {
      whitePiecess += cornerBonus;
    }
    if (gameBoard[0][getBoardWidth() - 1].getBackground().equals(Color.WHITE)) {
      whitePiecess += cornerBonus;
    }
    if (gameBoard[getBoardHeight() - 1][0].getBackground().equals(Color.WHITE)) {
      whitePiecess += cornerBonus;
    }
    if (gameBoard[getBoardHeight() - 1][getBoardWidth() - 1].getBackground().equals(Color.WHITE)) {
      whitePiecess += cornerBonus;
    }
    return whitePiecess - blackPieces;
  }
}
