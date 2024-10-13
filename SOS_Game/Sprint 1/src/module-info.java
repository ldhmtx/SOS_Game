module SOS_Game {
    requires javafx.controls;
	requires junit;
    opens application to javafx.graphics, javafx.fxml;
}