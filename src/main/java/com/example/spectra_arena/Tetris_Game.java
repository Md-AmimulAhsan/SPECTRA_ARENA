package com.example.spectra_arena;

import javafx.animation.AnimationTimer;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;


public class Tetris_Game {

    Stage stage;
    Scene scene;

    @FXML
    private Button rt;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private Canvas nextPieceCanvas;

    @FXML
    private Label bestScoreLabel;

    @FXML
    private Label currentScoreLabel;

    @FXML
    private Label gameOverLabel;

    private int bestScore = 0;

    private GraphicsContext gc;
    private GraphicsContext nextGc;
    private AnimationTimer gameLoop;

    private final int WIDTH = 10;
    private final int HEIGHT = 20;
    private final int TILE_SIZE = 30;

    private int[][] board;
    private Tetromino currentPiece;
    private Tetromino nextPiece;

    private int score = 0;

    private long lastTime = 0;
    private final long FALL_INTERVAL = 500_000_000;

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        nextGc = nextPieceCanvas.getGraphicsContext2D();

        bestScoreLabel.setText("Best: " + bestScore);

        board = new int[HEIGHT][WIDTH];
        spawnNewPiece();
        startGame();

        gameCanvas.setFocusTraversable(true);
        gameCanvas.requestFocus();

