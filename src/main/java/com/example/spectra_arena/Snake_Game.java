package com.example.spectra_arena;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake_Game {
    @FXML
    private Canvas canvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Button pauseButton;
    @FXML
    private Button restartButton;
    @FXML
    private Label over;
    @FXML
    private Label bestScoreLabel;
    @FXML
    private AnchorPane anchor;

    @FXML
    private ImageView pause;

    @FXML
    private ImageView resume;

    @FXML
    private ImageView restart;

    private Button Back;

    private int bestScore = 0;

    private GraphicsContext gc;
    private Timeline timeline;
    private List<int[]> snake;
    private int[] food;
    private int direction;
    private int nextDirection;
    private int score = 0;
    private boolean isPaused = false;

    private static final int BLOCK_SIZE = 20;
    private static final int WIDTH = 32;  // Grid width (in blocks)
    private static final int HEIGHT = 22;// Grid height (in blocks)

    private Image appleImage;
    private Image chickenImage;
    private boolean isSpecialFoodActive = false;
    private ProgressBar progressBar; // To show the timer for the chicken
    private Timeline chickenTimer; // Timer for the chicken food
    private double progress = 1.0;




    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();

        restartButton.setVisible(false);
        scoreLabel.setFont(new Font("Forte", 27));
        scoreLabel.setText("Score: 0");

        pause.setVisible(true);
        resume.setVisible(false);
        restart.setVisible(false);

        appleImage = new Image(getClass().getResourceAsStream("/com/example/spectra_arena/Google-Noto-Emoji-Food-Drink-32349-red-apple.1024-removebg-preview.png"));
        chickenImage = new Image(getClass().getResourceAsStream("/com/example/spectra_arena/0edf6d07d6f1e2ed60546fb63e36aa73__2_-removebg-preview.png"));

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(110);
        progressBar.setPrefHeight(10);
        progressBar.setProgress(0);
        progressBar.setVisible(false);

        anchor.getChildren().add(progressBar);

        bestScoreLabel.setFont(new Font("Forte", 27));
        bestScoreLabel.setText("Best: " + bestScore);

        initializeGame();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(event -> {
            if (!isPaused) {
                KeyCode key = event.getCode();
                int newDirection = key.getCode();
                // Prevent 180-degree turns
                if ((newDirection == KeyCode.UP.getCode() && direction != KeyCode.DOWN.getCode()) ||
                        (newDirection == KeyCode.DOWN.getCode() && direction != KeyCode.UP.getCode()) ||
                        (newDirection == KeyCode.LEFT.getCode() && direction != KeyCode.RIGHT.getCode()) ||
                        (newDirection == KeyCode.RIGHT.getCode() && direction != KeyCode.LEFT.getCode())) {
                    nextDirection = newDirection;
                }
            }
            canvas.requestFocus(); // Ensure the canvas remains in focus
            Back.setFocusTraversable(false);
        });



        pauseButton.setOnAction(event -> togglePause());
        pauseButton.setFocusTraversable(false);

        restartButton.setOnAction(event -> initializeGame());
        restartButton.setFocusTraversable(false);
    }


    private void initializeGame() {
        // Reset game variables
        snake = new ArrayList<>();
        snake.add(new int[]{WIDTH / 2, HEIGHT / 2}); // Start in the middle of the grid
        generateFood();
        direction = KeyCode.RIGHT.getCode();
        nextDirection = KeyCode.RIGHT.getCode();
        score = 0;
        scoreLabel.setText("Score: 0");
        isPaused = false;
        restartButton.setVisible(false);
        restart.setVisible(false);
        over.setText("");

        // Stop any previous timeline and create a new one
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> updateGame()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        canvas.requestFocus();
    }

    private void updateGame() {
        direction = nextDirection; // Update the direction

        // Get current head position
        int[] head = snake.get(0);
        int newX = head[0];
        int newY = head[1];

        // Update the head position based on direction
        switch (direction) {
            case 37 -> newX--; // Left
            case 38 -> newY--; // Up
            case 39 -> newX++; // Right
            case 40 -> newY++; // Down
        }

        // Check collision with walls (in terms of canvas size, not block size)
        if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
            gameOver();
            return;
        }

        // Check collision with itself
        for (int[] segment : snake) {
            if (segment[0] == newX && segment[1] == newY) {
                gameOver();
                return;
            }
        }

        // Move the snake
        snake.add(0, new int[]{newX, newY});
        boolean hasEatenFood = (newX == food[0] && newY == food[1]);

        if (!hasEatenFood) {
            snake.remove(snake.size() - 1); // Remove the tail if no food eaten
        } else {
            if (isSpecialFoodActive) {
                score += 10; // Add 10 points for the chicken
                deactivateSpecialFood();
            } else {
                snakeEatsApple();
            }
            scoreLabel.setText("Score: " + score);
            generateFood();


            // Increase speed every 5 points
            if (score % 5 == 0) {
                timeline.setRate(1 + score * 0.05); // Gradually increase speed
            }
        }

        drawGame();
    }



    private void drawGame() {
        // Clear the canvas
        gc.setFill(Color.LIGHTGREEN);
        gc.fillOval(0, 0, canvas.getWidth(), canvas.getHeight());

        drawGridBackground();

        // Draw the food
        if (isSpecialFoodActive) {
            gc.drawImage(chickenImage, food[0] * BLOCK_SIZE, food[1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        } else {
            gc.drawImage(appleImage, food[0] * BLOCK_SIZE, food[1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }

        // Draw the snake
        gc.setFill(Color.DARKGREEN);
        for (int[] segment : snake) {
            gc.fillOval(segment[0] * BLOCK_SIZE, segment[1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    private void drawGridBackground() {
        boolean toggleColor; // To alternate colors for each row

        for (int row = 0; row < HEIGHT; row++) {
            toggleColor = (row % 2 == 0); // Start each row with alternating color
            for (int col = 0; col < WIDTH; col++) {
                // Choose color based on toggle
                if (toggleColor) {
                    gc.setFill(Color.web("#bbed45")); // Light color (YellowGreen) with hex code
                } else {
                    gc.setFill(Color.web("#b4d95d")); // Dark color (Orchid) with hex code
                }
                // Draw the rectangle for the current cell
                gc.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

                // Toggle color for the next cell in the row
                toggleColor = !toggleColor;
            }
        }
    }

    private void generateFood() {
        Random random = new Random();
        while (true) {
            food = new int[]{random.nextInt(WIDTH), random.nextInt(HEIGHT)};
            boolean isOnSnake = snake.stream().anyMatch(segment -> segment[0] == food[0] && segment[1] == food[1]);
            if (!isOnSnake) break; // Ensure food doesn't spawn on the snake
        }

        // Activate special food (chicken) after every 5 apples
        if (appleCounter == 5 && !isSpecialFoodActive) {
            activateSpecialFood();
        }
    }


    int appleCounter;


    private void activateSpecialFood() {
        if (isSpecialFoodActive) return; // Prevent multiple activations

        isSpecialFoodActive = true;
        progress = 1.0;
        progressBar.setVisible(true);
        progressBar.setLayoutX(310);
        progressBar.setLayoutY(34);
        progressBar.setProgress(progress);

        // Start the timer for the chicken
        chickenTimer = new Timeline(
                new KeyFrame(Duration.millis(50), e -> {
                    progress -= 0.01; // Decrease progress every 50ms
                    progressBar.setProgress(progress);

                    // If progress reaches 0, deactivate special food
                    if (progress <= 0) {
                        deactivateSpecialFood();
                    }
                })
        );
        chickenTimer.setCycleCount(100); // 5 seconds total
        chickenTimer.play();
    }


    private void deactivateSpecialFood() {
        isSpecialFoodActive = false;
        progressBar.setVisible(false); // Hide the progress bar
        if (chickenTimer != null) {
            chickenTimer.stop(); // Stop the timer
        }

        // Reset the apple counter for the next cycle
        appleCounter = 0;
    }


    private void snakeEatsApple() {
        if (isSpecialFoodActive) {
            // Snake ate the chicken
            deactivateSpecialFood();
            score += 10; // Add 10 points for the chicken
        } else {// Add 1 point for the apple
            score++; // Increment the apple counter
            appleCounter++;
        }

        // Update the score label
        scoreLabel.setText("Score: " + score);

        // Generate the next food
        generateFood();
    }


    private void togglePause() {
        if (isPaused) {
            pause.setVisible(true);
            resume.setVisible(false);
            timeline.play();
        } else {
            pause.setVisible(false);
            resume.setVisible(true);
            timeline.pause();
        }
        isPaused = !isPaused;
        canvas.requestFocus();
    }

    private void gameOver() {
        // Stop the game timeline
        timeline.stop();

        // Display "Game Over" message
        over.setAlignment(Pos.CENTER);
        over.setText("Game Over !!");

        restartButton.setVisible(true);
        restart.setVisible(true);

        // Check and update the best score
        if (score > bestScore) {
            bestScore = score;
            bestScoreLabel.setText("Best: " + bestScore);
        }

        score = 0; // Reset the score
        appleCounter = 0; // Reset the apple counter
        isSpecialFoodActive = false; // Reset special food state

        // Stop and hide the progress bar
        progress = 1.0;
        progressBar.setVisible(false);
        if (chickenTimer != null) {
            chickenTimer.stop();
        }

        // Generate new food for the next game
        generateFood();

        // Update the score label for the next game
        scoreLabel.setText("Score: " + score);

        // Prepare the game board visuals for the next game
        drawGame();
    }

    Scene scene13;
    Stage stage13;

    @FXML
    private void GoBack(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Dashboard.fxml"));
        scene13 = new Scene(fxmlLoader.load());
        stage13 = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage13.setScene(scene13);
        stage13.show();
    }

}
