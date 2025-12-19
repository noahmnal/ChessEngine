package GameLogic;

import Models.Board;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

public class Move {
  private final int fromX;
  private final int fromY;
  private final int toX;
  private final int toY;
  private final Piece piece;
  private Piece capturedPiece;
  private final boolean enPassant;
  private int direction;
  private final boolean castle;
  private Rook castlingRook;
  private boolean firstMove = false;
  private Pawn promotion = null;

  public Move(int fromX, int fromY, int toX, int toY, Piece piece, Piece capturedPiece) {
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
    this.piece = piece;
    this.capturedPiece = capturedPiece;
    enPassant = false;
    castle = false;
    if (piece.getHaveNotMoved())
      firstMove = true;
    System.out.println(firstMove);
  }

  //special constructor for castling and passant
  public Move(int fromX, int fromY, int toX, int toY, Piece piece, boolean enPassant, int direction, Rook castlingRook, Pawn promotion) {
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
    this.piece = piece;
    this.enPassant = enPassant;
    this.direction = direction;
    this.castlingRook = castlingRook;
    this.castle = castlingRook != null;
    this.promotion = promotion;
    setCapturedPiece();
    if (enPassant)
      updateCapturedPieceAfterEnPassant(direction);
    if (piece.getHaveNotMoved())
      firstMove = true;
    System.out.println(this);

  }

  public boolean isPromotion() {
    return promotion != null;
  }

  public Pawn getPromotionPiece() {
    return promotion;
  }


  public Piece getPiece() {
    return piece;
  }


  public void setCapturedPiece() {
    if (Board.getTile(toX, toY).getPiece() != null) {
      if (!Board.getTile(toX, toY).getPiece().getColour().equals(piece.getColour())) {
        this.capturedPiece = Board.getTile(toX, toY).getPiece();
        System.out.println("captured piece" + capturedPiece);
        return;
      }
    }
    this.capturedPiece = null;
  }

  public boolean isCastle() {
    return castle;
  }

  public boolean isFirstMove() {
    return firstMove;
  }

  public Rook getCastlingRook() {
    return castlingRook;
  }

  public int getDirection() {
    return direction;
  }

  public int getRookNewPosition() {
    return castlingRook.getX() + direction;
  }

  private void updateCapturedPieceAfterEnPassant(int sign) {
    capturedPiece = Board.getTile(toX, toY-sign).getPiece();
    try {
      System.out.println(capturedPiece.toString() + "passant!!!");
    } catch (Exception e) {
      System.out.println("Passant null");
    }
  }

  public Piece getCapturedPiece() {
    return capturedPiece;
  }


  public int getFromX() {
    return fromX;
  }

  public int getFromY() {
    return fromY;
  }

  public int getToX() {
    return toX;
  }

  public int getToY() {
    return toY;
  }

  public boolean isEnPassant() {
    return enPassant;
  }

  public boolean getEnPassant() {
    return enPassant;
  }

  @Override
  public String toString() {
    return "Move{" +
            "fromX=" + fromX +
            ", fromY=" + fromY +
            ", toX=" + toX +
            ", toY=" + toY +
            ", piece=" + piece +
            ", capturedPiece=" + capturedPiece +
            ", enPassant=" + enPassant +
            ", direction=" + direction +
            ", castle=" + castle +
            ", castlingRook=" + castlingRook +
            ", firstMove=" + firstMove +
            ", promotion=" + promotion +
            '}';
  }
}
