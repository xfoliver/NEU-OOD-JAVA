package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents the game model for the Reversi game, encapsulating the game logic and state.
 * This class implements ReadonlyReversiModel and provides methods for managing the game.
 */
public class ReversiModel implements Model {
  private final int size; // The size of the game board
  private final Map<HexCoordinate, ChessPieces> board; // The game board
  private ChessPieces currentTurn; // The current player's turn
  private List<ReversiChangeListener> listeners = new ArrayList<>();

  /**
   * Constructs a new ReversiModel with the specified board size.
   *
   * @param size The size of the game board
   */
  public ReversiModel(int size) {
    this.size = size;
    this.board = new HashMap<>();
    this.currentTurn = ChessPieces.X; // Black player starts first
    initializeBoard();
  }

  public void addChangeListener(ReversiChangeListener listener) {
    listeners.add(listener);
  }

  private void notifyChange() {
    for (ReversiChangeListener listener : listeners) {
      listener.stateChanged();
    }
  }

  /**
   * Initializes the game board with the starting pieces.
   */
  private void initializeBoard() {
    // Calculate the center of the board in offset coordinates
    int centerQ = size / 4 - 1;
    int centerR = size / 4 - 1;

    // Place the initial four pieces in a square formation in the center
    board.put(new HexCoordinate(centerQ - 1, centerR), ChessPieces.X);
    board.put(new HexCoordinate(centerQ - 1, centerR + 1), ChessPieces.O); // Below center
    board.put(new HexCoordinate(centerQ + 1, centerR - 1), ChessPieces.X);
    board.put(new HexCoordinate(centerQ, centerR - 1), ChessPieces.O); // Right of center
    board.put(new HexCoordinate(centerQ, centerR + 1), ChessPieces.X);
    board.put(new HexCoordinate(centerQ + 1, centerR), ChessPieces.O);
  }

  /**
   * Checks if the game has started based on the number of pieces on the board.
   *
   * @return true if the game has started, otherwise false
   */
  private boolean isGameStart() {
    // 假设游戏开始时棋盘上只有6个棋子
    return board.size() == 6;
  }

  /**
   * Checks whether a move is valid for the specified chessPieces and coordinate.
   *
   * @param coord The coordinate for the move
   * @param chessPieces The chessPieces making the move
   * @return true if the move is valid, false otherwise
   */
  public boolean isValidMove(HexCoordinate coord, ChessPieces chessPieces) {
    if (board.getOrDefault(coord, ChessPieces.EMPTY) != ChessPieces.EMPTY) {
      return false;
    }
    for (int direction = 0; direction < 6; direction++) {
      List<HexCoordinate> toFlip = piecesToFlip(coord, chessPieces, direction);
      if (!toFlip.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves a list of all valid moves for the given chessPieces.
   *
   * @param chessPieces The chessPieces whose valid moves are to be found
   * @return List of HexCoordinate objects representing valid moves
   */
  public List<HexCoordinate> getValidMoves(ChessPieces chessPieces) {
    List<HexCoordinate> validMoves = new ArrayList<>();
    for (int q = 0; q < size; q++) {
      for (int r = 0; r < size; r++) {
        HexCoordinate coord = new HexCoordinate(q, r);
        if (isValidMove(coord, chessPieces)) {
          validMoves.add(coord);
        }
      }
    }
    return validMoves;
  }

  /**
   * Evaluates a move by calculating the total number of pieces that can be flipped.
   *
   * @param move The move to evaluate
   * @param chessPieces The chessPieces making the move
   * @return The total number of flippable pieces for the move
   */
  public int evaluateMove(HexCoordinate move, ChessPieces chessPieces) {
    int totalFlips = 0;
    for (int direction = 0; direction < 6; direction++) {
      List<HexCoordinate> flippablePieces = piecesToFlip(move, chessPieces, direction);
      totalFlips += flippablePieces.size();
    }
    return totalFlips;
  }

  /**
   * Executes a move for the specified chessPieces at the given coordinate.
   *
   * @param coord The coordinate where the move is made
   * @param chessPieces The chessPieces making the move
   * @throws IllegalArgumentException if the move is invalid or if the game has not started properly
   */
  public void makeMove(HexCoordinate coord, ChessPieces chessPieces) {
    // 检查是否是游戏开始时白方（ChessPieces.O）的回合
    if (isGameStart() && chessPieces == ChessPieces.O) {
      throw new IllegalArgumentException("White chessPieces cannot move first!");
    }
    if (!isValidMove(coord, chessPieces)) {
      throw new IllegalArgumentException("Invalid move!");
    }

    board.put(coord, chessPieces);
    for (int direction = 0; direction < 6; direction++) {
      for (HexCoordinate toFlip : piecesToFlip(coord, chessPieces, direction)) {
        board.put(toFlip, chessPieces);
      }
    }
    notifyChange();
  }

  /**
   * Determines the pieces to be flipped for a move in a given direction.
   *
   * @param coord The coordinate of the move
   * @param chessPieces The chessPieces making the move
   * @param direction The direction to check for flippable pieces
   * @return List of HexCoordinate objects representing the pieces to be flipped
   */
  private List<HexCoordinate> piecesToFlip(
          HexCoordinate coord, ChessPieces chessPieces, int direction) {
    List<HexCoordinate> path = new ArrayList<>();
    HexCoordinate current = coord.neighbor(direction);
    while (board.getOrDefault(current, ChessPieces.EMPTY) == chessPieces.opposite()) {
      path.add(current);
      current = current.neighbor(direction);
    }
    if (board.getOrDefault(current, ChessPieces.EMPTY) == chessPieces) {
      return path;
    }
    return Collections.emptyList();
  }

  /**
   * Checks whether the game is over by determining if there are any valid moves left.
   *
   * @return true if the game is over, false otherwise
   */
  public boolean isGameOver() {
    for (int r = 0; r < size; r++) {
      for (int q = 0; q < size; q++) {
        HexCoordinate coord = new HexCoordinate(q, r);
        if (isValidMove(coord, ChessPieces.X) || isValidMove(coord, ChessPieces.O)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Calculates the winner of the game based on the number of pieces each player has.
   *
   * @return ChessPieces who is the winner, or null in case of a tie
   */
  public ChessPieces calculateWinner() {
    int countX = 0;
    int countO = 0;
    for (ChessPieces p : board.values()) {
      if (p == ChessPieces.X) {
        countX++;
      }
      else if (p == ChessPieces.O) {
        countO++;
      }
    }
    if (countX > countO) {
      return ChessPieces.X;
    }
    else if (countO > countX) {
      return ChessPieces.O;
    }
    else {
      return null; // 平局
    }
  }

  /**
   * Switches the current turn to the opposite player.
   */
  public void switchTurn() {
    currentTurn = currentTurn.opposite();
    notifyChange();
  }


  /**
   * Gets the current player's turn.
   *
   * @return The current player
   */
  public ChessPieces getCurrentTurn() {
    return currentTurn;
  }

  /**
   * Gets the player occupying a specific cell on the board.
   *
   * @param coord The coordinate of the cell
   * @return The player occupying the cell
   */
  public ChessPieces getCell(HexCoordinate coord) {
    return board.getOrDefault(coord, ChessPieces.EMPTY);
  }

  /**
   * Gets the size of the game board.
   *
   * @return The size of the board
   */
  public int getSize() {
    return size;
  }


}

