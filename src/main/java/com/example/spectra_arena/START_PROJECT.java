package com.example.spectra_arena;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class START_PROJECT extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Login_Page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome To Spectra Arena");
        stage.setScene(scene);
        stage.show();

        Image uiu = new Image(new FileInputStream("src/main/resources/com/example/spectra_arena/photo-fire-eagle-logo-ai-render_646510-4544-removebg-preview.png"));
        stage.getIcons().add(uiu);
    }

    public static void main(String[] args) {
        launch();
    }
}
