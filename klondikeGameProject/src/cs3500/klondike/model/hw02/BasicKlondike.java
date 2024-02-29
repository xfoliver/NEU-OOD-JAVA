package cs3500.klondike.model.hw02;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
/**
 * This is a stub implementation of the {@link cs3500.klondike.model.hw02.KlondikeModel}
 * interface. You may assume that the actual implementation of BasicKlondike will have a
 * zero-argument (i.e. default) constructor, and that all the methods below will be
 * implemented.  You may not make any other assumptions about the implementation of this
 * class (e.g. what fields it might have, or helper methods, etc.).
 * 
 * <p>Once you've implemented all the constructors and methods on your own, you can
 * delete the placeholderWarning() method.
 */

public class BasicKlondike implements KlondikeModel {

  protected int cascadePiles;
  protected boolean gameStarted;
  protected List<Card> deck;
  protected final List<List<Card>> cascade;
  protected final List<List<Card>> foundation;
  protected final List<Card> drawPile;
  protected int numOfDraw;

  /**this class represent the Basicklondike,
   * model pert of this game,
   * construct the BasicKlondike.
   */
  public BasicKlondike() {
    this.gameStarted = false;
    this.deck = new ArrayList<>();
    this.cascade = new ArrayList<>();
    this.foundation = new ArrayList<>();
    this.drawPile = new ArrayList<>();
  }


  /**Initialize the cascade pile, foundation pile and draw pile.*/
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


    //turn up the bottom card of each cascade pile
    for (int i = 0; i < cascadePiles; i++) {
      List<Card> cards = cascade.get(i);
      Poker card = (Poker) cards.get(cards.size() - 1);
      card.setFlipState(true);
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
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<>();
    for (Poker.Suit suit : Poker.Suit.values()) {
      for (int value = 1; value <= 13; value++) {
        deck.add(new Poker(suit, value)); // 使用 poker 类来创建卡牌对象
      }
    }

    return deck;
  }


  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {

    // Check if the game has already started
    if (gameStarted) {
      throw new IllegalStateException("The game has already started.");
    }

    // Validate the deck
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null.");
    }

    isValidDeck(deck, numPiles);

    // Validate number of piles
    if (numPiles <= 0) {
      throw new IllegalArgumentException("Invalid number of piles.");
    }

    if (numDraw <= 0) {
      throw new IllegalArgumentException("Invalid number of draws.");
    }

    //set up foundations
    int numsAces = 0;
    for (Card card : deck) {
      if (((Poker) card).getValue() == 1) {
        numsAces += 1;
      }
    }

    for (int i = 0; i < numsAces; i++) {
      foundation.add(new ArrayList<>());
    }

    // Use the provided deck for the game, but make a copy so we don't modify the passed-in list
    this.deck = new ArrayList<>(deck);

    // Shuffle if necessary
    if (shuffle) {
      Collections.shuffle(this.deck);
    }

    // Setup the game state with dynamic piles and draw counts
    this.cascadePiles = numPiles; // Adjusting the number of cascade piles
    this.numOfDraw = numDraw;

