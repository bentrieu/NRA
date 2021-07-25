package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
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

    public void close(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("EXIT");
        alert.setHeaderText("You are about to exit this program!");
        alert.setContentText("Are you sure?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    public void gettingStarted(ActionEvent event) throws IOException {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), gettingStartedButton);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        loadingLabel.setVisible(true);
        FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(2000), loadingLabel);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);
        fadeTransition1.play();

        /*// Translate current scene to home scene
        Parent nextRoot = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setRoot(nextRoot);*/
    }


}
