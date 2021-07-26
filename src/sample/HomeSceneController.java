package sample;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class HomeSceneController implements Initializable {

    @FXML
    private Button menuButton, homeButton, videoButton, articlesListButton, settingsButton, refreshButton;

    @FXML
    private Button homeInMenuButton, videoInMenuButton, articlesListInMenuButton, settingsInMenuButton;

    @FXML
    private ImageView menuImageView;

    @FXML
    private TextField searchTextField;

    @FXML
    private AnchorPane menuPane, mainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuPane.setVisible(false);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), menuPane);
        translateTransition.setByX(-300);
        translateTransition.play();

    }

    public void menu(ActionEvent event) {
        if (!menuPane.isVisible()) {
            menuPane.setVisible(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), menuPane);
            translateTransition1.setByX(+300);
            translateTransition1.play();

            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.5), mainPane);
            translateTransition2.setByX(+212);
            translateTransition2.play();
        }
        else {
            menuPane.setVisible(false);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), menuPane);
            translateTransition1.setByX(-300);
            translateTransition1.play();

            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.5), mainPane);
            translateTransition2.setByX(-212);
            translateTransition2.play();
        }

    }

    public void home() {

    }
    public void video() {

    }
    public void articlesList() {

    }
    public void settings() {

    }
    public void homeInMenu() {

    }
    public void videoInMenu() {

    }
    public void articlesListInMenu() {

    }
    public void settingsInMenu() {

    }
    public void search() {

    }
    public void refresh() {

    }


}
