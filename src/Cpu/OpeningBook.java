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
  public static List<String> gmGames;
  public static List<String> noobOpeningsWhite;
  public static List<String> noobOpeningsBlack;




  public OpeningBook() throws IOException {
     gmGames = Files.readAllLines(
            Path.of("src/Cpu/GmGames.txt"));

     noobOpeningsWhite = Files.readAllLines(Path.of("src/Cpu/6movesOpeningsWhite+0.5.txt"));

    noobOpeningsBlack = Files.readAllLines(Path.of("src/Cpu/5movesOpeningsBlack-0.2.txt"));


  }

  public static String findMoveInBook(String[] gameNotation, List<String> openingBook) {
    System.out.println(Arrays.toString(gameNotation));
    int count = 0;
    outer: for (String move : openingBook)  {
      count++;
      String[] separatedMoves = move.split(" ");
      for (int i = 0; i < gameNotation.length; i++) {
        if (!gameNotation[i].equals(separatedMoves[i])) {
          continue outer;
        }
      }
      System.out.println(count);
      return separatedMoves[gameNotation.length];
    }
    return "";
  }

  public static Move notationToMove(String notation, String colour) {
    System.out.println(notation);
    int toX;
    int toY;
    notation = notation.replace("+", "").replace("#", "").replace("x", "");
    Tile tile;
    String pieceName;

    if (notation.equals("O-O")) {
      if (colour.equals("white")) {
        return new Move(5, 1, 7, 1, Board.getPiece(5, 1), null, false, 0,
                (Rook) Board.getPiece(8,1), null, null);
      }
      else return new Move(5, 8, 7, 8, Board.getPiece(5, 8), null, false, 0,
              (Rook) Board.getPiece(8,8), null, null);
    }
    if (notation.equals("O-O-O")) {
      if (colour.equals("white")) {
        return new Move(5, 1, 3, 1, Board.getPiece(5, 1), null, false, 0,
                (Rook) Board.getPiece(1, 1), null, null);
      } else return new Move(5, 8, 3, 8, Board.getPiece(5, 8), null, false, 0,
              (Rook) Board.getPiece(1, 8), null, null);
    }
    toX = charToXCord(notation.charAt(notation.length() - 2));
    toY = Character.getNumericValue(notation.charAt(notation.length() - 1));
    tile = new Tile(toX, toY);
    System.out.println(tile);


    if (Character.isLowerCase(notation.charAt(0)))
      pieceName = "Pawn";
    else
      pieceName = notationCharToPiece(notation.charAt(0));

    for (Piece p : Board.getPieces()) {
      if (p.getColour().equals(colour)) {
      if (p.getClass().getSimpleName().equals(pieceName)) {
        if (p.setAndGetLegalTiles().contains(tile))
          return new Move(p.getX(), p.getY(), toX, toY, p, Board.getCapturedPiece(toX, toY, colour, false, 0));
      }
      }
    }
    System.out.println("ERROR");
    return null;
  }

  public static String moveNotation(Move move) {
    if (move.isCastle()) {
      if (move.getCastlingRook().getX() == 8)
        return "O-O" + addCheckSymbol(move);
      else return "O-O-O" + addCheckSymbol(move);
    }
    if (move.getPiece() instanceof Pawn pawn && move.getCapturedPiece() != null)
      return  xCordToLetter(move.getFromX()) + captureNotation(move) + xCordToLetter(move.getCapturedPiece().getX()) + move.getToY();
    return pieceToNotationChar(move.getPiece()) + checkForClarification(move)
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
    String enemyColour = move.getPiece().getColour().equals("white") ? "black" : "white";
    for (King king : Board.getKings()) {
      if (king.getColour().equals(enemyColour)) {
        inCheck = Board.getAllAttackedTiles(move.getPiece().getColour()).contains(new Tile(king.getX(), king.getY()));
      }
    }
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
    return String.valueOf((char) ('a' + x - 1));
  }

  private static int charToXCord(char x) {
    return x - 'a' + 1;
  }

  private static String pieceToNotationChar(Piece p) {
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

    public static String notationCharToPiece(char c) {
      return switch (c) {
        case 'N' -> "Knight";
        case 'B' -> "Bishop";
        case 'R' -> "Rook";
        case 'Q' -> "Queen";
        case 'K' -> "King";
        default  -> "Pawn";
      };
    }





}
