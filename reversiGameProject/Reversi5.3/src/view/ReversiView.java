package view;

import model.ChessPieces;
import model.HexCoordinate;
import model.ReversiModel;

/**
 * The ReversiView class is responsible for displaying the game board of the Reversi game.
 * It provides a text-based representation of the board.
 */
public class ReversiView {
  private ReversiModel model; // The game model

  /**
   * Constructs a ReversiView with the specified game model.
   *
   * @param model The Reversi game model
   */
  public ReversiView(ReversiModel model) {
    this.model = model;
  }

  /**
   * Displays the game board in a text-based format.
   */
  public void displayBoard() {
    // Method implementation...
  }

  /**
   * Prints a single row of the game board.
   *
   * @param row The row index
   * @param startSpaces The number of spaces before the row starts
   * @param startQ The starting Q-coordinate for the row
   */
  private void printRow(int row, int startSpaces, int startQ) {
    // 打印前导空格
    for (int i = 0; i < startSpaces; i++) {
      System.out.print(" ");
    }

    // 打印棋盘内容
    for (int q = startQ; q < model.getSize() - startSpaces; q++) {
      HexCoordinate coord = new HexCoordinate(q, row);
      ChessPieces chessPieces = model.getCell(coord);

      if (chessPieces == ChessPieces.X) {
        System.out.print("X ");
      } else if (chessPieces == ChessPieces.O) {
        System.out.print("O ");
      } else {
        System.out.print("_ ");
      }
    }

    System.out.println();
  }

  public void displayCurrentPlayer() {
    System.out.println("Current ChessPieces: " + model.getCurrentTurn());
  }

  /**Show game over's view.*/
  public void displayGameOver() {
    if (model.isGameOver()) {
      ChessPieces winner = model.calculateWinner();
      if (winner != null) {
        System.out.println("Game Over！Winner: " + winner);
      } else {
        System.out.println("Game Over！Tie！");
      }
    }
  }
}








