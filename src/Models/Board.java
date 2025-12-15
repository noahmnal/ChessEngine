package Models;

import Pieces.*;
import java.util.ArrayList;

public class Board {
  private final ArrayList<Tile> tiles;
  public Board() {
    tiles = new ArrayList<>();
    for (int i = 1; i < 9; i++) {
      for (int j = 1; j < 9; j++) {
        if ((i + j) % 2 == 1)
          tiles.add(new Tile(i, j, "black", null));
        else
          tiles.add(new Tile(i, j, "white", null));
      }
    }
    for (Tile tile : tiles) {
      int x = tile.getX();
      int y = tile.getY();

      switch (y) {

        // White pawns
        case 2 -> tile.setPiece(new Pawn(x, y, "white", this));

        // Black pawns
        case 7 -> tile.setPiece(new Pawn(x, y, "black", this));

        // White back rank
        case 1 -> {
          switch (x) {
            case 1, 8 -> tile.setPiece(new Rook(x, y, "white", this));
            case 2, 7 -> tile.setPiece(new Knight(x, y, "white", this));
            case 3, 6 -> tile.setPiece(new Bishop(x, y, "white", this));
            case 4 -> tile.setPiece(new Queen(x, y, "white", this));
            case 5 -> tile.setPiece(new King(x, y, "white", this));
          }
        }
        //black Pieces
        case 8 -> {
          switch (x) {
            case 1, 8 -> tile.setPiece(new Rook(x, y, "black", this));
            case 2, 7 -> tile.setPiece(new Knight(x, y, "black", this));
            case 3, 6 -> tile.setPiece(new Bishop(x, y, "black", this));
            case 4 -> tile.setPiece(new Queen(x, y, "black", this));
            case 5 -> tile.setPiece(new King(x, y, "black", this));
          }
        }

      }
    }

  }

  public ArrayList<Tile> getTiles() {
    return tiles;
  }

  public void setPos(int x, int y, Piece piece) {
    removePiece(piece);
    piece.setX(x);
    piece.setY(y);
    movePieceToNewTIle(piece);
    piece.setHaveMoved(true);
  }

  private void movePieceToNewTIle(Piece piece) {
    for (Tile tile : tiles) {
      if (tile.getX() == piece.getX() && tile.getY() == piece.getY()) {
        tile.setPiece(piece);
        break;
      }
    }
  }

  private void removePiece(Piece piece) {
    for (Tile tile : tiles) {
      if (tile.getPiece() == piece) {
        tile.setPiece(null);
      }
    }
  }

  public ArrayList<Tile> getTilesWithPieces() {
    ArrayList<Tile> tilesWithPieces = new ArrayList<>();
    for (Tile tile : tiles) {
      if (tile.getPiece() != null) {
        tilesWithPieces.add(tile);
      }
    }
    return tilesWithPieces;
  }

  public Rook getRookWithPos(String colour, int side) {
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece() instanceof Rook && tile.getPiece().getColor().equals(colour) && tile.getX() == side) {
        return (Rook) tile.getPiece();
      }
    }
    return null;
  }

  ArrayList<King> getKings() {
    ArrayList<King> kings = new ArrayList<>();
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece() instanceof King) {
        kings.add((King) tile.getPiece());
      }
    }
    return kings;
  }
}
