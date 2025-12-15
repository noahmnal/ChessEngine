package GameLogic;

import Models.Board;
import Models.GameLogic;
import Models.Piece;
import Models.Tile;
import Pieces.King;
import Pieces.Rook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {

  private static final int screenLenght = 800;
  private static final int screenHeight = 800;

  private final Board board;
  private final GameLogic gameLogic;


  public static final int tileSize = screenLenght / 8;

  private int mouseX, mouseY;
  private Piece currentPieceMoving;
  public static String turn = "white";

  private boolean holdingPiece = false;


  public GamePanel(Board board, GameLogic gameLogic) {
    this.board = board;
    this.gameLogic = gameLogic;
    this.setPreferredSize(new Dimension(screenLenght, screenHeight));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    addKeyListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    setFocusable(true);


    Timer gameloop = new Timer(100, this);
    gameloop.start();

  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);
    draw(g);

  }
  public void draw(Graphics g) {
    for (Tile tile : board.getTiles()) {
      if (tile.getColour().equals("black")) {
        g.setColor(Color.RED);
        g.fillRect((tile.getX() - 1) * tileSize, (tile.getY() - 1) * tileSize, screenLenght, screenHeight);
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
          g.drawImage(tile.getPiece().getWhiteImage(), (tile.getPiece().getX() - 1) * tileSize, (tile.getPiece().getY() - 1) * tileSize, null);
        } else
          g.drawImage(tile.getPiece().getBlackImage(), (tile.getPiece().getX() - 1) * tileSize, (tile.getPiece().getY() - 1) * tileSize, null);
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
    holdingPiece = true;


  }

  @Override
  public void mouseReleased(MouseEvent e) {
    holdingPiece = false;
    System.out.println("Mouse released");
    mouseX = e.getX();
    mouseY = e.getY();
    int tileX = gameLogic.getMouseLocationTile(mouseX, mouseY).getX();
    int tileY = gameLogic.getMouseLocationTile(mouseX, mouseY).getY();
    if (turn.equals(currentPieceMoving.getColour())) {
      Tile chosenTile = new Tile(tileX, tileY);
      if(currentPieceMoving.getLegalTiles().contains(chosenTile)){
        if(currentPieceMoving instanceof King)
          try {

            if (((King) currentPieceMoving).getCastlingTiles().containsKey(chosenTile)) {
              Rook chosenRook = ((King) currentPieceMoving).getCastlingTiles().get(chosenTile);
              if (chosenRook.getX() == 8)
                board.setPos(chosenRook.getX() - 2, chosenRook.getY(), chosenRook);
              else
                board.setPos(chosenRook.getX() + 3, chosenRook.getY(), chosenRook);
            }
          } catch (NullPointerException error) {
            System.out.println("no castling tiles");
          }
        board.setPos(tileX, tileY, currentPieceMoving);
        turn = gameLogic.switchTurn(turn);
        board.setChecksForKings();
      }
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