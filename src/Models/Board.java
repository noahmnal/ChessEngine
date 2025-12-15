package Models;

import Pieces.*;

import java.util.ArrayList;
import GameLogic.GamePanel;

public class Board {
  private final ArrayList<Tile> tiles;
  public boolean whiteInCheck = false;
  public boolean blackInCheck = false;
  public Pawn enPassantPawn = null;
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

  public void setPos(int x, int y, Piece piece, boolean simulation) {
    if (piece instanceof Pawn pawn) {
      if (Math.abs(pawn.getY() - y) == 2) {
        enPassantPawn = pawn;
      } else {
        enPassantPawn = null;
      }
    } else {
      enPassantPawn = null;
    }
    deletePiece(piece);
    piece.setX(x);
    piece.setY(y);
    movePieceToNewTIle(piece);
    if (lookForPromotion() != null)
      promotion(lookForPromotion());
    if (!simulation) {
      piece.setHaveMoved(true);
    }
  }

  public void deletePiece(int x, int y) {
    for (Tile tile : tiles) {
      if (tile.getX() == x && tile.getY() == y) {
        tile.setPiece(null);
      }
    }
  }

  public void deletePiece(Piece piece) {
    for (Tile tile : tiles) {
      if (tile.getPiece() == piece) {
        tile.setPiece(null);
      }
    }
  }



  private void movePieceToNewTIle(Piece piece) {
    for (Tile tile : tiles) {
      if (tile.getX() == piece.getX() && tile.getY() == piece.getY()) {
        tile.setPiece(piece);
        break;
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

  public ArrayList<Piece> getColouredPieces(String colour) {
    ArrayList<Piece> pieces = new ArrayList<>();
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece().colour.equals(colour)) {
        pieces.add(tile.getPiece());
      }
    }
    return pieces;
  }

  public Rook getRookWithPos(String colour, int side) {
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece() instanceof Rook && tile.getPiece().getColour().equals(colour) && tile.getX() == side) {
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

  public void setInCheck(String colour, boolean value) {
    if (colour.equals("white"))
      whiteInCheck = value;
    else
      blackInCheck = value;
  }

  public ArrayList<Tile> getAllAttackedTiles(String colour) {
    ArrayList<Tile> attackedTiles = new ArrayList<>();
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece().getColour().equals(colour)) {
        try {
          attackedTiles.addAll(tile.getPiece().getAttackTiles());
        } catch (NullPointerException e) {System.out.println(tile.getPiece() + "Have" + e.getMessage());}
      }
    }
    return attackedTiles;
  }

  public Pawn lookForPromotion() {
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece() instanceof Pawn pawn) {
        boolean promote =
                (pawn.getColour().equals("white") && pawn.getY() == 8) ||
                        (pawn.getColour().equals("black") && pawn.getY() == 1);
        if (promote)
          return pawn;

      }
    }
    return null;
  }

  private void promotion(Pawn pawn) {
    boolean promote =
            (pawn.getColour().equals("white") && pawn.getY() == 8) ||
                    (pawn.getColour().equals("black") && pawn.getY() == 1);
      Queen queen = new Queen(pawn.getX(), pawn.getY(), pawn.getColour(), this);
      getTile(pawn.getX(), pawn.getY()).setPiece(queen);
  }

  public Tile getTile(int x, int y) {
    for (Tile tile : tiles) {
      if (tile.getX() == x && tile.getY() == y) {
        return tile;
      }
    }
    return null;
  }


  public void setChecksForKings() {
    Tile myTile;
    String colour;
    for (King king : getKings()) {
        myTile = new Tile(king.getX(), king.getY());
        if (GamePanel.turn.equals("white"))
          colour = "black";
        else colour = "white";
        if (getAllAttackedTiles(colour).contains(myTile)) {
          setInCheck(king.colour, true);
          king.inCheck = true;
        }
        else  {
          setInCheck(king.colour, false);
          king.inCheck = false;
        }
    }
  }

  public boolean simulateIsMoveLegal(Piece piece, Tile targetTile) {
    int oldX = piece.getX();
    int oldY = piece.getY();
    Pawn savedEnPassant = enPassantPawn;
    Piece captured = null;
    for (Tile t : getTiles()) {
      if (t.equals(targetTile)) {
        captured = t.getPiece();
      }
    }

    setPos(targetTile.getX(), targetTile.getY(), piece, true);
    setChecksForKings();

    boolean kingInCheck =
            piece.getColour().equals("white") ? whiteInCheck : blackInCheck;

    setPos(oldX, oldY, piece, true);
    if (captured != null) {
      setPos(targetTile.getX(), targetTile.getY(), captured, true);
    }
    setChecksForKings();
    enPassantPawn = savedEnPassant;

    return !kingInCheck;
  }

}
