package Pieces;

import Models.Tile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class Rook extends Piece {
  public Rook(int x, int y, String color) {
    super(x, y, color);
    value = 5;
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteRook.png"))
    ).getImage();
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackRook.png"))
    ).getImage();
  }

  @Override
  public ArrayList<Tile> getSudoLegalTiles() {
    return getAllStraightTiles();
  }

  @Override
  public ArrayList<Tile> getAttackTiles() {
    return getAllStraightTiles();
  }

  @Override
  public ArrayList<Tile> getLegalTiles() {
    return filterLegalTiles(getSudoLegalTiles());
  }


}
