package com.example.spectra_arena;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceInvaders_Game {

    @FXML
    private Canvas gameCanvas;

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private GraphicsContext gc;
    private Timeline timeline;

    private Rocket player;
    private List<Bomb> bombs;
    private List<Shot> shots;

    private boolean gameOver = false;
    private int score = 0;
    private final Random rand = new Random();

    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        setupGame();

        timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> gameLoop()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //  key listeners for keyboard-based movement and shooting
        gameCanvas.setFocusTraversable(true); //  the canvas is focusable
        gameCanvas.setOnKeyPressed(this::onKeyPressed);
        gameCanvas.setOnKeyReleased(this::onKeyReleased);
    }

    private void setupGame() {
        player = new Rocket(WIDTH / 2, HEIGHT - 80, 60);
        bombs = new ArrayList<>();
        shots = new ArrayList<>();
    }

    private void gameLoop() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.fillText("Game Over! Score: " + score, WIDTH / 2 - 50, HEIGHT / 2);
            return;
        }

        // Player mechanics
        player.update();
        player.draw(gc);

        // Shots mechanics
        for (int i = shots.size() - 1; i >= 0; i--) {
            Shot shot = shots.get(i);
            shot.update();

            // Collision detection between a shot and bombs
            for (int j = bombs.size() - 1; j >= 0; j--) {
                Bomb bomb = bombs.get(j);
                if (shot.collidesWith(bomb)) {
                    bombs.remove(j);
                    shots.remove(i); // Remove the shot
                    score++;
                    break; // Exit the loop to avoid ConcurrentModificationException
                }
            }

            if (shots.contains(shot) && shot.isOutOfBounds()) {
                shots.remove(i);
            } else if (shots.contains(shot)) {
                shot.draw(gc);
            }
        }

        // Bomb mechanics
        for (int i = bombs.size() - 1; i >= 0; i--) {
            Bomb bomb = bombs.get(i);

            bomb.update();
            if (bomb.collidesWith(player)) {
                player.explode();
                gameOver = true;
            }

            if (bomb.isOutOfBounds()) {
                bombs.remove(i);
            } else {
                bomb.draw(gc);
            }
        }

        // Generate new bombs randomly
        if (rand.nextInt(100) < 2) {
            bombs.add(new Bomb(rand.nextInt(WIDTH - 60), 0, 60));
        }

        // Display the score
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 10, 20);
    }

    // Handle key press for movement and shooting
    private void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
            case A:
                player.setMovingLeft(true);
                break;
            case RIGHT:
            case D:
                player.setMovingRight(true);
                break;
            case SPACE:
                shots.add(player.shoot()); // Shoot using Spacebar
                break;
        }
    }

    // Handle key release to stop movement
    private void onKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
            case A:
                player.setMovingLeft(false);
                break;
            case RIGHT:
            case D:
                player.setMovingRight(false);
                break;
        }


    }
}

class Rocket {
    int x, y, size;
    private boolean exploding = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private final int speed = 7;

    public Rocket(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void update() {
        if (exploding) {
            size += 2; // Simulate explosion size increase
        } else {
            if (movingLeft && x > 0) {
                x -= speed;
            }
            if (movingRight && x + size < 800) { // Prevent moving off the right screen edge
                x += speed;
            }
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, size, size);
    }

    public Shot shoot() {
        return new Shot(x + size / 2 - 2, y); // Bullet starts at rocket's center
    }

    public void explode() {
        exploding = true;
    }

    public boolean collidesWith(Bomb bomb) {
        return x < bomb.x + bomb.size && x + size > bomb.x &&
                y < bomb.y + bomb.size && y + size > bomb.y;
    }

    // Setter methods to toggle movement state
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
class Bomb {
    int x, y, size, speed = 3;

    public Bomb(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void update() {
        y += speed;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, size, size);
    }

    public boolean isOutOfBounds() {
        return y > 600; // Screen height
    }

    public boolean collidesWith(Rocket rocket) {
        return rocket.x < x + size && rocket.x + rocket.size > x &&
                rocket.y < y + size && rocket.y + rocket.size > y;
    }
}
class Shot {
    int x, y, speed = 10;
    final int width = 4, height = 10;

    public Shot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(x, y, width, height);
    }

    public boolean isOutOfBounds() {
        return y < 0;
    }

    public boolean collidesWith(Bomb bomb) {
        return x < bomb.x + bomb.size && x + width > bomb.x &&
                y < bomb.y + bomb.size && y + height > bomb.y;
    }
}