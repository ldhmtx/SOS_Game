package game_logic;

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
    	board.getPlayer1().disable();
    	board.getPlayer2().disable();
    	int score1 = board.getPlayer1().getPlayerScore();
    	int score2 = board.getPlayer2().getPlayerScore();
    	
    	if (score1 == score2) {
    		board.updateTurnLabel("Draw Game!");
    	}
    	else {
    		board.updateTurnLabel("Game Over! " + (score1 > score2 ? board.getPlayer1().getName()+" Won!" : board.getPlayer1().getName()+" Won!"));
    	}
    } 
}