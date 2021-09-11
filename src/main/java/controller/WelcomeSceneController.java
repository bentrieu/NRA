package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomeSceneController implements Initializable {
    @FXML
    private Button closeButton;
    @FXML
    private Button gettingStartedButton;
    @FXML
    private Label loadingLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingLabel.setVisible(false);
    }

    public void close() {
        System.exit(1);
    }

    public void gettingStarted(ActionEvent event) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), gettingStartedButton);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        loadingLabel.setVisible(true);
        FadeTransition fadeTransition1 = new FadeTransition();
        fadeTransition1.setNode(loadingLabel);
        fadeTransition1.setDuration(Duration.millis(2000));
        fadeTransition1.setCycleCount(3);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);
        fadeTransition.setAutoReverse(true);
        fadeTransition1.setInterpolator(Interpolator.LINEAR);

        // Translate current scene to home scene
        fadeTransition.setOnFinished(e -> {
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

                nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("layouts/HomeScene.fxml")));
                Main.stage.setScene(new Scene(nextRoot));
                ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
                Main.stage.show();
                System.gc();
                Runtime.getRuntime().gc();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        fadeTransition1.play();
    }


}
