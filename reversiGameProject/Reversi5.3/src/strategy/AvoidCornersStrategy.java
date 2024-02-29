package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.HexCoordinate;
import model.ReversiModel;
import strategy.AIStrategy;

/**
 * {@code AvoidCornersStrategy} implements the {@link AIStrategy} interface for the Reversi game.
 * This strategy focuses on avoiding moves that are adjacent to corners of the board.
 * The intention is to minimize the risk of giving the opponent strategic advantages
 * in the early stages of the game.
 * Moves next to corners are often disadvantageous as they can allow the opponent
 * to capture a corner, which is a highly strategic position. This strategy filters
 * out such moves and selects the best move based on maximum capture of opponent pieces
 * from the remaining options.
 * If no 'safe' moves are available (i.e., moves that are not next to corners),
 * the strategy defaults to a secondary approach, such as selecting the first available
 * valid move or a random choice.
 */
public class AvoidCornersStrategy implements AIStrategy {


  /**
   * Chooses a move for an AI player based on the current state of the Reversi game model.
   * Prioritizes moves that are not adjacent to corners and seeks to maximize the
   * capture of opponent pieces.
   * If no such moves are available, it may choose the first valid move or a random valid move.
   *
   * @param model The current state of the Reversi game model.
   * @return The chosen {@link HexCoordinate} representing the AI's next move,
   *         or {@code null} if no valid moves are available.
   */
  @Override
  public HexCoordinate chooseMove(ReversiModel model) {
    List<HexCoordinate> validMoves = model.getValidMoves(model.getCurrentTurn());
    List<HexCoordinate> safeMoves = new ArrayList<>();

    for (HexCoordinate move : validMoves) {
      if (!isNextToCorner(move, model.getSize())) {
        safeMoves.add(move);
      }
    }


    // 如果存在安全的移动，选择可以捕获最多对手棋子的移动
    HexCoordinate bestMove = null;
    int maxCaptures = 0;
    for (HexCoordinate move : safeMoves) {
      int captures = model.evaluateMove(move, model.getCurrentTurn());
      if (captures > maxCaptures) {
        bestMove = move;
        maxCaptures = captures;
      }
    }

    // 如果没有安全的移动，采用其他策略，例如随机选择一个合法移动
    if (bestMove == null && !validMoves.isEmpty()) {
      bestMove = validMoves.get(0); // 或者可以进一步实现随机选择
    }

    return bestMove;
  }

  /**
   * Checks whether a given move is adjacent to any corner of the board.
   *
   * @param move The move to check.
   * @param boardSize The size of the game board.
   * @return {@code true} if the move is next to a corner, {@code false} otherwise.
   */
  private boolean isNextToCorner(HexCoordinate move, int boardSize) {
    // 假设 boardSize 是从中心到角落的格子数
    // 首先定义所有角落的位置
    List<HexCoordinate> corners = Arrays.asList(
            new HexCoordinate(0, 0),
            new HexCoordinate(0, boardSize - 1),
            new HexCoordinate(boardSize - 1, 0),
            new HexCoordinate(boardSize - 1, boardSize - 1)
    );

    // 检查移动是否在任一角落旁边
    for (HexCoordinate corner : corners) {
      if (isAdjacent(move, corner)) {
        return true;
      }
    }

    return false;
  }


  /**
   * Determines if two hex coordinates are adjacent on the board.
   *
   * @param a The first coordinate.
   * @param b The second coordinate.
   * @return {@code true} if the coordinates are adjacent, {@code false} otherwise.
   */
  private boolean isAdjacent(HexCoordinate a, HexCoordinate b) {
    // 检查两个坐标是否相邻
    // 对于六边形棋盘，相邻意味着 q 或 r 坐标相差 1 或者两者都相差 1
    int dq = Math.abs(a.getQ() - b.getQ());
    int dr = Math.abs(a.getR() - b.getR());
    return dq + dr == 1 || (dq == 1 && dr == 1);
  }

}
