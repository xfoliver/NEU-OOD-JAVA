package controller;

import players.AIPlayer;
import model.HexCoordinate;
import model.ReversiModel;


/**
 * The {@code AIController} class manages the AI player's actions in a Reversi game.
 * This controller is responsible for initiating the AI player's moves
 * according to the current state
 * of the game model. It works in tandem with an
 * instance of {@link ReversiModel} and an {@link AIPlayer}
 * to determine when and where the AI player should make a move.
 * The controller checks the game model's current state to decide whether it's the AI player's turn.
 * If it is, the controller asks the AI player to make a decision using its strategy and then
 * executes that move in the game model.
 */
public class AIController {
  private ReversiModel model;
  private AIPlayer aiPlayer;

  public AIController(ReversiModel model, AIPlayer aiPlayer) {
    this.model = model;
    this.aiPlayer = aiPlayer;
  }

  /**
   * when ai turn, use this method.
   */
  public void makeAIMove() {
    if (model.getCurrentTurn() == aiPlayer.piece) {
      HexCoordinate aiMove = aiPlayer.makeDecision();
      if (aiMove != null) {
        model.makeMove(aiMove, aiPlayer.piece);
        model.switchTurn();
      }
    }
  }

  /**
   * the method that used for start game.
   */
  public void startGame() {
    // 如果 AI 是第一个行动的玩家，那么它需要立即做出决策
    if (model.getCurrentTurn() == aiPlayer.piece) {
      makeAIMove();
    }
  }
}
