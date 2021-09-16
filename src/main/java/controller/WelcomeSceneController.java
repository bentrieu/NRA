package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomeSceneController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button gettingStartedButton;

    public static ProgressBar progressBar = new ProgressBar();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gettingStartedButton.setText("Loading now");
        AnchorPane.setLeftAnchor(progressBar, 50.0);
        AnchorPane.setRightAnchor(progressBar, 50.0);
        AnchorPane.setBottomAnchor(progressBar, 20.0);
        anchorPane.getChildren().add(progressBar);
        progressBar.setProgress(0.0);
        progressBar.getStylesheets().add("/css/cssdarkmode.css");
        progressBar.getStyleClass().add("progress-bar");
        progressBar.setPrefHeight(5);
        progressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() == 1) {
                    Platform.runLater(() -> {
                        gettingStartedButton.setText("Getting Started");
                    });
                }
            }
        });
    }

    public void close() {
        System.exit(1);
    }

    public void gettingStarted() {
        if (progressBar.getProgress() == 1) {
            Parent nextRoot;
            try {
                Main.stage.setMinWidth(600);
                Main.stage.setMinHeight(500);
                Main.stage.getIcons().add(new Image("/images/newsicon.png"));
                Main.stage.setTitle("NRA News");
                Main.stage.setFullScreen(true);

                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                int width = gd.getDisplayMode().getWidth();
                int height = gd.getDisplayMode().getHeight();
                if (width < 1300) {
                    Main.stage.setWidth(width - 100);
                    Main.stage.setHeight(height - 100);
                }

                // Change the stage
                nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("layouts/HomeScene.fxml")));
                Main.stage.setScene(new Scene(nextRoot));
                ((Stage) ((Node) progressBar).getScene().getWindow()).close();
                Main.stage.show();

                // Set full screen using F11
                Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, e1 -> {
                    if (KeyCode.F11.equals(e1.getCode())) {
                        Main.stage.setFullScreen(!Main.stage.isFullScreen());
                    }
                });
                System.gc();
                Runtime.getRuntime().gc();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
