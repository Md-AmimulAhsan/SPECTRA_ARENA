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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class PingPong_Game {

    Stage stage;
    Scene scene;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private ImageView player1Heart1, player1Heart2, player1Heart3;
    @FXML
    private ImageView player2Heart1, player2Heart2, player2Heart3;

    @FXML
    private Label score1;
    @FXML
    private Label score2;

    @FXML
    private Label best1;
    @FXML
    private Label best2;

    @FXML
    private Label gameOver;

    @FXML
    private Label wins1;
    @FXML
    private Label wins2;
    @FXML
    private Label BestDisplay;




    private int player1Chances = 3;
    private int player2Chances = 3;
    private int bestScore = 0;

    // Variables
    private static final int width = 609;
    private static final int height = 588;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;
    private int ballYSpeed = 10;
    private int ballXSpeed = 10;
    private double playerOneYPos = height / 2;
    private double playerTwoYPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;
    private int playerOneXPos = 0;
    private double playerTwoXPos = width - PLAYER_WIDTH;

    public void initialize() {

        score1.setText("Score: 0");
        score2.setText("Score: 0");
        gameOver.setVisible(false);

        wins1.setVisible(false);
        wins2.setVisible(false);
        BestDisplay.setVisible(false);

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(5), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);

        // Ensure the canvas requests focus to handle key events
        gameCanvas.setFocusTraversable(true);

        // Player control: Left paddle with keyboard
        gameCanvas.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> playerOneYPos = Math.max(playerOneYPos - 50, 0);
                case DOWN -> playerOneYPos = Math.min(playerOneYPos + 50, height - PLAYER_HEIGHT);
            }
        });

        gameCanvas.setOnMouseClicked(e -> gameStarted = true);

        gameCanvas.requestFocus();
        tl.play();
    }

    private void run(GraphicsContext gc) {
        // Background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // Score
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));


        if (gameStarted) {
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;

            // AI Paddle Logic
            double aiSpeed = Math.abs(ballYPos - (playerTwoYPos + PLAYER_HEIGHT / 2)) * 0.1;
            if (ballYPos < playerTwoYPos + PLAYER_HEIGHT / 2) {
                playerTwoYPos = Math.max(playerTwoYPos - aiSpeed, 0);
            } else {
                playerTwoYPos = Math.min(playerTwoYPos + aiSpeed, height - PLAYER_HEIGHT);
            }

            // Ball
            gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);

        } else {
            // Start text
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click to Start", width / 2, height / 2);

            ballXPos = width / 2;
            ballYPos = height / 2;

            ballXSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1 : -1;
        }

        // Ball bounds
        if (ballYPos > height || ballYPos < 0) ballYSpeed *= -1;

        // Scoring and Heart Logic
        if (ballXPos < playerOneXPos - PLAYER_WIDTH) {
            player2Chances--;
            updateHearts(player2Chances, "player2");
            if (player2Chances == 0) {
                endGame("Player 1");
            } else {
                gameStarted = false;
            }
        }
        if (ballXPos > playerTwoXPos + PLAYER_WIDTH) {
            player1Chances--;
            updateHearts(player1Chances, "player1");
            if (player1Chances == 0) {
                endGame("Player 2");
            } else {
                gameStarted = false;
            }
        }
        // Ball collision with paddles
        if ((ballXPos + BALL_R > playerTwoXPos && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT)) {
            ballYSpeed += 0.5 * Math.signum(ballYSpeed);
            ballXSpeed += 0.5 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;

            // Update Player 1's score when ball hits Player 2's paddle
            scoreP1 += 10;
            score1.setText("Score: " + scoreP1);
        }

        if ((ballXPos < playerOneXPos + PLAYER_WIDTH && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
            ballYSpeed += 0.5 * Math.signum(ballYSpeed);
            ballXSpeed += 0.5 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;

            // Update Player 2's score when ball hits Player 1's paddle
            scoreP2 += 10;
            score2.setText("Score: " + scoreP2);

        }


        // Draw paddles
        gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
    }


    private void updateHearts(int chances, String player) {
        if (player.equals("player1")) {
            // Update Player 1's hearts visibility and decrease score
            if (chances == 2) {
                player1Heart3.setVisible(false);
                scoreP1 -= 10;
                score1.setText("Score: " + scoreP1);
            } else if (chances == 1) {
                player1Heart2.setVisible(false);
                scoreP1 -= 20;
                score1.setText("Score: " + scoreP1);
            } else if (chances == 0) {
                player1Heart1.setVisible(false);
                scoreP1 -= 30;
                score1.setText("Score: " + scoreP1);
            }


        } else if (player.equals("player2")) {
            // Update Player 2's hearts visibility and decrease score
            if (chances == 2) {
                player2Heart3.setVisible(false);
                scoreP2 -= 10;
                score2.setText("Score: " + scoreP2);
            } else if (chances == 1) {
                player2Heart2.setVisible(false);
                scoreP2 -= 20;
                score2.setText("Score: " + scoreP2);
            } else if (chances == 0) {
                player2Heart1.setVisible(false);
                scoreP2 -= 30;
                score2.setText("Score: " + scoreP2);
            }

        }
    }


    private void endGame(String winner) {
        gameStarted = false;

        gameOver.setVisible(true);

        // Update best score and assign it to the respective player
        int winnerScore = winner.equals("Player 1") ? scoreP1 : scoreP2;
        if (winnerScore > bestScore) {
            bestScore = winnerScore;

            // Update the best score labels based on the winner
            if (winner.equals("Player 1")) {
                bestScore = scoreP1;
                best2.setText("Best: " + bestScore);
            } else if (winner.equals("Player 2")) {
                bestScore = scoreP2;
                best1.setText("Best: " + bestScore);
            }
        }

        if(winner.equals("Player 1")){
            wins2.setVisible(true);
            BestDisplay.setText("Best = " + scoreP1);
            BestDisplay.setVisible(true);
        }
        else if(winner.equals("Player 2")){
            wins1.setVisible(true);
            BestDisplay.setText("Best = " + scoreP2);
            BestDisplay.setVisible(true);
        }



        gameCanvas.setOnMouseClicked(e -> {
            resetGame();
            gameCanvas.requestFocus();// Restart the game on click
        });

    }

    private void resetGame() {

        gameOver.setVisible(false);
        player1Chances = 3;
        player2Chances = 3;
        wins1.setVisible(false);
        wins2.setVisible(false);
        BestDisplay.setVisible(false);

        // Reset hearts visibility
        player1Heart1.setVisible(true);
        player1Heart2.setVisible(true);
        player1Heart3.setVisible(true);
        player2Heart1.setVisible(true);
        player2Heart2.setVisible(true);
        player2Heart3.setVisible(true);

        // Reset scores
        scoreP1 = 0;
        scoreP2 = 0;
        score1.setText("Score: 0");
        score2.setText("Score: 0");

        // Reset ball and player positions
        ballXPos = width / 2;
        ballYPos = height / 2;
        playerOneYPos = height / 2;
        playerTwoYPos = height / 2;

        // Reset ball speed
        ballXSpeed = new Random().nextBoolean() ? 10 : -10;
        ballYSpeed = new Random().nextBoolean() ? 10 : -10;

        // Reset game state
        gameStarted = false;

        // Redraw canvas to show "Click to Start"
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Click to Start", width / 2, height / 2);


        // Re-enable mouse click for starting the game
        gameCanvas.setOnMouseClicked(e -> gameStarted = true);
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
