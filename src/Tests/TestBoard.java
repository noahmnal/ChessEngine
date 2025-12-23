package Tests;
import GameLogic.*;
import Models.Board;
import Pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Models.*;

import static org.junit.jupiter.api.Assertions.*;

class TestBoard {

  @BeforeEach
  void setUp() {
    Board.getTiles().clear();
    Board.getPieces().clear();

    Board.whiteInCheck = false;
    Board.blackInCheck = false;

    GamePanel.turn = "white";

    Board.init();
  }
  @Test
  void castlingNotAllowedIfKingHasMovedBefore() {
    Board.getPieces().removeIf(p ->
            (p.getX() == 6 && p.getY() == 1) ||
                    (p.getX() == 7 && p.getY() == 1)
    );

    King king = (King) Board.getPiece(5, 1);

    king.setHaveMoved(true);

    king.getSudoLegalTiles();

    assertEquals(0, king.getCastlingTiles().size(),
            "Castling should not be allowed after king has moved");
  }

  @Test
  void castlingNotAllowedIfRookHasMovedBefore() {
    Board.getPieces().removeIf(p ->
            (p.getX() == 6 && p.getY() == 1) ||
                    (p.getX() == 7 && p.getY() == 1)
    );

    King king = (King) Board.getPiece(5, 1);
    Rook rook = (Rook) Board.getPiece(8, 1);

    rook.setHaveMoved(true);

    king.getSudoLegalTiles();

    assertEquals(0, king.getCastlingTiles().size(),
            "Castling should not be allowed after rook has moved");
  }

  @Test
  void undoCastlingRestoresKingAndRook() {
    Board.getPieces().removeIf(p ->
            (p.getX() == 6 && p.getY() == 1) ||
                    (p.getX() == 7 && p.getY() == 1)
    );

    King king = (King) Board.getPiece(5, 1);
    Rook rook = (Rook) Board.getPiece(8, 1);

    king.getSudoLegalTiles();
    Move castleMove = GameLogic.createMove(7, 1, king);

    Board.makeMove(castleMove, false);
    Board.undoMove();

    assertEquals(5, king.getX());
    assertEquals(1, king.getY());
    assertEquals(8, rook.getX());

    assertFalse(king.hasCastled);
    assertTrue(king.getHaveNotMoved());
  }

  @Test
  void castlingTilesReappearAfterUndo() {
    Board.getPieces().removeIf(p ->
            (p.getX() == 6 && p.getY() == 1) ||
                    (p.getX() == 7 && p.getY() == 1)
    );

    King king = (King) Board.getPiece(5, 1);

    king.getSudoLegalTiles();
    assertEquals(1, king.getCastlingTiles().size());

    Move castleMove = GameLogic.createMove(7, 1, king);
    Board.makeMove(castleMove, false);
    Board.undoMove();

    king.getSudoLegalTiles();
    assertEquals(1, king.getCastlingTiles().size(),
            "Castling tiles should reappear after undo");
  }

  @Test
  void capturedPawnIsRemovedFromBoard() {
    Board.getPieces().clear();

    Pawn whitePawn = new Pawn(4, 4, "white");
    Pawn blackPawn = new Pawn(5, 5, "black");

    Move capture = GameLogic.createMove(5, 5, whitePawn);
    Board.makeMove(capture, false);


    assertFalse(Board.getPieces().contains(blackPawn),
            "Captured pawn should be removed from pieces list");
  }

  @Test
  void enPassantCaptureDoesNotLeaveGhostPawn() {
    Board.getPieces().clear();

    Pawn whitePawn = new Pawn(4, 5, "white");
    Pawn blackPawn = new Pawn(5, 7, "black");

    // black pawn double step
    Move blackMove = new Move(
            5, 7, 5, 5,
            blackPawn, null,
            false, 0,
            null, null, blackPawn
    );
    Board.makeMove(blackMove, false);
    assertTrue(true , String.valueOf(whitePawn.getSudoLegalTiles().contains(new Tile(5, 6))));
    Move enPassant = GameLogic.createMove(5, 6, whitePawn);
    assertEquals(2 ,Board.getPieces().size());
    Board.makeMove(enPassant, false);

    assertNull(Board.getPiece(5, 5),
            "En passant captured pawn must be removed from original square");

    assertFalse(Board.getPieces().contains(blackPawn),
            "Captured pawn must be removed from pieces list");
  }

  @Test
  void simulateMoveDoesNotCreateGhostPawn() {
    Pawn whitePawn = (Pawn) Board.getPiece(4, 2);
    Pawn blackPawn = (Pawn) Board.getPiece(5, 7);
    Board.makeMove(GameLogic.createMove(5, 5, blackPawn), false);

    Tile captureTile = new Tile(5, 5);

    boolean legal = Board.simulateIsMoveLegal(whitePawn, captureTile);
    assertTrue(legal);

    assertEquals(whitePawn, Board.getPiece(4, 2),
            "White pawn must remain on original square after simulation");

    assertEquals(blackPawn, Board.getPiece(5, 5),
            "Black pawn must remain after simulation");
  }

  @Test
  void undoAfterCaptureRestoresPawnCorrectly() {
    Board.getPieces().clear();

    Pawn whitePawn = new Pawn(4, 4, "white");
    Pawn blackPawn = new Pawn(5, 5, "black");


    Move capture = GameLogic.createMove(5, 5, whitePawn);
    Board.makeMove(capture, false);

    Board.undoMove();

    assertEquals(5, blackPawn.getX());
    assertEquals(5, blackPawn.getY());

    assertTrue(Board.getPieces().contains(blackPawn),
            "Captured pawn should be restored after undo");
  }

  @Test
  void noTwoPiecesMayOccupySameTile() {
    for (Piece a : Board.getPieces()) {
      for (Piece b : Board.getPieces()) {
        if (a != b) {
          assertFalse(
                  a.getX() == b.getX() && a.getY() == b.getY(),
                  "Two pieces occupy same square: " +
                          a.getClass() + " & " + b.getClass() +
                          " at " + a.getX() + "," + a.getY()
          );
        }
      }
    }
  }


}


