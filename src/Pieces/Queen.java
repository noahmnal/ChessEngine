package Pieces;

import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Queen extends Piece {

  public Queen(int x, int y, String color) {
    super(x, y, color);
    value = 900;
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteQueen.png"))
    ).getImage();
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/BlackQueen.png"))
    ).getImage();

  }

  @Override
  public ArrayList<Tile> getOldLegalTiles() {
    return legalTiles;
  }

  @Override
  public ArrayList<Tile> setAndGetLegalTiles() {
    ArrayList<Tile> tiles = new ArrayList<>();
    tiles.addAll(getAllDiagonalTiles());
    tiles.addAll(getAllStraightTiles());
    legalTiles = filterLegalTiles(tiles);
    return legalTiles;
  }




  @Override
  public ArrayList<Tile> getAttackTiles() {
    ArrayList<Tile> tiles = new ArrayList<>();
    tiles.addAll(getAllDiagonalTiles());
    tiles.addAll(getAllStraightTiles());
    return tiles;
  }

  @Override
  public ArrayList<Tile> getSudoLegalTiles() {
      ArrayList<Tile> tiles = new ArrayList<>();
      tiles.addAll(getAllDiagonalTiles());
      tiles.addAll(getAllStraightTiles());
      return tiles;
  }
}
