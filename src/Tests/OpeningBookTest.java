package Tests;
import static org.junit.jupiter.api.Assertions.*;

import Cpu.OpeningBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Models.Board;
import GameLogic.Move;
import Pieces.Knight;
import Pieces.Piece;

public class OpeningBookTest {

  @BeforeEach
  void setup() {
    Board.getPieces().clear();

    // g1
    Knight knightG1 = new Knight(6, 1, "white");
    // e2
    Knight knightE2 = new Knight(4, 1, "white");

    Board.addNewPiece(knightG1);
    Board.addNewPiece(knightE2);
  }

  @Test
  void shouldReturnFileWhenTwoKnightsCanMoveToSameSquare() {
    Piece movingKnight = Board.getPiece(6, 1); // g1
    Piece non = Board.getPiece(4, 1); // g2
    non.setAndGetLegalTiles();

    // f3
    Move move = new Move(
            6, 1,
            5, 3,
            movingKnight,
            null
    );



    String clarification = OpeningBook.checkForClarification(move);

    assertEquals("f", clarification);
  }
}

