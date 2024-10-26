package game_function;

import java.util.ArrayList;
import java.util.List;
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
    
    private String gameMode;
    private int boardSize;

    private Player player1;
    private Player player2;

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
        this.drawnLines = new ArrayList<>();
        this.turnLabel = new Label("It's Player 1's turn!");
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
        drawingPane = new Pane();
        stackPane.getChildren().addAll(boardGrid, drawingPane);
        drawingPane.setMouseTransparent(true); // so that player can click squares
        gameWindow.setCenter(stackPane);
        
        // player 1's box
        VBox leftPlayerBox = getPlayer1().getPlayerBox();
        leftPlayerBox.setStyle(textStyle);
        leftPlayerBox.setSpacing(20);
        player1.getScoreLabel().setText("Score: " + logic.getScorePlayer1());
        
        // player 2's box
        VBox rightPlayerBox = getPlayer2().getPlayerBox();
        rightPlayerBox.setStyle(textStyle);
        rightPlayerBox.setSpacing(20);
        player2.getScoreLabel().setText("Score: " + logic.getScorePlayer2());
        
        //rightPlayerBox.getChildren().add(muteButton);
        
        gameWindow.setLeft(leftPlayerBox);
        gameWindow.setRight(rightPlayerBox);
        
        // bottom box for turn label and reset button
        setTurnLabel(new Label("It's Player 1's turn!"));
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
        });
        
        Button replayButton = new Button("Replay Game");
        replayButton.setOnAction(event -> {
    		if(logic.isGameOver()) {
    			System.out.print("dog");
    			
    		}
        });
        bottomBox.getChildren().add(resetButton);
        
        //replayButton.setVisible(false);
        // game window
        Scene scene = new Scene(gameWindow, 650, 600);
        boardStage.setTitle("SOS");
        boardStage.setScene(scene);
        boardStage.show();
    }
    
    // creating grid of squares in which player can click
    public GridPane createBoardGrid() {
        GridPane grid = new GridPane();
        
        // determining size by which to size everything else (the sizes are supposed to be proportional to the boardsize no matter what is selected)
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double maxWidth = screenBounds.getWidth() * 0.8;
        double maxHeight = screenBounds.getHeight() * 0.8;
        
        squareSize = Math.min(maxWidth / boardSize, maxHeight / boardSize) * 0.8;
        grid.setPadding(new javafx.geometry.Insets(20));
        
        // create each square in the grid
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Label square = createSquare(squareSize, row, col);
                grid.add(square, col, row);
            }
        }
        return grid;
    }
    
    // individual square creation
    protected Label createSquare(double size, int row, int col) {
        Label square = new Label();
        square.setMinSize(size, size);
        double fontSize = size * 0.6;  // size of letters within square
        square.setStyle("-fx-border-color: black; -fx-background-color: lightgray; -fx-alignment: center; -fx-font-size: " + fontSize + "px;");
        square.setOnMouseClicked(event -> {
            logic.onSquareClicked(row, col);
        });

        return square;
    }

    // add the appropriate letter and color to clicked square
    public void updateSquare(int row, int col, String letter, Color color) {
        Label square = (Label) boardGrid.getChildren().get(row * boardSize + col);
        square.setText(letter);
        square.setTextFill(color);
    }

    // mainly used for indicating which player's turn it is, also used for giving game win state and warning invalid moves
    public void updateTurnLabel(String text) {
        getTurnLabel().setText(text);
    }

    // a player has scored now update the label
    public void updateScoreLabel(int score) {
    	if (logic.isPlayer1Turn()) {
    		player1.getScoreLabel().setText("Score: " + logic.getScorePlayer1());
    	}
    	else {
    		player2.getScoreLabel().setText("Score: " + logic.getScorePlayer2());
    	}
    }

    // use drawingpane to draw a line starting at x1 and y1 and end at corresponding square
    public void clearSquare(int row, int col) {
        Label square = (Label) boardGrid.getChildren().get(row * boardSize + col);
        square.setText("");
        square.setStyle("-fx-border-color: black; -fx-background-color: lightgray; -fx-alignment: center; -fx-font-size: " + (squareSize * 0.6) + "px;"); // Reset style
    }

    // method to draw a line starting at x1 and y1 to x2 y2
    public void drawLine(double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(logic.isPlayer1Turn() ? player1.getColor() : player2.getColor());
        line.setStrokeWidth(getSquareSize() * 0.1);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        drawingPane.getChildren().add(line);
        drawnLines.add(line); // added for the infinite gamemode, to remove any made sos lines in removed rows
    }

    // remove lines in a cleared row
    public void removeLinesForRow(int row) {
        double y = row * squareSize;
        drawnLines.removeIf(line -> {
            
            boolean intersects = (line.getStartY() <= y + squareSize && line.getEndY() >= y);
            if (intersects) {
                drawingPane.getChildren().remove(line);
            }
            return intersects;
        });
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

    public void setTurnLabel(Label turnLabel) {
        this.turnLabel = turnLabel;
    }

    public double getSquareSize() {
        return squareSize;
    }
}
