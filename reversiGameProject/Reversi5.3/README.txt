- Overview:         Reversi is a code base that implements the classic board game Reversi.
                    This project aims to provide a complete Reversi game experience, including
                    game logic, interface and player interaction. The code base provides a
                    complete game process implementation and corresponding unit tests. By the way,
                    this game include to game choice player versus player and player versus AI. And
                    the player also can choose the AI player's strategy.


- Quick start:      /**
                     * The GameProgram class contains the main method to run the Reversi game.
                     * It initializes the model, view, and controller, and sets up the main application window, and so on.
                     */
                    public class GameProgram {

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

- Key components:   1.Model: the interface that contain basic methods for model.
                    2.ReadonlyReversiModel: the interface that contain basic methods for model.
                    3.ReversiChangeListener: Keep the view and model in sync.
                    4.HexCoordinate: Responsible for handling the coordinate system on the chessboard.
                    5.ChessPieces: Indicates the chess in the game, which are  X and O.
                    6.ReversiModel: This is the core model of the game and is responsible for managing the state and logic of the game.

                    7.AIPlayer: this class represent the AIPlayer
                    8.HumanPlayer: this class represent the human player
                    9.Player: the interface that contain basic methods for player.

                    10.  AiStrategy: the interface of strategy method of AI player
                    11. AvoidCornerStrategy: this class represent the strategy that the AIPlayer will avoid player put chess in corner
                    12. CornersStrategy: this class represent the strategy that the AIPlayer will put chess in corner
                    13. MaxCaptureStrategy: this class represent the strategy that the AIPlayer use to move the chess
                    14. RandomStrategy: this class represent the strategy that the AIPlayer will put chess randomly


                    15.ReversiSwingView: this class represent the GUI draw for the game.
                    16.ReversiView: Responsible for the visual presentation and user interface of the game.
                    17.View: this is interface of reversi game's view

                    18. GameProgram: Contains the main method and is the entry point for game startup




  Key subcomponents:  1. HexCoordinate:

                       Properties (member variables):

                       int q: represents a coordinate axis in the hexagonal coordinate system.
                       int r: represents another coordinate axis in the hexagonal coordinate system.
                       Constructor:

                       HexCoordinate(int q, int r): Used to create a HexCoordinate instance and set the q and r values.
                       method:

                       neighbor(int direction): Calculate and return the coordinates of adjacent hexagonal
                                                cells according to the specified direction.

                       getQ(): Returns the q value.
                       getR(): Returns the r value.
                       equals(Object o): Checks whether two HexCoordinate objects are equal.
                       hashCode(): generates the hash code of the object.

                      2. ChessPieces:

                      Enumeration value:

                      X("X"): Represents player X.
                      O("O"): represents player O.
                      EMPTY("-"): Indicates a space or no player.
                      Properties (member variables):

                      String disp: Display characters used to represent enumeration values.
                      Constructor:

                      ChessPieces(String disp): used to create Player enumeration instance and set disp value.
                      method:

                      toString(): Overridden method used to return the displayed characters of the Player enumeration value.
                      opposite(): Returns the opposite party based on the current player.

                      3. ReversiModel:

                      Member Variables:

                      int size: Represents the size of the game board.

                      Map<HexCoordinate, ChessPieces> board: Stores the mapping of each hexagonal coordinate on the board to the corresponding chess piece.

                      ChessPieces currentTurn: Indicates the current player's turn.

                      List<ReversiChangeListener> listeners: A list of listeners for notifying updates when the model's state changes.


                      Constructor:

                      ReversiModel(int size): Constructor that initializes the board size, board state, and the current player's turn.

                      Private Methods:

                      initializeBoard(): Initializes the starting layout of chess pieces on the board.

                      notifyChange(): Notifies all registered listeners that the model's state has changed.

                      piecesToFlip(HexCoordinate coord, ChessPieces chessPieces, int direction): Calculates the number of pieces that can be flipped in a specific direction.


                      Public Methods:

                      isValidMove(HexCoordinate coord, ChessPieces chessPieces): Checks whether a move is valid for the given coordinates and chess pieces.

                      makeMove(HexCoordinate coord, ChessPieces chessPieces): Executes a move and updates the board, also notifies listeners.

                      isGameStart(): Checks whether the game has started.

                      isGameOver(): Determines whether the game is over.

                      calculateWinner(): Calculates the winner of the game.

                      switchTurn(): Switches the current turn to the opposite player and notifies listeners.

                      getCurrentTurn(): Gets the current player's turn.

                      getCell(HexCoordinate coord): Retrieves the state of a cell at the given coordinates.

                      getSize(): Gets the size of the game board.

                      addChangeListener(ReversiChangeListener listener): Adds a change listener.

                      getValidMoves(ChessPieces chessPieces): Retrieves all valid moves for the given chess pieces.

                      evaluateMove(HexCoordinate move, ChessPieces chessPieces): Evaluates a move by calculating the total number of flippable pieces for the move.






                      4. ReversSwingiView:

                      Member Variables:

                      ReversiModel model: The game model.

                      int hexesPerSide: Number of hexes per side of the board.

                      HexCoordinate selectedHex: The currently selected hex on the board.

                      ReversiController controller: The controller handling game logic.


                      Constructor:

                      ReversiSwingView(ReversiModel model): Initializes the view with the specified game model. Sets board size, preferred dimensions, background color, and adds a mouse listener.


                      Public Methods:

                      getSelectedHex(): Returns the currently selected hex.

                      setController(ReversiController controller): Associates a controller with the view.


                      Private Methods:

                      handleMouseClick(MouseEvent e): Handles mouse click events to select hexes on the board and inform the controller about moves.

                      drawBoard(Graphics2D g2d): Draws the hexagonal game board and the pieces.

                      drawPiece(Graphics2D g2d, HexCoordinate hexCoord, ChessPieces occupant): Draws a piece on the given hex coordinate.

                      highlightSelectedHex(Graphics2D g2d, HexCoordinate hexCoord): Highlights the selected hex on the board.

                      createHexagon(int x, int y, int radius): Creates a hexagon shape at the given coordinates and radius.

                      hexToPixel(int radius, int q, int r, Point center): Converts hex coordinates to pixel coordinates on the screen.

                      calculateHexRadius(): Calculates the radius of the hexes based on the current view size.

                      displayGameOver(ChessPieces winner): Displays a game over dialog with the result of the game.


                      Override Methods:

                      paintComponent(Graphics g): Overrides the method to draw the game board and pieces. Calls drawBoard and highlights the selected hex if any.


- Source organization: /src: Contains all main classes and logic.

                         /model: Contains ReversiModel and related logic.

                           /model: the interface for model classes
                           /ReadonlyReversiModel: the interface for reversi model.
                           /ReversiModel: This is the core model of the game and is responsible
                                          for managing the state and logic of the game.
                           /ChessPieces: Indicates the player in the game,
                                    which can be a real player or an AI player.
                           /HexCoordinate: Responsible for handling the coordinate system on the chessboard.
                           /List<ReversiChangeListener> listeners: A list of listeners for notifying updates when the model's state changes.


                         /Players: the interface for player classes

                           /Players: the interface for player classes
                           /AIPlayer: this class that represent the AIPlayer.
                           /HumanPlayer: this class that represent the human player

                         /view: Contains ReversiView and related logic.

                           /View: the interface that contain the basic method for view class.
                           /ReversiView: Responsible for the visual presentation and user interface of the game.
                           /ReversiSwingView: Responsible for the visual presentation and user interface of the game.


                         /Strategy: contains the AI move chess strategy logic

                           /AIStrategy: the interface of strategy class.
                           /AvoidCornerStrategy: this class represent the strategy that the AIPlayer will avoid player put chess in corner
                           /CornersStrategy: this class represent the strategy that the AIPlayer will put chess in corner
                           /MaxCaptureStrategy: this class represent the strategy that the AIPlayer use to move the chess
                           /RandomStrategy: this class represent the strategy that the AIPlayer will put chess randomly


                         /GameProgram: the main method that start up the game.

                       /test: Contains unit tests.

                         /ReversiModelTest: Contains the ReversiModelTest test method.
                         /ReversiViewTest: Contains the ReversiViewTest test method.
                         /StrategyTest: Contains the strategy test method.