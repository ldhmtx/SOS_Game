package game_function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game_logic.SimpleGame;

import java.util.concurrent.CountDownLatch;

public class ComputerTest {

    private SimpleGame game;
    private Board board;
    private Computer computer;
    private static CountDownLatch latch;

    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(new Label("JavaFX Test App")));
        primaryStage.show();
        latch.countDown();
    }

    @BeforeAll
    public static void setupClass() throws InterruptedException {
        latch = new CountDownLatch(1);
        new Thread(() -> Application.launch(TestApplication.class)).start();
        latch.await();
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Player player1 = new Player("Player 1", Color.BLUE);
            computer = new Computer("Computer", Color.RED, "Normal", "S");
            board = new Board("Test Game", 3, player1, computer);
            game = new SimpleGame(board, 3);
            latch.countDown();
        });
        latch.await();
    }

    @AfterAll
    public static void tearDown() {
        Platform.exit();
    }

    @Test
    public void testComputerInitialization() {
        Platform.runLater(() -> {
            assertNotNull(computer);
            assertEquals("Computer", computer.getName());
            assertTrue(computer.isComputer());
        });
    }

    @Test // computer should affect the board state, leaving a new letter on the board
    public void testComputerMakesMove() {
        Platform.runLater(() -> {
            computer.makeMove();
            boolean moveMade = false;
            char[][] gameGrid = game.getGameGrid();
            for (int row = 0; row < game.getBoardSize(); row++) {
                for (int col = 0; col < game.getBoardSize(); col++) {
                    if (gameGrid[row][col] != '\0') {
                        moveMade = true;
                        break;
                    }
                }
                if (moveMade) break;
            }
            assertTrue(moveMade, "Computer should have made a move on the board");
        });
    }

    @Test
    public void testComputerSpeedSelection() {
        Platform.runLater(() -> {
            assertEquals(0.5, computer.getSpeed(), "speed should be 0.5 for normal speed");
            computer = new Computer("Computer", Color.RED, "Lightspeed", "S");
            assertEquals(0.1, computer.getSpeed(), "speed should be 0.1 for lightspeed");
            computer = new Computer("Computer", Color.RED, "Faster", "S");
            assertEquals(0.3, computer.getSpeed(), "speed should be 0.3 for faster speed");
        });
    }

    public static class TestApplication extends Application {
        @Override
        public void start(Stage primaryStage) {
            primaryStage.setScene(new Scene(new Label("JavaFX Test App")));
            primaryStage.show();
            latch.countDown();
        }
    }
}