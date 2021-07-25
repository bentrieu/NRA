package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private Button closeButton;
    @FXML
    private Button gettingStartedButton;

    public void close(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("EXIT");
        alert.setHeaderText("You are about to exit this program!");
        alert.setContentText("Are you sure?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
        }

    }

    public void gettingStarted(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        Scene scene = ((Node)event.getSource()).getScene();
        scene.setRoot(nextRoot);
    }

}
