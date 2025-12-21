package Models;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Rook;
import GameLogic.Move;

public class GameLogic {
  public Piece getMouseLocationPiece(int mouseX, int mouseY){
    for (Tile tile : Board.getTiles()) {
      if (tile.getHitbox().contains(mouseX, mouseY) && tile.getPiece() != null) {
        return tile.getPiece();
      }
    }
    return null;
  }

  public Tile getMouseLocationTile(int mouseX, int mouseY){
    for (Tile tile : Board.getTiles()) {
      if (tile.getHitbox().contains(mouseX, mouseY)) {
        return tile;
      }
    }
    return null;
  }

  public static String switchTurn(String inTurn){
    String turn;
    if (inTurn.equals("white")) turn = "black";
    else turn = "white";
    return turn;
  }

  public static Move createMove(int x, int y, Piece piece) {
    Tile chosenTile = new Tile(x, y);
    if (piece instanceof Pawn pawn &&
            pawn.enPassantPawn) {
      int sign = pawn.getColour().equals("white") ? 1 : -1;
      pawn.enPassantPawn = false;
      return new Move(piece.getX(), piece.getY(), x, y, piece, true, sign, null, null);
    } else if (Board.lookForPromotion(piece)  != null) {
      return new Move(piece.getX(), piece.getY(), x, y, piece,
              false, 0, null, Board.lookForPromotion(piece));
    }
    else if(piece instanceof King) {
      try {
        if (((King) piece).getCastlingTiles().containsKey(chosenTile)) {
          Rook chosenRook = ((King) piece).getCastlingTiles().get(chosenTile);
          if (chosenRook.getX() == 8) {
            return new Move(piece.getX(),
                    piece.getY(), x, y, piece, false,
                    -2, chosenRook, null);
          }
          else {
            return new Move(piece.getX(), piece.getY(),
                    x, y, piece, false, 3, chosenRook, null);
          }
        }
      } catch (NullPointerException _) {
      }
    }
    Piece capturedPiece = Board.getCapturedPiece(x, y, piece, false, 0);
    return new Move(piece.getX(), piece.getY(), x, y, piece, capturedPiece);
  }

}
