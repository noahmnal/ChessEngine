package Pieces;

import GameLogic.GamePanel;
import Models.Board;
import Models.Piece;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Rook extends Piece {
  public Rook(int x, int y, String color, Board board) {
    super(x, y, color, board);
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteRook.png"))
    ).getImage().getScaledInstance(
            GamePanel.tileSize,
            GamePanel.tileSize,
            Image.SCALE_SMOOTH
    );
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackRook.png"))
    ).getImage().getScaledInstance(
            GamePanel.tileSize,
            GamePanel.tileSize,
            Image.SCALE_SMOOTH
    );
  }

  @Override
  public ArrayList<Tile> getLegalTiles() {
    return getAllStraightTiles();
  }
}
