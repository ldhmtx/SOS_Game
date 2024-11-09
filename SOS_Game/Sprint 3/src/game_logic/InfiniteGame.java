/**package game_logic;

import game_function.Board;

public class InfiniteGame extends SOSLogic {

    public InfiniteGame(Board board, int boardSize) {
        super(board, boardSize);
    }

    @Override
    public void onSquareClicked(int row, int col) {
        super.onSquareClicked(row, col);
        checkAndClearFilledRows();
    }

    // check all rows
    private void checkAndClearFilledRows() {
        for (int row = 0; row < getBoardSize(); row++) {
            if (isRowFilled(row)) {
                clearRow(row);
                incrementScore();
            }
        }
    }

    // check if a row is completely filled
    private boolean isRowFilled(int row) {
        for (int col = 0; col < getBoardSize(); col++) {
            if (getGameGrid()[row][col] == '\0') {
                return false;
            }
        }
        return true; 
    }

    // clear a row
    private void clearRow(int row) {
        for (int col = 0; col < getBoardSize(); col++) {
            getGameGrid()[row][col] = '\0'; 
            board.clearSquare(row, col); 
        }
        board.removeLinesForRow(row);
    }
}**/
