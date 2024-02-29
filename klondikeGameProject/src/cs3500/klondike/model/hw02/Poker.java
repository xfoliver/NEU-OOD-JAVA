package cs3500.klondike.model.hw02;


import java.util.Objects;

/**This class represent Poker Object, construct each Poker in Klondike.*/
public class Poker implements Card {
  private final Suit suit;
  private final int value;
  private final Color cardColor;
  private boolean flipState = false;

  // Constants for face cards
  public static final int ACE = 1;
  public static final int JACK = 11;
  public static final int QUEEN = 12;
  public static final int KING = 13;

  /** There are four different Suits. */
  public enum Suit {
    DIAMONDS, CLUBS, HEARTS, SPADES
  }

  /** There are two colors in the deck. */
  public enum Color {
    RED, BLACK
  }

  /** Construct a poker. */
  public Poker(Suit suit, int value) {
    this.suit = suit;
    this.value = value;
    this.cardColor = defineColor(suit);
  }

  private Color defineColor(Suit suit) {
    if (suit == Suit.DIAMONDS || suit == Suit.HEARTS) {
      return Color.RED;
    } else {
      return Color.BLACK;
    }
  }

  public Suit getSuit() {
    return suit;
  }

  public int getValue() {
    return value;
  }

  public Color getCardColor() {
    return cardColor;
  }

  public boolean getFlipState() {
    return flipState;
  }

  public void setFlipState(boolean flipState) {
    this.flipState = flipState;
  }

  @Override
  public String toString() {
    String strValue;
    switch (value) {
      case ACE:
        strValue = "A";
        break;
      case JACK:
        strValue = "J";
        break;
      case QUEEN:
        strValue = "Q";
        break;
      case KING:
        strValue = "K";
        break;
      default:
        strValue = String.valueOf(value);
    }

    char suitString;
    switch (suit) {
      case HEARTS:
        suitString = '♡';
        break;
      case DIAMONDS:
        suitString = '♢';
        break;
      case CLUBS:
        suitString = '♣';
        break;
      case SPADES:
        suitString = '♠';
        break;
      default:
        throw new IllegalStateException("Invalid symbol");
    }

    return strValue + suitString;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Poker otherCard = (Poker) obj;
    return value == otherCard.value && suit == otherCard.suit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(suit, value);
  }
}
