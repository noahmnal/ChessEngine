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
        whitePieceValue += piece.getSudoLegalTiles().size();
        if (piece instanceof King king && king.hasCastled) {
          whitePieceValue += 200;
        }
      } else {
        blackPieceValue += piece.value;
        blackPieceValue += piece.getSudoLegalTiles().size();
        if (piece instanceof King king && king.hasCastled) {
          blackPieceValue += 2000;
        }
      }
    }
    rating = whitePieceValue - blackPieceValue;
    return rating;

  }
}
