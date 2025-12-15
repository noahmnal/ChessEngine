package Pieces;

import GameLogic.GamePanel;
import Models.Board;
import Models.Piece;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {

  public Pawn(int x, int y, String color, Board board) {
    super(x, y, color, board);
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whitePawn.png"))).getImage().getScaledInstance(
            GamePanel.tileSize, GamePanel.tileSize, Image.SCALE_SMOOTH
    );
    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackPawn.png"))).getImage().getScaledInstance(
            GamePanel.tileSize, GamePanel.tileSize, Image.SCALE_SMOOTH
    );
  }

  @Override
  public ArrayList<Tile> getLegalTiles() {
    if (GamePanel.turn.equals("white")) {
      return func(2, 1);
    }
    else {return func(7, -1);}
}

  private ArrayList<Tile> func(int rowCheck, int sign) {
    ArrayList<Tile> legalTiles = new ArrayList<>();
    Tile newTile;
    if (y == rowCheck) {
      for (int i = sign; Math.abs(i) < 3; i += sign) {
        newTile = new Tile(x, y + i);
        if (checkForPiecesBlocking(newTile))
          break;
        legalTiles.add(newTile);
      }
    } else {
      newTile = new Tile(x, y + sign);
      if (!checkForPiecesBlocking(newTile))
        legalTiles.add(newTile);
    }
    for (int i = -1; i < 2; i += 2) {
      newTile = new Tile(x + i, y + sign);
      if (checkForPiecesBlocking(newTile))
        checkIfOwnColour(legalTiles, newTile);
    }
    return legalTiles;
  }
}