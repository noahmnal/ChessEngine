package GameLogic;

import Models.Board;
import Models.GameLogic;
import Pieces.Piece;
import Models.Tile;
import Pieces.King;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
  public boolean playCpu = false;
  private static final int screenLength = 800;
  private static final int screenHeight = 800;
  private final GameLogic gameLogic;
  private Cpu cpu = null;
  private String cpuColour;


  public static final int tileSize = screenLength / 8;

  private int mouseX, mouseY;
  private Piece currentPieceMoving;
  public static String turn = "white";

  private boolean holdingPiece = false;


  public GamePanel(GameLogic gameLogic) throws InterruptedException {
    Board.init();
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
    for (Tile tile : Board.getTiles()) {
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
    for (Piece piece : Board.getPieces()) {
        if (piece instanceof King king)
          if (king.getInCheck()) {
            g.setColor(Color.BLUE);
            g.fillRect((king.getX()-1)*tileSize, (king.getY()-1)*tileSize, tileSize, tileSize);
          }
        if (piece.getColour().equals("white")) {
          g.drawImage(
                  piece.getWhiteImage(),
                  (piece.getX() - 1) * tileSize,
                  (piece.getY() - 1) * tileSize,
                  tileSize,
                  tileSize,
                  null
          );
        } else
          g.drawImage(
                  piece.getBlackImage(),
                  (piece.getX() - 1) * tileSize,
                  (piece.getY() - 1) * tileSize,
                  tileSize,
                  tileSize,
                  null
          );
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
  Board.undoMove();
  repaint();
    }
  }
  public void initCpu() {
    if (cpu == null) {
      cpuColour = GameLogic.switchTurn(turn);
      cpu = new Cpu();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
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
    mouseX = e.getX();
    mouseY = e.getY();
    int tileX = gameLogic.getMouseLocationTile(mouseX, mouseY).getX();
    int tileY = gameLogic.getMouseLocationTile(mouseX, mouseY).getY();
    try {
      if (turn.equals(currentPieceMoving.getColour())) {
        Tile chosenTile = new Tile(tileX, tileY);
        if(currentPieceMoving.getLegalTiles().contains(chosenTile)) {
          Move move = GameLogic.createMove(tileX, tileY, currentPieceMoving);
          Board.makeMove(move, false);
          repaint();
          if(playCpu && turn.equals(cpuColour))
            cpu.playMove();
        }
      }
    } catch (NullPointerException _ ) {
    }


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