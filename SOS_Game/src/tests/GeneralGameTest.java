/*package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game_function.Board;
import game_function.Player;
import game_logic.GeneralGame;

import java.util.concurrent.CountDownLatch;

public class GeneralGameTest {

    private GeneralGame game;
    private Board board;
    private Player player1;
    private Player player2;
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
            player1 = new Player("Player 1", javafx.scene.paint.Color.BLUE);
            player2 = new Player("Player 2", javafx.scene.paint.Color.RED);
            board = new Board("Board", 3, player1, player2);
            game = new GeneralGame(board, 3);
            latch.countDown();
        });
        latch.await();
    }

    @AfterAll
    public static void tearDown() {
        Platform.exit();
    }

    @Test
    public void testIsGameOverWithHigherPlayerScore() {
        Platform.runLater(() -> {
            game.getGameGrid()[0][1] = 'S';
            game.getGameGrid()[0][0] = 'S';
            game.getGameGrid()[0][2] = 'S';
            game.getGameGrid()[1][0] = 'S';
            game.getGameGrid()[1][2] = 'S';
            game.getGameGrid()[2][0] = 'S';
            game.getGameGrid()[2][1] = 'S';
            game.getGameGrid()[2][2] = 'S';
            game.getGameGrid()[1][1] = 'O';
            game.incrementScore();

            assertTrue(game.isGameOver());
        });
    }

    @Test
    public void testGameOverWithFullBoardDraw() {
        Platform.runLater(() -> {
            char[][] fullBoard = {
                {'S', 'S', 'S'},
                {'S', 'S', 'S'},
                {'S', 'S', 'S'}
            };
            game.setGameGrid(fullBoard);
            game.setBoardSize(3);

            assertTrue(game.isGameOver());
            assertEquals("Draw Game!", board.getTurnLabel().getText());
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
*/