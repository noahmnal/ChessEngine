package GameLogic;

import Models.Board;
import Models.GameLogic;
import Pieces.Piece;
import Models.Tile;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Rook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
  public boolean playCpu = false;
  private static final int screenLength = 800;
  private static final int screenHeight = 800;

  private final Board board;
  private final GameLogic gameLogic;
  private Cpu cpu = null;


  public static final int tileSize = screenLength / 8;

  private int mouseX, mouseY;
  private Piece currentPieceMoving;
  public static String turn = "white";

  private boolean holdingPiece = false;


  public GamePanel(Board board, GameLogic gameLogic) throws InterruptedException {
    this.board = board;
    board.init();
    this.gameLogic = gameLogic;
    this.setPreferredSize(new Dimension(screenLength, screenHeight));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    addKeyListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    setFocusable(true);
    SwingUtilities.invokeLater(this::repaint);
  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);

  }
  public void draw(Graphics g) {
    for (Tile tile : board.getTiles()) {
      if (tile.getColour().equals("black")) {
        g.setColor(Color.RED);
        g.fillRect((tile.getX() - 1) * tileSize, (tile.getY() - 1) * tileSize, screenLength, screenHeight);
      } else {
        g.setColor(Color.WHITE);
        g.fillRect((tile.getX() - 1) * tileSize, (tile.getY() - 1) * tileSize, tileSize, tileSize);
      }
    }
    if (holdingPiece) {
      for (Tile tile : currentPieceMoving.getLegalTiles()){
        g.setColor(Color.GREEN);
        g.fillRect((tile.getX()-1) * tileSize, (tile.getY()-1) * tileSize, tileSize, tileSize);
      }
    }
    for (Tile tile : board.getTiles()) {
      if (tile.getPiece() != null) {
        if (tile.getPiece() instanceof King)
          if (((King) tile.getPiece()).getInCheck()) {
            g.setColor(Color.BLUE);
            g.fillRect((tile.getX()-1)*tileSize, (tile.getY()-1)*tileSize, tileSize, tileSize);
          }
        if (tile.getPiece().getColour().equals("white")) {
          g.drawImage(
                  tile.getPiece().getWhiteImage(),
                  (tile.getPiece().getX() - 1) * tileSize,
                  (tile.getPiece().getY() - 1) * tileSize,
                  tileSize,
                  tileSize,
                  null
          );
        } else
          g.drawImage(
                  tile.getPiece().getBlackImage(),
                  (tile.getPiece().getX() - 1) * tileSize,
                  (tile.getPiece().getY() - 1) * tileSize,
                  tileSize,
                  tileSize,
                  null
          );

      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_C) {
      if (!playCpu) {
        initCpu();
        playCpu = true;
      } else playCpu = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_Z) {
  board.undoMove();
  repaint();
    }
  }
  public void initCpu() {
    if (cpu == null)
      cpu = new Cpu(board, this);
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    System.out.println("Mouse pressed");
    mouseX = e.getX();
    mouseY = e.getY();
    currentPieceMoving = gameLogic.getMouseLocationPiece(mouseX, mouseY);
    try {
      if (currentPieceMoving.getColour().equals(turn)) {
        holdingPiece = true;
      }
    } catch (NullPointerException _) {
    }
    repaint();


  }

  @Override
  public void mouseReleased(MouseEvent e) {
    holdingPiece = false;
    System.out.println("Mouse released");
    mouseX = e.getX();
    mouseY = e.getY();
    int tileX = gameLogic.getMouseLocationTile(mouseX, mouseY).getX();
    int tileY = gameLogic.getMouseLocationTile(mouseX, mouseY).getY();
    try {
      if (turn.equals(currentPieceMoving.getColour())) {
        Tile chosenTile = new Tile(tileX, tileY);
        if(currentPieceMoving.getLegalTiles().contains(chosenTile)){
          makeMove(tileX, tileY, currentPieceMoving);
        }
      }
    } catch (NullPointerException _ ) {
    }


  repaint();
  }

  public void makeMove(int x, int y, Piece piece) {
    Tile chosenTile = new Tile(x, y);
    boolean moveMade = false;

    if (piece instanceof Pawn pawn &&
            pawn.enPassantPawn) {
      int sign = pawn.getColour().equals("white") ? 1 : -1;
      pawn.enPassantPawn = false;
      board.makeMove(new Move(piece.getX(), piece.getY(), x, y, piece, true, sign, null), false);
      board.deletePieceFromTile(x, y - sign);
      moveMade = true;
    } else if(currentPieceMoving instanceof King) {
      try {
        if (((King) currentPieceMoving).getCastlingTiles().containsKey(chosenTile)) {
          Rook chosenRook = ((King) currentPieceMoving).getCastlingTiles().get(chosenTile);
          if (chosenRook.getX() == 8) {
            board.makeMove(new Move(currentPieceMoving.getX(),
                    currentPieceMoving.getY(), x, y, currentPieceMoving, false,
                    -2, chosenRook), false);
          }
          else {
            board.makeMove(new Move(currentPieceMoving.getX(), currentPieceMoving.getY(),
                    x, y, currentPieceMoving, false, 3, chosenRook), false);
          }
          moveMade = true;
        }
      } catch (NullPointerException error) {
        System.out.println("no castling tiles");
      }
    }
    if (!moveMade) {
      Piece capturedPiece = Board.getCapturedPiece(x, y, piece, false, 0);
      board.makeMove(new Move(piece.getX(), piece.getY(), x, y, piece, capturedPiece), false);
      if (capturedPiece != null) {System.out.println(capturedPiece);}
      else {System.out.println("noooo");}

    }


    turn = gameLogic.switchTurn(turn);
    board.setChecksForKings();
    if(playCpu && turn.equals("black"))
      cpu.playMove();
    PositionRater.ratePosition(board.getPieces());
    repaint();
  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

  }
}