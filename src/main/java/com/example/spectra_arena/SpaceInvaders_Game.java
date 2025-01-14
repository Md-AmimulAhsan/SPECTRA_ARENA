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
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;



import java.io.IOException;
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

    private Image backgroundImage;

    private boolean gameOver = false;
    private boolean paused = false;
    private int score = 0;
    private final Random rand = new Random();

    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        backgroundImage = new Image(getClass().getResourceAsStream("/com/example/spectra_arena/gameBackground.png"));

        setupGame();

        timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> gameLoop()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(this::onKeyPressed);
        gameCanvas.setOnKeyReleased(this::onKeyReleased);
    }

    private void setupGame() {
        player = new Rocket(WIDTH / 2, HEIGHT - 80, 60);
        bombs = new ArrayList<>();
        shots = new ArrayList<>();
    }

    private void gameLoop() {
        if (paused) {
            return;
        }

        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT);

        if (gameOver) {
            gc.setFill(Color.RED);
            gc.fillText("Game Over! Score: " + score, WIDTH / 2 - 50, HEIGHT / 2);
            return;
        }

        player.update();
        player.draw(gc);

        for (int i = shots.size() - 1; i >= 0; i--) {
            Shot shot = shots.get(i);
            shot.update();

            for (int j = bombs.size() - 1; j >= 0; j--) {
                Bomb bomb = bombs.get(j);
                if (shot.collidesWith(bomb)) {
                    bomb.breakBomb();
                    shots.remove(i);
                    score++;
                    break;
                }
            }

            if (shots.contains(shot) && shot.isOutOfBounds()) {
                shots.remove(i);
            } else if (shots.contains(shot)) {
                shot.draw(gc);
            }
        }

        for (int i = bombs.size() - 1; i >= 0; i--) {
            Bomb bomb = bombs.get(i);
            bomb.update();
            if (bomb.collidesWith(player)) {
                player.explode();
                gameOver = true;
            }

            if (bomb.isOutOfBounds() || bomb.isToRemove()) {
                bombs.remove(i);
            } else {
                bomb.draw(gc);
            }
        }

        if (rand.nextInt(100) < 2) {
            bombs.add(new Bomb(rand.nextInt(WIDTH - 60), 0, 60));
        }

        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 10, 20);
    }

    @FXML
    private void GoBack(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void restartGame(ActionEvent e) {
        gameOver = false;
        score = 0;
        setupGame();
        timeline.playFromStart();
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(this::onKeyPressed);
        gameCanvas.setOnKeyReleased(this::onKeyReleased);
    }

    @FXML
    private void pauseGame() {
        paused = true;
    }

    @FXML
    private void resumeGame() {
        paused = false;
    }

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
                shots.add(player.shoot());
                break;
            case P:
                if (paused) {
                    resumeGame();
                } else {
                    pauseGame();
                }
                break;
        }
    }

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
    private Image rocketImage;




     public Rocket(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;


        this.rocketImage = new Image(getClass().getResourceAsStream("/com/example/spectra_arena/rocket.png"));

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
        gc.drawImage(rocketImage, x, y, size, size);
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

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}

class Bomb {
    int x, y, size, speed = 2;
    private Image alienImage;
    private Image brokenImage;
    private boolean isBroken = false;
    private boolean toRemove = false;

    public Bomb(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.alienImage = new Image(getClass().getResourceAsStream("/com/example/spectra_arena/alien.png"));
        this.brokenImage = new Image(getClass().getResourceAsStream("/com/example/spectra_arena/broken.png"));
    }

    public void update() {
        if (!isBroken) {
            y += speed;
        }
    }

    public void draw(GraphicsContext gc) {
        if (isBroken) {
            gc.drawImage(brokenImage, x, y, size, size);
        } else {
            gc.drawImage(alienImage, x, y, size, size);
        }
    }

    public boolean isOutOfBounds() {
        return y > 600; // Screen height
    }

    public boolean collidesWith(Rocket rocket) {
        return rocket.x < x + size && rocket.x + rocket.size > x &&
                rocket.y < y + size && rocket.y + rocket.size > y;
    }

    public void breakBomb() {
        isBroken = true;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> toRemove = true));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public boolean isToRemove() {
        return toRemove;
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