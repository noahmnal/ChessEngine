package gameLogic;

import pieces.King;
import pieces.Pawn;
import pieces.Piece;

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

  private static final int[][] pawnEndgameBoard = {
          { 100, 100, 100, 100, 100, 100, 100, 100 },
          {  80,  80,  80,  80,  80,  80,  80,  80 },
          {  50,  50,  50,  50,  50,  50,  50,  50 },
          {  30,  30,  30,  30,  30,  30,  30,  30 },
          {  20,  20,  20,  20,  20,  20,  20,  20 },
          {  10,  10,  10,  10,  10,  10,  10,  10 },
          {   0,   0,   0,   0,   0,   0,   0,   0 },
          {   0,   0,   0,   0,   0,   0,   0,   0 }
  };

  private static boolean endgame = false;


  public static int ratePosition(ArrayList<Piece> pieces) {
    int whitePieceValue = 0;
    int blackPieceValue = 0;
    int rating;
    for (Piece piece : pieces) {
      if (piece.getColour().equals("white")) {
        whitePieceValue += piece.value;
        if (!endgame) {
          whitePieceValue += piece.getAttackTiles().size();
          if (piece instanceof King king && king.hasCastled) {
            whitePieceValue += 80;
          }
        } else {
          if (piece instanceof King king ) {
            if (whitePieceValue < blackPieceValue)
              whitePieceValue -= kingPointBoard[king.getX() - 1][king.getY() - 1];
            else whitePieceValue += kingPointBoard[king.getX() - 1][king.getY() - 1];
          }

          if (piece instanceof Pawn pawn) {
            whitePieceValue += pawnEndgameBoard[pawn.getX() - 1][pawn.getY() - 1];
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
          if (piece instanceof King king ) {
            if (whitePieceValue > blackPieceValue)
              blackPieceValue -= kingPointBoard[king.getX() - 1][king.getY() - 1];
            else blackPieceValue += kingPointBoard[king.getX() - 1][king.getY() - 1];
          }
          if (piece instanceof Pawn pawn) {
            blackPieceValue += pawnEndgameBoard[pawn.getX() - 1][8-pawn.getY()];
          }
        }
      }
    }
    if (whitePieceValue < 1700 || blackPieceValue < 1700) {
      endgame = true;
    }
    rating = whitePieceValue - blackPieceValue;
    return rating;

  }
}
