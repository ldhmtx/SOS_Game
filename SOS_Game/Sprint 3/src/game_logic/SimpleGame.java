/**package game_logic;

import game_function.Board;

public class SimpleGame extends SOSLogic {

    // Constructor
    public SimpleGame(Board board, int boardSize) {
        super(board, boardSize);
    }

    @Override
    public boolean isGameOver() {
        if(getScorePlayer1() > 0 || getScorePlayer2() > 0 ||isBoardFull()) {
        	gameOver();
        	return true;
        }
        return false;
    }
    
    @Override
    protected void gameOver() {
    	if (isBoardFull() && getScorePlayer1() == getScorePlayer2()) {
    		board.updateTurnLabel("Draw Game!");
    	}
    	else {
    		board.updateTurnLabel("Game Over! " + (getScorePlayer1() > getScorePlayer2() ? "Player 1 Won!" : "Player 2 Won!"));
    	}
    } 
    
}**/
