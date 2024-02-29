import javax.swing.JOptionPane;
import javax.swing.JFrame;

import players.Players;
import players.AIPlayer;
import players.HumanPlayer;
import strategy.AvoidCornersStrategy;
import strategy.CornersStrategy;
import strategy.MaxCaptureStrategy;
import strategy.RandomStrategy;
import controller.ReversiController;
import model.ChessPieces;

import model.ReversiModel;
import view.ReversiSwingView;

/**
 * The GameProgram class contains the main method to run the Reversi game.
 * It initializes the model, view, and controller, and sets up the main application window.
 */
public class GameProgram {

  /**
   * The main method for the Reversi game application.
   * Initializes the game model, sets up players based on user choice,
   * and configures the game view and controller. The method also handles the creation
   * and display of the game window.
   * The game allows the user to choose between playing against another human player
   * or an AI opponent. If AI is chosen, the user is then prompted to select an AI strategy.
   * Depending on the user's choices, the appropriate players, views,
   * and controllers are initialized.
   * The game supports different strategies for AI players and sets up two game views
   * in case of two human players.
   * The main application window is set up and displayed, with the game ready for play.
   */
  public static void main(String[] args) {
    ReversiModel model = new ReversiModel(6);
    String[] options = {"AI", "Human"};
    int response = JOptionPane.showOptionDialog(null, "Choose your opponent", "Game Setup",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, options, options[0]);

    Players player1 = new HumanPlayer(model, ChessPieces.X);
    Players player2;

    ReversiSwingView viewPlayer1 = new ReversiSwingView(model);
    ReversiController controller1;

    if (response == 0) {
      // 用户选择与 AI 对战，现在让他们选择一个 AI 策略
      String[] strategies = {"Max Capture", "Avoid Corners", "Corners", "Random"};
      int strategyChoice = JOptionPane.showOptionDialog(null, "Choose AI Strategy", "AI Setup",
              JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
              null, strategies, strategies[0]);

      // 根据选择的策略创建 AI 玩家
      switch (strategyChoice) {
        case 0:
          player2 = new AIPlayer(new MaxCaptureStrategy(), model, ChessPieces.O);
          break;
        case 1:
          player2 = new AIPlayer(new AvoidCornersStrategy(), model, ChessPieces.O);
          break;
        case 2:
          player2 = new AIPlayer(new CornersStrategy(), model, ChessPieces.O);
          break;
        case 3:
          player2 = new AIPlayer(new RandomStrategy(), model, ChessPieces.O);
          break;
        default:
          throw new IllegalArgumentException("Unknown strategy");
      }
      controller1 = new ReversiController(model, viewPlayer1, player1);
      controller1.setPlayerO(player2); // 设置 AI 玩家
    } else {
      // 用户选择与另一个玩家对战
      player2 = new HumanPlayer(model, ChessPieces.O);
      ReversiSwingView viewPlayer2 = new ReversiSwingView(model);
      controller1 = new ReversiController(model, viewPlayer1, player1);
      ReversiController controller2 = new ReversiController(model, viewPlayer2, player2);
      viewPlayer2.setController(controller2);
      createAndShowGUI("Player 2", viewPlayer2);
    }

    viewPlayer1.setController(controller1);
    createAndShowGUI("Player 1", viewPlayer1);
  }

  private static void createAndShowGUI(String title, ReversiSwingView view) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(view);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}




