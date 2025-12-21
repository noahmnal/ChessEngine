package Pieces;

import GameLogic.GamePanel;
import Models.Board;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Queen extends Piece {

  public Queen(int x, int y, String color) {
    super(x, y, color);
    value = 9;
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteQueen.png"))
    ).getImage();
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/BlackQueen.png"))
    ).getImage();

  }

  @Override
  public ArrayList<Tile> getLegalTiles() {
    ArrayList<Tile> tiles = new ArrayList<>();
    tiles.addAll(getAllDiagonalTiles());
    tiles.addAll(getAllStraightTiles());
    return filterLegalTiles(tiles);
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
