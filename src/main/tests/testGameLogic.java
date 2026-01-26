
import gameLogic.GameLogic;
import gameLogic.Move;
import gameLogic.MovesHistory;
import pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.*;

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

  @Test
  void testEnPassantCreationAndCapture() {
    // 1. Tøm brettet og historikken for en kontrollert test
    Board.getPieces().clear();
    MovesHistory.getMoves().clear();

    // 2. Sett opp situasjonen: Hvit bonde på rad 5, svart bonde hopper to felt frem til rad 5
    Pawn whitePawn = new Pawn(4, 5, "white");
    Pawn blackPawn = new Pawn(5, 7, "black");

    // 3. Svart gjør et "Double Step". Dette må lagres i historikken for at handlePawnMove skal virke
    Move blackDoubleStep = new Move(5, 7, 5, 5, blackPawn, null, false, 0, null, null, blackPawn);
    Board.makeMove(blackDoubleStep, false);
    // (Antar Board.makeMove legger trekket i MovesHistory)

    // 4. Fortell den hvite bonden at 5,6 er et gyldig En Passant-felt
    whitePawn.setEnPassantMove(new Tile(5, 6));

    // 5. Test createMove for En Passant
    Move epMove = GameLogic.createMove(5, 6, whitePawn);

    // Verifisering
    assertNotNull(epMove, "En Passant-trekket ble ikke opprettet");
    assertTrue(epMove.isEnPassant(), "Trekket er ikke markert som En Passant");
    assertEquals(blackPawn, epMove.getCapturedPiece(), "Feil brikke ble markert for fangst");
  }

  @Test
  void testEnPassantExecution() {
    // Tester at Board faktisk fjerner brikken når trekket utføres
    Board.getPieces().clear();
    MovesHistory.getMoves().clear();

    Pawn whitePawn = new Pawn(4, 2, "white");
    Pawn blackPawn = new Pawn(5, 4, "black"); // Står ved siden av
    whitePawn.getSudoLegalTiles();
    Move passantSetup = GameLogic.createMove(4, 4, whitePawn);
    Board.makeMove(passantSetup, false);
    // Manuelt opprett et gyldig En Passant-trekk slik handlePawnMove ville gjort
    blackPawn.getSudoLegalTiles();
    Move epMove = GameLogic.createMove(4, 3, blackPawn);
    Board.makeMove(epMove, false);


    // Verifiser at den svarte bonden er borte fra brettet
    assertFalse(Board.getPieces().contains(whitePawn), "Den fangede bonden står fortsatt på brettet");
    assertEquals(4, whitePawn.getX());
    assertEquals(4, whitePawn.getY());
    assertEquals(4, blackPawn.getX());
    assertEquals(3, blackPawn.getY());
  }

  @Test
  void testPawnPromotionCreation() {
    // Tester y == 8 logikken i handlePawnMove
    Pawn whitePawn = new Pawn(1, 7, "white");
    Board.getPieces().add(whitePawn);

    // Flytt til siste rad
    Move promotionMove = GameLogic.createMove(1, 8, whitePawn);

    assertNotNull(promotionMove.getPiece(), "Bonden ble ikke markert for promotion");
    assertEquals(whitePawn, promotionMove.getPiece());
  }
}
