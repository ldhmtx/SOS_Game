package game_function;

import java.util.ArrayList;
import java.util.List;

import game_logic.ChessGame;
import game_logic.GeneralGame;
import game_logic.InfiniteGame;
import game_logic.SOSLogic;
import game_logic.SimpleGame;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class Board {
    private static String titleStyle = "-fx-font-size: 22px; -fx-text-fill: #333; -fx-font-family: 'Times New Roman';";
    private static String textStyle = "-fx-font-size: 18px; -fx-text-fill: #333; -fx-font-family: 'Arial';";
    private String squareStyle;
    
    private String gameMode;
    private int boardSize;
    private boolean isPlayer1Turn;
    private boolean playerMakingChessMove;
    private int chessOldLetterRow = -1;
    private int chessOldLetterCol = -1;

    private Player player1;
    private Player player2;
    private Player currentPlayer;

    private GridPane boardGrid;
    private Label turnLabel;

    private SOSLogic logic;
    private double squareSize;

    private Pane drawingPane;
    private List<Line> drawnLines;

    public Board(String gameMode, int boardSize, Player player1, Player player2) {
        this.gameMode = gameMode;
        this.boardSize = boardSize;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        player2.disable();
        this.playerMakingChessMove = false;
        this.drawnLines = new ArrayList<>();
        this.isPlayer1Turn = true;
    }
    
    // constructor creating window the game resides on
    protected void startBoard(Stage boardStage) {
        BorderPane gameWindow = new BorderPane();
        
        // use correct constructor according to game mode
        if (gameMode.equals("Simple Game")) {
            logic = new SimpleGame(this, boardSize);
        } else if (gameMode.equals("General Game")) {
            logic = new GeneralGame(this, boardSize);
        } else if (gameMode.equals("Infinite Game")) {
        	logic = new InfiniteGame(this, boardSize);
        } else if (gameMode.equals("Chess Game")) {
        	logic = new ChessGame(this, boardSize);
        	
        }
        
        // display game mode/board size
        VBox gameInfoBox = new VBox();
        gameInfoBox.setAlignment(Pos.CENTER);
        gameInfoBox.setSpacing(5);
        
        Label titleLabel = new Label(gameMode + "\nBoard Size: " + boardSize);
        titleLabel.setStyle(titleStyle);
        
        Button muteButton = new Button("Mute");
        muteButton.setStyle(textStyle);
        muteButton.setOnAction(event -> {
        	// later
        });
        muteButton.setVisible(false);
        
        gameInfoBox.getChildren().addAll(titleLabel);
        gameWindow.setTop(gameInfoBox);

        // while creating the board's grid, create drawingPane on top for drawing lines later
        StackPane stackPane = new StackPane();
        boardGrid = createBoardGrid();
        setDrawingPane(new Pane());
        stackPane.getChildren().addAll(boardGrid, getDrawingPane());
        getDrawingPane().setMouseTransparent(true); // so that player can click squares
        gameWindow.setCenter(stackPane);
        stackPane.setAlignment(Pos.CENTER);
        
        // player 1's box
        VBox leftPlayerBox = getPlayer1().getPlayerBox();
        leftPlayerBox.setStyle(textStyle);
        leftPlayerBox.setSpacing(20);
        player1.getScoreLabel().setText("Score: " + player1.getPlayerScore());
        
        // player 2's box
        VBox rightPlayerBox = getPlayer2().getPlayerBox();
        rightPlayerBox.setStyle(textStyle);
        rightPlayerBox.setSpacing(20);
        player2.getScoreLabel().setText("Score: " + player2.getPlayerScore());
        
        //rightPlayerBox.getChildren().add(muteButton);
        
        gameWindow.setLeft(leftPlayerBox);
        gameWindow.setRight(rightPlayerBox);
        
        if (gameMode == "Chess Game") {
    		player1.disable();
    		player2.disable();
    	}
        
        // bottom box for turn label and reset button
        setTurnLabel(new Label("It's "+currentPlayer.getName()+"'s turn!"));
        getTurnLabel().setStyle(textStyle);
        VBox bottomBox = new VBox(getTurnLabel());
        bottomBox.setAlignment(Pos.CENTER);
        gameWindow.setBottom(bottomBox);

        // reset button to return to game setup window
        Button resetButton = new Button("Reset Game");
        resetButton.setStyle(textStyle);
        resetButton.setOnAction(event -> {
        		boardStage.close();
                GameSetup.getInstance().start(new Stage());
                player1.disable();
                player2.disable();
        });
        
        Button replayButton = new Button("Replay Game");
        replayButton.setOnAction(event -> {
    		if(logic.isGameOver()) {

    		}
        });
        bottomBox.getChildren().add(resetButton);
        
        player1.setSOSLogic(logic);
        player2.setSOSLogic(logic);
        
        //replayButton.setVisible(true);
        // game window
        Scene scene = new Scene(gameWindow, 650, 600);
        boardStage.setTitle("SOS");
        boardStage.setScene(scene);
        boardStage.show();
        
        getPlayer1().makeMove();
    }
    
    // creating grid of squares in which player can click
    public GridPane createBoardGrid() {
        GridPane grid = new GridPane();
        
        // determining size by which to size everything else (the sizes are supposed to be proportional to the boardsize no matter what is selected)
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double maxWidth = screenBounds.getWidth() * 0.8;
        double maxHeight = screenBounds.getHeight() * 0.8;
        
        squareSize = Math.min((maxWidth / boardSize), (maxHeight / boardSize)) * 0.8;
        grid.setPadding(new javafx.geometry.Insets(20));
        
        // create each square in the grid
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Label square = createSquare(squareSize, row, col);
                
                if (gameMode == "Chess Game") {
                	if (row < 2) {
                        square.setText("S");
                        square.setTextFill(player1.getColor());
                    }
                    else if (row >= boardSize - 2) {
                        square.setText("O");
                        square.setTextFill(player2.getColor());
                    }
                }
                
                grid.add(square, col, row);
            }
        }
        return grid;
    }
    
    // individual square creation
    protected Label createSquare(double size, int row, int col) {
        Label square = new Label();
        square.setMinSize(size, size);
        double fontSize = size * 0.7;  // size of letters within square
        squareStyle = "-fx-border-color: black; -fx-background-color: lightgray; -fx-alignment: center; -fx-font-size: " + fontSize + "px;";
        square.setStyle(squareStyle);
        square.setOnMouseClicked(event -> {
        	if (gameMode == "Chess Game") {
        		if (playerMakingChessMove && row == chessOldLetterRow && col == chessOldLetterCol) {
        			clearHighlights();
        		}
        		else if (playerMakingChessMove && square.getStyle().contains("lightblue")) {
        			String letter = (currentPlayer == player1)? "S" : "O";
                    updateTurnLabel("Valid Move");
                    updateSquare(row,col,letter,currentPlayer.getColor());
                    Label oldSquare = (Label) boardGrid.getChildren().get(chessOldLetterRow * boardSize + chessOldLetterCol);
                    oldSquare.setText("");
                    oldSquare.setStyle(squareStyle);
                    clearHighlights();
                    logic.onSquareClicked(row,col,letter);
                }
        		else if (playerMakingChessMove && !square.getStyle().contains("lightblue")) {
        			updateTurnLabel("Invalid Move!");
        		}
        		else if (currentPlayer == player1 && square.getText() == "S") {
                    highlightValidSquares(row, col, fontSize);
                    chessOldLetterRow = row;
                    chessOldLetterCol = col;
                    updateTurnLabel("Please Make a Move");
                    playerMakingChessMove = !playerMakingChessMove;
        		}
        		else if (currentPlayer == player2 && square.getText() == "O") {
        			highlightValidSquares(row, col, fontSize);
                    chessOldLetterRow = row;
                    chessOldLetterCol = col;
                    updateTurnLabel("Please Make a Move");
                    playerMakingChessMove = !playerMakingChessMove;
        		}
        	}
        	else if (isPlayer1Turn ? !player1.isComputer() : !player2.isComputer()) { // players cannot take the computer's turn
                String letter = getCurrentPlayer().getChoice();
                logic.onSquareClicked(row, col, letter);
            }
        });
        return square;
    }
    
    // method to color valid squares for player moves, its partially cosmetic and partially important for move validation so we don't do it a second time
    private void highlightValidSquares(int selectedRow, int selectedCol, double fontSize) {
        int radius = (currentPlayer == player2) ? 2 : 1;
        
        for (int row = selectedRow - radius; row <= selectedRow + radius; row++) {
            for (int col = selectedCol - radius; col <= selectedCol + radius; col++) {
                if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
                    Label square = (Label) boardGrid.getChildren().get(row * boardSize + col);
                    if (square.getText().isEmpty()) { // if a chess piece can be moved there
                        square.setStyle("-fx-border-color: black; -fx-background-color: lightblue; -fx-alignment: center; -fx-font-size: " + squareSize * 0.7 + "px;");
                    }
                }
            }
        }
    }
    
    //return square to factory condition
    private void clearHighlights() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Label square = (Label) boardGrid.getChildren().get(i * boardSize + j);
                if (square.getStyle().contains("lightblue")) {
                    square.setStyle(squareStyle);
                }
            }
        }
        playerMakingChessMove = !playerMakingChessMove;
        updateTurnLabel("It's "+currentPlayer.getName()+"'s turn!");
    }

    // add the appropriate letter and color to clicked square
    public void updateSquare(int row, int col, String letter, Color color) {
        Label square = (Label) boardGrid.getChildren().get(row * boardSize + col);
        square.setStyle(squareStyle);
        square.setText(letter);
        square.setTextFill(color);
        isPlayer1Turn = !isPlayer1Turn;
    }

    // mainly used for indicating which player's turn it is, also used for giving game win state and warning invalid moves
    public void updateTurnLabel(String text) {
        getTurnLabel().setText(text);
    }

    // use drawingpane to draw a line starting at x1 and y1 and end at corresponding square
    public void clearSquareRow(int row, int col) {
        Label square = (Label) boardGrid.getChildren().get(row * boardSize + col);
        System.out.println(squareSize);
        square.setText("");
        square.setStyle("-fx-border-color: black; -fx-background-color: lightgray; -fx-alignment: center; -fx-font-size: " + (squareSize * 0.7) + "px;");
    }
    
    public void clearSquareCol(int row, int col) {
        Label square = (Label) boardGrid.getChildren().get(col * boardSize + row);
        square.setText("");
        square.setStyle("-fx-border-color: black; -fx-background-color: lightgray; -fx-alignment: center; -fx-font-size: " + (squareSize * 0.7) + "px;");
    }

    // method to draw a line starting at x1 and y1 to x2 y2
    public void drawLine(double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(getCurrentPlayer().getColor());
        line.setStrokeWidth(getSquareSize() * 0.1);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        getDrawingPane().getChildren().add(line);
        drawnLines.add(line); // added for the infinite gamemode, to track and remove any made sos lines in removed rows
    }
    
    // rather than previously using random offsets to approximate, we use these labels to find the percise middle for drawing the line
    public void findLineCoords(int startRow, int startCol, int endRow, int endCol) {
        Label startSquare = (Label) boardGrid.getChildren().get(startRow * boardSize + startCol);
        Label endSquare = (Label) boardGrid.getChildren().get(endRow * boardSize + endCol);

        double startX = startSquare.getLayoutX() + startSquare.getWidth() / 2;
        double startY = startSquare.getLayoutY() + startSquare.getHeight() / 2;
        double endX = endSquare.getLayoutX() + endSquare.getWidth() / 2;
        double endY = endSquare.getLayoutY() + endSquare.getHeight() / 2;

        drawLine(startX, startY, endX, endY);
    }

    public Player getCurrentPlayer() {
    	return currentPlayer;
    }
    
    public void updateCurrentPlayer() {
    	if (currentPlayer == player1) {
    		currentPlayer = player2;
    		player1.disable();
    		player2.enable();
    	} else {
    		currentPlayer = player1;
    		player2.disable();
    		player1.enable();
    	}
    	currentPlayer.makeMove();
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Label getTurnLabel() {
        return turnLabel;
    }
    
    public int getBoardSize() {
    	return boardSize;
    }

    public void setTurnLabel(Label turnLabel) {
        this.turnLabel = turnLabel;
    }

    public double getSquareSize() {
        return squareSize;
    }
    
    public List<Line> getDrawnLines (){
    	return drawnLines;
    }

	public Pane getDrawingPane() {
		return drawingPane;
	}

	public void setDrawingPane(Pane drawingPane) {
		this.drawingPane = drawingPane;
		
	}
    
	public String getGameMode() {
		return gameMode;
	}

}
