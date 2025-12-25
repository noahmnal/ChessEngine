package GameLogic;

import Cpu.*;
import Models.Board;
import Models.GameLogic;
import Pieces.Piece;
import Models.Tile;
import Pieces.King;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
  public boolean playCpu = false;
  public static final int screenLength = 760;
  public static final int screenHeight = 760;
  private static final int screenLengthAddOn = 50;
  private static final int screenHeightAddOn = 50;
  public static final ArrayList<String> sanMoveHistory = new ArrayList<>();

  private final GameLogic gameLogic;
  private Cpu cpu = null;
  public static String cpuColour;

  public static final int tileSize = screenLength / 8;

  private int mouseX, mouseY;
  private Piece currentPieceMoving;
  public static String turn = "white";
  private int currentPositionRating = 0;

  private static boolean holdingPiece = false;
  public static boolean flipScreen = true;


  public GamePanel(GameLogic gameLogic) throws InterruptedException {
    Board.init();
    this.gameLogic = gameLogic;
    this.setPreferredSize(new Dimension(screenLength+screenLengthAddOn, screenHeight+screenHeightAddOn));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    addKeyListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    setFocusable(true);
    SwingUtilities.invokeLater(this::repaint);
  }

  public void flipScreen() {
    flipScreen = !flipScreen;
    for (Tile tile : Board.getTiles()) {
      tile.setTileHitbox(flipScreen);
    }
    repaint();

  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);
  }
  public void draw(Graphics g) {
    for (Tile tile : Board.getTiles()) {
      if (tile.getColour().equals("black")) {
        g.setColor(Color.RED);
        g.fillRect((tile.getX() - 1) * tileSize, screenHeight-(tile.getY() * tileSize), tileSize, tileSize);
      } else {
        g.setColor(Color.WHITE);
        g.fillRect((tile.getX() - 1) * tileSize, screenHeight-(tile.getY() * tileSize), tileSize, tileSize);
      }
    }
    if (holdingPiece) {
      for (Tile tile : currentPieceMoving.setAndGetLegalTiles()){
        g.setColor(Color.GREEN);
        if (flipScreen)
          g.fillRect((tile.getX()-1) * tileSize, (8-tile.getY()) * tileSize, tileSize, tileSize);
        else
          g.fillRect((tile.getX()-1) * tileSize, (tile.getY()-1) * tileSize, tileSize, tileSize);

      }
    }
    for (Piece piece : Board.getPieces()) {
        if (piece instanceof King king)
          if (king.getInCheck()) {
            g.setColor(Color.BLUE);
            if (flipScreen)
              g.fillRect((king.getX()-1)*tileSize, (8-king.getY())*tileSize, tileSize, tileSize);
            else g.fillRect((king.getX()-1)*tileSize, (king.getY()-1)*tileSize, tileSize, tileSize);
          }
        if (piece.getColour().equals("white")) {
          if (flipScreen)
            g.drawImage(piece.getWhiteImage(), (piece.getX() - 1) * tileSize,
                    (8 - piece.getY()) * tileSize, tileSize, tileSize, null);
          else g.drawImage(piece.getWhiteImage(), (piece.getX() - 1) * tileSize,
                  (piece.getY()-1) * tileSize, tileSize, tileSize, null);
        } else
          if (flipScreen)
            g.drawImage(piece.getBlackImage(), (piece.getX() - 1) * tileSize,
                    (8-piece.getY() ) * tileSize, tileSize, tileSize, null);

          else g.drawImage(piece.getBlackImage(), (piece.getX() - 1) * tileSize,
                  (piece.getY() -1) * tileSize, tileSize, tileSize, null);
      }

    g.setColor(Color.BLUE);
    g.setFont(new Font("Arial", Font.BOLD, 30));
    g.drawString(String.valueOf(currentPositionRating), screenLength-(screenHeightAddOn/2), screenHeight-(screenHeightAddOn)/2);
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
        if(currentPieceMoving.setAndGetLegalTiles().contains(chosenTile)) {
          Move move = GameLogic.createMove(tileX, tileY, currentPieceMoving);
          Board.makeMove(move, false);
          repaint();
          currentPositionRating = PositionRater.ratePosition(Board.getPieces());
          //flipScreen();
          if(playCpu && turn.equals(cpuColour)) {
            cpu.playMove();
          }
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