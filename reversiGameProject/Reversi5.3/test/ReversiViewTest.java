

import model.ReversiModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import view.ReversiView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**This class is tests class, check each whether the view method work correct.*/
public class ReversiViewTest {

  /**Initialize the game.*/
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  public void testInitializeGameboard() {
    ReversiModel model = new ReversiModel(11);
    ReversiView view = new ReversiView(model);
    view.displayBoard();
    // 这里你需要填入你期望的棋盘输出字符串
    String expectedOutput = "_ _ _ _ _ _ \r\n"
            + "    _ _ _ _ _ _ _ \r\n"
            + "   _ _ _ _ _ _ _ _ \r\n"
            + "  _ _ _ _ _ _ _ _ _ \r\n"
            + " _ _ _ _ O X _ _ _ _ \r\n"
            + "_ _ _ _ X _ O _ _ _ _ \r\n"
            + " _ _ _ _ O X _ _ _ _ \r\n"
            + "  _ _ _ _ _ _ _ _ _ \r\n"
            + "   _ _ _ _ _ _ _ _ \r\n"
            + "    _ _ _ _ _ _ _ \r\n"
            + "     _ _ _ _ _ _";
    assertEquals(expectedOutput, outContent.toString().trim());
  }

  @Test
  public void testCurrentPlayer() {
    ReversiModel model = new ReversiModel(11);
    ReversiView view = new ReversiView(model);
    view.displayCurrentPlayer();
    //display current player.
    String expectedOutput = "Current ChessPieces: X";
    assertEquals(expectedOutput, outContent.toString().trim());
  }

}
