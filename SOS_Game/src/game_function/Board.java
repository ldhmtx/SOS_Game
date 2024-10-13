package game_function;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Board {
    private static String titleStyle = "-fx-font-size: 22px; -fx-text-fill: #333; -fx-font-family: 'Times New Roman';";
    private static String textStyle = "-fx-font-size: 18px; -fx-text-fill: #333; -fx-font-family: 'Arial';";
    
    private String gameMode;
    private int boardSize;

    private Player player1;
    private Player player2;

    private boolean isPlayer1Turn = true;

    private GridPane boardGrid;
    private Label turnLabel;

    public Board(String gameMode, int boardSize) {
        this.gameMode = gameMode;
        this.boardSize = boardSize;
    }

    // constructor creating window the game resides on
    public void startBoard(Stage boardStage) {
        BorderPane gameWindow = new BorderPane();

        setPlayer1(new Player("Player 1", Color.BLUE));
        setPlayer2(new Player("Player 2", Color.RED));

        VBox gameInfoBox = createGameInfoBox(); // text at top of window
        gameWindow.setTop(gameInfoBox);

        boardGrid = createBoardGrid(); // grid
        gameWindow.setCenter(boardGrid);

        VBox leftPlayerBox = getPlayer1().getPlayerBox(); // player controls
        leftPlayerBox.setStyle(textStyle); 
        leftPlayerBox.setSpacing(20);
        VBox rightPlayerBox = getPlayer2().getPlayerBox();        
        rightPlayerBox.setStyle(textStyle);
        rightPlayerBox.setSpacing(20);
        
        gameWindow.setLeft(leftPlayerBox);
        gameWindow.setRight(rightPlayerBox);

        getPlayer1().enable(); // player 1's turn first
        getPlayer2().disable();

        setTurnLabel(new Label("It's Player 1's turn!")); // current player
        getTurnLabel().setStyle(textStyle); 
        VBox bottomBox = new VBox(getTurnLabel());
        bottomBox.setAlignment(Pos.CENTER);
        gameWindow.setBottom(bottomBox);

        Button resetButton = new Button("Reset Game"); // re-setup game with new settings
        resetButton.setStyle(textStyle); 
        resetButton.setOnAction(event -> resetGame(boardStage));

        bottomBox.getChildren().add(resetButton);

        Scene scene = new Scene(gameWindow, 600, 600);
        boardStage.setTitle("SOS");
        boardStage.setScene(scene);
        boardStage.show();
    }

    // if player wants to reset, close current game and redirect to GameSetup
    private void resetGame(Stage boardStage) {
        boardStage.close();
        GameSetup.getInstance().start(new Stage());
    }

    // box at top of window with gamemode/board size displayed
    private VBox createGameInfoBox() {
        VBox gameInfoBox = new VBox();
        gameInfoBox.setAlignment(Pos.CENTER);
        gameInfoBox.setSpacing(10);

        Label titleLabel = new Label(gameMode + "\nBoard Size: " + boardSize);
        titleLabel.setStyle(titleStyle);
        gameInfoBox.getChildren().add(titleLabel);
        
        return gameInfoBox;
    }

    // creating the grid for the gameboard
    protected GridPane createBoardGrid() {
        GridPane grid = new GridPane();
        
        // set window based on screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); 
        double maxWidth = screenBounds.getWidth() * 0.8;
        double maxHeight = screenBounds.getHeight() * 0.8;

        double squareSize = Math.min(maxWidth / boardSize, maxHeight / boardSize) * 0.8;
        grid.setPadding(new javafx.geometry.Insets(20));

        // create/add each square in the grid
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Label square = createSquare(squareSize);
                grid.add(square, col, row);
            }
        }

        return grid;
    }

    // creating each individual square in the grid, sizes based on window size
    protected Label createSquare(double size) {
        Label square = new Label();
        square.setMinSize(size, size);
        square.setStyle("-fx-border-color: black; -fx-background-color: lightgray; -fx-alignment: center; -fx-font-size: " + (size - 35) + "px;");

        square.setOnMouseClicked(event -> { // when square is clicked
            if (!square.getText().isEmpty()) {
                warnOccupiedSquare();
                return;
            }

            // adding the color and letter for the square
            square.setText(isPlayer1Turn() ? getPlayer1().getChoice() : getPlayer2().getChoice());
            square.setTextFill(isPlayer1Turn() ? getPlayer1().getColor() : getPlayer2().getColor());

            switchPlayerTurn();
        });

        return square;
    }

    // pass control over to next player and update label
    protected void switchPlayerTurn() {
        Player currentPlayer = isPlayer1Turn() ? getPlayer1() : getPlayer2();
        Player nextPlayer = isPlayer1Turn ? getPlayer2() : getPlayer1();

        currentPlayer.disable();
        nextPlayer.enable();

        getTurnLabel().setText("It's " + nextPlayer.getName() + "'s turn!");
        setPlayer1Turn(!isPlayer1Turn());
    }

    // if a player tries to place a letter on an occupied space
    private void warnOccupiedSquare() {
        getTurnLabel().setText("You cannot place a letter there!");
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(event -> getTurnLabel().setText("It's " + (isPlayer1Turn ? "Player 1" : "Player 2")+ "'s turn!"));
        pause.play();
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

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public void setPlayer1Turn(boolean isPlayer1Turn) {
        this.isPlayer1Turn = isPlayer1Turn;
    }

    public Label getTurnLabel() {
        return turnLabel;
    }

    public void setTurnLabel(Label turnLabel) {
        this.turnLabel = turnLabel;
    }

    // Method to get the game mode
    public String getGameMode() {
        return gameMode; // Return the game mode
    }

    // Method to get the board size
    public int getBoardSize() {
        return boardSize; // Return the board size
    }
}
