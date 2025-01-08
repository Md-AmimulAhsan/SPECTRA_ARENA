package com.example.spectra_arena;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BreakBrick_Game {

    Stage stage;
    Scene scene;

    @FXML
    private Pane gamePane; // Link to game pane in FXML

    @FXML
    private Label gameOverLabel; // "Game Over" message label

    @FXML
    private Label chancesLabel;  // To display chances left

    @FXML
    private Label scoreLabel;

    @FXML
    private Label bestScore;

    @FXML
    private Button restart;



    // Game objects
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;

    // Constants
    private static final int BRICK_ROWS = 7;
    private static final int BRICK_COLUMNS = 8;
    private static final double BRICK_WIDTH = 50;
    private static final double BRICK_HEIGHT = 20;

    // Game loop
    private Timeline gameLoop;
    private int chancesLeft = 3;
    private int bestScoreValue = 0;// Track chances left

    @FXML
    public void initialize() {
        // Initialize game elements
        initGame();

        // Set initial position of the ball on the paddle
        ball.setPosition(paddle.getX() + paddle.getWidth() / 2 - ball.getRadius(), paddle.getY() - ball.getRadius()); // Center the ball on the paddle

        // Set initial chances and score
        chancesLabel.setText("Chances: " + chancesLeft);  // Display initial chances
        scoreLabel.setText("Score: 0");// Display initial score
        bestScore.setText("Best: " + bestScoreValue);

        // Add event listener for key press events to move the paddle
        gamePane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                paddle.moveLeft(15); // Move paddle left
            } else if (event.getCode() == KeyCode.RIGHT) {
                paddle.moveRight(15, gamePane.getWidth()); // Move paddle right
            } else if (event.getCode() == KeyCode.SPACE) {
                // Launch ball upwards when space is pressed
                if (isGameOver()) {
                    restartGame();
                } else {
                    if (ball.dy == 0) {
                        ball.launch(); // Start moving the ball
                    }
                }
            }
        });

        // Set the focus on the game pane to capture key events
        gamePane.setFocusTraversable(true);

        // Start game loop
        gameLoop = new Timeline(new KeyFrame(Duration.millis(16), e -> updateGame()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
        restart.setVisible(false);

    }

    private void initGame() {
        // Initialize ball
        ball = new Ball(260, 150, 7, Color.WHITE);

        // Initialize paddle
        paddle = new Paddle(200, 490, 80, 10, Color.BLUE);

        // Initialize bricks
        bricks = new ArrayList<>();
        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLUMNS; col++) {
                double x = col * (BRICK_WIDTH + 5) + 10;
                double y = row * (BRICK_HEIGHT + 5) + 10;
                bricks.add(new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, Color.RED));
            }
        }

        // Hide "Game Over" label initially
        gameOverLabel.setVisible(false);

        // Add all elements to the game pane
        repaintGame();
    }

    private void updateGame() {
        // Move ball
        ball.move();

        // Check collisions
        checkCollisions();

        // Repaint game elements
        repaintGame();

        // Check if game over
        if (isGameOver()) {
            chancesLeft--;
            chancesLabel.setText("Chances: " + chancesLeft);
            if (chancesLeft <= 0) {
                restart.setVisible(true);
                endGame();
            } else {
                ball.setPosition(paddle.getX() + paddle.getWidth() / 2 - ball.getRadius(), paddle.getY() - ball.getRadius());
                ball.dy = 0;
                ball.dx = 0;
                gameOverLabel.setVisible(false);
            }
        }
    }

    private void checkCollisions() {
        // Ball and paddle collision
        if (ball.getBounds().intersects(paddle.getNode().getBoundsInLocal())) {
            ball.reverseY();
        }

        // Ball and bricks collision
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getNode().getBoundsInLocal())) {
                brick.setDestroyed(true);
                ball.reverseY();
                // Increase score by 10 when a brick is destroyed
                int currentScore = Integer.parseInt(scoreLabel.getText().split(": ")[1]);
                currentScore += 10;
                scoreLabel.setText("Score: " + currentScore);  // Update the score label
                break;
            }
        }

        // Ball and walls collision
        if (ball.getX() <= 0 || ball.getX() >= gamePane.getWidth() - ball.getRadius() * 2) {
            ball.reverseX();
        }
        if (ball.getY() <= 0) {
            ball.reverseY();
        }
    }

    private void repaintGame() {
        // Clear the game pane
        gamePane.getChildren().clear();

        // Add the ball
        gamePane.getChildren().add(ball.getNode());

        // Add the paddle
        gamePane.getChildren().add(paddle.getNode());

        // Add visible bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                gamePane.getChildren().add(brick.getNode());
            }
        }
    }

    private boolean isGameOver() {
        // Game over if the ball goes below the paddle
        return ball.getY() > gamePane.getHeight();
    }

    private void endGame() {
        gameOverLabel.setVisible(true);
        int currentScore = Integer.parseInt(scoreLabel.getText().split(": ")[1]);
        if (currentScore > bestScoreValue) {
            bestScoreValue = currentScore;
            bestScore.setText("Best: " + bestScoreValue);
        }
        gameLoop.stop();
    }

    @FXML
    private void restartGame() {

        chancesLeft = 3;
        chancesLabel.setText("Chances: " + chancesLeft);
        scoreLabel.setText("Score: 0");

        // Reset game elements to initial state
        ball = new Ball(232, 483, 7, Color.WHITE);
        ball.dy = 0;  // Ball is stationary at the start
        ball.dx = 0;

        chancesLabel.setText("Chances: " + 3);
        scoreLabel.setText("Score: 0");// Ensure horizontal movement is also stopped

        // Reset paddle and bricks as well (Optional, depending on your restart logic)
        paddle = new Paddle(200, 490, 80, 10, Color.BLUE);
        bricks.clear();
        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLUMNS; col++) {
                double x = col * (BRICK_WIDTH + 5) + 10;
                double y = row * (BRICK_HEIGHT + 5) + 10;
                bricks.add(new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, Color.RED));
            }
        }

        // Hide "Game Over" label
        gameOverLabel.setVisible(false);
        restart.setVisible(false);

        // Restart game loop
        gameLoop.play();
    }

    // Inner class for Ball
    private class Ball {
        private Circle ball;
        private double dx = 0;
        private double dy = 0; // Initially, ball does not move

        public Ball(double x, double y, double radius, Color color) {
            ball = new Circle(radius, color);
            ball.setCenterX(x);
            ball.setCenterY(y);
        }

        public void setPosition(double x, double y) {
            ball.setCenterX(x);
            ball.setCenterY(y);
        }

        public void move() {
            ball.setCenterX(ball.getCenterX() + dx);
            ball.setCenterY(ball.getCenterY() + dy);
        }

        public void reverseX() {
            dx = -dx;
        }

        public void reverseY() {
            dy = -dy;
        }

        public void launch() {
            dx = 2;  // Horizontal movement is set to 2 when launched
            dy = -2; // Vertical movement is set to -2 when launched (upwards)
        }

        public double getX() {
            return ball.getCenterX();
        }

        public double getY() {
            return ball.getCenterY();
        }

        public double getRadius() {
            return ball.getRadius();
        }

        public Circle getNode() {
            return ball;
        }

        public javafx.geometry.Bounds getBounds() {
            return ball.getBoundsInParent();
        }
    }

    private class Paddle {
        private Rectangle paddle;

        public Paddle(double x, double y, double width, double height, Color color) {
            paddle = new Rectangle(width, height, color);
            paddle.setX(x);
            paddle.setY(y);
        }

        public Rectangle getNode() {
            return paddle;
        }

        public double getX() {
            return paddle.getX();
        }

        public double getY() {
            return paddle.getY();
        }

        public double getWidth() {
            return paddle.getWidth();
        }

        public void moveLeft(double distance) {
            if (paddle.getX() > 0) {
                paddle.setX(paddle.getX() - distance);
            }
        }

        public void moveRight(double distance, double maxX) {
            // Check if the paddle's right edge (paddle.getX() + paddle.getWidth()) is within the game pane's width
            if (paddle.getX() + paddle.getWidth() + distance <= maxX) {
                paddle.setX(paddle.getX() + distance);
            } else {
                // Ensure that the paddle doesn't move beyond the right edge
                paddle.setX(maxX - paddle.getWidth());
            }
        }
    }

    private class Brick {
        private Rectangle brick;
        private boolean destroyed;

        public Brick(double x, double y, double width, double height, Color color) {
            brick = new Rectangle(width, height, color);
            brick.setX(x);
            brick.setY(y);
            destroyed = false;
        }

        public Rectangle getNode() {
            return brick;
        }

        public boolean isDestroyed() {
            return destroyed;
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
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

