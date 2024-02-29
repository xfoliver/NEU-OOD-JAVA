package strategy;

import java.util.Arrays;
import java.util.List;

import model.HexCoordinate;
import model.ReversiModel;

/**
 * {@code CornersStrategy} implements the {@link AIStrategy} interface for the Reversi game.
 * This strategy focuses on occupying corner positions on the board whenever possible.
 * Occupying corners is a strategic advantage in Reversi, as pieces placed in corners
 * cannot be flipped by the opponent.
 * The strategy first checks for any available corner moves. If a corner move is available,
 * it is chosen as the preferred move. If no corner moves are available, the strategy defaults
 * to a secondary strategy, such as maximizing the capture of opponent pieces.
 * Corners are pre-defined and are considered the most strategic positions on the board.
 * This strategy is particularly effective in the mid to late stages of the game, where
 * capturing corners can lead to significant advantages.
 */
public class CornersStrategy implements AIStrategy {


  /**
   * A static list of corner positions on the board.
   */
  private static final List<HexCoordinate> corners = Arrays.asList(
          new HexCoordinate(0, 0),
          new HexCoordinate(0, 5),
          new HexCoordinate(5, 0),
          new HexCoordinate(5, 5)
  );


  /**
   * Chooses a move for an AI player based on the current state of the Reversi game model.
   * Prioritizes corner moves for their strategic value. If no corners are available,
   * it defaults to a strategy that maximizes the capture of opponent pieces.
   *
   * @param model The current state of the Reversi game model.
   * @return The chosen {@link HexCoordinate} representing the AI's next move,
   *         or {@code null} if no valid moves are available.
   */
  @Override
  public HexCoordinate chooseMove(ReversiModel model) {
    List<HexCoordinate> validMoves = model.getValidMoves(model.getCurrentTurn());

    // 首先检查是否有角落可以被占领
    HexCoordinate cornerMove = validMoves.stream()
            .filter(corners::contains)
            .findFirst()
            .orElse(null);

    // 如果可以占领角落，直接返回该移动
    if (cornerMove != null) {
      return cornerMove;
    }

    // 如果没有角落可占领，可以退回到最大捕获策略
    return new MaxCaptureStrategy().chooseMove(model);
  }
}

