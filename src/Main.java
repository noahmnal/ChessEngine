import GameLogic.GamePanel;
import Models.Board;
import Models.GameLogic;

import javax.swing.JFrame;




void main() throws InterruptedException {
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("Chess");
    window.setLocation(200, 0);
    window.setVisible(true);
    GameLogic gameLogic = new GameLogic();

    GamePanel game_window = new GamePanel(gameLogic);
    window.add(game_window);
    window.pack();
    game_window.requestFocus();


  }
