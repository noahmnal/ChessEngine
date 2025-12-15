package Models;

import Pieces.King;
import GameLogic.GamePanel;
import java.util.ArrayList;

public class GameLogic {
  private final Board board;
  public GameLogic(Board board) {
    this.board = board;

  }
  public void setChecks() {
    for (King king : board.getKings()) {
      king.setCheckIfInCheck(getAllLegalMoves(GamePanel.turn));
    }
  }

  public Piece getMouseLocationPiece(int mouseX, int mouseY){
    for (Tile tile : board.getTiles()) {
      if (tile.getHitbox().contains(mouseX, mouseY) && tile.getPiece() != null) {
        return tile.getPiece();
      }
    }
    return null;
  }

  public Tile getMouseLocationTile(int mouseX, int mouseY){
    for (Tile tile : board.getTiles()) {
      if (tile.getHitbox().contains(mouseX, mouseY)) {
        return tile;
      }
    }
    return null;
  }

  public String switchTurn(String inTurn){
    String turn;
    if (inTurn.equals("white")) turn = "black";
    else turn = "white";
    return turn;
  }

  public ArrayList<Tile> getAllLegalMoves(String colour){
    ArrayList<Tile> legalMoves = new ArrayList<>();
    for (Tile tile : board.getTilesWithPieces()) {
      if (tile.getPiece() != null && tile.getPiece().getColor().equals(colour)) {
        legalMoves.addAll(tile.getPiece().getLegalTiles());
      }
    }
    return legalMoves;
  }
}
