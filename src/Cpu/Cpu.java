package Cpu;

import Models.Board;
import Models.GameLogic;
import Pieces.Piece;
import Models.Tile;
import GameLogic.*;


public class Cpu {
  private int nodes;
  private boolean openingMoveFound = false;

  public Cpu() {
  }

  public void playMove() {
    System.out.println("Playing move");
    Move move = findBestMove();
     Board.makeMove(move, false);
     System.out.println(nodes);
     System.out.println(move);

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
            if (openingMoveFound) {
              return bestMove;
            }
          }
        }
      }
    }
    return bestMove;
  }

  public int minimax(int depth, boolean isWhiteTurn, int alpha, int beta) {
    if (openingMoveFound) {
      return -1000000;

    }
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
