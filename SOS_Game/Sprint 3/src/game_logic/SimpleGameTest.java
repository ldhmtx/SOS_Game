/**package game_logic;

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

import game_function.Board;
import game_function.Player;

import java.util.concurrent.CountDownLatch;

public class SimpleGameTest {

    private SimpleGame game;
    private Board board;
    private Player player1;
    private Player player2;
    private static CountDownLatch latch;

    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(new Label("JavaFX Test App"))); // Start JavaFX
        primaryStage.show();
        latch.countDown(); // Signal that the application is ready
    }

    @BeforeAll
    public static void setupClass() throws InterruptedException {
        latch = new CountDownLatch(1);
        new Thread(() -> Application.launch(TestApplication.class)).start(); // Start JavaFX application in a separate thread
        latch.await(); // Wait until the application has started
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            player1 = new Player("Player 1", javafx.scene.paint.Color.BLUE);
            player2 = new Player("Player 2", javafx.scene.paint.Color.RED);
            board = new Board("Test Game", 3, player1, player2); // Set up the board
            game = new SimpleGame(board, 3); // Initialize the game
            latch.countDown(); // Signal that setup is complete
        });
        latch.await(); // Wait for game setup
    }

    @AfterAll
    public static void tearDown() {
        Platform.exit(); // Clean up JavaFX
    }

    @Test
    public void testGameInitialization() {
        Platform.runLater(() -> {
            assertNotNull(game);
            assertEquals(player1, board.getPlayer1());
            assertEquals(player2, board.getPlayer2());
        });
    }

    @Test
    public void testSquareClickUpdatesTurn() throws InterruptedException {
        Platform.runLater(() -> {
            // Simulate clicking the first square in the grid
            Label firstSquare = (Label) board.createBoardGrid().getChildren().get(0);
            firstSquare.setText("S");
            firstSquare.setTextFill(player1.getColor());

            // Simulate mouse click
            MouseEvent clickEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                    MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, true, false, null);
            firstSquare.fireEvent(clickEvent);

            // Check that turn label updates correctly
            assertEquals("It's Player 2's turn!", board.getTurnLabel().getText());
        });
    }

    @Test
    public void testGameOverWithScore() {
        Platform.runLater(() -> {
            game.getGameGrid()[0][0] = 'S';
            game.getGameGrid()[0][1] = 'O';
            game.getGameGrid()[0][2] = 'S'; // Player 1 forms an SOS
            game.incrementScore(); // Increment Player 1's score

            assertEquals("Player 1 wins!", game.board.getTurnLabel().getText()); // Assuming there's a method to check winner
        });
    }

    @Test
    public void testGameDraw() {
        Platform.runLater(() -> {
            char[][] fullBoard = {
                    {'S', 'O', 'S'},
                    {'O', 'S', 'O'},
                    {'S', 'O', 'S'}
            };
            game.setGameGrid(fullBoard);
            game.setBoardSize(3); // Set the board size

            // Check if the game is over (it should be, as the board is full)
            assertEquals("Draw Game!", game.board.getTurnLabel().getText()); // Assuming there's a method to check for draw
        });
    }
    
    // Inner class to handle JavaFX lifecycle
    public static class TestApplication extends Application {
        @Override
        public void start(Stage primaryStage) {
            // We can set up the application UI here if needed
            primaryStage.setScene(new Scene(new Label("JavaFX Test App"))); // Start JavaFX
            primaryStage.show();
            latch.countDown(); // Signal that the application is ready
        }
    }
}**/
