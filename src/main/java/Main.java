import cpu.OpeningBook;
import gameLogic.GamePanel;
import gameLogic.GameLogic;

import javax.swing.JFrame;




void main() throws Exception {
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("Chess");
    window.setLocation(200, 0);
    window.setVisible(true);
    GameLogic gameLogic = new GameLogic();
    OpeningBook.init();

    GamePanel game_window = new GamePanel(gameLogic);
    window.add(game_window);
    window.pack();
    game_window.requestFocus();


  }
