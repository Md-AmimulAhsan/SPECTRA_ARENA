package com.example.spectra_arena;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class Login_Page {

    Stage stage1;
    Scene scene1;

    //For violet-SignUP:
    @FXML
    private AnchorPane layer1;
    @FXML
    private Button su;
    @FXML
    private Label l1;
    @FXML
    private Text t1;
    @FXML
    private Text t2;

    //For violet-SignIn:
    @FXML
    private Button si;
    @FXML
    private Label l2;
    @FXML
    private Text t3;
    @FXML
    private Text t4;

    //For InfoSignIn:
    @FXML
    private AnchorPane layer2;
    @FXML
    private Button si_f;
    @FXML
    private Label l3;
    @FXML
    private TextField tf1;
    @FXML
    private PasswordField pw1;
    @FXML
    private Label l4;
    @FXML
    private Hyperlink h1;
    @FXML
    private Button in;
    @FXML
    private Button g;
    @FXML
    private Button f;
    @FXML
    private Hyperlink h2;
    @FXML
    private Label l5;
    @FXML
    private PasswordField pw2;
    @FXML
    private TextField tf2;
    @FXML
    private Button b_last;

    @FXML
    private TextField tf1SignIn;

    @FXML
    private Label invalidUsername;
    @FXML
    private Label invalidEmail;
    @FXML
    private Label invalidPassword;

    @FXML
    private Label invalidUsernameSignIn;
    @FXML
    private Label invalidPasswordSignIn;


    @FXML
    private void initialize(){
        //p-1
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.0001));
        slide.setNode(layer1);

        slide.setToX(537);
        slide.play();
        layer1.setTranslateX(-422);

        //p-11
        invalidUsername.setVisible(false);
        invalidEmail.setVisible(false);
        invalidPassword.setVisible(false);
        invalidUsernameSignIn.setVisible(false);
        invalidPasswordSignIn.setVisible(false);

        su.setVisible(true);
        si.setVisible(false);
        l1.setVisible(true);
        l2.setVisible(false);
        t1.setVisible(true);
        t2.setVisible(true);
        t3.setVisible(false);
        t4.setVisible(false);

        //p-2
        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(0.0001));
        slide2.setNode(layer2);

        slide2.setToX(-422);
        slide2.play();
        layer2.setTranslateX(537);

        //p-22
        b_last.setVisible(false);
        si_f.setVisible(true);
        l3.setVisible(true);
        pw1.setVisible(true);
        h1.setVisible(true);
        in.setVisible(true);
        g.setVisible(true);
        f.setVisible(true);
        h2.setVisible(true);
        l5.setVisible(false);
        tf2.setVisible(false);
        pw2.setVisible(false);
        tf1SignIn.setVisible(true);
        tf1.setVisible(false);
    }

    @FXML
    private void si(ActionEvent e){

        //p-1
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(layer1);

        slide.setToX(537);
        slide.play();
        layer1.setTranslateX(-422);

        //p-11
        su.setVisible(true);
        si.setVisible(false);
        l1.setVisible(true);
        l2.setVisible(false);
        t1.setVisible(true);
        t2.setVisible(true);
        t3.setVisible(false);
        t4.setVisible(false);

        //p-2
        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(0.5));
        slide2.setNode(layer2);

        slide2.setToX(-422);
        slide2.play();
        layer2.setTranslateX(537);

        //p-22
        b_last.setVisible(false);
        si_f.setVisible(true);
        l3.setVisible(true);
        tf1SignIn.setVisible(true);
        pw1.setVisible(true);
        h1.setVisible(true);
        in.setVisible(true);
        g.setVisible(true);
        f.setVisible(true);
        h2.setVisible(true);
        l5.setVisible(false);
        tf2.setVisible(false);
        pw2.setVisible(false);
        tf1.setVisible(false);
        invalidUsername.setVisible(false);
        invalidEmail.setVisible(false);
        invalidPassword.setVisible(false);
    }

    @FXML
    private void su(ActionEvent e){
        //p-1
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(layer1);

        slide.setToX(-5);
        slide.play();
        layer1.setTranslateX(537);

        //p-11
        su.setVisible(false);
        si.setVisible(true);
        l1.setVisible(false);
        l2.setVisible(true);
        t1.setVisible(false);
        t2.setVisible(false);
        t3.setVisible(true);
        t4.setVisible(true);

        //p-2
        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(0.5));
        slide2.setNode(layer2);

        slide2.setToX(0.5);
        slide2.play();
        layer2.setTranslateX(-422);

        //p-22
        b_last.setVisible(true);
        si_f.setVisible(false);
        l3.setVisible(false);
        pw1.setVisible(false);
        h1.setVisible(false);
        l5.setVisible(true);
        tf2.setVisible(true);
        pw2.setVisible(true);
        tf1.setVisible(true);
        tf1SignIn.setVisible(false);
        invalidUsernameSignIn.setVisible(false);
        invalidPasswordSignIn.setVisible(false);

        tf1SignIn.clear();
        pw1.clear();

    }

    @FXML
    private void b_last(ActionEvent e) {
        // Retrieve input values
        String username = tf1.getText().trim();
        String email = tf2.getText().trim();
        String pass = pw2.getText().trim();

        // Validation for empty fields
        boolean isValid = true;

        if (username.isEmpty()) {
            invalidUsername.setVisible(true);
            isValid = false;
        } else {
            invalidUsername.setVisible(false);
        }

        if (email.isEmpty()) {
            invalidEmail.setVisible(true);
            isValid = false;
        } else {
            invalidEmail.setVisible(false);
        }

        if (pass.isEmpty()) {
            invalidPassword.setVisible(true);
            isValid = false;
        } else {
            invalidPassword.setVisible(false);
        }

        // Proceed only if all fields are valid
        if (isValid) {
            try {
                FileWriter file = new FileWriter("src/main/resources/Login_Data_Storing_File", true);
                BufferedWriter bw = new BufferedWriter(file);

                bw.write(username + "," + email + "," + pass + "\n");

                JOptionPane.showMessageDialog(null, "Sign Up Successfully Done!!");

                bw.close();




            } catch (IOException g) {
                g.printStackTrace();
            }

            //p-1
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(layer1);

            slide.setToX(537);
            slide.play();
            layer1.setTranslateX(-422);

            //p-11
            su.setVisible(true);
            si.setVisible(false);
            l1.setVisible(true);
            l2.setVisible(false);
            t1.setVisible(true);
            t2.setVisible(true);
            t3.setVisible(false);
            t4.setVisible(false);

            //p-2
            TranslateTransition slide2 = new TranslateTransition();
            slide2.setDuration(Duration.seconds(0.5));
            slide2.setNode(layer2);

            slide2.setToX(-422);
            slide2.play();
            layer2.setTranslateX(537);

            // Reset fields after successful sign-up
            tf1.clear();
            tf2.clear();
            pw2.clear();

            //p-22
            b_last.setVisible(false);
            si_f.setVisible(true);
            l3.setVisible(true);
            tf1.setVisible(false);
            tf1SignIn.setVisible(true);
            pw1.setVisible(true);
            h1.setVisible(true);
            in.setVisible(true);
            g.setVisible(true);
            f.setVisible(true);
            h2.setVisible(true);
            l5.setVisible(false);
            tf2.setVisible(false);
            pw2.setVisible(false);



        }
    }


    @FXML
    private void si_f(ActionEvent e) {
        // Retrieve input values
        String username = tf1SignIn.getText().trim();
        String pass = pw1.getText().trim();

        // Validation for empty fields
        boolean isValid = true;

        if (username.isEmpty()) {
            invalidUsernameSignIn.setVisible(true);
            isValid = false;
        } else {
            invalidUsernameSignIn.setVisible(false);
        }

        if (pass.isEmpty()) {
            invalidPasswordSignIn.setVisible(true);
            isValid = false;
        } else {
            invalidPasswordSignIn.setVisible(false);
        }

        // Proceed only if all fields are valid
        if (isValid) {
            boolean found = false;

            try {
                FileReader file = new FileReader("src/main/resources/Login_Data_Storing_File");
                Scanner sc = new Scanner(file);

                // Skip the first line (if it's a header)
                if (sc.hasNextLine()) {
                    sc.nextLine();
                }

                // Process each line for credentials
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] credentials = line.split(","); // Split by comma

                    if (credentials.length == 3) { // Ensure line has USERNAME, EMAIL, PASSWORD
                        String us = credentials[0].trim(); // USERNAME
                        String pa = credentials[2].trim(); // PASSWORD

                        if (username.equals(us) && pass.equals(pa)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Successful");
                            alert.setHeaderText(null); // No header text
                            alert.setContentText("Login Succesfully Done");

                            DialogPane dialogPane = alert.getDialogPane();

                            // Add CSS for center alignment and custom font
                            dialogPane.setStyle(
                                    "-fx-font-family: 'Times New Roman';" +   // Set font family
                                            "-fx-font-size: 20px;" +        // Set font size
                                            "-fx-alignment: center;"+
                                            "-fx-background-color: #49e661" // Center align the text
                            );

                            // Show the alert
                            alert.showAndWait();
                            found = true;

                            FXMLLoader fxmlLoader = new FXMLLoader(START_PROJECT.class.getResource("F_Dashboard.fxml"));
                            scene1 = new Scene(fxmlLoader.load());
                            stage1 = (Stage)((Node)e.getSource()).getScene().getWindow();
                            stage1.setTitle("Welcome To Spectra Arena");
                            stage1.setScene(scene1);
                            stage1.show();

                            break;
                        }
                    }
                }

                if (!found) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null); // No header text
                    alert.setContentText("Invalid Username or Password");

                    DialogPane dialogPane = alert.getDialogPane();

                    // Add CSS for center alignment and custom font
                    dialogPane.setStyle(
                            "-fx-font-family: 'Times New Roman';" +   // Set font family
                                    "-fx-font-size: 20px;" +        // Set font size
                                    "-fx-alignment: center;"+       // Center align the text
                                    "-fx-background-color: #e64956"
                    );

                    // Show the alert
                    alert.showAndWait();
                }

                sc.close();

            } catch (IOException g) {
                g.printStackTrace();
            }
        }

    }

}
