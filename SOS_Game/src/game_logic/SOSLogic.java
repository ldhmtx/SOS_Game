package game_logic;

import java.util.ArrayList;
import java.util.List;

import game_function.Board;
import game_function.Player;

public class SOSLogic {
	
    protected Board board;
    private char[][] gameGrid;
    private int boardSize;
    private boolean isPlayer1Turn = true;

    private int scorePlayer1;
    private int scorePlayer2;

    // inherited constructor
    public SOSLogic(Board board, int boardSize) {
        this.board = board;
        this.setBoardSize(boardSize);
        this.setGameGrid(new char[boardSize][boardSize]);
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
    }
    
    // overriden by child classes
    protected void gameOver() {
    }
    
    // when player clicks a square, if it is empty and the game is not over, add to board and determine if sos
    public void onSquareClicked(int row, int col) {
        if (getGameGrid()[row][col] == '\0' && !isGameOver()) {
        	
            Player currentPlayer = isPlayer1Turn ? board.getPlayer1() : board.getPlayer2();
            String letter = currentPlayer.getChoice();
            getGameGrid()[row][col] = letter.charAt(0); 
            board.updateSquare(row, col, letter, currentPlayer.getColor());
            List<int[]> allFoundSOSsList = findSOSs(row, col);
            
            // for all found soss
            if (allFoundSOSsList != null && !allFoundSOSsList.isEmpty()) {
                for (int[] coords : allFoundSOSsList) { // for any number SOSs
                	drawSOSLine(coords[0], coords[1], coords[2], coords[3]); 
                    incrementScore(); 
                }
            }
            //check if game is over, otherwise hand over control to next player
            if (isGameOver()) {
                gameOver();
            } else {
                switchPlayer();
            }
        } 
        else { // player tried to place when/where they couldn't, game is either over or space is occupied
        	board.updateTurnLabel(isGameOver() ? "The game is over!" : "You cannot place a letter there!");
        }
        
    }
    
