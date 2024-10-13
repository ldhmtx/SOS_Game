package game_function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class BoardTest extends Application {
    private Board board;
    private static CountDownLatch latch;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(new Label("JavaFX Test App"))); // Start JavaFX
        primaryStage.show();
        latch.countDown(); // Signal that the application is ready
    }

    @BeforeAll
    public static void setupClass() throws InterruptedException {
        latch = new CountDownLatch(1);
        new Thread(() -> Application.launch(BoardTest.class)).start(); // Start JavaFX application in a separate thread
        latch.await(); // Wait until the application has started
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            board = new Board("Test Game", 3); // Set up the board
            latch.countDown(); // Signal that setup is complete
        });
        latch.await(); // Wait for board setup
    }

    @AfterAll
    public static void tearDown() {
        Platform.exit(); // Clean up JavaFX
    }

    @Test
    public void testSwitchPlayerTurn() {
        Platform.runLater(() -> {
            board.switchPlayerTurn();
            assertEquals("It's Player 2's turn!", board.getTurnLabel().getText());
            board.switchPlayerTurn();
            assertEquals("It's Player 1's turn!", board.getTurnLabel().getText());
        });
    }

    @Test
    public void testWarnOccupiedSquare() throws InterruptedException {
        Label firstSquare = (Label) board.createBoardGrid().getChildren().get(0);
        Platform.runLater(() -> {
            firstSquare.setText("S");
            firstSquare.setTextFill(board.getPlayer1().getColor());
            firstSquare.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                    MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, true, false, null));
        });

        Thread.sleep(3000); // Wait for UI update
        Platform.runLater(() -> {
            assertEquals("It's Player 1's turn!", board.getTurnLabel().getText());
        });
    }
    
    @Test
    public void testCreateBoardGrid() {
        Platform.runLater(() -> {
            GridPane grid = board.createBoardGrid();
            assertNotNull(grid);
            assertEquals(9, grid.getChildren().size()); // Check for 3x3 grid
            assertEquals(20.0, grid.getPadding().getTop()); // Verify padding
            assertEquals(20.0, grid.getPadding().getRight());
            assertEquals(20.0, grid.getPadding().getBottom());
            assertEquals(20.0, grid.getPadding().getLeft());
        });
    }
    
    @Test
    public void testCreateSquare() {
        double size = 100;
        Platform.runLater(() -> {
            Label square = board.createSquare(size);
            assertNotNull(square);
            assertEquals(size, square.getMinWidth());
            assertEquals(size, square.getMinHeight());
            assertEquals("-fx-border-color: black; -fx-background-color: lightgray; -fx-alignment: center; -fx-font-size: " + (size - 35) + "px;", square.getStyle());

            MouseEvent clickEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                    MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, true, false, null);
            square.fireEvent(clickEvent);
            assertEquals("S", square.getText());
            assertEquals(board.getPlayer1().getColor(), square.getTextFill());

            square.fireEvent(clickEvent);
            assertEquals("You cannot place a letter there!", board.getTurnLabel().getText());
        });
    }
}
