package cs3500.klondike;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.KlondikeCreator;

import java.io.InputStreamReader;
import java.util.List;

/**this class represent the Klondike game, initialize the Klondike game
 *  and choose which type of Klondike Game to play.
 */
public final class Klondike {

  /**initialize the game and choose which type of game to play.*/
  public static void main(String[] args) {

    // 默认设置
    int numRows = 7;
    int numDraw = 3;
    KlondikeCreator.GameType gameType;

    // 检查命令行参数
    if (args.length < 1) {
      throw new IllegalArgumentException(
              "Please provide the game type: basic, limited, or whitehead.");
    }

    // 获取游戏类型
    switch (args[0]) {
      case "basic":
        gameType = KlondikeCreator.GameType.BASIC;
        break;
      case "limited":
        if (args.length < 2) {
          throw new IllegalArgumentException(
                  "please provide the number of times the draw pile can be used.");
        }
        gameType = KlondikeCreator.GameType.LIMITED;
        if (args.length >= 3) {
          numRows = Integer.parseInt(args[2]);
          if (args.length == 4) {
            numDraw = Integer.parseInt(args[3]);
          }
        }
        break;

      case "whitehead":
        gameType = KlondikeCreator.GameType.WHITEHEAD;
        break;
      default:
        System.out.println("Invalid game type provided.");
        return;
    }

    // 如果提供了额外的参数，且游戏类型不是 "limited"，则更新级联堆数和可见抽牌数
    if (gameType != KlondikeCreator.GameType.LIMITED) {
      if (args.length >= 3) {
        numRows = Integer.parseInt(args[1]);
        numDraw = Integer.parseInt(args[2]);
      } else if (args.length == 2) {
        numRows = Integer.parseInt(args[1]);
      }
    }

    // 添加验证
    if (numRows < 1 || numRows > 13) {
      System.out.println(
              "Invalid number of cascade piles specified.");
      return;
    }

    // 添加numDraw的验证
    if (numDraw <= 0) {
      System.out.println(
              "Invalid number of draws specified. Must be greater than 0.");
      return;
    }

    KlondikeModel gameModel = KlondikeCreator.create(gameType);
    List<Card> deck = gameModel.getDeck();
    boolean shuffle = true;
    Readable r = new InputStreamReader(System.in);
    Appendable a = System.out;
    KlondikeTextualController controller = new KlondikeTextualController(r, a);
    controller.playGame(gameModel, deck, shuffle, numRows, numDraw);
  }

}


