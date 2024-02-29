package players;

import strategy.AIStrategy;
import model.ChessPieces;
import model.HexCoordinate;
import model.ReversiModel;

/**
 * The AIPlayer class represents an artificial intelligence player in the Reversi game.
 * It is responsible for making automated decisions for moves based on the game's state.
 */
public class AIPlayer implements Players {
  private final AIStrategy strategy;
  private final ReversiModel model;
  public final ChessPieces piece;


  /**
   * Constructs an AI player for the Reversi game.
   * Initializes the AI player with a specific strategy, game model, and chess piece type.
   *
   * @param strategy The AI strategy to be used by this player. It defines how the AI
   *                 will make decisions during the game.
   * @param model    The game model representing the state of the Reversi game. This model
   *                 provides necessary information for the AI to make informed decisions.
   * @param piece    The type of chess piece (ChessPieces) that this AI player will be
   *                 playing with. It could be either black (X) or white (O) pieces.
   */
  public AIPlayer(AIStrategy strategy, ReversiModel model, ChessPieces piece) {
    this.strategy = strategy;
    this.model = model;
    this.piece = piece;
  }

  @Override
  public HexCoordinate makeDecision() {
    HexCoordinate decision = strategy.chooseMove(model);
    return decision;
  }


}
