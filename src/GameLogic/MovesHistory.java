package GameLogic;


import java.util.ArrayList;

public class MovesHistory {
  private static final ArrayList<Move> moves = new ArrayList<>();


  public static void addMove(Move move) {
    moves.add(move);
  }

  public static ArrayList<Move> getMoves() {
    return moves;
  }
  public static void removeLast() {
    if (moves.isEmpty()) {return;}
    moves.removeLast();
  }

}
