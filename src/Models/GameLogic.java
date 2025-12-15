package Models;

import Pieces.Piece;

public class GameLogic {
  private final Board board;
  public GameLogic(Board board) {
    this.board = board;

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

}
