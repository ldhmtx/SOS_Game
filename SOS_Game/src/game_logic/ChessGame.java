package game_logic;

import game_function.Board;

public class ChessGame extends SOSLogic {
	
	public ChessGame(Board board, int boardSize) {
        super(board, boardSize);
    }
	
	@Override
    public boolean isGameOver() {
        if(board.getPlayer1().getPlayerScore() > 0 || board.getPlayer2().getPlayerScore() > 0 ||isBoardFull()) {
        	gameOver();
        	return true;
        }
        return false;
    }
    
    @Override
    public void gameOver() {
    	board.getPlayer1().disable();
    	board.getPlayer2().disable();
    	int score1 = board.getPlayer1().getPlayerScore();
    	int score2 = board.getPlayer2().getPlayerScore();
    	if (isBoardFull() && (score1 == score2)) {
    		board.updateTurnLabel("Draw Game!");
    	}
    	else {
    		board.updateTurnLabel("Game Over! " + (score1 > score2 ? board.getPlayer1().getName()+" Won!" : board.getPlayer2().getName()+" Won!"));
    	}
    } 
	
}
