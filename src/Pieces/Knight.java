package Pieces;

import GameLogic.GamePanel;
import Models.Board;
import Models.Piece;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;


public class Knight extends Piece {

  public Knight(int x, int y, String color, Board board) {
    super(x,y,color, board);
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteKnight.png"))
    ).getImage().getScaledInstance(
            GamePanel.tileSize,
            GamePanel.tileSize,
            Image.SCALE_SMOOTH
    );
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackKnight.png"))
    ).getImage().getScaledInstance(
            GamePanel.tileSize,
            GamePanel.tileSize,
            Image.SCALE_SMOOTH
    );
  }

  @Override
  public ArrayList<Tile> getLegalTiles() {
    ArrayList<Tile> legalTiles = new ArrayList<>();
    for (int i = -2; i < 3; i++) {
      for (int j = -2; j < 3; j++) {
        if (Math.abs(i) + Math.abs(j) == 3) {
          Tile tile = new Tile(x+i, y+j);
          if (!checkIfOwnColour(legalTiles, tile))
            legalTiles.add(tile);
        }
      }
    }
    return legalTiles;
  }

}