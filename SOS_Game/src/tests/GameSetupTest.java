/*package tests;

import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game_function.GameSetup;

import java.util.concurrent.CountDownLatch;

public class GameSetupTest extends Application {
    private GameSetup gameSetup;
    private static CountDownLatch latch;

    @Override
    public void start(Stage primaryStage) {
        gameSetup = new GameSetup();
        gameSetup.start(primaryStage); // Start the GameSetup UI
        latch.countDown(); // Signal that the application is ready
    }

    @BeforeAll
    public static void setupClass() throws InterruptedException {
        latch = new CountDownLatch(1);
        new Thread(() -> Application.launch(GameSetupTest.class)).start(); // Start JavaFX application in a separate thread
        latch.await(); // Wait until the application has started
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gameSetup = GameSetup.getInstance(); // Get the instance of GameSetup
            latch.countDown(); // Signal that setup is complete
        });
        latch.await(); // Wait for setup
    }

    @AfterAll
    public static void tearDown() {
        Platform.exit(); // Clean up JavaFX
    }

    @Test
    public void testInstanceCreation() {
        assertNotNull(GameSetup.getInstance());
    }

    @Test
    public void testDefaultSelectedGameMode() {
        // Check the default selected game mode (Simple Game)
        assertEquals("Simple Game", gameSetup.getSelectedGameMode());
    }

    @Test
    public void testBoardSizeSpinnerInitialValue() {
        // Check the initial value of the board size spinner
        assertEquals(3, gameSetup.getBoardSize());
    }

    @Test
    public void testBeginGameButtonFunctionality() throws InterruptedException {
        CountDownLatch buttonLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage primaryStage = new Stage();
            gameSetup.start(primaryStage);  // Start the UI in the test

            // Lookup for the button using its ID after the UI is initialized
            Button beginGameButton = (Button) primaryStage.getScene().lookup("#beginGameButton");

            // Assert that the button is not null
            assertNotNull(beginGameButton, "Begin Game button should not be null");
            buttonLatch.countDown(); // Signal the test can continue
        });

        // Wait for the button to be checked
        buttonLatch.await();
    }

    @Test
    public void testSetBoardSize() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            gameSetup.setBoardSize(10); // Set board size to 10
            assertEquals(10, gameSetup.getBoardSize()); // Ensure the spinner value updates
            latch.countDown();
        });
        latch.await(); // Wait for the assertion
    }

    @Test
    public void testGetSelectedGameMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Initially, the selected game mode should be "Simple Game"
            assertEquals("Simple Game", gameSetup.getSelectedGameMode());

            // Change the selected game mode to "General Game"
            gameSetup.generalGame.setSelected(true);
            assertEquals("General Game", gameSetup.getSelectedGameMode());
            latch.countDown();
        });
        latch.await(); // Wait for the assertion
    }
}
*/