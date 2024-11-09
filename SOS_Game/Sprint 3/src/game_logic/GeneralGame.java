/**package game_logic;

import game_function.Board;

public class GeneralGame extends SOSLogic {

    // Constructor
    public GeneralGame(Board board, int boardSize) {
        super(board, boardSize);
    }

    @Override
    public boolean isGameOver() {
    	if (isBoardFull()) {
    		gameOver();
    		return true;
    	}
    	return false;
    }
    
    protected void gameOver() {
    	if (getScorePlayer1() == getScorePlayer2()) {
    		board.updateTurnLabel("Draw Game!");
    	}
    	else {
    		board.updateTurnLabel("Game Over! " + (getScorePlayer1() > getScorePlayer2() ? "Player 1 Won!" : "Player 2 Won!"));
    	}
    } 
}**/