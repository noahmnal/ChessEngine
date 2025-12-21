package GameLogic;

import Models.Board;
import Pieces.Piece;
import Models.Tile;

import java.util.*;

public class Cpu {
  private final GamePanel gamePanel;
  private final Board board;
  private int nodes;

  public Cpu(Board board, GamePanel gamePanel) {
    this.board = board;
    this.gamePanel = gamePanel;
  }

  public void playMove() {
    System.out.println("Playing move");
    Move move = findBestMove();
     board.makeMove(move, false);
     System.out.println(nodes);

  }

  public Move findBestMove() {
    nodes = 0;
    int bestScore;
    Move bestMove = null;
    if (GamePanel.turn.equals("white"))
      bestScore = Integer.MIN_VALUE;
    else
      bestScore = Integer.MAX_VALUE;
    for (Piece piece : board.getColouredPieces(GamePanel.turn)) {
      for (Tile tile : piece.getLegalTiles()) {
        Move move = gamePanel.createMove(tile.getX(), tile.getY(), piece);
        board.makeMove(move, true);
        int score = minimax(2, GamePanel.turn.equals("white"));
        board.reverseMove(move, true);
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

  public int minimax(int depth, boolean isWhiteTurn) {
    nodes++;
    if (depth == 0) {
      return PositionRater.ratePosition(board.getPieces());
    }
    if (isWhiteTurn) {
      int higestScore = Integer.MIN_VALUE;
    for (Piece piece : board.getColouredPieces("white")) {
      for (Tile tile : piece.getSudoLegalTiles()) {
        Move move = new Move(piece.getX(), piece.getY(), tile.getX(), tile.getY(),
                piece, board.getPiece(tile.getX(), tile.getY()));
        board.makeMove(move, true);
        int score = minimax(depth - 1, false);
        board.reverseMove(move, true);
        higestScore = Math.max(higestScore, score);
      }
    }
    return higestScore;
    } else {
      int lowestScore = Integer.MAX_VALUE;
      for (Piece piece : board.getColouredPieces("black")) {
        for (Tile tile : piece.getSudoLegalTiles()) {
          Move move = gamePanel.createMove(tile.getX(), tile.getY(), piece);
          board.makeMove(move, true);
          int score = minimax(depth - 1, true);
          board.reverseMove(move, true);
          lowestScore = Math.min(lowestScore, score);
        }
      }
      return lowestScore;
    }
  }
}
