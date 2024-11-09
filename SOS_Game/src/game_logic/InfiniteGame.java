package game_logic;

import game_function.Board;

public class InfiniteGame extends SOSLogic {

    public InfiniteGame(Board board, int boardSize) {
        super(board, boardSize);
    }

    @Override
    public void onSquareClicked(int row, int col, String letter) {
        super.onSquareClicked(row, col, letter);
        checkAndClearFilledRows();
        checkAndClearFilledColumns();
    }

    // check all rows
    private void checkAndClearFilledRows() {
        for (int row = 0; row < getBoardSize(); row++) {
            if (isRowFilled(row)) {
                clearRow(row);
            }
        }
    }

    // check all columns
    private void checkAndClearFilledColumns() {
        for (int col = 0; col < getBoardSize(); col++) {
            if (isColumnFilled(col)) {
                clearColumn(col);
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

    // check if a column is completely filled
    private boolean isColumnFilled(int col) {
        for (int row = 0; row < getBoardSize(); row++) {
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
            board.clearSquareRow(row, col);
        }
        removeLinesForRow(row);
        board.getCurrentPlayer().incrementScore(); // if a player clears a row, they get bonus points
    }

    // clear a column
    private void clearColumn(int col) {
        for (int row = 0; row < getBoardSize(); row++) {
            getGameGrid()[row][col] = '\0';
            board.clearSquareRow(row, col);
        }
        removeLinesForColumn(col);
        board.getCurrentPlayer().incrementScore(); // if a player clears a row, they get bonus points
    }
    
 // remove lines in a cleared row
    public void removeLinesForRow(int row) {
        double y = row * board.getSquareSize();
        board.getDrawnLines().removeIf(line -> {
            boolean intersects = (line.getStartY() <= y + board.getSquareSize() && line.getEndY() >= y);
            if (intersects) {
                board.getDrawingPane().getChildren().remove(line);
            }
            return intersects;
        });
    }
    
    private void removeLinesForColumn(int col) {
        double x = col * board.getSquareSize(); // x-coordinate of the column to be cleared
        board.getDrawnLines().removeIf(line -> {
            boolean intersects = (line.getStartX() <= x + board.getSquareSize() && line.getEndX() >= x);
            if (intersects) {
                board.getDrawingPane().getChildren().remove(line);
            }
            return intersects;
        });
    }
}