package Models;

import Pieces.*;

import java.util.ArrayList;
import java.util.Objects;

import GameLogic.GamePanel;
import GameLogic.Move;
import GameLogic.MovesHistory;

public class Board {
  private static final ArrayList<Tile> tiles = new ArrayList<>();
  public boolean whiteInCheck = false;
  public boolean blackInCheck = false;
  public Pawn enPassantPossible = null;

  public void init() {
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

  public void makeMove(Move move, boolean simulation) {
    if (move.getPiece() instanceof Pawn pawn) {
      if (Math.abs(move.getFromY() - move.getToY()) == 2) {
        enPassantPossible = pawn;
      } else {
        enPassantPossible = null;
      }
    } else {
      enPassantPossible = null;
    }
    if (move.isCastle()) {
      deletePieceFromTile(move.getCastlingRook());
      move.getCastlingRook().setX(move.getRookNewPosition());
      movePieceToNewTIle(move.getCastlingRook());
    }
    deletePieceFromTile(move.getPiece());
    move.getPiece().setX(move.getToX());
    move.getPiece().setY(move.getToY());
    movePieceToNewTIle(move.getPiece());
    if (lookForPromotion() != null)
      promotion(Objects.requireNonNull(lookForPromotion()));
    if (!simulation) {
      MovesHistory.addMove(move);
      move.getPiece().setHaveMoved(true);
    }
  }

  public void deletePieceFromTile(int x, int y) {
    for (Tile tile : tiles) {
      if (tile.getX() == x && tile.getY() == y) {
        tile.setPiece(null);
      }
    }
  }

  public void deletePieceFromTile(Piece piece) {
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

  public static ArrayList<Tile> getTilesWithPieces() {
    ArrayList<Tile> tilesWithPieces = new ArrayList<>();
    for (Tile tile : tiles) {
      if (tile.getPiece() != null) {
        tilesWithPieces.add(tile);
      }
    }
    return tilesWithPieces;
  }

  public ArrayList<Piece> getPieces() {
    ArrayList<Piece> pieces = new ArrayList<>();
    for (Tile tile : tiles) {
      if (tile.getPiece() != null) {
        pieces.add(tile.getPiece());
      }
    }
    return pieces;
  }

  public ArrayList<Piece> getColouredPieces(String colour) {
    ArrayList<Piece> pieces = new ArrayList<>();
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece().getColour().equals(colour)) {
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

  private ArrayList<King> getKings() {
    ArrayList<King> kings = new ArrayList<>();
    for (Tile tile : getTilesWithPieces()) {
      if (tile.getPiece() instanceof King) {
        kings.add((King) tile.getPiece());
      }
    }
    return kings;
  }

  private void setInCheck(String colour, boolean value) {
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

  private Pawn lookForPromotion() {
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
      Queen queen = new Queen(pawn.getX(), pawn.getY(), pawn.getColour(), this);
      getTile(pawn.getX(), pawn.getY()).setPiece(queen);
  }

  public static Tile getTile(int x, int y) {
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
          setInCheck(king.getColour(), true);
          king.inCheck = true;
        }
        else  {
          setInCheck(king.getColour(), false);
          king.inCheck = false;
        }
    }
  }

  public void undoMove() {
    Move lastMove = null;
    if (MovesHistory.getMoves() != null)
       lastMove = MovesHistory.getMoves().getLast();
    assert lastMove != null;
    reverseMove(lastMove, false);
  }

  public static Piece getCapturedPiece(int x, int y, Piece piece, boolean enPassant, int sign) {
    Piece capturedPiece;
    if (!enPassant) {
      System.out.println("kom 250");
      if (getTile(x, y).getPiece() != null) {
        System.out.println("kom 251");
        System.out.println(x + " y :" + y);
        if (!Board.getTile(x, y).getPiece().getColour().equals(piece.getColour())) {

          capturedPiece = Board.getTile(x, y).getPiece();
          System.out.println("captured piece" + capturedPiece);
          return capturedPiece;
        }
      }
      return null;
    } else {
      capturedPiece = Board.getTile(x, y-sign).getPiece();
      return capturedPiece;
    }
  }



  public boolean simulateIsMoveLegal(Piece piece, Tile targetTile) {

      int oldX = piece.getX();
      int oldY = piece.getY();

      Piece captured = getTile(targetTile.getX(), targetTile.getY()).getPiece();
      Pawn savedEnPassant = enPassantPossible;

      getTile(oldX, oldY).setPiece(null);

      piece.setX(targetTile.getX());
      piece.setY(targetTile.getY());
      getTile(piece.getX(), piece.getY()).setPiece(piece);

      setChecksForKings();

      boolean illegal =
              piece.getColour().equals("white") ? whiteInCheck : blackInCheck;

      getTile(piece.getX(), piece.getY()).setPiece(captured);
      piece.setX(oldX);
      piece.setY(oldY);
      getTile(oldX, oldY).setPiece(piece);

      enPassantPossible = savedEnPassant;
      setChecksForKings();

      return !illegal;
  }

  public  void reverseMove(Move move, boolean simulate) {
    if (move.isCastle()) {
      deletePieceFromTile(move.getCastlingRook());
      move.getCastlingRook().setX(move.getCastlingRook().getX()-move.getDirection());
      movePieceToNewTIle(move.getCastlingRook());
    }
    if (move.getCapturedPiece() != null)
      getTile(move.getCapturedPiece().getX(), move.getCapturedPiece().getY()).setPiece(move.getCapturedPiece());

    deletePieceFromTile(move.getPiece());
    move.getPiece().setX(move.getFromX());
    move.getPiece().setY(move.getFromY());
    movePieceToNewTIle(move.getPiece());
    if (!simulate) {
      MovesHistory.removeLast();
      if (move.isFirstMove())
        move.getPiece().setHaveMoved(false);
    }

    if (GamePanel.turn.equals("white"))
      GamePanel.turn = "black";
    else
      GamePanel.turn = "white";
  }


    /*
    if (move.getEnPassant())
      return new Move(move.getToX(), move.getToY(), move.getFromX(),
              move.getFromY(), move.getPiece(), move.getEnPassant(), move.getDirection(), null);
    else if (move.isCastle())
      return new Move(move.getToX(), move.getToY(), move.getFromX(),
              move.getFromY(), move.getPiece(), move.getEnPassant(), -move.getDirection(), move.getCastlingRook());
    else
      return new Move(move.getToX(), move.getToY(), move.getFromX(), move.getFromY(), move.getPiece());
  }
  */

}
