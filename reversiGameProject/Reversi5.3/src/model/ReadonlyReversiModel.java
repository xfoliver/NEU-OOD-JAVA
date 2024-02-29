package model;

import java.util.List;

/**
 * The ReadonlyReversiModel interface provides a read-only view of the Reversi game model.
 * It includes methods to query the game state and board information without the ability
 * to modify it.
 */
public interface ReadonlyReversiModel {
  /**
   * Returns the size of the game board.
   *
   * @return The size of the board
   */
  int getSize();

  /**
   * Returns the player occupying a specific cell on the board.
   *
   * @param coord The coordinate of the cell
   * @return The player occupying the cell
   */
  ChessPieces getCell(HexCoordinate coord);

  /**
   * Checks whether the game is over by determining if there are any valid moves
   * left for either player.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Calculates and returns the winner of the game based on the number of pieces
   * each player has on the board.
   * In case of a tie, it returns null.
   *
   * @return The player who has won the game, or null if the game is a tie
   */
  ChessPieces calculateWinner();

  /**
   * Returns the player whose turn it is currently.
   *
   * @return The current player
   */
  ChessPieces getCurrentTurn();

  /**
   * Retrieves a list of all valid moves for the specified chessPieces.
   * This method is used to determine possible moves for a chessPieces at any point in the game.
   *
   * @param chessPieces The chessPieces whose valid moves are to be determined
   * @return A list of HexCoordinate objects representing all valid moves for the given chessPieces
   */
  List<HexCoordinate> getValidMoves(ChessPieces chessPieces);

}
