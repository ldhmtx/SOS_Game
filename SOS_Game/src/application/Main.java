package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            VBox vbox = new VBox(10);
            
            Label textLabel = new Label("Sample Text");
            CheckBox checkBox = new CheckBox("CheckBox");
            
            ToggleGroup toggleGroup = new ToggleGroup();
            RadioButton radioButton1 = new RadioButton("Radio Button #1");
            RadioButton radioButton2 = new RadioButton("Radio Button #2");
            radioButton1.setToggleGroup(toggleGroup);
            radioButton2.setToggleGroup(toggleGroup);
            
            vbox.getChildren().addAll(textLabel, checkBox, radioButton1, radioButton2);
            root.setCenter(vbox);
            
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
            primaryStage.setTitle("JavaFX Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}