/**package game_function;

import org.junit.platform.commons.util.StringUtils;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameSetup extends Application {

	private static GameSetup instance;
    private static String textStyle = "-fx-font-size: 18px; -fx-text-fill: #333; -fx-font-family: 'Times New Roman';";
    
    private RadioButton simpleGame; 
    private RadioButton generalGame;
    private RadioButton infiniteGame;
    
    private Label warnOccupiedColorLabel;
    
    private TextField player1Name;
    private TextField player2Name;
    
    private Spinner<String> player1ColorSpinner;
    private Spinner<String> player2ColorSpinner;
    private boolean player1Computer;
    private boolean player2Computer;
    
    private Spinner<Integer> boardSizeSpinner;
    private Scene scene;

    public GameSetup() {
        instance = this;
    }

    public static GameSetup getInstance() {
        return instance;
    }

    // game setup window building
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox centerVBox = new VBox(15); 
        centerVBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // game mode selection
        Label gameModeLabel = new Label("Game Mode:");  
        gameModeLabel.setStyle(textStyle);

        ToggleGroup toggleGroup = new ToggleGroup();
        simpleGame = new RadioButton("Simple Game"); 
        generalGame = new RadioButton("General Game"); 
        infiniteGame = new RadioButton("Infinite Game");

        simpleGame.setToggleGroup(toggleGroup);
        generalGame.setToggleGroup(toggleGroup);
        infiniteGame.setToggleGroup(toggleGroup);
        
        simpleGame.setSelected(true);

        simpleGame.setStyle(textStyle);
        generalGame.setStyle(textStyle);
        infiniteGame.setStyle(textStyle);

        // board size
        Label boardSizeLabel = new Label("Board size: (3-20):");  
        boardSizeLabel.setStyle(textStyle);

        boardSizeSpinner = new Spinner<>(); 
        boardSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 20, 3));
        boardSizeSpinner.setStyle(textStyle);
        
        // warning for prevented game start because of two players trying to use the same color
        warnOccupiedColorLabel = new Label("");
        warnOccupiedColorLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: red;");

        // begin game button
        Button beginGameButton = new Button("Begin Game");
        beginGameButton.setId("beginGameButton");
        beginGameButton.setStyle(textStyle);
        beginGameButton.setOnAction(event -> {
        	String player1Color = player1ColorSpinner.getValue();
            String player2Color = player2ColorSpinner.getValue();
            
            // if players are trying to use the same color
        	if (player1Color.equals(player2Color)){
        		warnOccupiedColorLabel.setText("Two players cannot share the same color!");
        	} 
        	// if players are using a name that is null or only spaces
        	else if (StringUtils.isBlank(player1Name.getText()) || StringUtils.isBlank(player1Name.getText()))  {
        		warnOccupiedColorLabel.setText("Player names cannot be empty!");
        	}
        	// else all is good in the world
        	else {
	            String gameMode = getSelectedGameMode(); 
	            int boardSize = getBoardSize(); 
	
	            Player player1 = player1Computer
	                    ? new Computer(getPlayer1Name(), getPlayerColor(player1Color))
	                    : new Player(getPlayer1Name(), getPlayerColor(player1Color));

	                // call appropriate constructor if player is computer
	                Player player2 = player2Computer
	                    ? new Computer(getPlayer2Name(), getPlayerColor(player2Color))
	                    : new Player(getPlayer2Name(), getPlayerColor(player2Color));
	                
	            Board board = new Board(gameMode, boardSize, player1, player2);
	            
	            Stage boardStage = new Stage();
	            board.startBoard(boardStage);
	
	            primaryStage.close();
        	}
        });
        
        // player customization windows(optional, hence initially hidden)
        VBox leftVBox = playerCustomizationVBox("Player 1", "Blue", true);
        VBox rightVBox = playerCustomizationVBox("Player 2", "Red", false);
        
        leftVBox.setVisible(false);
        leftVBox.setManaged(false);
        rightVBox.setVisible(false);
        rightVBox.setManaged(false);
        
        // button to make customization boxes visible
        Button playerCustomizationButton = new Button("Player Customization");
        playerCustomizationButton.setId("playerCustomizationButton");
        playerCustomizationButton.setStyle(textStyle);
        playerCustomizationButton.setOnAction(event -> {
        	// make customization boxes visible, also change window size
        	boolean isVisible = leftVBox.isVisible();
            
        	leftVBox.setVisible(!isVisible); 
            leftVBox.setManaged(!isVisible); 
            rightVBox.setVisible(!isVisible);
            rightVBox.setManaged(!isVisible);
            
            if (isVisible) {
                primaryStage.setWidth(400);
            } else {
                primaryStage.setWidth(600);
            }
        });

        // add everything that needs to be in the center
        centerVBox.getChildren().addAll(gameModeLabel, simpleGame, generalGame, infiniteGame, boardSizeLabel, boardSizeSpinner,playerCustomizationButton, beginGameButton, warnOccupiedColorLabel);

        root.setLeft(leftVBox);
        root.setRight(rightVBox);
        root.setCenter(centerVBox);
        
        scene = new Scene(root, 400, 400);
        primaryStage.setTitle("SOS Game Setup");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // create each player customization box, boolean is to correctly attribute to appropriate player
    private VBox playerCustomizationVBox(String defaultName, String defaultColor, boolean isPlayer1) {
        VBox vbox = new VBox(10);
        
        // title label
        Label playerCustomLabel = new Label("Player Customization:");
        playerCustomLabel.setStyle(textStyle);
        
        // spinner for selecting player's preferred color
        Spinner<String> colorSpinner = new Spinner<>();
        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(
            FXCollections.observableArrayList("Red", "Orange", "Green", "Blue", "Purple")
        );
        colorSpinner.setValueFactory(valueFactory);
        colorSpinner.getValueFactory().setValue(defaultColor);

        // player name field
        TextField playerChosenName = new TextField(defaultName);
        playerChosenName.setPromptText("Enter your name: (limit 9 characters)");
        playerChosenName.textProperty().addListener((observable, oldName, newName) -> {
            if (newName.length() > 9) {
            	playerChosenName.setText(oldName);
            }
        });

        // selecting computer players, implement later
        CheckBox computerPlayerOption = new CheckBox("Computer?");
        
        // attribute attributes to appropriate player
        if (isPlayer1) {
            player1Name = playerChosenName;
            player1ColorSpinner = colorSpinner;
            player1Computer = computerPlayerOption.isSelected();
        } else {
            player2Name = playerChosenName;
            player2ColorSpinner = colorSpinner;
            player2Computer = computerPlayerOption.isSelected();
        }

        vbox.getChildren().addAll(playerCustomLabel,colorSpinner, playerChosenName, computerPlayerOption);
        
        return vbox;
    }
    
    // based on user selection, determine their given color
    private Color getPlayerColor(String colorName) {
        switch (colorName) {
            case "Red":
                return Color.RED;
            case "Orange":
                return Color.ORANGE;
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
            case "Purple":
                return Color.PURPLE;
            default:
                return Color.BLACK;  
        }
    }

    public String getSelectedGameMode() {
        return simpleGame.isSelected() ? "Simple Game" : (generalGame.isSelected() ? "General Game" : "Infinite Game");
    }
    
    public String getPlayer1Name() {
		return player1Name.getText();
    }

    public String getPlayer2Name() {
        return player2Name.getText();
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
