package GameLogic;

import Pieces.King;
import Pieces.Piece;

import java.util.ArrayList;

public class PositionRater {

  public static int ratePosition(ArrayList<Piece> pieces) {
    int whitePieceValue = 0;
    int blackPieceValue = 0;
    int rating;

    for (Piece piece : pieces) {
      if (piece.getColour().equals("white")) {
        whitePieceValue += piece.value;
        whitePieceValue += piece.getAttackTiles().size();
        if (piece instanceof King king && king.hasCastled) {
          whitePieceValue += 80;
        }
      } else {
        blackPieceValue += piece.value;
        blackPieceValue += piece.getAttackTiles().size();
        if (piece instanceof King king && king.hasCastled) {
          blackPieceValue += 80;
        }
      }
    }
    rating = whitePieceValue - blackPieceValue;
    return rating;

  }
}
