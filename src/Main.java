import GameLogic.GamePanel;
import Models.Board;
import Models.GameLogic;

import javax.swing.JFrame;




void main() {
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("Chess");
    window.setLocation(200, 0);
    window.setVisible(true);
    Board board = new Board();
    GameLogic gameLogic = new GameLogic(board);

    GamePanel game_window = new GamePanel(board,  gameLogic);
    window.add(game_window);
    window.pack();
    game_window.requestFocus();


  }
