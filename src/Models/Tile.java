package Models;

import java.awt.*;
import java.util.Objects;

import GameLogic.GamePanel;
import Pieces.Piece;

public class Tile {
  private final int y;
  private final int x;
  private String color;

  public Rectangle getHitbox() {
    return hitbox;
  }

  private Rectangle hitbox;

  public Tile(int x, int y, String color, Piece piece) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.hitbox = new Rectangle((x-1)*GamePanel.tileSize, (y-1)*GamePanel.tileSize, GamePanel.tileSize,  GamePanel.tileSize);

  }

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String getColour() {
    return color;
  }



  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Tile tile)) return false;
    return y == tile.y && x == tile.x;
  }

  @Override
  public int hashCode() {
    return Objects.hash(y, x);
  }

  @Override
  public String toString() {
    return "Tile{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}

