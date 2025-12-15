package GameLogic;

import Models.Board;
import Pieces.Piece;

import java.util.ArrayList;

public class PositionRater {
  private final Board board;


  public PositionRater(Board board) {
    this.board = board;
  }

  public static void ratePosition(ArrayList<Piece> pieces) {
    int whitePieceValue = 0;
    int blackPieceValue = 0;
    int rating;

    for (Piece piece : pieces) {
      if (piece.getColour().equals("white"))
        whitePieceValue += piece.value;
      else blackPieceValue += piece.value;
    }
    rating = whitePieceValue - blackPieceValue;
    System.out.println(rating);


  }
}
