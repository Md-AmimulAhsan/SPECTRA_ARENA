package com.example.spectra_arena;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snake_Game {

    Stage stage;
    Scene scene;

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
    private static final int WIDTH = 30;  // Grid width (in blocks)
    private static final int HEIGHT = 30; // Grid height (in blocks)


    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        restartButton.setVisible(false);
        scoreLabel.setFont(new Font("Arial", 20));
        scoreLabel.setText("Score: 0");


        bestScoreLabel.setFont(new Font("Arial", 20));
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
        pauseButton.setText("Pause");
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
            score++;
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
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw the food
        gc.setFill(Color.RED);
        gc.fillRect(food[0] * BLOCK_SIZE, food[1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

        // Draw the snake
        gc.setFill(Color.GREEN);
        for (int[] segment : snake) {
            gc.fillRect(segment[0] * BLOCK_SIZE, segment[1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    private void generateFood() {
        Random random = new Random();
        while (true) {
            food = new int[]{random.nextInt(WIDTH), random.nextInt(HEIGHT)};
            boolean isOnSnake = snake.stream().anyMatch(segment -> segment[0] == food[0] && segment[1] == food[1]);
            if (!isOnSnake) break; // Ensure food doesn't spawn on the snake
        }
    }

    private void togglePause() {
        if (isPaused) {
            pauseButton.setText("Pause");
            timeline.play();
        } else {
            pauseButton.setText("Resume");
            pauseButton.setStyle("-fx-background-color: orange;");
            timeline.pause();
        }
        isPaused = !isPaused;
        canvas.requestFocus();
    }

    private void gameOver() {
        timeline.stop();
        over.setText("Game Over !!");
        restartButton.setVisible(true);

        // Check and update best score
        if (score > bestScore) {
            bestScore = score;
            bestScoreLabel.setText("Best: " + bestScore);
        }

    }

    @FXML
    private void GoBack(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Dashboard.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }




}