        gameCanvas.setOnKeyPressed(event -> handleKeyPress(event.getCode()));
        rt.setVisible(false);

    }


    private void startGame() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastTime >= FALL_INTERVAL) {
                    update();
                    lastTime = now;
                }
                render();
            }
        };
        gameLoop.start();
    }

    private void spawnNewPiece() {
        if (nextPiece == null) {
            currentPiece = Tetromino.getRandomPiece();
            nextPiece = Tetromino.getRandomPiece();
        } else {
            currentPiece = nextPiece;
            nextPiece = Tetromino.getRandomPiece();
        }
    }

    private void update() {
        if (canMove(currentPiece.x, currentPiece.y + 1)) {
            currentPiece.y++;
        } else {
            placePiece();
            clearLines();
            spawnNewPiece();
            if (!canMove(currentPiece.x, currentPiece.y)) {
                gameOver();
            }
        }
    }

    private void render() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        drawBoard();
        drawPiece(currentPiece);
        drawNextPiece(nextPiece);
        updateScore();
    }

    private void drawBoard() {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] != 0) {
                    gc.setFill(getColor(board[i][j]));
                    gc.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private void drawPiece(Tetromino piece) {
        for (int[] cell : piece.getCells()) {
            gc.setFill(getColor(piece.type));
            gc.fillRect((piece.x + cell[0]) * TILE_SIZE, (piece.y + cell[1]) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawNextPiece(Tetromino piece) {
        nextGc.clearRect(0, 0, nextPieceCanvas.getWidth(), nextPieceCanvas.getHeight());
        double startX = (nextPieceCanvas.getWidth() - piece.getWidth() * TILE_SIZE) / 2;
        double startY = (nextPieceCanvas.getHeight() - piece.getHeight() * TILE_SIZE) / 2;

        for (int[] cell : piece.getCells()) {
            nextGc.setFill(getColor(piece.type));
            nextGc.fillRect(startX + cell[0] * TILE_SIZE, startY + cell[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void updateScore() {
        currentScoreLabel.setText(String.valueOf(score));
        bestScoreLabel.setText(String.valueOf(bestScore));
    }

    private boolean canMove(int x, int y) {
        for (int[] cell : currentPiece.getCells()) {
            int newX = x + cell[0];
            int newY = y + cell[1];
            if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT || board[newY][newX] != 0) {
                return false;
            }
        }
        return true;
    }

    private void placePiece() {
        for (int[] cell : currentPiece.getCells()) {
            board[currentPiece.y + cell[1]][currentPiece.x + cell[0]] = currentPiece.type;
        }
    }

    private void clearLines() {
        for (int i = HEIGHT - 1; i >= 0; i--) {
            boolean fullLine = true;
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == 0) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                score += 100;
                for (int k = i; k > 0; k--) {
                    board[k] = board[k - 1];
                }
                board[0] = new int[WIDTH];
                i++;
            }
        }
    }

    private Color getColor(int type) {
        switch (type) {
            case 1: return Color.RED;
            case 2: return Color.BLUE;
            case 3: return Color.YELLOW;
            case 4: return Color.CYAN;
            case 5: return Color.ORANGE;
            case 6: return Color.GREEN;
            case 7: return Color.PURPLE;
            default: return Color.BLACK;
        }
    }

    private void handleKeyPress(KeyCode key) {
        switch (key) {
            case LEFT:  if (canMove(currentPiece.x - 1, currentPiece.y)) currentPiece.x--; break;
            case RIGHT: if (canMove(currentPiece.x + 1, currentPiece.y)) currentPiece.x++; break;
            case DOWN:  if (canMove(currentPiece.x, currentPiece.y + 1)) currentPiece.y++; break;
            case UP:    rotatePiece(); break;
            case SPACE: rotatePiece(); break;
        }
    }

    private void rotatePiece() {
        int[][] originalShape = currentPiece.shape;
        currentPiece.rotate();

        if (!canMove(currentPiece.x, currentPiece.y)) {
            currentPiece.shape = originalShape;
        }
    }

    private void gameOver() {
        gameLoop.stop();
        gameOverLabel.setText("Game Over !");
        rt.setVisible(true);
        if (score > bestScore) {
            bestScore = score;
            bestScoreLabel.setText("Best: " + bestScore);
        }
    }

    @FXML
    private void restartB() {
        // Reset the game board
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = 0;
            }
        }

        // Reset the score
        score = 0;

        // Reset labels and UI
        gameOverLabel.setText("");
        rt.setVisible(false);

        // Spawn new pieces
        currentPiece = Tetromino.getRandomPiece();
        nextPiece = Tetromino.getRandomPiece();

        // Reset the game loop
        lastTime = 0;
        gameLoop.start();

        // Re-render the canvas
        render();
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

class Tetromino {
    public int x, y;
    public int type;
    public int[][] shape;

    public Tetromino(int[][] shape, int x, int y, int type) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public static Tetromino getRandomPiece() {
        Random random = new Random();
        int type = random.nextInt(7) + 1;

        switch (type) {
            case 1: return new Tetromino(new int[][] {
                    {1, 1, 1, 1}
            }, 3, 0, type);
            case 2: return new Tetromino(new int[][] {
                    {1, 1},
                    {1, 1}
            }, 3, 0, type);
            case 3: return new Tetromino(new int[][] {
                    {0, 1, 0},
                    {1, 1, 1}
            }, 3, 0, type);
            case 4: return new Tetromino(new int[][] {
                    {1, 0, 0},
                    {1, 1, 1}
            }, 3, 0, type);
            case 5: return new Tetromino(new int[][] {
                    {0, 0, 1},
                    {1, 1, 1}
            }, 3, 0, type);
            case 6: return new Tetromino(new int[][] {
                    {1, 1, 0},
                    {0, 1, 1}
            }, 3, 0, type);
            case 7: return new Tetromino(new int[][] {
                    {0, 1, 1},
                    {1, 1, 0}
            }, 3, 0, type);
            default: return new Tetromino(new int[][] {
                    {0, 0},
                    {0, 0}
            }, 3, 0, 0);
        }
    }

    public int[][] getCells() {
        int[][] cells = new int[shape.length * shape[0].length][2];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] != 0) {
                    cells[i * shape[0].length + j] = new int[] {j, i};
                }
            }
        }
        return cells;
    }

    public int getWidth() {
        return shape[0].length;
    }

    public int getHeight() {
        return shape.length;
    }

    public void rotate() {
        int[][] newShape = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                newShape[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = newShape;
    }



}

