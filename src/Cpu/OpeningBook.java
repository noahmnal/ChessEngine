package Cpu;

import GameLogic.GamePanel;
import GameLogic.MovesHistory;
import Models.Board;
import Models.Tile;
import Pieces.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class OpeningBook {
  public static List<String> moves2;
  public static List<String> moves3;
  public static List<String> moves4;



  public OpeningBook() throws IOException {
     moves2 = Files.readAllLines(
            Path.of("src/Cpu/2moves.epd"));
    moves3 = Files.readAllLines(
            Path.of("src/Cpu/3moves.epd"));
    moves4 = Files.readAllLines(
            Path.of("src/Cpu/4moves.epd"));


  }


  private static char pieceToFenChar(Piece p) {
    char c = switch (p) {
      case Pawn _ -> 'p';
      case Knight _ -> 'n';
      case Bishop _ -> 'b';
      case Rook _ -> 'r';
      case Queen _ -> 'q';
      case King _ -> 'k';
      default -> '?';
    };
    return p.getColour().equals("white") ? Character.toUpperCase(c) : c;
  }

  private static String makeBoardToFen() {
    int spaces = 0;
    StringBuilder fenPos = new StringBuilder();
    for (int y = 8; y >= 1; y--) {
      for (int x = 1; x <= 8; x++) {
        Piece piece = Board.getPiece(x, y);
        if (piece == null) {
          spaces++;
        } else if (spaces > 0) {
          fenPos.append(spaces);
          spaces = 0;
          fenPos.append(pieceToFenChar(piece));
        } else  {
          fenPos.append(pieceToFenChar(piece));
        }
      }
      if (spaces != 0) {
        fenPos.append(spaces);
        spaces = 0;
      }
      if (y != 1)
        fenPos.append("/");
    }
    return fenPos.toString();
  }

  private static String castleToFen() {
    boolean whiteKingNotMoved = false;
    boolean blackKingNotMoved = false;
    StringBuilder fen = new StringBuilder();
    for (King king : Board.getKings()) {
      if (king.getColour().equals("white")) {
        whiteKingNotMoved = king.getHaveNotMoved();
      } else blackKingNotMoved = king.getHaveNotMoved();
    }
    Rook rook = Board.getRookWithPos("white", 8);
    if (rook != null && rook.getHaveNotMoved() && whiteKingNotMoved)
      fen.append("K");

    rook = Board.getRookWithPos("white", 1);
    if (rook != null && rook.getHaveNotMoved() && whiteKingNotMoved)
      fen.append("Q");

    rook = Board.getRookWithPos("black", 8);
    if (rook != null && rook.getHaveNotMoved() && blackKingNotMoved)
      fen.append("k");

    rook = Board.getRookWithPos("black", 1);
    if (rook != null && rook.getHaveNotMoved() && blackKingNotMoved)
      fen.append("q");


    if (fen.isEmpty())
      fen.append("-");
    return fen.toString();
  }

  private static Tile findenPassantMove() {
    for (Piece piece : Board.getPieces()) {
      if (piece instanceof Pawn pawn) {
        if (pawn.getEnPassantMove() != null && pawn.getColour().equals(GamePanel.turn))
          return pawn.getEnPassantMove();
      }
    }
    return null;
  }

  private static String tileToFen(Tile tile) {
    if (tile == null)
      return "-";
    String fen = switch (tile.getX()) {
      case 1 -> "a";
      case 2 -> "b";
      case 3 -> "c";
      case 4 -> "d";
      case 5 -> "e";
      case 6 -> "f";
      case 7 -> "g";
      case 8 -> "h";
      default -> "?";
    };
    return fen + tile.getY();
  }

  public static String buildWholeFen(int gameTurn) {
    String turn = GamePanel.turn.equals("white") ? "w" : "b";
    if (MovesHistory.getMoves() == null)
      return "-";
    return makeBoardToFen() + " " +
             turn + " " + castleToFen() + " " +
            tileToFen(findenPassantMove()) + " " + Board.fiftyMoveCounter + " " + gameTurn;

  }
}
