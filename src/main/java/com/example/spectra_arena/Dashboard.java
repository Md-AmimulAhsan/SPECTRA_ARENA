package com.example.spectra_arena;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Dashboard extends Parent {


    @FXML
    private AnchorPane detailPane;
    @FXML
    private Line sp;
    @FXML
    private AnchorPane MainPane;

    @FXML
    private AnchorPane editProfile;
    @FXML
    private ImageView changeImage;
    @FXML
    private ImageView profileImage;

    private boolean isEditProfileVisible = false;





    @FXML
    private void initialize() throws FileNotFoundException {
        sp.setVisible(true);
        detailPane.setTranslateX(-200);
        editProfile.setVisible(false);
    }

    @FXML
    private void saveChanges(ActionEvent e){
        editProfile.setVisible(false);
        removeBlurEffect();
    }

    @FXML
    private void editAccount(ActionEvent e) {
        // Toggle visibility of the editProfile AnchorPane
        isEditProfileVisible = !isEditProfileVisible;
        editProfile.setVisible(isEditProfileVisible);

        // Apply or remove blur effect
        if (isEditProfileVisible) {
            applyBlurEffect();
        } else {
            removeBlurEffect();
        }
    }

    // Method to apply blur effect, excluding the editProfile pane
    private void applyBlurEffect() {
        BoxBlur blur = new BoxBlur(10, 10, 3); // Parameters: width, height, iterations
        for (Node child : MainPane.getChildren()) {
            if (child != editProfile) { // Exclude the editProfile pane
                child.setEffect(blur);
            }
        }
    }

    // Method to remove blur effect
    private void removeBlurEffect() {
        for (Node child : MainPane.getChildren()) {
            child.setEffect(null); // Remove blur effect from all children
        }
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
        Scene newScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Create fade-out transition for the current scene
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), currentStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            // Set the new scene
            currentStage.setScene(newScene);
            currentStage.setTitle("Snake Game");

            // Create fade-in transition for the new scene
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), newScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        // Start the fade-out animation
        fadeOut.play();
    }


    @FXML
    private void Tetris(ActionEvent e) throws Exception {
        // Create a new Tetris game instance and stage
        Tetris tetrisGame = new Tetris();
        Stage newStage = new Stage();

        // Get the current stage
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Create fade-out transition for the current stage
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), currentStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            // Start the Tetris game on the new stage
            try {
                tetrisGame.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            currentStage.close(); // Close the current stage after fade-out
        });

        // Start the fade-out animation
        fadeOut.play();
    }


    Scene scene;
    Stage stage;

    @FXML
    private void BrickBreakGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Break_Brick.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void PackManGame(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null); // No header text
        alert.setContentText("The Game is Under Developement");

        DialogPane dialogPane = alert.getDialogPane();

        // Add CSS for center alignment and custom font
        dialogPane.setStyle(
                "-fx-font-family: 'Times New Roman';" +   // Set font family
                        "-fx-font-size: 20px;" +        // Set font size
                        "-fx-alignment: center;"
        );
//        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Tetris_Game.fxml"));
//        scene = new Scene(fxmlLoader.load());
//        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//        stage.setTitle("Tetris Game");
//        stage.setScene(scene);
//        stage.show();
    }

    @FXML
    private void FlappyBirdGame(ActionEvent e) throws IOException {
        // Create a new Flappy Bird game instance and stage
        Flappy_Bird flappyGame = new Flappy_Bird();
        Stage newStage = new Stage();

        // Get the current stage
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Create fade-out transition for the current stage
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), currentStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            // Start the Flappy Bird game on the new stage
            try {
                flappyGame.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            currentStage.close(); // Close the current stage after fade-out
        });

        // Start the fade-out animation
        fadeOut.play();
    }


    @FXML
    private void SpaceInvadersGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_SpaceInvaders_Game.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setTitle("S P A C E   I N V A D E R S");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void TicTacToeGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_TicTacToe_Game.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Create fade-out transition for the current scene
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), currentStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            // Set the new scene
            currentStage.setScene(newScene);
            currentStage.setTitle("Tic-Tac-Toe Game");

            // Create fade-in transition for the new scene
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), newScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        // Start the fade-out animation
        fadeOut.play();
    }

    @FXML
    private void PingPongGame(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_PingPong_Game.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Create fade-out transition for the current scene
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), currentStage.getScene().getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            // Set the new scene
            currentStage.setScene(newScene);
            currentStage.setTitle("Ping Pong Game");

            // Create fade-in transition for the new scene
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), newScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        // Start the fade-out animation
        fadeOut.play();
    }


    Stage stage10;
    Scene scene10;

    @FXML
    private void Logout(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Login_Page.fxml"));
        scene10 = new Scene(fxmlLoader.load());
        stage10 = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage10.setTitle("Login Page");
        stage10.setScene(scene10);
        stage10.show();
    }



    @FXML
    private void chooseProfileImage(ActionEvent e) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Image");

        // Set filters for image file types
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")
        );

        // Open the FileChooser dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            changeImage.setImage(selectedImage);
            profileImage.setImage(selectedImage);
            makeImageViewCircular(changeImage);
            makeImageViewCircular(profileImage);
        }
    }

    // Method to make ImageView circular
    private void makeImageViewCircular(ImageView imageView) {
        // Calculate radius based on the ImageView's dimensions
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;

        // Create a circular clip
        Circle clip = new Circle(radius, radius, radius);
        imageView.setClip(clip);
    }

}