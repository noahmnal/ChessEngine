package Models;

import Pieces.*;

import java.util.ArrayList;
import GameLogic.GamePanel;
import GameLogic.Move;
import GameLogic.MovesHistory;

public class Board {
  private static final ArrayList<Tile> tiles = new ArrayList<>();
  public static boolean whiteInCheck = false;
  public static boolean blackInCheck = false;
  private static final ArrayList<Piece> pieces = new ArrayList<>();
  private static final ArrayList<Move> whitePieces = new ArrayList<>();
  private static final ArrayList<Move> blackPieces = new ArrayList<>();
  public static int fiftyMoveCounter = 0;

  public static void init() {
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
        case 2 -> new Pawn(x, y, "white");

        // Black pawns
        case 7 -> new Pawn(x, y, "black");

        // White back rank
        case 1 -> {
          switch (x) {
            case 1, 8 -> new Rook(x, y, "white");
            case 2, 7 -> new Knight(x, y, "white");
            case 3, 6 -> new Bishop(x, y, "white");
            case 4 -> new Queen(x, y, "white");
            case 5 -> new King(x, y, "white");
          }
        }
        //black Pieces
        case 8 -> {
          switch (x) {
            case 1, 8 -> new Rook(x, y, "black");
            case 2, 7 -> new Knight(x, y, "black");
            case 3, 6 -> new Bishop(x, y, "black");
            case 4 -> new Queen(x, y, "black");
            case 5 -> new King(x, y, "black");
          }
        }

      }
    }
  }


  public static void addNewPiece(Piece newPiece) {
    pieces.add(newPiece);
  }


  public static ArrayList<Tile> getTiles() {
    return tiles;
  }

  public static void makeMove(Move move, boolean simulation) {
    int saveFiftyMoveCounter = fiftyMoveCounter;
    fiftyMoveCounter++;
    if (move.isCastle()) {
      move.getCastlingRook().setX(move.getRookNewPosition());
      if (move.getPiece() instanceof King king) {
        king.setHasCastled(true);
      }
    }
    move.getPiece().setX(move.getToX());
    move.getPiece().setY(move.getToY());

    if (move.isPromotion()) {
      Queen promotedQueen = new Queen(move.getToX(), move.getToY(), move.getPiece().getColour());
      move.setPromotedQueen(promotedQueen);
      pieces.remove(move.getPiece());
    }
    if (move.getCapturedPiece() != null) {
      fiftyMoveCounter = 0;
      pieces.remove(move.getCapturedPiece());
    }
    if (move.getPiece() instanceof Pawn) {
      fiftyMoveCounter = 0;
    }
    MovesHistory.addMove(move);
    move.getPiece().setHaveMoved(true);
    if (!simulation)
      setChecksForKings();
    else fiftyMoveCounter = saveFiftyMoveCounter;
    GamePanel.turn = GameLogic.switchTurn(GamePanel.turn);
  }

  public static void reverseMove(Move move, boolean simulation) {
    int saveFiftyMoveCounter = fiftyMoveCounter;
    fiftyMoveCounter--;
    if (move.isCastle()) {
      move.getCastlingRook().setX(move.getCastlingRook().getX() - move.getDirection());
      if (move.getPiece() instanceof King king) {
        king.setHasCastled(false);
      }
    }
    if  (move.isPromotion()) {
      pieces.remove(move.getPromotedQueen());
      pieces.add(move.getPiece());
    }

    if (move.getCapturedPiece() != null) {
      fiftyMoveCounter = move.getFiftyMoveCounter();
      pieces.add(move.getCapturedPiece());
    }
    if (move.getPiece() instanceof Pawn) {
      fiftyMoveCounter = move.getFiftyMoveCounter();
    }

    move.getPiece().setX(move.getFromX());
    move.getPiece().setY(move.getFromY());

    assert MovesHistory.getMoves() != null;

    if (move.isFirstMove())
      move.getPiece().setHaveMoved(false);
    MovesHistory.removeLast();
    if (simulation)
      fiftyMoveCounter = saveFiftyMoveCounter;
    GamePanel.turn = GameLogic.switchTurn(GamePanel.turn);
  }

  public static ArrayList<Piece> getPieces() {
    return pieces;
  }

  public static ArrayList<Move> getWhitePieces() {
    return whitePieces;
  }
  public static ArrayList<Move> getBlackPieces() {
    return blackPieces;
  }

  public static ArrayList<Piece> getColouredPieces(String colour) {
    ArrayList<Piece> coloredPieces = new ArrayList<>();
    for (Piece piece : getPieces()) {
      if (piece.getColour().equals(colour)) {
        coloredPieces.add(piece);
      }
    }
    return coloredPieces;
  }

  public static Rook getRookWithPos(String colour, int xPos) {
    for (Piece piece : getPieces()) {
      if (piece instanceof Rook rook && rook.getColour().equals(colour) && rook.getX() == xPos) {
        return rook;
      }
    }
    return null;
  }

  public static ArrayList<King> getKings() {
    ArrayList<King> kings = new ArrayList<>();
    for (Piece piece : getPieces()) {
      if (piece instanceof King king) {
        kings.add(king);
      }
    }
    return kings;
  }

  private static void setInCheck(String colour, boolean value) {
    if (colour.equals("white"))
      whiteInCheck = value;
    else
      blackInCheck = value;
  }

  public static ArrayList<Tile> getAllAttackedTiles(String colour) {
    ArrayList<Tile> attackedTiles = new ArrayList<>();
    for (Piece piece : getPieces()) {
      if (piece.getColour().equals(colour)) {
        try {
          attackedTiles.addAll(piece.getAttackTiles());
        } catch (NullPointerException e) {
          System.out.println(e.getMessage());
        }
      }
    }
    return attackedTiles;
  }


  public static void setChecksForKings() {
    for (King king : getKings()) {
      String enemyColour =
              king.getColour().equals("white") ? "black" : "white";

      boolean inCheck =
              getAllAttackedTiles(enemyColour)
                      .contains(new Tile(king.getX(), king.getY()));

      setInCheck(king.getColour(), inCheck);
      king.inCheck = inCheck;
    }
  }


  public static void undoMove() {
    Move lastMove = null;
    if (MovesHistory.getMoves() != null)
       lastMove = MovesHistory.getMoves().getLast();
    assert lastMove != null;
    reverseMove(lastMove, false);
  }

  public static Piece getCapturedPiece(int x, int y, String colour, boolean enPassant, int sign) {
    Piece capturedPiece;
    if (!enPassant) {
      if (getPiece(x, y) != null) {
        if (!Board.getPiece(x, y).getColour().equals(colour)) {
          capturedPiece = Board.getPiece(x, y);
          return capturedPiece;
        }
      }
      return null;
    } else {
      capturedPiece = Board.getPiece(x, y-sign);
      return capturedPiece;
    }
  }



  public static boolean simulateIsMoveLegal(Piece piece, Tile targetTile) {
      Move move = GameLogic.createMove(targetTile.getX(), targetTile.getY(), piece);
      makeMove(move, true);

      setChecksForKings();

      boolean illegal =
              piece.getColour().equals("white") ? whiteInCheck : blackInCheck;

      reverseMove(move, true);
      setChecksForKings();
      return !illegal;
  }

  public static Piece getPiece(int x, int y) {
    for (Piece piece : pieces) {
      if (piece.getX() == x && piece.getY() == y) {
        return piece;
      }
    }
    return null;
  }


}
