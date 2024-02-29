package view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.geom.Path2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import players.HumanPlayer;
import controller.ReversiController;
import model.ChessPieces;
import model.ReversiModel;
import model.HexCoordinate;

/**
 * This class represents the graphical view for the Reversi game using Swing.
 * It extends JPanel and is responsible for drawing the hexagonal game board and pieces,
 * as well as handling user interactions.
 */
public class ReversiSwingView extends JPanel implements View {
  private ReversiModel model;
  private final int hexesPerSide;
  private HexCoordinate selectedHex;

  private ReversiController controller;

  /**
   * Constructs a new ReversiSwingView with the specified game model.
   * Initializes the board size, sets preferred dimensions and background color,
   * and adds a mouse listener to handle click events.
   *
   * @param model The game model
   */
  public ReversiSwingView(ReversiModel model) {
    this.model = model;
    this.hexesPerSide = model.getSize();
    this.setPreferredSize(new Dimension(800, 800));
    this.setBackground(new Color(0xCCCCCC));

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleMouseClick(e);
      }
    });
  }

  public HexCoordinate getSelectedHex() {
    return selectedHex;
  }

  public void setController(ReversiController controller) {
    this.controller = controller;
  }

  /**
   * Handles mouse click events to select hexes on the board.
   * Calculates the hex that was clicked and updates the selectedHex field.
   *
   * @param e The MouseEvent object
   */
  private void handleMouseClick(MouseEvent e) {
    int radius = calculateHexRadius();
    Point center = new Point(getWidth() / 2, getHeight() / 2);

    for (int q = -hexesPerSide + 1; q < hexesPerSide; q++) {
      int r1 = -hexesPerSide + 1 - Math.min(0, q);
      int r2 = hexesPerSide - 1 - Math.max(0, q);
      for (int r = r1; r <= r2; r++) {
        Point hexCenter = hexToPixel(radius, q, r, center);
        Path2D hex = createHexagon(hexCenter.x, hexCenter.y, radius);
        if (hex.contains(e.getPoint())) {
          selectedHex = new HexCoordinate(q, r);
          controller.handleMove(selectedHex); // 调用控制器的方法处理移动
          repaint();
          return;
        }
      }
    }
    if (controller.getCurrentPlayer() instanceof HumanPlayer) {
      controller.handleMove(selectedHex);
    }
  }


  /**
   * Overrides the paintComponent method to draw the game board and pieces.
   * Calls drawBoard to draw hexagons and highlights the selected hex if any.
   *
   * @param g The Graphics object used for drawing
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    // 首先绘制棋盘和所有棋子
    drawBoard(g2d);

    // 如果有选中的六边形，绘制一个浅蓝色的高亮
    if (selectedHex != null) {
      highlightSelectedHex(g2d, selectedHex);

      // 确保在选中的六边形上绘制当前棋子（如果有的话）
      ChessPieces occupant = model.getCell(selectedHex);
      if (occupant != ChessPieces.EMPTY) {
        drawPiece(g2d, selectedHex, occupant);
      }
    }

    g2d.dispose();
  }

  private void drawPiece(Graphics2D g2d, HexCoordinate hexCoord, ChessPieces occupant) {
    int radius = calculateHexRadius();
    Point center = new Point(getWidth() / 2, getHeight() / 2);
    Point hexCenter = hexToPixel(radius, hexCoord.getQ(), hexCoord.getR(), center);

    g2d.setColor(occupant == ChessPieces.X ? Color.BLACK : Color.WHITE);
    int diameter = (int) (radius * 0.8);  // 略小于六边形的直径以便美观
    int circleX = hexCenter.x - diameter / 2;
    int circleY = hexCenter.y - diameter / 2;
    g2d.fillOval(circleX, circleY, diameter, diameter);
  }



  /**
   * Draws and highlights the selected hex on the board.
   * Fills the selected hex with a color and redraws its border.
   *
   * @param g2d The Graphics2D object used for drawing
   * @param hexCoord The coordinate of the hex to be highlighted
   */
  private void highlightSelectedHex(Graphics2D g2d, HexCoordinate hexCoord) {
    int radius = calculateHexRadius();
    Point center = new Point(getWidth() / 2, getHeight() / 2);
    Point hexCenter = hexToPixel(radius, hexCoord.getQ(), hexCoord.getR(), center);
    Path2D hex = createHexagon(hexCenter.x, hexCenter.y, radius);


    g2d.setColor(new Color(0xADD8E6));
    g2d.fill(hex);

    // 重绘边界以保持六边形的清晰度
    g2d.setColor(Color.BLACK);
    g2d.draw(hex);
  }

  /**
   * Draws the hexagonal game board with pieces.
   * This method calculates the radius and center of the board and applies a rotation
   * to the graphics context to orient the board at a 30-degree angle. Each hexagon
   * is then drawn on the board based on the model's current state. After all hexagons
   * are drawn, the rotation is reset to avoid affecting subsequent drawing operations.
   *
   * @param g2d The {@link Graphics2D} object used for drawing the board and pieces.
   */
  public void drawBoard(Graphics2D g2d) {
    int radius = calculateHexRadius();
    Point center = new Point(getWidth() / 2, getHeight() / 2);



    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int q = -hexesPerSide + 1; q < hexesPerSide; q++) {
      int r1 = -hexesPerSide + 1 - Math.min(0, q);
      int r2 = hexesPerSide - 1 - Math.max(0, q);
      for (int r = r1; r <= r2; r++) {
        Point hexCenter = hexToPixel(radius, q, r, center);
        drawHexagon(g2d, hexCenter.x, hexCenter.y, radius, model.getCell(new HexCoordinate(q, r)));
      }
    }

  }


  private Point hexToPixel(int radius, int q, int r, Point center) {
    int x = (int) (center.x + radius * 3 / 2 * q);
    int y = (int) (center.y + radius * Math.sqrt(3) * (r + q / 2.0));
    return new Point(x, y);
  }

  private void drawHexagon(Graphics2D g2d, int x, int y, int radius, ChessPieces occupant) {
    Path2D hex = createHexagon(x, y, radius);

    if (occupant != ChessPieces.EMPTY) {
      g2d.setColor(occupant == ChessPieces.X ? Color.BLACK : Color.WHITE);

      // Calculate the diameter of the circle, could be
      // slightly less than the radius for visual appeal
      int diameter = (int)(radius);
      // Calculate the top left corner of the circle
      int circleX = x - diameter / 2;
      int circleY = y - diameter / 2;

      // Draw the circle (oval) inside the hexagon
      g2d.fillOval(circleX, circleY, diameter, diameter);
    }

    g2d.setColor(Color.BLACK);
    g2d.draw(hex);
  }

  private Path2D createHexagon(int x, int y, int radius) {
    Path2D path = new Path2D.Double();
    double angle = Math.PI / 3;

    for (int i = 0; i < 6; i++) {
      double xOff = x + radius * Math.cos(angle * i);
      double yOff = y + radius * Math.sin(angle * i);
      if (i == 0) {
        path.moveTo(xOff, yOff);
      } else {
        path.lineTo(xOff, yOff);
      }
    }

    path.closePath();
    return path;
  }

  private int calculateHexRadius() {
    int maxRadiusWidth = getWidth() / ((hexesPerSide * 3) - 2);
    int maxRadiusHeight = getHeight() / ((int) ((hexesPerSide * 2) * Math.sqrt(3)));
    return Math.min(maxRadiusWidth, maxRadiusHeight);
  }

  /** this method display the gameover view when game is over.
   *
   * @param winner if null，tie game
   */
  public void displayGameOver(ChessPieces winner) {
    String message;
    if (winner != null) {
      message = "Game Over, winner is " + winner.toString();
    } else {
      message = "Game Tied";
    }
    JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
  }
}








