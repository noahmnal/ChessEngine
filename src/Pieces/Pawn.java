package Pieces;

import GameLogic.MovesHistory;
import Models.Tile;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {
  Tile enPassantMove = null;
  public Pawn(int x, int y, String color) {
    super(x, y, color);
    value = 100;
    this.whiteImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/whitePawn.png"))).getImage();

    this.blackImage = new ImageIcon(
            Objects.requireNonNull(getClass().getResource("/Images/blackPawn.png"))).getImage();

  }


  @Override
  public ArrayList<Tile> getSudoLegalTiles() {
    ArrayList<Tile> legalTiles;

    if (colour.equals("white")) {
      legalTiles = findLegalMoves(2, 1);
    } else {
      legalTiles = findLegalMoves(7, -1);
    }

    addEnPassant(legalTiles);

    return legalTiles;
  }

  @Override
  public ArrayList<Tile> getOldLegalTiles() {
    return legalTiles;
  }

  @Override
  public ArrayList<Tile> setAndGetLegalTiles() {
    legalTiles = filterLegalTiles(getSudoLegalTiles());
    return legalTiles;
  }


  @Override
  public ArrayList<Tile> getAttackTiles() {
    defendingTiles.clear();
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
        checkIfOwnColour(legalTiles, newTile, defendingTiles);
    }
  }

  public Tile getEnPassantMove() {
    return enPassantMove;
  }


  private void addEnPassant(ArrayList<Tile> legalTiles) {
    if (MovesHistory.getMoves().isEmpty()) return;
    Pawn pawnToBeCaptured= MovesHistory.getMoves().getLast().getEnPasantNextTurn();
    if (pawnToBeCaptured == null) return;
    if (pawnToBeCaptured.getColour().equals(colour)) return;
    if (pawnToBeCaptured.getY() == y && Math.abs(pawnToBeCaptured.getX()-x) == 1) {
      int direction = colour.equals("white") ? 1 : -1;
      Tile enpassantTile = new Tile(pawnToBeCaptured.getX(), y + direction);
      legalTiles.add(enpassantTile);
      enPassantMove = enpassantTile;
    }
  }

  public void setEnPassantMove(Tile enPassantMove) {
    this.enPassantMove = enPassantMove;
  }
}