package view;

import java.awt.Graphics2D;

/**
 * The View interface might be intended to define common functionalities
 * for the graphical representation of the Reversi game.
 * Currently, this interface is empty and might serve as a placeholder
 * for future implementations or extensions.
 */
public interface View {

  /**
   * Draws the hexagonal game board with pieces.
   * This method calculates the radius and center of the board and applies a rotation
   * to the graphics context to orient the board at a 30-degree angle. Each hexagon
   * is then drawn on the board based on the model's current state. After all hexagons
   * are drawn, the rotation is reset to avoid affecting subsequent drawing operations.
   *
   * @param g2d The {@link Graphics2D} object used for drawing the board and pieces.
   */
  void drawBoard(Graphics2D g2d);
}
