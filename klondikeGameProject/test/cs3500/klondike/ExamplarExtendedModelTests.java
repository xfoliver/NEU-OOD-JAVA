
package cs3500.klondike;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

/**Examplar -- test each condition in Limited and WhiteHead Klondike.*/
public class ExamplarExtendedModelTests {
  private KlondikeModel klondikeGame;
  private List<Card> deck;



  /**this method is help to make a list.*/
  public void makeList(List<Card> deck, int idx, String card) {
    Card first;
    Card second;
    for (int i = 0; i < deck.size(); i++) {
      if (deck.get(i).toString().equals(card)) {
        first = deck.get(idx);
        second = deck.get(i);
        deck.set(i, first);
        deck.set(idx, second);
      }
    }
  }

  //测试将pile第二列整个移到第一列
  @Test(expected = IllegalStateException.class)
  public void testMoveLongPileInWhiteGameInvalid() {
    klondikeGame = new WhiteheadKlondike();
    deck = klondikeGame.getDeck();

    makeList(deck, 0, "K♠");
    makeList(deck, 1, "Q♡");
    makeList(deck, 2, "4♠");
    makeList(deck, 3, "J♡");
    makeList(deck, 4, "10♠");
    makeList(deck, 5, "2♢");
    makeList(deck, 6, "A♢");
    klondikeGame.startGame(deck, false, 3, 3);

    // K♠  Q♡  4♠
    //     J♡  10♠
    //         2♢
    klondikeGame.movePile(1, 2, 0);
  }

  //测试将pile第二列整个移到第一列
  @Test(expected = IllegalStateException.class)
  public void testMoveLongPileInWhiteGameInvalid2() {
    klondikeGame = new WhiteheadKlondike();
    deck = klondikeGame.getDeck();

    makeList(deck, 0, "3♠");
    makeList(deck, 1, "2♣");
    makeList(deck, 2, "A♠");
    makeList(deck, 3, "A♢");
    klondikeGame.startGame(deck, false, 2, 3);

    // 3♠  2♣
    //     A♠
    klondikeGame.movePile(1, 2, 0);
  }


  //将第二个pile整个移到empty pile，也就是第一列当中
  @Test
  public void testMovePileInToEmptyPileWhiteGame() {
    klondikeGame = new WhiteheadKlondike();
    deck = klondikeGame.getDeck();

    makeList(deck, 0, "A♣");
    makeList(deck, 1, "k♡");
    makeList(deck, 2, "4♠");
    makeList(deck, 3, "2♣");
    makeList(deck, 4, "10♠");
    makeList(deck, 5, "2♢");
    makeList(deck, 6, "2♡");

    // A♣ k♡ 4♠
    //    2♣ 10♠
    //       2♢
    klondikeGame.startGame(deck, false, 3, 3);
    klondikeGame.movePile(0, 1, 1);

    //    k♡ 4♠
    //    2♣ 10♠
    //    A♣ 2♢
    klondikeGame.movePile(1, 2, 0);
    // 2♣ k♡ 4♠
    // A♣    10♠
    //       2♢
    Assert.assertEquals(klondikeGame.getPileHeight(0), 2);
  }

  @Test
  public void testMovePileInWhiteGame() {
    klondikeGame = new WhiteheadKlondike();
    deck = klondikeGame.getDeck();

    makeList(deck, 0, "A♠");
    makeList(deck, 1, "k♡");
    makeList(deck, 2, "4♠");
    makeList(deck, 3, "2♣");
    makeList(deck, 4, "10♠");
    makeList(deck, 5, "2♢");
    makeList(deck, 6, "2♡");

    // A♠ k♡ 4♠
    //    2♣ 10♠
    //       2♢
    klondikeGame.startGame(deck, false, 3, 3);
    klondikeGame.movePile(0, 1, 1);

    //    k♡ 4♠
    //    2♣ 10♠
    //    A♠ 2♢
    Assert.assertEquals(klondikeGame.getPileHeight(1), 3);
  }



  @Test
  public void testLimitedDrawCardRecycle() {
    klondikeGame = new LimitedDrawKlondike(2);
    List<Card> deck = klondikeGame.getDeck();

    makeList(deck, 0, "A♠");
    makeList(deck, 1, "A♣");
    makeList(deck, 2, "A♢");
    makeList(deck, 3, "A♡");

    klondikeGame.startGame(deck, false, 1, 51);

    for (int i = 0 ; i < 153; i++) {
      klondikeGame.discardDraw();
    }
    Assert.assertEquals(0, klondikeGame.getDrawCards().size());

  }



}
