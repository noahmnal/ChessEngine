package gameLogic;

import models.Board;
import pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public class PositionHistory {
  private static final Map<String, Integer> history = new HashMap<>();

  public static void addCurrentPosition() {
    String fen = generateSimplifiedFEN();
    history.put(fen, history.getOrDefault(fen, 0) + 1);
  }

  public static boolean isThreefoldRepetition() {
    System.out.println(history.keySet());
    System.out.println(history.values());
    return history.getOrDefault(generateSimplifiedFEN(), 0) >= 3;
  }

  public static void removeLastPosition() {
    String fen = generateSimplifiedFEN();
    if (history.containsKey(fen)) {
      int count = history.get(fen);
      if (count <= 1) history.remove(fen);
      else history.put(fen, count - 1);
    }
  }

  private static String generateSimplifiedFEN() {
    StringBuilder sb = new StringBuilder();

    // 1. Brikkeplassering (fra rad 8 ned til 1)
    for (int y = 8; y >= 1; y--) {
      int emptyTiles = 0;
      for (int x = 1; x <= 8; x++) {
        Piece p = Board.getPiece(x, y);
        if (p == null) {
          emptyTiles++;
        } else {
          if (emptyTiles > 0) {
            sb.append(emptyTiles);
            emptyTiles = 0;
          }
          sb.append(getPieceChar(p));
        }
      }
      if (emptyTiles > 0) sb.append(emptyTiles);
      if (y > 1) sb.append("/");
    }

    return sb.toString();
  }

  private static String getPieceChar(Piece p) {
    String name = p.getClass().getSimpleName();
    char c = (name.equals("Knight")) ? 'N' : name.charAt(0);
    return p.getColour().equals("white") ? String.valueOf(c).toUpperCase() : String.valueOf(c).toLowerCase();
  }
}
