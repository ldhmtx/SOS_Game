/**package game_function;

import javafx.scene.paint.Color;
import game_logic.SOSLogic;
import java.util.Random;

public class Computer extends Player {
    private SOSLogic logic;
    private Random random = new Random();

    public Computer(String name, Color color) {
        super(name, color);
    }

    public void setLogic(SOSLogic logic) {
        this.logic = logic;
    }

    @Override
    public String getChoice() {
        int boardSize = logic.getBoardSize();
        char[][] gameGrid = logic.getGameGrid();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (gameGrid[row][col] == '\0') {  // if square is empty
                    if (logic.compSOSFinder(row, col, 'O')) { // try placing an o (returns true at first found)
                        logic.onSquareClicked(row, col);
                        return "comp";
                    }
                    if (logic.compSOSFinder(row, col, 'S')) { // try placing a s (returns true at first found)
                        logic.onSquareClicked(row, col);
                        return "comp";
                    }
                }
            }
        }
        getRandomMove(); // none found
        return "comp";
    }

    // if none found
    private void getRandomMove() {
        int size = logic.getBoardSize();
        while (true) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            if (logic.getGameGrid()[row][col] == '\0') {
                logic.onSquareClicked(row, col);
                return;
            }
        }
    }
}**/
