package Pieces;

import GameLogic.GamePanel;
import Models.Board;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {
  public boolean enPassantPawn = false;

  public Pawn(int x, int y, String color, Board board) {
    super(x, y, color, board);
    value = 1;
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
    ArrayList<Tile> legalTiles;

    if (colour.equals("white")) {
      legalTiles = findLegalMoves(2, 1);
    } else {
      legalTiles = findLegalMoves(7, -1);
    }

    addEnPassant(legalTiles);

    return filterLegalTiles(legalTiles);
  }


  @Override
  public ArrayList<Tile> getAttackTiles() {
    ArrayList<Tile> attackingTiles = new ArrayList<>();
    if (colour.equals("white")) findDiagonalTiles(attackingTiles, 1);
    else findDiagonalTiles(attackingTiles, -1);
    return attackingTiles;
  }

  private ArrayList<Tile> findLegalMoves(int rowCheck, int sign) {
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
    findDiagonalTiles(legalTiles, sign);

    return legalTiles;
  }
  private void findDiagonalTiles(ArrayList<Tile> legalTiles, int sign) {
    Tile newTile;
    for (int i = -1; i < 2; i += 2) {
      newTile = new Tile(x + i, y + sign);
      if (checkForPiecesBlocking(newTile))
        checkIfOwnColour(legalTiles, newTile);
    }
  }

  private void addEnPassant(ArrayList<Tile> legalTiles) {
    if (board.enPassantPawn == null) return;
    Pawn p = board.enPassantPawn;

    if (p.getColour().equals(colour)) return;

    if (p.getY() != this.y) return;

    if (Math.abs(p.getX() - this.x) != 1) return;

    int direction = colour.equals("white") ? 1 : -1;
    legalTiles.add(new Tile(p.getX(), y + direction));
    enPassantPawn = true;
  }
}