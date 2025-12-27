package Pieces;

import GameLogic.Move;
import Models.Board;
import Models.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Piece {

  protected ArrayList<Tile> legalTiles = new ArrayList<>();
  public ArrayList<Tile> defendingTiles = new ArrayList<>();
  protected boolean haveMoved = false;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setHaveMoved(boolean haveMoved) {
    this.haveMoved = haveMoved;
  }
  public boolean getHaveNotMoved() {
    return !haveMoved;
  }

  public String getColour() {
    return colour;
  }

  public Image getWhiteImage() {
    return whiteImage;
  }
  public Image getBlackImage() {
    return blackImage;
  }


  public void setY(int y) {
    this.y = y;
  }

  public void setX(int x) {
    this.x = x;
  }


  protected int x;
  protected int y;
  protected String colour;
  protected Image whiteImage;

  public abstract ArrayList<Tile> getOldLegalTiles();
  public abstract ArrayList<Tile> setAndGetLegalTiles();
  public abstract ArrayList<Tile> getAttackTiles();
  public abstract ArrayList<Tile> getSudoLegalTiles();
  public int value;
  protected Image blackImage;


  protected Piece(int x, int y, String colour) {
    this.x = x;
    this.y = y;
    this.colour = colour;
    Board.addNewPiece(this);
  }

  public Tile getTile() {
  return new Tile(x, y);
  }

  public boolean checkForPiecesBlocking(Tile legalTile) {
    for (Piece piece : Board.getPieces()) {
      if (piece.getTile().equals(legalTile))
        return true;
    }
    return false;
  }

  protected ArrayList<Tile> findTilesStraightLine(int sign, int pos, boolean isX) {
    ArrayList<Tile> tiles = new ArrayList<>();
    Tile tile;
    for(int i = sign; (i+pos <= 8 && i+pos >=1); i+=sign){
      if (isX)
        tile = new Tile(pos+i, y);
      else
        tile = new Tile(x, pos+i);
      if (checkIfOwnColour(tiles, tile, defendingTiles)) break;
    }
    return tiles;
  }

  protected boolean checkIfOwnColour(ArrayList<Tile> legalTiles, Tile tile, ArrayList<Tile> defendingTile) {
    Piece piece = Board.getPiece(tile.getX(), tile.getY());
    if (piece == null) {
      legalTiles.add(tile);
      return false;
    }
    if (!piece.getColour().equals(colour)) {
      legalTiles.add(tile);
    } else defendingTile.add(tile);
    return true;
  }

  protected ArrayList<Tile> getAllStraightTiles() {
    ArrayList<Tile> tiles = new ArrayList<>();
    tiles.addAll(findTilesStraightLine(1, x, true));
    tiles.addAll(findTilesStraightLine(-1, x, true));
    tiles.addAll(findTilesStraightLine(1, y, false));
    tiles.addAll(findTilesStraightLine(-1, y,false));

    return tiles;
  }

  protected ArrayList<Tile> findTilesDiagonal(int sign, boolean inverted) {
    ArrayList<Tile> tiles = new ArrayList<>();
    Tile tile;
    int i = 0;
    while (true){
      i+=sign;
      if (!inverted)
        tile = new Tile(x+i, y+i);
      else
        tile = new Tile(x+i, y-i);
      if (tile.getX() > 8 || tile.getX() < 1 || tile.getY() > 8 || tile.getY() < 1)
        break;
      if (checkIfOwnColour(tiles, tile, defendingTiles)) break;
    }
    return tiles;
  }

  protected ArrayList<Tile> getAllDiagonalTiles(){
    ArrayList<Tile> tiles = new ArrayList<>();
    tiles.addAll(findTilesDiagonal(1, true));
    tiles.addAll(findTilesDiagonal(-1, true));
    tiles.addAll(findTilesDiagonal(1, false));
    tiles.addAll(findTilesDiagonal(-1, false));
    return tiles;
  }

  protected ArrayList<Tile> filterLegalTiles(ArrayList<Tile> candidateTiles) {
    ArrayList<Tile> legal = new ArrayList<>();
    for (Tile tile : candidateTiles) {
      if (Board.simulateIsMoveLegal(this, tile)) {
        legal.add(tile);
      }
    }
    return legal;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() +
            ", colour='" + colour + '\'' +
            ", y=" + y +
            ", x=" + x +
            ", haveMoved=" + haveMoved +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Piece piece = (Piece) o;
    return x == piece.x && y == piece.y &&
            Objects.equals(colour, piece.colour);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, colour);
  }
}
