package com.example.spectra_arena;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class TicTacToe_Game {

    Stage stage;
    Scene scene;

    private char currentPlayer = 'X';
    private char[][] board = new char[3][3];

    @FXML
    private Button cell00, cell01, cell02, cell10, cell11, cell12, cell20, cell21, cell22;

    @FXML
    private Text status;

    @FXML
    public void initialize() {
        resetBoard();
    }

    @FXML
    public void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int row = Integer.parseInt(clickedButton.getId().substring(4, 5));
        int col = Integer.parseInt(clickedButton.getId().substring(5, 6));

        if (board[row][col] == '\0') {
            board[row][col] = currentPlayer;
            clickedButton.setText(String.valueOf(currentPlayer));
            if (checkWinner(row, col)) {
                status.setText("Player " + currentPlayer + " wins!");
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }
    }

    private boolean checkWinner(int row, int col) {
        // Check row
        if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer) {
            return true;
        }
        // Check column
        if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer) {
            return true;
        }
        // Check diagonals
        if (row == col && board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (row + col == 2 && board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        return false;
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '\0';
            }
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
