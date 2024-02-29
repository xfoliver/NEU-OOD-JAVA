package players;

import model.ChessPieces;
import model.HexCoordinate;
import model.ReversiModel;

/**
 * Represents a human player in the Reversi game.
 * This class encapsulates the behavior and decision-making process
 * for a player who is controlled by a human user, rather than by an AI algorithm.
 */
public class HumanPlayer implements Players {

  /**
   * Constructs a human player for the Reversi game.
   * Initializes the human player with a specific game model and the type of chess piece
   * the player will be using (either black or white).
   *
   * @param model The game model representing the state of the Reversi game. This model
   *              provides the game's current state and is used to make valid moves.
   * @param piece The type of chess piece (ChessPieces) that this human player will be
   *              playing with. It could be either black (X) or white (O) pieces.
   */
  public HumanPlayer(ReversiModel model, ChessPieces piece) {
    // Currently, there is no initialization required in the constructor.
    // Future modifications can add relevant initialization code here.
  }

  /**
   * Determines the move for the human player.
   * The decision-making process for a human player is based on user input, which
   * is not handled in this method. Therefore, this method may return null or await
   * user input before returning a decision.
   *
   * @return The chosen move as a HexCoordinate, or null if no move has been made yet.
   */
  @Override
  public HexCoordinate makeDecision() {
    // 人类玩家的决策由用户输入确定，这里可能返回 null 或等待用户输入
    return null;
  }
}

