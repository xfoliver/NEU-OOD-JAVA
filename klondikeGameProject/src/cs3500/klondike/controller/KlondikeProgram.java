package cs3500.klondike.controller;


import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;

import java.io.InputStreamReader;
import java.util.List;

/**
 * Represents the entry point of the Klondike game.
 * This class initializes the game's model, deck, and controller,
 * and starts a textual version of the Klondike Solitaire game.
 */
public class KlondikeProgram {

  /** This method help initializes the game's model, deck, and controller,
   * and starts a textual version of the Klondike Solitaire game.
   * */
  public static void main(String []args) {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = model.getDeck();
    boolean shuffle = true;
    int numRows = 7;
    int numDraw = 3;
    Readable r = new InputStreamReader(System.in);
    Appendable a = System.out;
    KlondikeTextualController controller = new KlondikeTextualController(r,a);
    controller.playGame(model, deck, shuffle, numRows, numDraw);
  }
}