    setupGame(); // This method needs to be adjusted to work with dynamic cascade pile counts
    // Mark game as started
    gameStarted = true;
  }

  private void isValidDeck(List<Card> deck, int numOfPiles) {

    //check if there is null in deck
    for (Card c : deck) {
      if (c == null) {
        throw new IllegalArgumentException("Null in the deck");
      }
    }

    //check for numbers
    int num = deck.size();
    if (num < numOfPiles * (numOfPiles + 1) / 2) {
      throw new IllegalArgumentException("card is not enough to be dealt to a full cascade");
    }


    Map<Poker.Suit, List<Card>> counts = new HashMap<>();

    for (Card card : deck) {
      Poker.Suit suit = ((Poker)card).getSuit();
      if (!counts.containsKey(suit)) {
        counts.put(suit, new ArrayList<>());
      }
      counts.get(suit).add(card);
    }

    int suitCount = -1;
    for (List<Card> list : counts.values()) {
      if (suitCount == -1) {
        suitCount = list.size();
      }
      if (suitCount != list.size()) {
        throw new IllegalArgumentException("card is not equal-length at each suit");
      }
    }


    //check if each suit is consecutive cards starting from Ace
    for (Poker.Suit s : counts.keySet()) {
      List<Card> list = counts.get(s);
      List<Card> newlist = new ArrayList<>(list);
      newlist.sort((card1, card2) -> {
        return card1.getValue() - card2.getValue();
      });

      int aces = 0;
      for (int i = 0; i < newlist.size(); i++) {
        Poker p = (Poker) newlist.get(i);
        if (p.getValue() == 1) {
          aces++;
        } else {
          break;
        }
      }

      if (newlist.size() % aces != 0) {
        throw new IllegalArgumentException("card is not consecutive starting from Ace");
      }

      for (int i = 0; i < newlist.size(); i++) {
        Poker p = (Poker) newlist.get(i);
        if (p.getValue() != i / aces + 1) {
          throw new IllegalArgumentException("card is not consecutive starting from Ace");
        }
      }
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

    if (topCardDestPile != null && !isValidBuildSequence(lastCardMoved, topCardDestPile)) {
      throw new IllegalStateException("Invalid move sequence.");
    }

    if (topCardDestPile == null) {
      if (lastCardMoved.getValue() != Poker.KING) {
        throw new IllegalStateException("invalid move, must be a KING to be put in a empty pile");
      }
    }
  }


  private boolean isValidPileIndex(int pileIndex) {
    return pileIndex >= 0 && pileIndex < cascade.size();
  }

  private boolean isValidBuildSequence(Card card1, Card card2) {
    int value1 = card1.getValue();
    int value2 = card2.getValue();
    Poker.Color color1 = card1.getCardColor();
    Poker.Color color2 = card2.getCardColor();

    return (value1 == value2 - 1) && (color1 != color2);
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

    if (topCardDestPile == null) {
      //must be an KING to be put in the empty pile
      if (cardToMove.getValue() != Poker.KING) {
        throw new IllegalStateException("invalid move. must be a king to be moved to empty pile");
      }
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



  @Override
  public void moveToFoundation(int srcPile, int foundationPile) throws IllegalStateException {
    // 检查游戏是否已经开始
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // 检查输入参数的有效性
    if (!isValidPileIndex(srcPile) || !isValidFoundationIndex(foundationPile)) {
      throw new IllegalArgumentException("Invalid pile indices.");
    }

    List<Card> sourcePile = cascade.get(srcPile);
    List<Card> targetFoundation = foundation.get(foundationPile);

    if (sourcePile.isEmpty()) {
      throw new IllegalStateException("Source pile is empty.");
    }

    Card cardToMove = sourcePile.get(sourcePile.size() - 1);

    if (!isValidMoveToFoundation(cardToMove, targetFoundation)) {
      throw new IllegalStateException("Invalid move to foundation.");
    }

    // 将卡片添加到基础堆
    targetFoundation.add(cardToMove);

    // 从源堆中移除卡片
    sourcePile.remove(sourcePile.size() - 1);

    //if there is no faced up cards in sourcepile, need to turn over another card if not empty
    if (!sourcePile.isEmpty() && !sourcePile.get(sourcePile.size() - 1).getFlipState()) {
      Poker p = (Poker) sourcePile.get(sourcePile.size() - 1);
      p.setFlipState(true);
    }
  }

  private boolean isValidFoundationIndex(int foundationIndex) {
    return foundationIndex >= 0 && foundationIndex < foundation.size();
  }

  private boolean isValidMoveToFoundation(Card cardToMove, List<Card> targetFoundation) {
    if (targetFoundation.isEmpty()) {
      // 如果基础堆是空的，只能添加A（1）作为开始
      return cardToMove.getValue() == Poker.ACE;
    } else {
      // 如果基础堆不为空，下一张卡片必须是递增的，并且花色相同
      Card topCardFoundation = targetFoundation.get(targetFoundation.size() - 1);
      return cardToMove.getValue() == topCardFoundation.getValue() + 1
              && cardToMove.getSuit().equals(topCardFoundation.getSuit());
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
          throws IllegalStateException, IllegalArgumentException {
    // Check if the game has started
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // Check if foundationPile is a valid pile index
    if (!isValidFoundationIndex(foundationPile)) {
      throw new IllegalArgumentException("Invalid foundation pile index.");
    }

    // Check if the draw pile is empty
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty. Cannot move cards.");
    }

    List<Card> targetFoundation = foundation.get(foundationPile);
    Card topCardFoundation = targetFoundation.isEmpty()
            ? null : targetFoundation.get(targetFoundation.size() - 1);

    Card cardToMove = drawPile.get(0);
    if (!isValidMoveDrawToFoundation(cardToMove, topCardFoundation)) {
      throw new IllegalStateException("Invalid move to foundation.");
    }

    // Perform the move: remove the top card from the draw pile and add it to the foundation pile
    Poker move = (Poker) drawPile.remove(0);
    targetFoundation.add(move);

    for (int i = 0; i < Math.min(numOfDraw, drawPile.size()); i++) {
      Poker p = (Poker) drawPile.get(i);
      p.setFlipState(true);
    }
  }

  private boolean isValidMoveDrawToFoundation(Card cardToMove, Card topCardFoundation) {
    // 如果基础堆是空的，只能添加Aces（值为1）作为开始
    if (topCardFoundation == null) {
      return cardToMove.getValue() == Poker.ACE;
    } else {
      // 如果基础堆不为空，下一张卡片必须是递增的，并且花色相同
      return cardToMove.getValue() == topCardFoundation.getValue() + 1
              && cardToMove.getSuit().equals(topCardFoundation.getSuit());
    }
  }

  protected void checkGameStarted() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }
  }


  @Override
  public void discardDraw() throws IllegalStateException {
    checkGameStarted();
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("Draw pile is empty. Cannot discard cards.");
    }
    Card discardedCard = drawPile.remove(0);
    ((Poker) discardedCard).setFlipState(false);
    drawPile.add(discardedCard);

    //turn up the next card
    if (numOfDraw <= drawPile.size()) {
      Poker p = (Poker) drawPile.get(numOfDraw - 1);
      p.setFlipState(true);
    }
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    checkGameStarted();

    // Calculate the maximum number of cards present in any cascade pile
    int maxCardsInPile = 0;
    for (List<Card> pile : cascade) {
      maxCardsInPile = Math.max(maxCardsInPile, pile.size());
    }

    return maxCardsInPile;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    checkGameStarted();

    // Simply return the number of cascade piles
    return this.cascadePiles;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    checkGameStarted();
    return this.numOfDraw;
  }


  @Override
  public boolean isGameOver() throws IllegalStateException {
    checkGameStarted();
    // Check if any of the potential moves is valid.
    // If any move is valid, the game is not over.
    return !(canMovePileToPile() || canMoveDrawToFoundation()
            || canMoveDrawToPile() || canMovePileToFoundation());
  }


  private boolean canMovePileToPile() {
    for (int i = 0; i < cascade.size(); i++) {
      List<Card> sourcePile = cascade.get(i);
      if (sourcePile.isEmpty()) {
        continue;
      }
      for (int j = 0; j < cascade.size(); j++) {
        if (i != j) {
          List<Card> destPile = cascade.get(j);
          Card topCardDestPile = destPile.isEmpty() ? null : destPile.get(destPile.size() - 1);

          // 检查从源pile的最底部的卡片开始的任何连续的卡片序列，是否可以被放到目标pile的最上面的卡上
          for (int k = 0; k < sourcePile.size(); k++) {
            List<Card> subList = sourcePile.subList(k, sourcePile.size());
            if (subList.size() > 0 && subList.get(0).getFlipState()) {
              if (topCardDestPile == null && subList.size() > 0) {
                if (subList.get(0).getValue() == Poker.KING) {
                  return true;
                }
              } else if (topCardDestPile != null && subList.size() > 0
                      && isValidBuildSequence(subList.get(0), topCardDestPile)) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }


  private boolean canMoveDrawToFoundation() {
    for (Card card : drawPile) {
      // Let's assume foundation is a List of Piles (List<Card>),
      // where each pile represents a different suit.
      for (List<Card> foundationPile : foundation) {
        if (foundationPile.isEmpty()) {
          // If foundation pile is empty, only an Ace can start it.
          if (card.getValue() == 1) { // Assuming Ace has a value of 1
            return true;
          }
        } else {
          Card topCardInFoundation = foundationPile.get(foundationPile.size() - 1);
          // Check if the card from draw pile can be placed on top of the current foundation pile.
          // The card should have the same suit and be exactly one value higher.
          if (topCardInFoundation.getSuit() == card.getSuit()
                  && topCardInFoundation.getValue() == card.getValue() - 1) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean canMoveDrawToPile() {
    for (Card card : drawPile) {
      for (List<Card> pile : cascade) {
        if (!pile.isEmpty()) {
          Card topCardOfPile = pile.get(pile.size() - 1);

          // Check if the card from draw pile can be placed on top of the pile.
          if (isValidBuildSequence(card, topCardOfPile)) {
            return true;
          }
        } else {
          // Must be a king to be moved to a pile
          if (card.getValue() == 13) {
            return true;
          }
        }
      }
    }
    return false;
  }


  private boolean canMovePileToFoundation() {
    // Iterate through each cascade pile
    for (List<Card> pile : cascade) {
      // Check if pile is not empty
      if (!pile.isEmpty()) {
        Card topCardOfPile = pile.get(pile.size() - 1);

        // Now, iterate through each foundation pile to see if the card can be placed there
        for (List<Card> foundationPile : foundation) {
          if (foundationPile.isEmpty()) {
            // If foundation pile is empty, only an Ace can start it.
            if (topCardOfPile.getValue() == 1) { // Assuming Ace has a value of 1
              return true;
            }
          } else {
            Card topCardInFoundation = foundationPile.get(foundationPile.size() - 1);
            // Check if the card from the cascade pile can be placed
            // on top of the current foundation pile.
            // The card should have the same suit and be exactly one value higher.
            if (topCardInFoundation.getSuit() == topCardOfPile.getSuit()
                    && topCardInFoundation.getValue() == topCardOfPile.getValue() - 1) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }



  @Override
  public int getScore() throws IllegalStateException {
    checkGameStarted();

    int score = 0;

    // 计算基础堆顶部牌的值之和
    for (List<Card> foundationPile : foundation) {
      if (!foundationPile.isEmpty()) {
        score += foundationPile.get(foundationPile.size() - 1).getValue();
      }
    }

    return score;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    // 检查游戏是否已经开始
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // 检查传递的牌堆索引是否有效
    if (!isValidPileIndex(pileNum)) {
      throw new IllegalArgumentException("Invalid pile number.");
    }

    // 获取指定牌堆的卡片数量
    List<Card> pile = cascade.get(pileNum);
    return pile.size();
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

    // 获取指定位置的卡片
    Card cardToCheck = cascade.get(pileNum).get(card);

    // 如果卡片是null, 返回false
    if (cardToCheck == null) {
      return false;
    }

    // 返回卡片的正面朝上状态
    return cardToCheck.getFlipState();
  }



  // 辅助方法：检查坐标是否有效
  private boolean isValidCoordinates(int pileNum, int card) {
    return isValidPileIndex(pileNum) && card >= 0 && card < cascade.get(pileNum).size();
  }

  @Override
  public Card getCardAt(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    // 检查游戏是否已经开始
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // 检查坐标是否有效
    if (!isValidCoordinates(pileNum, card)) {
      if (!isValidPileIndex(pileNum)) {
        throw new IllegalArgumentException("Invalid pile number");
      } else {
        throw new IllegalArgumentException("Invalid card index");
      }
    }

    // 获取指定位置的卡片
    Card cardInPile = cascade.get(pileNum).get(card);

    // 如果卡片不是可见的，抛出异常
    if (cardInPile != null && !cardInPile.getFlipState()) {
      throw new IllegalArgumentException("Card is not visible.");
    }

    return cardInPile;
  }



  @Override
  public Card getCardAt(int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    // 检查游戏是否已经开始
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // 检查基础堆索引的有效性
    if (!isValidFoundationIndex(foundationPile)) {
      throw new IllegalArgumentException("Invalid foundation pile index.");
    }

    List<Card> targetFoundation = foundation.get(foundationPile);

    // 如果基础堆不为空，返回顶部卡片，否则返回 null
    if (!targetFoundation.isEmpty()) {
      return targetFoundation.get(targetFoundation.size() - 1);
    } else {
      return null;
    }
  }


  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    // 检查游戏是否已经开始
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    List<Card> sublist = drawPile.subList(0, Math.min(getNumDraw(), drawPile.size()));

    // 返回一个不可修改的抽牌堆卡片列表
    return Collections.unmodifiableList(sublist);
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    // 检查游戏是否已经开始
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }

    // 返回基础堆的数量
    return foundation.size();
  }
}
