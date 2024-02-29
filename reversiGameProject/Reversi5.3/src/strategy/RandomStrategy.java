package strategy;

import java.util.List;
import java.util.Random;

import model.HexCoordinate;
import model.ReversiModel;
import strategy.AIStrategy;


/**
 * {@code RandomStrategy} implements the {@link AIStrategy} interface for the Reversi game.
 * This strategy selects a move for the AI player randomly from the list of all valid moves
 * available for the current turn. This approach introduces unpredictability in the AI's gameplay,
 * making it less predictable and potentially more challenging for human players.
 * The strategy uses a {@link Random} instance to ensure the randomness of the move selection.
 * If there are no valid moves available, it returns {@code null}, indicating that the AI player
 * cannot make a move.
 */
public class RandomStrategy implements AIStrategy {
  private final Random random = new Random();


  /**
   * Chooses a move randomly for an AI player based on the current state of the Reversi game model.
   * It selects from the list of all valid moves available for the current player.
   *
   * @param model The current state of the Reversi game model.
   * @return The randomly chosen {@link HexCoordinate} representing the AI's next move,
   *         or {@code null} if no valid moves are available.
   */
  @Override
  public HexCoordinate chooseMove(ReversiModel model) {
    List<HexCoordinate> validMoves = model.getValidMoves(model.getCurrentTurn());
    if (validMoves.isEmpty()) {
      return null; // 没有有效移动时返回null
    }
    return validMoves.get(random.nextInt(validMoves.size())); // 随机选择一个有效移动
  }
}
