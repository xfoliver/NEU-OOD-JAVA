package strategy;

import java.util.List;

import model.HexCoordinate;
import model.ReversiModel;
import strategy.AIStrategy;


/**
 * {@code MaxCaptureStrategy} implements the {@link AIStrategy} interface for the Reversi game.
 * This strategy focuses on choosing moves that maximize the number of opponent pieces captured
 * in a single move. It evaluates all valid moves for the current player and selects the one
 * that flips the maximum number of opponent's pieces.
 * In cases where multiple moves result in the same maximum number of captures, the strategy
 * prefers the move that is positioned upper-left on the board, based on the hexagonal coordinates.
 * This approach is particularly effective in the early and middle stages of the game,
 * where capturing more pieces can lead to a significant advantage.
 */
public class MaxCaptureStrategy implements AIStrategy {


  /**
   * Chooses a move for an AI player based on the current state of the Reversi game model.
   * Prioritizes moves that result in the maximum capture of opponent pieces. If multiple moves
   * have the same capture potential, it chooses the one that is upper-left on the board.
   *
   * @param model The current state of the Reversi game model.
   * @return The chosen {@link HexCoordinate} representing the AI's next move,
   *         or {@code null} if no valid moves are available.
   */
  @Override
  public HexCoordinate chooseMove(ReversiModel model) {
    List<HexCoordinate> validMoves = model.getValidMoves(model.getCurrentTurn());
    HexCoordinate bestMove = null;
    int maxCaptures = -1; // 初始化为-1，确保至少有0个捕获时可以更新

    for (HexCoordinate move : validMoves) {
      int captures = model.evaluateMove(move, model.getCurrentTurn());
      if (captures > maxCaptures || (captures == maxCaptures && isUpperLeft(move, bestMove))) {
        bestMove = move;
        maxCaptures = captures;
      }
    }

    return bestMove;
  }


  /**
   * Determines if a move is more upper-left on the board compared to another move.
   *
   * @param move1 The first hex coordinate to compare.
   * @param move2 The second hex coordinate to compare.
   * @return {@code true} if move1 is more upper-left than move2, otherwise {@code false}.
   */
  private boolean isUpperLeft(HexCoordinate move1, HexCoordinate move2) {
    // 如果 move2 是 null 或 move1 在 move2 的上方或同在一行但更靠左，则返回 true
    if (move2 == null) {
      return true;
    }
    if (move1.getR() < move2.getR()) {
      return true;
    }
    return move1.getR() == move2.getR() && move1.getQ() < move2.getQ();
  }
}
