package Models;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Rook;
import GameLogic.Move;
import GameLogic.MovesHistory;

public class GameLogic {
  public Piece getMouseLocationPiece(int mouseX, int mouseY){
    for (Tile tile : Board.getTiles()) {
      if (tile.getHitbox().contains(mouseX, mouseY) && Board.getPiece(tile.getX(), tile.getY())  != null) {
        return Board.getPiece(tile.getX(), tile.getY());
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
    if (piece instanceof Pawn pawn) {
      if (y == 1 || y == 8) {
        return new Move(piece.getX(), piece.getY(), x, y, piece, Board.getPiece(x, y), false, 0, null, pawn, null);
      }
      if (Math.abs(pawn.getY() - y) == 2) {
        Piece capturedPiece = Board.getCapturedPiece(x, y, piece.getColour(), false, 0);
        return new Move(pawn.getX(), pawn.getY(), x, y, piece,
                capturedPiece, false, 0, null, null, pawn);
      }

      if (pawn.getEnPassantMove() != null && pawn.getEnPassantMove().getX() == x && pawn.getEnPassantMove().getY() == y) {
        int sign = pawn.getColour().equals("white") ? 1 : -1;
        if (!MovesHistory.getMoves().isEmpty()) {
          Piece capturedPiece = MovesHistory.getMoves().getLast().getEnPasantNextTurn();
          return new Move(piece.getX(), piece.getY(), x, y, piece,
                  capturedPiece, true, sign, null, null, (Pawn) capturedPiece);
        }
      }
    }
    else if (piece instanceof King king && !king.hasCastled) {
      try {
        if (king.getCastlingTiles().containsKey(chosenTile)) {
          Rook chosenRook = king.getCastlingTiles().get(chosenTile);
          if (chosenRook.getX() == 8) {
            return new Move(piece.getX(),
                    piece.getY(), x, y, piece, null, false,
                    -2, chosenRook, null, null);
          }
          else {
            return new Move(piece.getX(), piece.getY(),
                    x, y, piece, null, false, 3, chosenRook, null, null);
          }
        }
      } catch (NullPointerException e) {
        System.out.println(e.getMessage());
      }
    }
    Piece capturedPiece = Board.getCapturedPiece(x, y, piece.getColour(), false, 0);
    return new Move(piece.getX(), piece.getY(), x, y, piece, capturedPiece);
  }

}
