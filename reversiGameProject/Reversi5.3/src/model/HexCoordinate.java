package model;

import java.util.Objects;

/**
 * Represents a location on a hexagonal grid, used in the Reversi game.
 * This class provides functionalities to work with hexagonal coordinates.
 */
public class HexCoordinate {
  private final int q; // The q-coordinate
  private final int r; // The r-coordinate

  /**
   * Constructs a HexCoordinate with specified q and r values.
   *
   * @param q The q-coordinate
   * @param r The r-coordinate
   */
  public HexCoordinate(int q, int r) {
    this.q = q;
    this.r = r;
  }

  /**
   * Calculates and returns the neighboring HexCoordinate in a given direction.
   *
   * @param direction The direction for the neighboring coordinate
   * @return The neighboring HexCoordinate
   */
  public HexCoordinate neighbor(int direction) {
    int[][] dirs = {{+1, 0}, {+1, -1}, {0, -1}, {-1, 0}, {-1, +1}, {0, +1}};
    int newQ = this.q + dirs[direction][0];
    int newR = this.r + dirs[direction][1];
    return new HexCoordinate(newQ, newR);
  }

  public int getQ() {
    return q;
  }

  public int getR() {
    return r;
  }

  // Getters, equals, hashCode, etc.
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HexCoordinate that = (HexCoordinate) o;
    return q == that.q && r == that.r;
  }

  @Override
  public int hashCode() {
    return Objects.hash(q, r);
  }
}