    // if all spaces are filled on board
    protected boolean isBoardFull() {
        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                if (getGameGrid()[row][col] == '\0') {
                    return false; // if square empty return false on first one found
                }
            }
        }
        return true;
    }
    
    // increments score for current player and update labels
    public void incrementScore() {
        if (isPlayer1Turn) {
            scorePlayer1++;
            board.updateScoreLabel(scorePlayer1);
        } else {
            scorePlayer2++;
            board.updateScoreLabel(scorePlayer2);
        }
    }
    
    // disable moves if game is over or if not player's turn
    private void disableMoves() {
        if (isGameOver()) {
            board.getPlayer1().disable();
            board.getPlayer2().disable();
        } else {
            if (isPlayer1Turn()) {           // player 1's turn
                board.getPlayer1().enable();
                board.getPlayer2().disable();
            } else {                         // player 2's turn
                board.getPlayer1().disable();
                board.getPlayer2().enable();
            }
        }
    }
    
    // overriden by child classes (except infinite game, which would return false regardless)
    public boolean isGameOver() {
		return false;
	}

    // find start and end points and send to board to draw line
    private void drawSOSLine(int startRow, int startCol, int endRow, int endCol) {
        double squareSize = board.getSquareSize();
        double offset = squareSize * -0.15*(getBoardSize()/3);  // offset because for some reason the line isn't centered without it

        // find where the line needs to start and end
        double startX = startCol * squareSize + (squareSize / 2) - offset;
        double startY = startRow * squareSize + (squareSize / 2) - offset;
        double endX = endCol * squareSize + (squareSize / 2) - offset;
        double endY = endRow * squareSize + (squareSize / 2) - offset;

        board.drawLine(startX, startY, endX, endY);
    }

    // given a square, find any possible sos around it
    private List<int[]> findSOSs(int row, int col) {
        List<int[]> allFoundSOSs = new ArrayList<>();

        int[] horizontal = checkHorizontal(row, col);
        if (horizontal != null) {
        	allFoundSOSs.add(horizontal);}
        
        int[] vertical = checkVertical(row, col);
        if (vertical != null) {
        	allFoundSOSs.add(vertical);}
        
        int[] diagonalForward = checkForwardDiagonal(row, col);
        if (diagonalForward != null) {
        	allFoundSOSs.add(diagonalForward);}

        int[] diagonalBackward = checkBackwardDiagonal(row, col);
        if (diagonalBackward!= null) {
        	allFoundSOSs.add(diagonalBackward);}

        return allFoundSOSs;
    }
    
 // check for SOS horizontally
    protected int[] checkHorizontal(int row, int col) {
        if (getGameGrid()[row][col] == 'O'               // if player placed an O
        		&& col >= 1                              // left square exists
                && col <= getBoardSize() - 2             // right side exists
                && getGameGrid()[row][col - 1] == 'S'    // if left = S
                && getGameGrid()[row][col + 1] == 'S') { // if right = S
            return new int[] { row, col - 1, row, col + 1 };
        }
        if (getGameGrid()[row][col] == 'S'               // player placed last s (now looking for s and o to left)
        		&& col >= 2                              // leftmost square exists
                && getGameGrid()[row][col - 2] == 'S'    // there is an s two squares behind
                && getGameGrid()[row][col - 1] == 'O') { // middle square = o
            return new int[] { row, col - 2, row, col };
        }
        if (getGameGrid()[row][col] == 'S'				 // player placed first s (now looking for o and s to right)
        		&& col <= getBoardSize() - 3             // rightmost square exists
                && getGameGrid()[row][col + 2] == 'S'    // there is a s two squares ahead
                && getGameGrid()[row][col + 1] == 'O') { // middle square = o
            return new int[] { row, col, row, col + 2 };
        }
        return null; // none found
    }

    // check for SOS vertically
    protected int[] checkVertical(int row, int col) {
        if (getGameGrid()[row][col] == 'O'               // if player placed an O
                && row >= 1                              // top square exists
                && row <= getBoardSize() - 2             // bottom square exists
                && getGameGrid()[row - 1][col] == 'S'    // top square = S
                && getGameGrid()[row + 1][col] == 'S') { // bottom square = S
            return new int[] { row - 1, col, row + 1, col };
        }
        if (getGameGrid()[row][col] == 'S'               // if player placed an S
                && row >= 2                              // topmost square exists
                && getGameGrid()[row - 2][col] == 'S'    // two squares up = S
                && getGameGrid()[row - 1][col] == 'O') { // middle square = O
            return new int[] { row - 2, col, row, col };
        }
        if (getGameGrid()[row][col] == 'S'               // if player placed an S
                && row <= getBoardSize() - 3             // bottommost square exists
                && getGameGrid()[row + 2][col] == 'S'    // two squares down = S
                && getGameGrid()[row + 1][col] == 'O') { // middle square = O
            return new int[] { row, col, row + 2, col };
        }
        return null; // none found
    }

    // check for SOS in forward diagonal
    protected int[] checkForwardDiagonal(int row, int col) { 
        if (getGameGrid()[row][col] == 'O'                   // if player placed an O 
                && row >= 1 && col >= 1                      // top left square exists
                && row <= getBoardSize() - 2
                && col <= getBoardSize() - 2				 // bottom right square exists
                && getGameGrid()[row - 1][col - 1] == 'S'    // top left squares = S 
                && getGameGrid()[row + 1][col + 1] == 'S') { // bottom right squares = S
            return new int[] { row - 1, col - 1, row + 1, col + 1 };
        }
        if (getGameGrid()[row][col] == 'S'                   // if player placed an S
                && row >= 2 && col >= 2                      // two squares top left exist
                && getGameGrid()[row - 2][col - 2] == 'S'    // two squares top left = S
                && getGameGrid()[row - 1][col - 1] == 'O') { // middle square = O
            return new int[] { row - 2, col - 2, row, col };
        }
        if (getGameGrid()[row][col] == 'S'                   // if player placed an S
                && row <= getBoardSize() - 3
                && col <= getBoardSize() - 3			     // bottom right square exists
                && getGameGrid()[row + 2][col + 2] == 'S'    // two squares bottom right = S
                && getGameGrid()[row + 1][col + 1] == 'O') { // middle square = O
            return new int[] { row, col, row + 2, col + 2 };
        }
        return null; // none found
    }

    // check for SOS in backward diagonal
    protected int[] checkBackwardDiagonal(int row, int col) {
        if (getGameGrid()[row][col] == 'O'                   // if player placed an O
                && row >= 1 && col <= getBoardSize() - 2     // top right square exists
                && row <= getBoardSize() - 2 && col >= 1     // bottom left square exists
                && getGameGrid()[row - 1][col + 1] == 'S'    // top right square = S
                && getGameGrid()[row + 1][col - 1] == 'S') { // bottom left square = S
            return new int[] { row - 1, col + 1, row + 1, col - 1 };
        }
        if (getGameGrid()[row][col] == 'S'                   // if player placed an S
                && row >= 2 && col <= getBoardSize() - 3     // two squares top right exist
                && getGameGrid()[row - 2][col + 2] == 'S'    // two squares top right = S
                && getGameGrid()[row - 1][col + 1] == 'O') { // middle square = O
            return new int[] { row - 2, col + 2, row, col };
        }
        if (getGameGrid()[row][col] == 'S'                   // if player placed an S
                && row <= getBoardSize() - 3 && col >= 2     // two squares bottom left exists
                && getGameGrid()[row + 2][col - 2] == 'S'    // two squares bottom left = S
                && getGameGrid()[row + 1][col - 1] == 'O') { // middle square = O
            return new int[] { row, col, row + 2, col - 2 };
        }
        return null; // none found
    }

    // switch to next player
    private void switchPlayer() {
        isPlayer1Turn = !isPlayer1Turn;
        String nextPlayerName = isPlayer1Turn ? board.getPlayer1().getName() : board.getPlayer2().getName();
        board.updateTurnLabel("It's " + nextPlayerName + "'s turn!");
        disableMoves();
    }
    
    // getter for computer player to use to find SOSs
    public boolean compSOSFinder(int row, int col, char letter) {
        gameGrid[row][col] = letter;
        boolean foundSOS = checkHorizontal(row, col) != null ||      // if exists horizontal
                           checkVertical(row, col) != null ||        // if exists vertical
                           checkForwardDiagonal(row, col) != null || // if exists diagonal
                           checkBackwardDiagonal(row, col) != null;
        return foundSOS;
    }


    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }
    
    public boolean isPlayer1Turn() {
    	return isPlayer1Turn;
    }

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public char[][] getGameGrid() {
		return gameGrid;
	}

	public void setGameGrid(char[][] gameGrid) {
		this.gameGrid = gameGrid;
	}
}