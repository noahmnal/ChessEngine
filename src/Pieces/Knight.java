package Pieces;

import Models.Board;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;


public class Knight extends Piece {

  public Knight(int x, int y, String color, Board board) {
    super(x,y,color, board);
    value = 3;
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteKnight.png"))
    ).getImage();
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackKnight.png"))
    ).getImage();
  }

  @Override
  public ArrayList<Tile> getLegalTiles() {
    return filterLegalTiles(findMoves());
  }

  public ArrayList<Tile> findMoves() {
    ArrayList<Tile> tiles = new ArrayList<>();
    for (int i = -2; i < 3; i++) {
      for (int j = -2; j < 3; j++) {
        if (Math.abs(i) + Math.abs(j) == 3 && x+i > 0 && y+j > 0 && x+i <= 8 && y+j <= 8) {
          Tile tile = new Tile(x+i, y+j);
          if (!checkIfOwnColour(tiles, tile))
            tiles.add(tile);
        }
      }
    }
    return tiles;
  }

  @Override
  public ArrayList<Tile> getAttackTiles() {
    return findMoves();
  }


}