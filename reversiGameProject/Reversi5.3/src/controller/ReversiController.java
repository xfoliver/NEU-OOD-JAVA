package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import players.Players;
import players.AIPlayer;

import model.HexCoordinate;
import model.ReversiModel;
import model.ChessPieces;
import view.ReversiSwingView;


/**
 * The {@code ReversiController} class manages the interactions between the model and view
 * in a Reversi game. It acts as a mediator between the {@link ReversiModel} (game logic)
 * and the {@link ReversiSwingView} (user interface). This controller handles user actions,
 * updates the game state, and refreshes the view accordingly.
 * The controller supports player interactions through mouse clicks and coordinates the turns
 * between human and AI players.
 */
public class ReversiController {
  private ReversiModel model;
  private ReversiSwingView view;
  private Players player;
  public Players playerO;


  /**
   * Constructs a new ReversiController with the given model, view, and player.
   *
   * @param model The game model representing the state of the Reversi game.
   * @param view The graphical user interface for the game.
   * @param player The player (human or AI) for player X.
   */
  public ReversiController(ReversiModel model, ReversiSwingView view, Players player) {
    this.model = model;
    this.view = view;
    this.player = player;

    // 注册鼠标点击事件的处理
    view.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleMouseClick(e);
      }
    });
    model.addChangeListener(() -> view.repaint());
  }

  /**
   * Sets the player O for the game. This can be either a human or an AI player.
   *
   * @param playerO The player (human or AI) for player O.
   */
  public void setPlayerO(Players playerO) {
    this.playerO = playerO;
  }

  private void handleMouseClick(MouseEvent e) {
    HexCoordinate selectedHex = view.getSelectedHex();
    if (selectedHex != null && model.isValidMove(selectedHex, model.getCurrentTurn())) {
      model.makeMove(selectedHex, model.getCurrentTurn());
      if (model.isGameOver()) {
        ChessPieces winner = model.calculateWinner();
        view.displayGameOver(winner);
      } else {
        model.switchTurn();
        triggerAIMove();
      }
    }
  }

  private void triggerAIMove() {
    Players currentPlayer = getCurrentPlayer();
    if (currentPlayer instanceof AIPlayer) {
      HexCoordinate aiMove = currentPlayer.makeDecision();
      if (aiMove != null) {
        handleMove(aiMove);
      } else {
        // AI doesn't have valid moves
        JOptionPane.showMessageDialog(view, "AI has no valid moves and surrenders.",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        model.switchTurn(); // Switch turns manually
        if (model.isGameOver()) {
          ChessPieces winner = model.calculateWinner();
          view.displayGameOver(winner);
        }
      }
    }
  }



  /**
   * Returns the current player based on the turn in the game model.
   *
   * @return The current player, either player X or O.
   */
  public Players getCurrentPlayer() {
    // 根据当前轮到的棋子类型返回相应的玩家对象
    // 这里假设 `player` 是玩家 X，AI 或另一个玩家是玩家 O
    if (model.getCurrentTurn() == ChessPieces.X) {
      return player; // 如果当前轮到玩家 X，返回 controller 初始化时传入的 player
    } else {
      // 如果当前轮到玩家 O，我们需要返回一个 AIPlayer 或另一个 HumanPlayer
      // 这里的代码取决于你如何存储或获取玩家 O 的对象
      // 例如，你可以有一个成员变量来存储玩家 O，或者根据需要创建一个新的 AIPlayer/HumanPlayer
      return playerO; // 需要根据你的程序设计来实现 playerO 的获取方式
    }
  }



  /**
   * Handles a move made by a player or AI.
   * This method validates the move, updates the model, and refreshes the view.
   *
   * @param move The hex coordinate where the move is made.
   */
  public void handleMove(HexCoordinate move) {
    if (model.isValidMove(move, model.getCurrentTurn())) {
      model.makeMove(move, model.getCurrentTurn());
      view.repaint(); // 更新视图
      if (model.isGameOver()) {
        ChessPieces winner = model.calculateWinner();
        view.displayGameOver(winner); // 显示游戏结束信息
      } else {
        model.switchTurn();
        triggerAIMove(); // 触发AI移动（如果当前玩家是AI）
      }
    } else {
      JOptionPane.showMessageDialog(view, "Invalid move", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
