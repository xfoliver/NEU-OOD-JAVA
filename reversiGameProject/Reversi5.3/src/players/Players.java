package players;

import model.HexCoordinate;

/**
 * The Players interface might be intended to define common
 * functionalities for player management in the Reversi game.
 * Currently, this interface is empty and might serve as a
 * placeholder for future implementations or extensions.
 */
public interface Players {

  /**
   * Makes a decision for the AI chessPieces's next move.
   * Evaluates and returns the best move based on the current state of the game.
   *
   * @return HexCoordinate representing the best move for the AI chessPieces
   */
  HexCoordinate makeDecision();
}
