package GameLogic;

import Pieces.King;
import Pieces.Piece;

import java.util.ArrayList;

public class PositionRater {

  private static final int[][] kingPointBoard = {
          { 40, 30, 20, 10, 10, 20, 30, 40 },
          { 30, 20, 10,  0,  0, 10, 20, 30 },
          { 20, 10,  0, -10, -10,  0, 10, 20 },
          { 10,  0, -10, -20, -20, -10,  0, 10 },
          { 10,  0, -10, -20, -20, -10,  0, 10 },
          { 20, 10,  0, -10, -10,  0, 10, 20 },
          { 30, 20, 10,  0,  0, 10, 20, 30 },
          { 40, 30, 20, 10, 10, 20, 30, 40 }
  };

  public static int ratePosition(ArrayList<Piece> pieces) {
    boolean endgame = pieces.size() <= 5;
    int whitePieceValue = 0;
    int blackPieceValue = 0;
    int rating;
    for (Piece piece : pieces) {
      if (piece.getColour().equals("white")) {
        whitePieceValue += piece.value;
        if (!endgame) {
          whitePieceValue += piece.getAttackTiles().size();
          System.out.println(piece+ "   :  " + piece.getAttackTiles().size());
          if (piece instanceof King king && king.hasCastled) {
            whitePieceValue += 80;
          }
        } else {
          if (piece instanceof King king && whitePieceValue > blackPieceValue) {
            blackPieceValue += kingPointBoard[king.getX()][king.getY()];
          }
        }
      } else {
        blackPieceValue += piece.value;
        if (!endgame) {
          blackPieceValue += piece.getAttackTiles().size();
          if (piece instanceof King king && king.hasCastled) {
            blackPieceValue += 80;
          }
        } else  {
          if (piece instanceof King king && blackPieceValue > whitePieceValue) {
            whitePieceValue += kingPointBoard[king.getX()][king.getY()];
          }
        }
      }
    }
    rating = whitePieceValue - blackPieceValue;
    return rating;

  }
}
