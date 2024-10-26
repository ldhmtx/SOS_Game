/**package game_function;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameSetup extends Application {

    private static GameSetup instance;
    private static String textStyle = "-fx-font-size: 18px; -fx-text-fill: #333; -fx-font-family: 'Times New Roman';";
    
    private Board board; 
    private RadioButton simpleGame; 
    protected RadioButton generalGame;
    private Spinner<Integer> boardSizeSpinner;
    private Scene scene;

    public GameSetup() {
        instance = this;
    }

    public static GameSetup getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        // game setup window building
        BorderPane root = new BorderPane();
        VBox vbox = new VBox(15);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label gameModeLabel = new Label("Game Mode:"); // game mode
        gameModeLabel.setStyle(textStyle);

        ToggleGroup toggleGroup = new ToggleGroup();
        simpleGame = new RadioButton("Simple Game"); 
        generalGame = new RadioButton("General Game"); 
        simpleGame.setToggleGroup(toggleGroup);
        generalGame.setToggleGroup(toggleGroup);
        simpleGame.setSelected(true);

        simpleGame.setStyle(textStyle);
        generalGame.setStyle(textStyle);

        Label boardSizeLabel = new Label("Board size: (3-20):"); // board size
        boardSizeLabel.setStyle(textStyle);
        
        boardSizeSpinner = new Spinner<>(); 
        boardSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 20, 3));
        boardSizeSpinner.setStyle(textStyle);

        Button beginGameButton = new Button("Begin Game");
        beginGameButton.setId("beginGameButton");
        beginGameButton.setStyle(textStyle);
        beginGameButton.setOnAction(event -> {
            // take user inputs and send to Board class
            String gameMode = getSelectedGameMode(); 
            int boardSize = getBoardSize(); 

            // create board instance
            board = new Board(gameMode, boardSize);
            Stage boardStage = new Stage();
            board.startBoard(boardStage);

            primaryStage.close();
        });

        vbox.getChildren().addAll(gameModeLabel, simpleGame, generalGame, boardSizeLabel, boardSizeSpinner, beginGameButton);
        root.setCenter(vbox);

        scene = new Scene(root, 400, 400); 
        primaryStage.setTitle("SOS Game Setup");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Board getBoard() {
        return board;
    }

    public String getSelectedGameMode() {
        return simpleGame.isSelected() ? "Simple Game" : "General Game";
    }

    public int getBoardSize() {
        return boardSizeSpinner.getValue();
    }

    public void setBoardSize(int size) {
        boardSizeSpinner.getValueFactory().setValue(size); 
    }

    public Scene getScene() {
        return scene; 
    }

    public static void main(String[] args) {
        launch(args);
    }
}**/
