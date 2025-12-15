package Pieces;

import GameLogic.GamePanel;
import Models.Board;
import Models.Piece;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class King extends Piece {
  private final HashMap<Tile, Rook> castlingTile;
  public boolean checkIfInCheck = false;

  public King(int x, int y, String color,  Board board) {
    super(x,y,color, board);
     castlingTile = new HashMap<>();
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whiteKing.png"))
    ).getImage().getScaledInstance(
            GamePanel.tileSize,
            GamePanel.tileSize,
            Image.SCALE_SMOOTH
    );
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackKing.png"))
    ).getImage().getScaledInstance(
            GamePanel.tileSize,
            GamePanel.tileSize,
            Image.SCALE_SMOOTH
    );
  }
  @Override
  public ArrayList<Tile> getLegalTiles() {
    ArrayList<Tile> legalTiles = new ArrayList<>();
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        Tile tile = new Tile(x+i, y+j);
        if (!checkIfOwnColour(legalTiles, tile))
          legalTiles.add(tile);
      }
    }
    castleIfPossible(legalTiles);
    return legalTiles;
  }
  public boolean getCheckIfInCheck() {
    return checkIfInCheck;
  }

  public void setCheckIfInCheck(ArrayList<Tile> AllLegalTilesOponent) {
    Tile myTile = new Tile(x, y);
    checkIfInCheck = AllLegalTilesOponent.contains(myTile);
  }

  public void castleIfPossible(ArrayList<Tile> legalTiles) {
    if (!haveMoved) {
      Rook kingRook = board.getRookWithPos(color, 8);
      Rook queenRook = board.getRookWithPos(color, 1);
      if (kingRook.getHaveNotMoved()) {
        ArrayList<Tile> castlingKing = findTilesStraightLine(1, x, true);
        if (castlingKing.size() == 2) {
          legalTiles.add(castlingKing.getLast());
          castlingTile.putIfAbsent(castlingKing.getLast(), kingRook);

        }
      }
      if (queenRook.getHaveNotMoved()) {
        ArrayList<Tile> castlingQueen = findTilesStraightLine(-1, x, true);
        if (castlingQueen.size() == 3) {
          legalTiles.add(castlingQueen.get(1));
          castlingTile.put(castlingQueen.get(1),  queenRook);
        }
      }
    }
  }

  public HashMap<Tile, Rook> getCastlingTiles() {
    return castlingTile;
  }
}
