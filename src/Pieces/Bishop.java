package Pieces;

import Models.Board;
import Models.Tile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class Bishop extends Piece {
  public Bishop(int x, int y, String color, Board board)  {
    super(x,y,color,board);
    value = 3;
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteBishop.png"))
    ).getImage();
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackBishop.png"))
    ).getImage();

  }

  @Override
  public ArrayList<Tile> getLegalTiles() {
      return filterLegalTiles(getAllDiagonalTiles());
  }

  @Override
  public ArrayList<Tile> getAttackTiles() {
    return getAllDiagonalTiles();
  }

  @Override
  public ArrayList<Tile> getSudoLegalTiles() {
    return getAllDiagonalTiles();
  }
}
