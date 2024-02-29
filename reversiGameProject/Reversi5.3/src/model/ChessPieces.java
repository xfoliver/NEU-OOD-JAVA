package model;

/**
 * Represents the types of pieces in the Reversi game.
 * This includes pieces for human players (X, O),
 * AI players (AI_X, AI_O), and an EMPTY state for unoccupied cells.
 */
public enum ChessPieces {
  X("X"), // Human player X
  O("O"), // Human player O
  EMPTY("-"), // Unoccupied cell
  AI_X("AI_X"), // AI player X
  AI_O("AI_O"); // AI player O

  private final String disp; // Display string for the piece

  /**
   * Constructs a ChessPieces enum with a display string.
   *
   * @param disp The display string for the piece
   */
  ChessPieces(String disp) {
    this.disp = disp;
  }

  /**
   * Returns the string representation of the piece.
   * This is used mainly for display purposes on the game board.
   *
   * @return The display string of the piece
   */
  @Override
  public String toString() {
    return disp;
  }

  /**
   * Returns the opposite piece type. For human players, it returns the other human player's piece,
   * for AI players, it returns the opposite AI player's piece, and for EMPTY, it returns EMPTY.
   * This is useful in determining the player's opponent in the game.
   *
   * @return The opposite piece type
   */
  public ChessPieces opposite() {
    switch (this) {
      case X:
        return O;
      case O:
        return X;
      case AI_X:
        return AI_O;
      case AI_O:
        return AI_X;
      default:
        return EMPTY; // If the current piece is EMPTY, the opposite is also EMPTY
    }
  }
}
