package strategy;

import model.HexCoordinate;
import model.ReversiModel;

/**
 * The {@code AIStrategy} interface defines the strategy for an AI player in the Reversi game.
 * It provides a method to choose the next move based on the current state of the game model.
 * Implementations of this interface should encapsulate the logic for making decisions
 * about moves in the game, allowing different strategies to be employed by AI players.
 */
public interface AIStrategy {

  /**
   * Chooses the next move for an AI player based on the current state of the game model.
   * Implementations should analyze the game board and determine the best move
   * according to the strategy's criteria.
   *
   * @param model The current state of the Reversi game model.
   * @return The chosen {@link HexCoordinate} representing the AI's next move.
   *         Returns {@code null} if there are no valid moves available.
   */
  HexCoordinate chooseMove(ReversiModel model);
}
