package com.example.spectra_arena;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Dashboard {

    Stage stage;
    Scene scene;

    @FXML
    private AnchorPane detailPane;
    @FXML
    private Line sp;




    @FXML
    private void initialize() throws FileNotFoundException {
        sp.setVisible(true);
        detailPane.setTranslateX(-200);
    }

    @FXML
    private void showDetails() {
        TranslateTransition slideIn = new TranslateTransition();
        slideIn.setDuration(Duration.seconds(0.1)); // Adjust duration for smoothness
        slideIn.setNode(detailPane);
        slideIn.setToX(0); // Slide to its original position
        slideIn.play();
        sp.setVisible(false);
    }

    @FXML
    private void hideDetails() {
        TranslateTransition slideOut = new TranslateTransition();
        slideOut.setDuration(Duration.seconds(0.1)); // Adjust duration for smoothness
        slideOut.setNode(detailPane);
        slideOut.setToX(-200); // Slide out to the left
        slideOut.play();
        sp.setVisible(true);
    }

    @FXML
    private void SnakeGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Snake_Game.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void TetrisGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Tetris_Game.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setTitle("Tetris Game");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void BrickBreakGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_BreakBrick_Game.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setTitle("Brick Break Game");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void PackManGame(ActionEvent e) throws IOException {
        JOptionPane.showMessageDialog(null, "The game is currently under development");
//        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Tetris_Game.fxml"));
//        scene = new Scene(fxmlLoader.load());
//        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//        stage.setTitle("Tetris Game");
//        stage.setScene(scene);
//        stage.show();
    }

    @FXML
    private void FlappyBirdGame(ActionEvent e) throws IOException {
        JOptionPane.showMessageDialog(null, "The game is currently under development");
//        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Tetris_Game.fxml"));
//        scene = new Scene(fxmlLoader.load());
//        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//        stage.setTitle("Tetris Game");
//        stage.setScene(scene);
//        stage.show();
    }

    @FXML
    private void SpaceInvadersGame(ActionEvent e) throws IOException {
      //  JOptionPane.showMessageDialog(null, "The game is currently under development");
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_SpaceInvaders_Game.fxml"));
       scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setTitle("Tetris Game");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void TicTacToeGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_TicTacToe_Game.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setTitle("Tic-Tac-Toe Game");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void PingPongGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_PingPong_Game.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setTitle("Ping Pong Game");
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    private void Logout(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Login_Page.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}