package model;

import java.util.List;

/**
 * The Model interface defines the essential functionalities for the game model in Reversi.
 * It includes methods for managing game moves, checking game state, and handling player turns.
 */
public interface Model {
  /**
  * Checks if a move is valid for a given chessPieces at the specified coordinate.
  *
  * @param coord The hexagonal coordinate of the move
  * @param chessPieces The chessPieces making the move
  * @return true if the move is valid, false otherwise
  */
  boolean isValidMove(HexCoordinate coord, ChessPieces chessPieces);

  /**
  * Executes a move for a given chessPieces at the specified coordinate.
  *
  * @param coord The hexagonal coordinate where the move is made
  * @param chessPieces The chessPieces making the move
  */

  void makeMove(HexCoordinate coord, ChessPieces chessPieces);

  /**
  * Checks whether the game is over by determining if there are
  * any valid moves left for either player.
  *
  * @return true if the game is over, false otherwise
  */
  boolean isGameOver();

  /**
  * Calculates and returns the winner of the game based on the number
  * of pieces each player has on the board.
  * In case of a tie, it returns null.
  *
  * @return The player who has won the game, or null if the game is a tie
  */
  ChessPieces calculateWinner();

  /**
  * Switches the current turn to the opposite player.
  * This method is typically called after a player completes a move.
  */
  void switchTurn();

  /**
  * Returns the player whose turn it is currently.
  *
  * @return The current player
  */
  ChessPieces getCurrentTurn();

  /**
  * Retrieves the player occupying a specific cell on the board.
  *
  * @param coord The coordinate of the cell
  * @return The player occupying the cell, or ChessPieces.EMPTY if the cell is unoccupied
  */
  ChessPieces getCell(HexCoordinate coord);

  /**
  * Returns the size of the game board, typically representing
  * the number of hexagonal cells along each side.
  *
  * @return The size of the board
  */
  int getSize();

  /**
  * Retrieves a list of all valid moves for the specified chessPieces.
  * This method is used to determine possible moves for a chessPieces at any point in the game.
  *
  * @param chessPieces The chessPieces whose valid moves are to be determined
  * @return A list of HexCoordinate objects representing all valid moves for the given chessPieces
  */
  List<HexCoordinate> getValidMoves(ChessPieces chessPieces);
}
