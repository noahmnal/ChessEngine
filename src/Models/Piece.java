package Models;

import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {

  protected Board board;
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


  public abstract ArrayList<Tile> getLegalTiles();
  public abstract ArrayList<Tile> getAttackTiles();
  protected Image blackImage;


  protected Piece(int x, int y, String colour, Board board) {
    this.x = x;
    this.y = y;
    this.colour = colour;
    this.board = board;

  }

  public boolean checkForPiecesBlocking(Tile legalTile) {
    return board.getTilesWithPieces().contains(legalTile);
  }

  protected ArrayList<Tile> findTilesStraightLine(int sign, int pos, boolean isX) {
    ArrayList<Tile> tiles = new ArrayList<>();
    Tile tile;
    for(int i = sign; (i+pos <= 8 && i+pos >=1); i+=sign){
      if (isX)
        tile = new Tile(pos+i, y);
      else
        tile = new Tile(x, pos+i);
      if (checkIfOwnColour(tiles, tile)) break;
    }
    return tiles;
  }

  protected boolean checkIfOwnColour(ArrayList<Tile> tiles, Tile tile) {
    if (board.getTilesWithPieces().contains(tile)) {
      int index = board.getTilesWithPieces().indexOf(tile);
      if (!board.getTilesWithPieces().get(index).getPiece().getColour().equals(colour)) {
        tiles.add(tile);
      }
      return true;
    }
    tiles.add(tile);
    return false;
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
      if (checkIfOwnColour(tiles, tile)) break;
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
      if (board.simulateIsMoveLegal(this, tile)) {
        legal.add(tile);
      }
    }
    return legal;
  }

}
