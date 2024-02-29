package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;


/**Create three type of klondike game.*/
public class KlondikeCreator {

  /**enum the game type.*/
  public enum GameType {
    BASIC,
    LIMITED,
    WHITEHEAD
  }

  /**create each klondike type.*/
  public static KlondikeModel create(GameType type) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();

      case LIMITED:
        int numTimesRedrawAllowed = 0;
        return new LimitedDrawKlondike(numTimesRedrawAllowed);

      case WHITEHEAD:
        return new WhiteheadKlondike();

      default:
        throw new IllegalArgumentException("Unsupported game type: " + type);
    }
  }

}
