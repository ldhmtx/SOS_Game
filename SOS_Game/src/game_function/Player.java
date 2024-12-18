package game_function;

import game_logic.SOSLogic;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Player {
    private String name;
    private Color color;
    private VBox playerBox;
    protected RadioButton s;
    protected RadioButton o;
    private int playerScore;
    
    protected SOSLogic logic;
    
    private Label scoreLabel;
    
    private static String playerStyle = "-fx-font-size: 18px;";
    private static String playerNameStyle = "-fx-font-size: 25px; -fx-font-weight: bold;";

    public Player(String name, Color color) {
        this.setName(name);
        this.color = color;
        this.playerBox = createRadioButtons(name);
        this.playerScore = 0;
    }
    
    public void makeMove() {
    	
    }
    
    public boolean isComputer() {
    	return false;
    }
    
    public void setSOSLogic(SOSLogic logic) {
        this.logic = logic;
    }

    // player buttons for individual's controls (S/O)
    private VBox createRadioButtons(String playerName) {
        VBox vbox = new VBox(10);
        ToggleGroup toggleGroup = new ToggleGroup();
        vbox.setSpacing(20);

        Label playerLabel = new Label(playerName);
        playerLabel.setStyle(playerNameStyle + " -fx-text-fill: " + toHex(color) + ";"); // player name in appropriate color

        s = new RadioButton("S");
        o = new RadioButton("O");
        s.setStyle(playerStyle);
        o.setStyle(playerStyle);

        s.setToggleGroup(toggleGroup);
        o.setToggleGroup(toggleGroup);

        s.setSelected(true); // s selected by default
        
        scoreLabel = new Label();

        vbox.getChildren().addAll(playerLabel, s, o,scoreLabel);

        return vbox;
    }

    public VBox getPlayerBox() {
        return playerBox;
    }

    public String getChoice() {
        return s.isSelected() ? s.getText() : o.getText();
    }

    public Color getColor() {
        return color;
    }

    public void enable() { // enable player's buttons
        s.setDisable(false);
        o.setDisable(false);
    }

    public void disable() { // disable player's buttons
        s.setDisable(true);
        o.setDisable(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Label getScoreLabel() {
    	return this.scoreLabel;
    }
    
    public void incrementScore() {
    	this.playerScore ++;
    	this.getScoreLabel().setText("Score: "+ this.getPlayerScore());;
    }
    
    public int getPlayerScore() {
    	return this.playerScore;
    }

    // return appropriate hex from player's color choice
    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

}