package cs3500.klondike.model.hw02;

import java.util.List;

/**
 * Represents the primary model interface for playing a game of Klondike.
 */
public interface KlondikeModel {
  /**
   * Return a valid and complete deck of cards for a game of Klondike.
   * There is no restriction imposed on the ordering of these cards in the deck.
   * The validity of the deck is determined by the rules of the specific game in
   * the classes implementing this interface.  This method may be called as often
   * as desired.
   *
   * @return the deck of cards as a list
   */
  List<Card> getDeck();

  /**
   * <p>Deal a new game of Klondike.
   * The cards to be used and their order are specified by the the given deck,
   * unless the {@code shuffle} parameter indicates the order should be ignored.</p>
   *
   * <p>This method first verifies that the deck is valid. It deals cards in rows
   * (left-to-right, top-to-bottom) into the characteristic cascade shape
   * with the specified number of rows, followed by (at most) the specified number of
   * draw cards. When {@code shuffle} is {@code false}, the {@code deck} must be used in
   * order and the 0th card in {@code deck} is used as the first card dealt.
   * There will be as many foundation piles as there are Aces in the deck.</p>
   *
   * <p>A valid deck must consist cards that can be grouped into equal-length,
   * consecutive runs of cards (each one starting at an Ace, and each of a single
   * suit).</p>
   *
   * <p>This method should have no side effects other than configuring this model
   * instance, and should work for any valid arguments.</p>
   *
   * @param deck      the deck to be dealt
   * @param shuffle   if {@code false}, use the order as given by {@code deck},
   *                  otherwise use a randomly shuffled order
   * @param numPiles  number of piles to be dealt
   * @param numDraw   maximum number of draw cards available at a time
   * @throws IllegalStateException if the game has already started
   * @throws IllegalArgumentException if the deck is null or invalid,
   *                  a full cascade cannot be dealt with the given sizes,
   *                  or another input is invalid
   */
  void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Moves the requested number of cards from the source pile to the destination pile,
   * if allowable by the rules of the game.
   * @param srcPile  the 0-based index (from the left) of the pile to be moved
   * @param numCards how many cards to be moved from that pile
   * @param destPile the 0-based index (from the left) of the destination pile for the
   *                 moved cards
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid, if the pile
   *                  numbers are the same, or there are not enough cards to move from
   *                  the srcPile to the destPile (i.e. the move is not physically
   *                  possible)
   * @throws IllegalStateException if the move is not allowable (i.e. the move is not
   *                  logically possible)
   */
  void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Moves the topmost draw-card to the destination pile.  If no draw cards remain,
   * reveal the next available draw cards
   * @param destPile the 0-based index (from the left) of the destination pile for the
   *                 card
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if destination pile number is invalid
   * @throws IllegalStateException if there are no draw cards, or if the move is not
   *                               allowable
   */
  void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException;

  /**
   * Moves the top card of the given pile to the requested foundation pile.
   * @param srcPile        the 0-based index (from the left) of the pile to move a card
   * @param foundationPile the 0-based index (from the left) of the foundation pile to
   *                       place the card
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if either pile number is invalid
   * @throws IllegalStateException if the source pile is empty or if the move is not
   *                               allowable
   */
  void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Moves the topmost draw-card directly to a foundation pile.
   * @param foundationPile the 0-based index (from the left) of the foundation pile to
   *                       place the card
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   * @throws IllegalStateException if there are no draw cards or if the move is not
   *                               allowable
   */
  void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Discards the topmost draw-card.
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalStateException if move is not allowable
   */
  void discardDraw() throws IllegalStateException;

  /**
   * Returns the number of rows currently in the game.
   * @return the height of the current table of cards
   * @throws IllegalStateException if the game hasn't been started yet
   */
  int getNumRows() throws IllegalStateException;

  /**
   * Returns the number of piles for this game.
   * @return the number of piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  int getNumPiles() throws IllegalStateException;

  /**
   * Returns the maximum number of visible cards in the draw pile.
   *
   * @return the number of visible cards in the draw pile
   * @throws IllegalStateException if the game hasn't been started yet
   */
  int getNumDraw() throws IllegalStateException;

  /**
   * Signal if the game is over or not.  A game is over if there are no more
   * possible moves to be made, or draw cards to be used (or discarded).
   *
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if the game hasn't been started yet
   */
  boolean isGameOver() throws IllegalStateException;

  /**
   * Return the current score, which is the sum of the values of the topmost cards
   * in the foundation piles.
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */
  int getScore() throws IllegalStateException;

  /**
   * Returns the number of cards in the specified pile.
   * @param pileNum the 0-based index (from the left) of the pile
   * @return the number of cards in the specified pile
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if pile number is invalid
   */
  int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException;

  /**
   * Returns whether the card at the specified coordinates is face-up or not.
   * @param pileNum  column of the desired card (0-indexed from the left)
   * @param card     row of the desired card (0-indexed from the top)
   * @return whether the card at the given position is face-up or not
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Returns the card at the specified coordinates, if it is visible.
   * @param pileNum  column of the desired card (0-indexed from the left)
   * @param card     row of the desired card (0-indexed from the top)
   * @return the card at the given position
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  Card getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Returns the card at the top of the specified foundation pile.
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return the card at the given position, or <code>null</code> if no card is there
   * @throws IllegalStateException if the game hasn't been started yet
   * @throws IllegalArgumentException if the foundation pile number is invalid
   */
  Card getCardAt(int foundationPile)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Returns the currently available draw cards.
   * There should be at most {@link KlondikeModel#getNumDraw} cards (the number
   * specified when the game started) -- there may be fewer, if cards have been removed.
   * NOTE: Users of this method should not modify the resulting list.
   * @return the ordered list of available draw cards (i.e. first element of this list
   *         is the first one to be drawn)
   * @throws IllegalStateException if the game hasn't been started yet
   */
  List<Card> getDrawCards() throws IllegalStateException;

  /**
   * Return the number of foundation piles in this game.
   * @return the number of foundation piles
   * @throws IllegalStateException if the game hasn't been started yet
   */
  int getNumFoundations() throws IllegalStateException;
}
