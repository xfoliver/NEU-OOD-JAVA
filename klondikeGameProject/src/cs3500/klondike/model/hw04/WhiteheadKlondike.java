package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.Poker;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel} interface.
 * You may assume that the actual implementation of WhiteheadKlondike will have a zero-argument
 * (i.e. default) constructor, and that all the methods below will be implemented.  You may not make
 * any other assumptions about the implementation of this class (e.g. what fields it might have,
 * or helper methods, etc.).
 */
public class WhiteheadKlondike extends BasicKlondike implements KlondikeModel {
  public WhiteheadKlondike() {
    super();
  }

  @Override
  public void setupGame() {

    for (int i = 0; i < cascadePiles; i++) {
      List<Card> cascadePile = new ArrayList<>();
      cascade.add(cascadePile);
    }

    for (int i = 0; i < cascadePiles; i++) {
      for (int j = i; j < cascadePiles; j++) {
        cascade.get(j).add(deck.remove(0));
      }
    }

    // turn up all cards in each cascade pile
    for (int i = 0; i < cascadePiles; i++) {
      List<Card> cards = cascade.get(i);
      for (Card card : cards) {
        ((Poker) card).setFlipState(true);
      }
    }

    // put rest into draw pile
    drawPile.addAll(deck);

    // first n visible draws
    int n = Math.min(drawPile.size(), this.numOfDraw);
    for (int i = 0; i < n; i++) {
      Poker p = (Poker) drawPile.get(i);
      p.setFlipState(true);
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalArgumentException, IllegalStateException {
    // Check if the game has started
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // Validate the move
    validateMove(srcPile, numCards, destPile);

    // Perform the move
    List<Card> sourcePile = cascade.get(srcPile);
    List<Card> destinationPile = cascade.get(destPile);

    // Extract and move the cards
    List<Card> cardsToMove = new ArrayList<>(
            sourcePile.subList(sourcePile.size() - numCards, sourcePile.size()));
    destinationPile.addAll(cardsToMove);

    for (int i = 0; i < numCards; i++) {
      sourcePile.remove(sourcePile.size() - 1);
    }

    //check if the last of sourcepile have turn up cards
    if (!sourcePile.isEmpty() && !sourcePile.get(sourcePile.size() - 1).getFlipState()) {
      Poker p = (Poker) sourcePile.get(sourcePile.size() - 1);
      p.setFlipState(true);
    }
  }

  @Override
  public void moveDraw(int destPile)
          throws IllegalStateException, IllegalArgumentException {
    // Check if the game has started
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // Check if destPile is a valid pile index
    if (!isValidPileIndex(destPile)) {
      throw new IllegalArgumentException("Invalid pile index.");
    }

    // Check if there are cards in the draw pile to move
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty. Cannot move cards.");
    }

    Card topCardDestPile = cascade.get(destPile).isEmpty()
            ? null
            : cascade.get(destPile).get(cascade.get(destPile).size() - 1);

    // Check if the move is valid based on game's rules
    Card cardToMove = drawPile.get(0);
    if (topCardDestPile != null && !isValidBuildSequence(cardToMove, topCardDestPile)) {
      throw new IllegalStateException(
              "Invalid move. Cards cannot be placed on top of each other.");
    }

    // Log before operation
    System.out.println("Before move: DrawPile size: "
            + drawPile.size() + ", DestPile size: " + cascade.get(destPile).size());

    Card cardMove = drawPile.remove(0);
    cascade.get(destPile).add(cardMove);

    // Log after operation
    System.out.println("After move: DrawPile size: "
            + drawPile.size() + ", DestPile size: " + cascade.get(destPile).size());


    for (int i = 0; i < Math.min(numOfDraw, drawPile.size()); i++) {
      Poker p = (Poker) drawPile.get(i);
      p.setFlipState(true);
    }
  }

  private void validateMove(int srcPile, int numCards, int destPile)
          throws IllegalArgumentException, IllegalStateException {
    if (srcPile == destPile) {
      throw new IllegalArgumentException("Source and destination piles cannot be the same.");
    }
    if (!isValidPileIndex(srcPile) || !isValidPileIndex(destPile)) {
      throw new IllegalArgumentException("Invalid pile indices.");
    }

    List<Card> sourcePile = cascade.get(srcPile);
    if (sourcePile.isEmpty() || numCards <= 0 || numCards > sourcePile.size()) {
      throw new IllegalArgumentException("Invalid number of cards to move from the source pile.");
    }

    List<Card> cardsToMove = sourcePile.subList(sourcePile.size() - numCards, sourcePile.size());
    Card lastCardMoved = cardsToMove.get(0);
    Card topCardDestPile = cascade.get(destPile).isEmpty() ? null :
            cascade.get(destPile).get(cascade.get(destPile).size() - 1);

    // 检查所有卡片是否具有相同的颜色和花色
    for (int i = 1; i < cardsToMove.size(); i++) {
      if (!haveSameColorAndSuit(cardsToMove.get(i - 1), cardsToMove.get(i))) {
        throw new IllegalStateException(
                "All cards to be moved must have the same color and suit.");
      }
    }

    if (topCardDestPile != null && !isValidBuildSequence(lastCardMoved, topCardDestPile)) {
      throw new IllegalStateException("Invalid move sequence.");
    }
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    // 检查游戏是否已经开始
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // 检查坐标是否有效
    if (!isValidCoordinates(pileNum, card)) {
      throw new IllegalArgumentException("Invalid coordinates.");
    }

    return true;
  }

  private boolean isValidCoordinates(int pileNum, int card) {
    return isValidPileIndex(pileNum) && card >= 0 && card < cascade.get(pileNum).size();
  }

  private boolean isValidPileIndex(int pileIndex) {
    return pileIndex >= 0 && pileIndex < cascade.size();
  }

  private boolean isValidBuildSequence(Card card1, Card card2) {
    int value1 = card1.getValue();
    int value2 = card2.getValue();
    Poker.Color color1 = card1.getCardColor();
    Poker.Color color2 = card2.getCardColor();

    return (value1 == value2 - 1) && (color1 == color2);
  }

  private boolean haveSameColorAndSuit(Card card1, Card card2) {
    return card1.getCardColor().equals(card2.getCardColor())
            && card1.getSuit().equals(card2.getSuit());
  }



}
