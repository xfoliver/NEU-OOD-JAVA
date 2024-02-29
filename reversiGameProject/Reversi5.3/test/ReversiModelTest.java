import model.ChessPieces;
import model.HexCoordinate;
import model.ReversiModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**This class is tests class, check each whether the model method work correct.*/
public class ReversiModelTest {

  private ReversiModel model;

  @BeforeEach
  void setUp() {
    model = new ReversiModel(11); // Assuming a standard 11*11 board
  }

  @Test
  void testInitialState() {
    assertEquals(ChessPieces.X, model.getCurrentTurn(), "Black should start the game");
    assertEquals(ChessPieces.EMPTY, model.getCell(
            new HexCoordinate(0, 0)), "Top-left corner should be empty");
  }


  @Test
  void testInvalidMove() {
    assertThrows(IllegalArgumentException.class, () -> model.makeMove(
                    new HexCoordinate(0, 0), ChessPieces.X),
            "Making an invalid move should throw an exception");
  }

  @Test
  void testIsGameOver() {
    assertFalse(model.isGameOver(), "Game should not be over at the beginning");
  }

  @Test
  void testGetSize() {
    assertEquals(model.getSize(),11);
  }

  @Test
  public void testNeighborCoordinates() {
    HexCoordinate coord = new HexCoordinate(3, 3);
    assertEquals(new HexCoordinate(4, 3), coord.neighbor(0));
    assertEquals(new HexCoordinate(4, 2), coord.neighbor(1));
  }

  @Test
  public void testEquality() {
    HexCoordinate coord1 = new HexCoordinate(3, 3);
    HexCoordinate coord2 = new HexCoordinate(3, 3);
    HexCoordinate coord3 = new HexCoordinate(4, 3);
    assertEquals(coord1, coord2);
    assertNotEquals(coord1, coord3);
  }

  @Test
  public void testHashCode() {
    HexCoordinate coord1 = new HexCoordinate(3, 3);
    HexCoordinate coord2 = new HexCoordinate(3, 3);
    assertEquals(coord1.hashCode(), coord2.hashCode());
  }

  @Test
  public void testValidMove() {
    ReversiModel model = new ReversiModel(8);
    assertTrue(model.isValidMove(new HexCoordinate(2, 3), ChessPieces.X), "it is a valid move");
    assertFalse(model.isValidMove(new HexCoordinate(0, 0), ChessPieces.X), "it is a invalid move");
  }

  @Test
  public void testMakeMove() {
    ReversiModel model = new ReversiModel(8);
    model.makeMove(new HexCoordinate(2, 3), ChessPieces.X);
    assertEquals(ChessPieces.X, model.getCell(new HexCoordinate(2, 3)), "chess piece put");
  }

  @Test
  public void testGetCell() {
    ReversiModel model = new ReversiModel(8);
    model.makeMove(new HexCoordinate(2, 3), ChessPieces.X);
    assertEquals(ChessPieces.X, model.getCell(new HexCoordinate(2, 3)), "chess piece put");
  }



  @Test
  public void testSwitchTurn() {
    assertEquals(ChessPieces.X, model.getCurrentTurn(), "black turn");
    model.switchTurn();
    assertEquals(ChessPieces.O, model.getCurrentTurn(), "white turn");
  }

}
