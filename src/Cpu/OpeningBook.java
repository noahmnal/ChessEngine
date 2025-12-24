package Cpu;

import GameLogic.Move;
import Models.Board;
import Models.Tile;
import Pieces.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class OpeningBook {
  public static List<String> moves;



  public OpeningBook() throws IOException {
     moves = Files.readAllLines(
            Path.of("src/Cpu/Games.txt"));

     System.out.println(moves.getFirst());
  }

  public static String moveNotation(Move move) {
    if (move.isCastle()) {
      if (move.getCastlingRook().getX() == 8)
        return "O-O"+addCheckSymbol(move);
      else return "O-O-O"+addCheckSymbol(move);
    }
    if (move.getPiece() instanceof Pawn pawn && move.getCapturedPiece() != null)
      return  xCordToLetter(pawn.getX()) + captureNotation(move) + xCordToLetter(move.getCapturedPiece().getX()) + move.getToY();
    return pieceToFenChar(move.getPiece()) + checkForClarification(move)
            + captureNotation(move) + xCordToLetter(move.getToX()) + move.getToY() + addCheckSymbol(move);
  }

  public static String checkForClarification(Move move) {
    boolean twoLegalMoves = false;
    for (Piece piece : new ArrayList<>(Board.getPieces())) {
      if (piece.getColour().equals(move.getPiece().getColour()) && piece.getClass().equals(move.getPiece().getClass())
              && !piece.equals(move.getPiece())) {
        if (piece.setAndGetLegalTiles().contains(new Tile(move.getToX(), move.getToY()))) {
          twoLegalMoves = true;
          break;
        }
      }
    }
    if (twoLegalMoves)
      return xCordToLetter(move.getFromX());
    return "";
  }

  private static String addCheckSymbol(Move move) {
    boolean inCheck = false;
    Board.makeMove(move, true);
    String enemyColour =
            move.getPiece().getColour().equals("white") ? "black" : "white";
    for (King king : Board.getKings()) {
      if (king.getColour().equals(enemyColour)) {
        inCheck = Board.getAllAttackedTiles(move.getPiece().getColour()).contains(new Tile(king.getX(), king.getY()));
      }
    }
    Board.reverseMove(move, true);
    if (inCheck)
      return "+";
    return "";
  }


  private static String captureNotation(Move move) {
    if (move.getCapturedPiece() != null) {
      return "x";
    }
    return "";
  }
  private static String xCordToLetter(int x) {
    return switch (x) {
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
  }

  private static String pieceToFenChar(Piece p) {
    return switch (p) {
      case Pawn _ -> "";
      case Knight _ -> "N";
      case Bishop _ -> "B";
      case Rook _ -> "R";
      case Queen _ -> "Q";
      case King _ -> "K";
      default -> "?";
    };
  }




}
