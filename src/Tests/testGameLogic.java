package Tests;

import GameLogic.Move;
import Pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Models.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ai generated.
 */
public class testGameLogic {

  @BeforeEach
  void setup() {
    Board.getPieces().clear();
    Board.getTiles().clear();

    Board.whiteInCheck = false;
    Board.blackInCheck = false;

    Board.init();
  }

  @Test
  void testBoardInitialization() {
    assertEquals(64, Board.getTiles().size());
    assertEquals(32, Board.getPieces().size());
  }

  @Test
  void testGetPiece() {
    Piece pawn = Board.getPiece(1, 2);
    assertNotNull(pawn);
    assertInstanceOf(Pawn.class, pawn);
    assertEquals("white", pawn.getColour());
  }

  @Test
  void testMakeAndUndoMove() {
    Pawn pawn = (Pawn) Board.getPiece(2, 2);

    Move move = GameLogic.createMove(2, 4, pawn);
    Board.makeMove(move, false);

    assertEquals(2, pawn.getX());
    assertEquals(4, pawn.getY());

    Board.undoMove();

    assertEquals(2, pawn.getX());
    assertEquals(2, pawn.getY());
  }


  @Test
  void testCapturePiece() {
    Pawn whitePawn = (Pawn) Board.getPiece(4, 2);
    Pawn blackPawn = (Pawn) Board.getPiece(5, 7);

    Move blackMove = GameLogic.createMove(5, 5, blackPawn);
    Board.makeMove(blackMove, false);

    Move whiteMove = GameLogic.createMove(4, 4, whitePawn);
    Board.makeMove(whiteMove, false);

    Move capture = GameLogic.createMove(5, 5, whitePawn);
    Board.makeMove(capture, false);

    assertFalse(Board.getPieces().contains(blackPawn));
  }


  @Test
  void testEnPassantDoesNotCrash() {
    Board.getPieces().clear();

    Pawn whitePawn = new Pawn(4, 5, "white");
    Pawn blackPawn = new Pawn(5, 7, "black");


    Move blackMove = new Move(
            5, 7, 5, 5,
            blackPawn, null,
            false, 0,
            null, null, blackPawn
    );

    Board.makeMove(blackMove, false);

    whitePawn.setEnPassantMove(new Tile(5, 6));

    assertDoesNotThrow(() -> {
      Move epMove = GameLogic.createMove(5, 6, whitePawn);
      assertTrue(epMove.isEnPassant());
      assertInstanceOf(Pawn.class, epMove.getCapturedPiece());
    });
  }


  @Test
  void testCastlingMove() {
    King king = (King) Board.getPiece(5, 1);
    Rook rook = (Rook) Board.getPiece(8, 1);

    Board.getPieces().removeIf(p ->
            (p.getX() == 6 && p.getY() == 1) ||
                    (p.getX() == 7 && p.getY() == 1)
    );

    king.getSudoLegalTiles();

    assertEquals(1, king.getCastlingTiles().size());

    Move castleMove = GameLogic.createMove(7, 1, king);
    Board.makeMove(castleMove, false);

    assertEquals(7, king.getX());
    assertEquals(6, rook.getX());
    assertTrue(king.hasCastled);

    king.getSudoLegalTiles();
    assertEquals(0, king.getCastlingTiles().size());
  }

  @Test
  void testKingInCheck() {
    Board.getPieces().clear();

    King whiteKing = new King(5, 1, "white");
    Rook blackRook = new Rook(5, 8, "black");


    Board.setChecksForKings();

    assertTrue(Board.whiteInCheck);
    assertFalse(Board.blackInCheck);
  }
}
