package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


/**
 * Represents a textual controller for the Klondike game.
 * This class reads input from a given Readable object, and
 * writes outputs and game messages to an Appendable object.
 * The controller interpreting user input and sending commands to the game model.
 *
 * @author Zhehao Zhang
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {
  private final Readable r;
  private final Appendable a;

  /**
   * The controller interpreting user input and sending commands to the game model.
   */

  public KlondikeTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }
    this.r = r;
    this.a = a;
  }

  // Helper method to get valid input or quit signal
  private int getValidInput(Scanner scanner) throws IOException {
    while (true) {
      if (scanner.hasNextInt()) {
        return scanner.nextInt();
      } else if (scanner.hasNext()) {
        String nextInput = scanner.next();
        if ("q".equalsIgnoreCase(nextInput)) {
          throw new IllegalStateException("Quit the game.");
        } else {
          a.append("Invalid input. Please enter a valid command or 'q' to quit.\n");
        }
      }
    }
  }


  @Override
  public void playGame(KlondikeModel model, List<Card> deck,
                       boolean shuffle, int numRows, int numDraw) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    if (deck == null || deck.isEmpty()) {
      throw new IllegalStateException("Deck can not be null");
    }

    Scanner scanner = new Scanner(r);
    KlondikeTextualView view = new KlondikeTextualView(model, a);

    model.startGame(deck, shuffle, numRows, numDraw);

    try {
      while (true) {
        view.render();
        a.append("Score: " + model.getScore() + "\n");

        if (!scanner.hasNext()) {
          throw new IllegalStateException("Ran out of input.");
        }

        String command = scanner.next().toLowerCase();

        switch (command) {
          case "q":
            a.append("Game quit!\nState of game when quit:\n");
            view.render();
            a.append("Score: " + model.getScore() + "\n");
            return;

          case "mpp":
            int fromPile = getValidInput(scanner);
            int count = getValidInput(scanner);
            int toPile = getValidInput(scanner);

            try {
              model.movePile(fromPile - 1, count, toPile - 1);
            } catch (Exception e) {
              a.append("Invalid move. Play again.\n");
            }
            break;

          case "md":
            int index = getValidInput(scanner);

            try {
              model.moveDraw(index - 1);
            } catch (Exception e) {
              a.append("Invalid move. Play again.\n");
            }
            break;

          case "mpf":
            int sourcePile = getValidInput(scanner);
            int foundationPile = getValidInput(scanner);

            try {
              model.moveToFoundation(sourcePile - 1, foundationPile - 1);
            } catch (Exception e) {
              a.append("Invalid move. Play again.\n");
            }
            break;

          case "mdf":
            int foundation = getValidInput(scanner);
            if (foundation == -1) {
              return;
            }

            try {
              model.moveDrawToFoundation(foundation - 1);
            } catch (Exception e) {
              a.append("Invalid move. Play again.\n");
            }
            break;

          case "dd":
            try {
              model.discardDraw();
            } catch (Exception e) {
              a.append("Invalid move. Play again.\n");
            }
            break;

          default:
            a.append("Unknown command.\n");
            break;
        }

        if (model.isGameOver()) {
          view.render();
          if (model.getScore() == deck.size()) {
            a.append("You win!\n");
          } else {
            a.append("Game over. Score: " + model.getScore() + "\n");
          }
          break;
        }
      }

    } catch (IOException e) {
      throw new IllegalStateException("Appendable or Readable failed", e);
    } catch (IllegalStateException e) {
      try {
        a.append("Game quit!\nState of game when quit:\n");
      } catch (IOException ex) {
        System.out.println("Game quit!");
      }
      try {
        view.render();
      } catch (IOException ex) {
        System.out.println("Game quit!");
      }
      try {
        a.append("Score: " + model.getScore() + "\n");
      } catch (IOException ex) {
        System.out.println("Game quit!");
      }
    }
  }
}


