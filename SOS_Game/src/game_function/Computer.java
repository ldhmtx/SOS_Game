package game_function;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Computer extends Player {
    private Random random;
    private double speed;
    private String chessLetter;
    private List<int[]> allCompsPieces;

    public Computer(String name, Color color, String speed, String chessLetter) {
        super(name, color);
        this.random = new Random();
        this.chessLetter = chessLetter;
        allCompsPieces = new ArrayList<>();
        if (speed == "Lightspeed") {
        	this.speed = 0.1;
        } else if (speed == "Faster") {
        	this.speed = 0.3;
        } else { // normal
        	this.speed = 0.5;
        }
    }

    public void makeMove() {
    	PauseTransition pause = new PauseTransition(Duration.seconds(speed));
    	if (s.isDisabled()) {// if gamemode is chess
    		pause.setOnFinished(event -> {
	            getChessChoice();
	        });
	        pause.play();
    	} 
    	else {
	         // computer delay for observer's quality of life
	        pause.setOnFinished(event -> {
	            getChoice();
	        });
	        pause.play();
	    }
    }
    
    private void getChessChoice() {
    	allCompsPieces = getAllCompsPieces(chessLetter); // use comps given letter and create list of all pieces
		Collections.shuffle(allCompsPieces, random);
	}

	private List<int[]> getAllCompsPieces(String letter){
    	List<int[]> compPieces = new ArrayList<>();
        for (int row = 0; row < logic.getBoardSize(); row++) {
            for (int col = 0; col < logic.getBoardSize(); col++) {
                if (logic.getGameGrid()[row][col] == letter.charAt(0)) {
                    compPieces.add(new int[]{row, col});
                }
            }
        }
		return compPieces;
    }

    public String getChoice() {
        int boardSize = logic.getBoardSize();
        char[][] gameGrid = logic.getGameGrid();

        //if (board.getGameMode() == "Infinite Game") {}
        
        int bestScore = 0;
        int bestrow = 0;
        int bestcol = 0;
        String bestLetter = "";
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (gameGrid[row][col] == '\0') {
                	if (bestScore < logic.compSOSFinder(row, col, 'S')) {
                		bestScore = logic.compSOSFinder(row, col, 'S');
                		bestrow = row;
                		bestcol = col;
                		bestLetter = "S";
                	}
                	else if (bestScore < logic.compSOSFinder(row, col, 'O')){
                		bestScore = logic.compSOSFinder(row, col, 'O');
                		bestrow = row;
                		bestcol = col;
                		bestLetter = "O";
	                }
                }
            }
        }
        if (bestScore > 0 || bestLetter != "") {
        	logic.onSquareClicked(bestrow, bestcol, bestLetter);
            return "comp";
        }else {
        	getRandomMove(); // if no sos found
        	return "comp";
        }
    }
    
    @Override
    public boolean isComputer() {
    	return true;
    }

    // make a random move if no sos found
    private void getRandomMove() {
        int size = logic.getBoardSize();
        boolean moveMade = false;
        while (!moveMade) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            if (logic.getGameGrid()[row][col] == '\0') { // if found empty spot to place
                String letter = random.nextBoolean() ? "S" : "O"; 
                logic.onSquareClicked(row, col, letter);
                moveMade = true;
            }
        }
    }

	public Double getSpeed() {
		return speed;
	}
}
