import strategy.AIStrategy;

import org.junit.Before;
import org.junit.Test;

import Players.AIPlayer;
import model.ChessPieces;
import model.HexCoordinate;
import model.ReversiModel;

import static org.junit.Assert.assertEquals;

/**
 * The AIPlayerMockTest class is responsible for testing the AIPlayer's decision-making
 * capabilities in the Reversi game.
 * It uses a mock model to simulate different game scenarios and verifies the AI's responses.
 */
public class AIPlayerMockTest {
  private AIPlayer aiPlayer;
  private AIStrategy avoidCornersStrategy;

  @Before
  public void setUp() {
    ReversiModel mockModel = new ReversiModel(6); // 假设棋盘大小为 6x6
    aiPlayer = new AIPlayer((Strategy.AIStrategy)
            avoidCornersStrategy, mockModel, ChessPieces.X); // 创建 AI 玩家

    // 设置一些特定的走法和对应的翻转数
    mockModel.makeMove(new HexCoordinate(-1, -1), ChessPieces.X); // 移动1: 坐标 [-1,-1] 可以翻转 1 个棋子
    mockModel.makeMove(new HexCoordinate(2, -1), ChessPieces.O);  // 移动2: 坐标 [2,-1] 可以翻转 1 个棋子
    mockModel.makeMove(new HexCoordinate(3, -1), ChessPieces.X);  // 移动3: 坐标 [3,-1] 可以翻转 2 个棋子
  }

  @Test
  public void testMakeDecision() {
    HexCoordinate decision = aiPlayer.makeDecision();

    // 检查 AI 是否选择了翻转数最多的走法
    assertEquals("AI should choose the move with the most flips",
            new HexCoordinate(3, -1), decision);
  }
}

