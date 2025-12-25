package Cpu;

import Models.Board;
import Models.GameLogic;
import Pieces.Piece;
import Models.Tile;
import GameLogic.*;

import static GameLogic.GamePanel.sanMoveHistory;


public class Cpu {
  private int nodes;
  private boolean opening = true;

  public Cpu() {
  }

  public void playMove() {
    if (opening) {
      String nextMove = OpeningBook.findMoveInBook(sanMoveHistory.toArray(new String[0]), OpeningBook.gmGames);
      if (nextMove.isEmpty()) {
        nextMove = OpeningBook.findMoveInBook(sanMoveHistory.toArray(new String[0]), OpeningBook.noobOpenings);
        System.out.println("from nobbboom" + nextMove);
      }

      if (nextMove.isEmpty()) {
        opening = false;
      } else {
        Board.makeMove(OpeningBook.notationToMove(nextMove, GamePanel.turn), false);
        return;
      }
    }
    Move move = findBestMove();
     Board.makeMove(move, false);
     //System.out.println(nodes);
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
        int score = minimax(3, GamePanel.turn.equals("white"), -100000, 100000);
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

  public int minimax(int depth, boolean isWhiteTurn, int alpha, int beta) {
    int gameTurn = (MovesHistory.getMoves().size()/2+1);
    nodes++;
    if (depth == 0) {
      return PositionRater.ratePosition(Board.getPieces());
    }
    if (isWhiteTurn) {
      int higestScore = Integer.MIN_VALUE;
    for (Piece piece : Board.getColouredPieces("white")) {
      for (Tile tile : piece.getSudoLegalTiles()) {
        Move move = GameLogic.createMove(tile.getX(), tile.getY(), piece);
        Board.makeMove(move, true);
        int score = minimax(depth - 1, false,  alpha, beta);
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
          if (gameTurn == 3) {
          }
          int score = minimax(depth - 1, true, alpha, beta);
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
