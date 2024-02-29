package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Poker;
import cs3500.klondike.model.hw02.BasicKlondike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of LimitedDrawKlondike will have a one-argument
 * constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class LimitedDrawKlondike extends BasicKlondike implements KlondikeModel {
  private final int numTimesRedrawAllowed;
  private Map<Card, Integer> cardDrawCount = new HashMap<>();

  /**this is the constructor of LimitedDrawKlondike.*/
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Invalid number of redraws allowed.");
    }
    this.numTimesRedrawAllowed = numTimesRedrawAllowed;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numCascadePiles, int numOpenPiles) {
    super.startGame(deck, shuffle, numCascadePiles, numOpenPiles);
    // reset card draw counts at the beginning of the game
    cardDrawCount.clear();
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    checkGameStarted();

    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No cards left to draw.");
    }

    Card discardedCard = drawPile.remove(0);

    int currentDrawCount = cardDrawCount.getOrDefault(discardedCard, 0);

    // If the card has been drawn less than the allowed number
    // of times, put it back to the draw pile
    if (currentDrawCount < numTimesRedrawAllowed) {
      drawPile.add(discardedCard);
    }

    // Update the card draw count
    cardDrawCount.put(discardedCard, currentDrawCount + 1);

    // Turn up the next card if any
    if (!drawPile.isEmpty()) {
      ((Poker) drawPile.get(0)).setFlipState(true);
    }
  }
}



