package Cpu;

import Models.Board;
import Models.GameLogic;
import Pieces.Pawn;
import Pieces.Piece;
import Models.Tile;
import GameLogic.*;

import java.util.*;

import static GameLogic.GamePanel.cpuColour;
import static GameLogic.GamePanel.sanMoveHistory;


public class Cpu {
  private int nodes;
  private boolean opening = true;

  public Cpu() {
  }

  public void playMove() {
    int debugger = Board.getPieces().size();
    if (opening) {
      System.out.println("test");
      String nextMove = OpeningBook.findMoveInBook(sanMoveHistory.toArray(new String[0]), OpeningBook.gmGames);
      System.out.println("is here");
      if (nextMove.isEmpty()) {
        if (cpuColour.equals("white")) {
          nextMove = OpeningBook.findMoveInBook(sanMoveHistory.toArray(new String[0]), OpeningBook.noobOpeningsWhite);
          System.out.println("from nobbboom" + nextMove);
        } else{
          nextMove = OpeningBook.findMoveInBook(sanMoveHistory.toArray(new String[0]), OpeningBook.noobOpeningsBlack);
          System.out.println("from nobbboom" + nextMove);
        }
      }
      if (nextMove.isEmpty()) {
        opening = false;
      } else {
        if (debugger == Board.getPieces().size()) {
          for (int i = 0; i < 1000; i++) {
            System.out.println("test");
          }
        }
        Board.makeMove(OpeningBook.notationToMove(nextMove, GamePanel.turn), false);
        return;
      }
    }
    Move move = findBestMove();
     Board.makeMove(move, false);
  }

  public Move findBestMove() {
    nodes = 0;
    int bestScore;
    Move bestMove = null;
    if (GamePanel.turn.equals("white"))
      bestScore = Integer.MIN_VALUE;
    else
      bestScore = Integer.MAX_VALUE;
    for (Piece piece : Board.getColouredPieces(GamePanel.turn)) {
      for (Tile tile : piece.setAndGetLegalTiles()) {
        Move move = GameLogic.createMove(tile.getX(), tile.getY(), piece);
        Board.makeMove(move, true);
        int score = minimax(3, GamePanel.turn.equals("white"), -100000, 100000, move);
        Board.reverseMove(move, true);
        if (GamePanel.turn.equals("white")) {
          if (score > bestScore) {
            bestScore = score;
            bestMove = move;
          }
        } else  {
          if (score < bestScore) {
            bestScore = score;
            bestMove = move;
          }
        }
      }
    }
    return bestMove;
  }

  private int exchangeEvaluation(Move move) {
    PriorityQueue<Piece> attackingPieces = new PriorityQueue<>(Comparator.comparingInt(p -> p.value));
    PriorityQueue<Piece> defendingPieces = new PriorityQueue<>(Comparator.comparingInt(p -> p.value));


    int[] scores = new int[32];
    int depth = 0;

    Piece currentAttacker = move.getPiece();

    scores[depth] = 0;

    while (!attackingPieces.isEmpty()) {
      depth++;
      scores[depth] = scores[depth - 1] - currentAttacker.value;
      currentAttacker = attackingPieces.poll();

      if (defendingPieces.isEmpty()) break;

      depth++;
      scores[depth] = scores[depth - 1] + currentAttacker.value;
      currentAttacker = defendingPieces.poll();
    }

    while (depth > 0) {

      scores[depth - 1] = Math.min(scores[depth - 1], -scores[depth]);
      depth--;
    }

    return scores[0];
  }

  public int minimax(int depth, boolean isWhiteTurn, int alpha, int beta, Move lastMove) {
    nodes++;
    if (depth == 0) {
      int boardScore = PositionRater.ratePosition(Board.getPieces());
      if (lastMove.getCapturedPiece() != null) {
        int seeValue = exchangeEvaluation(lastMove);

        if (!isWhiteTurn) {
          return boardScore + seeValue;
        } else {
          return boardScore - seeValue;
        }
      }
      return boardScore;
    }
    if (isWhiteTurn) {
      int higestScore = Integer.MIN_VALUE;
    for (Piece piece : Board.getColouredPieces("white")) {
      for (Tile tile : piece.getSudoLegalTiles()) {
        Move move = GameLogic.createMove(tile.getX(), tile.getY(), piece);
        Board.makeMove(move, true);
        int score = minimax(depth - 1, false,  alpha, beta, move);
        Board.reverseMove(move, true);
        higestScore = Math.max(higestScore, score);
        alpha = Math.max(alpha, higestScore);
        if (beta <= alpha) {
          return higestScore;
        }

        }
    }
    return higestScore;
    } else {
      int lowestScore = Integer.MAX_VALUE;
      for (Piece piece : Board.getColouredPieces("black")) {
        for (Tile tile : piece.getSudoLegalTiles()) {
          Move move = GameLogic.createMove(tile.getX(), tile.getY(), piece);
          Board.makeMove(move, true);
          int score = minimax(depth - 1, true, alpha, beta, move);
          Board.reverseMove(move, true);
          lowestScore = Math.min(lowestScore, score);
          beta = Math.min(beta, score);
          if (beta <= alpha) {
            return lowestScore;
          }
        }
      }
      return lowestScore;
    }
  }
}
