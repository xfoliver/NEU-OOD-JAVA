package cs3500.klondike.view;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

import java.io.IOException;
import java.util.List;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private KlondikeModel model;
  private Appendable ap;  // 新增的 Appendable 成员变量

  //constructor with ap is null
  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
    this.ap = null;
  }

  //constructor with ap is not null
  public KlondikeTextualView(KlondikeModel model, Appendable ap) {
    this.model = model;
    this.ap = ap;
  }

  // render method
  @Override
  public void render() throws IOException {
    if (ap != null) {
      ap.append(this.toString());  // 这里我们只是将 toString() 方法的输出追加到 Appendable 对象
    } else {
      throw new IOException("No Appendable object provided.");
    }
  }

  public String toString() {
    return drawPileToString() + "\n" + foundationPileToString() + "\n" + cascadePileToString();
  }

  /**Help method: help show drawPile textual view.*/
  private String drawPileToString() {
    StringBuilder draw = new StringBuilder("Draw: ");
    List<Card> drawCards = model.getDrawCards();

    for (int i = 0; i < drawCards.size(); i++) {
      draw.append(drawCards.get(i).toString());
      if (i != drawCards.size() - 1) {
        draw.append(", ");
      }
    }

    return draw.toString();
  }

  /**Help method: help show foundationPile textual view.*/
  private String foundationPileToString() {
    StringBuilder foundation = new StringBuilder("Foundation: ");

    for (int i = 0; i < model.getNumFoundations(); i++) {
      if (model.getCardAt(i) == null) {
        foundation.append("<none>");
      } else {
        foundation.append(model.getCardAt(i).toString());
      }

      if (i != model.getNumFoundations() - 1) {
        foundation.append(", ");
      }
    }

    return foundation.toString();
  }


  /**Help method: help show cascadePile textual view.*/
  private String cascadePileToString() {
    StringBuilder cascade = new StringBuilder();

    // 寻找所有piles中的最大高度，因为我们需要遍历到最高的pile的最底部的卡片
    int maxPileHeight = 0;
    for (int i = 0; i < model.getNumPiles(); i++) {
      if (model.getPileHeight(i) > maxPileHeight) {
        maxPileHeight = model.getPileHeight(i);
      }
    }

    // 首先按行遍历
    for (int i = 0; i < maxPileHeight; i++) {
      // 然后在每行中遍历每个pile
      for (int j = 0; j < model.getNumPiles(); j++) {
        if (model.getPileHeight(j) == 0 && i == 0) {
          cascade.append("  X");
        } else if (i >= model.getPileHeight(j)) {
          cascade.append("   ");
        } else {
          try {
            String card = model.getCardAt(j, i).toString();
            int length = card.length();
            cascade.append(" ".repeat(3 - length));
            cascade.append(card);
          } catch (IllegalArgumentException x) {
            if ("Card is not visible.".equals(x.getMessage())) {
              cascade.append("  ?");
            }
          }
        }
      }
      cascade.append("\n");
    }

    return cascade.toString();
  }

}
