package GameLogic;

import Models.Board;
import Pieces.Piece;
import Models.Tile;

import java.util.ArrayList;
import java.util.Random;

public class Cpu {
  private final GamePanel gamePanel;
  private Piece piece;
  private final Board board;

  public Cpu(Board board, GamePanel gamePanel) {
    this.board = board;
    this.gamePanel = gamePanel;
  }

  public void playMove() {
    ArrayList<Piece> pieces = board.getColouredPieces("black");
    Random rand = new Random();
    int random;
    do {
      piece = pieces.get(rand.nextInt(pieces.size()));
    } while (piece.getLegalTiles().isEmpty());
    ArrayList<Tile> legalMoves = piece.getLegalTiles();
    random = rand.nextInt(piece.getLegalTiles().size());
    Tile chosenTile = legalMoves.get(random);
    System.out.println("piece: " + piece + "tile: " + chosenTile);
    gamePanel.makeMove(chosenTile.getX(), chosenTile.getY(), piece);

  }
}
