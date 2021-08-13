package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;


import java.net.URL;
import java.util.ResourceBundle;


public class HomeLayoutController implements Initializable {
    @FXML
    private Button button1, button2, button3, button4, button5, button6, button7, button8;

    @FXML
    private Label label1, label2, label3, label4, label5, label6, label7, label8;

    @FXML
    private AnchorPane wrapAnchorPane, anchorPane1, anchorPane2, anchorPane3, anchorPane4, anchorPane5, anchorPane6, anchorPane7, anchorPane8;

//    static Button[] buttonList = {button1, button2, button3, button4, button5, button6, button7, button8};
//    static Label[] labelList = {label1, label2, label3, label4, label5, label6, label7, label8};
//    static AnchorPane[] anchorPaneList = {anchorPane1, anchorPane2, anchorPane3, anchorPane4, anchorPane5, anchorPane6, anchorPane7, anchorPane8};

    HomeLayoutController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Button[] buttonList = {button1, button2, button3, button4, button5, button6, button7, button8};
        Label[] labelList = {label1, label2, label3, label4, label5, label6, label7, label8};
        AnchorPane[] anchorPaneList = {anchorPane1, anchorPane2, anchorPane3, anchorPane4, anchorPane5, anchorPane6, anchorPane7, anchorPane8};
        for (int i = 0; i < 8; i++) {
            BackgroundImage backgroundImage = new BackgroundImage(new Image(Main.zingNewsList.get(i).getThumb()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
            Background background = new Background(backgroundImage);
            anchorPaneList[i].setBackground(background);
            labelList[i].setText(Main.zingNewsList.get(i).getTitle());
        }
    }

    @FXML
    public AnchorPane initialize(int pageIndex) {
        Button[] buttonList = {button1, button2, button3, button4, button5, button6, button7, button8};
        Label[] labelList = {label1, label2, label3, label4, label5, label6, label7, label8};
        AnchorPane[] anchorPaneList = {anchorPane1, anchorPane2, anchorPane3, anchorPane4, anchorPane5, anchorPane6, anchorPane7, anchorPane8};
        for (int i = 0; i < 8; i++) {
            BackgroundImage backgroundImage = new BackgroundImage(new Image(Main.zingNewsList.get(i).getThumb()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
            Background background = new Background(backgroundImage);
            anchorPaneList[i].setBackground(background);
            labelList[i].setText(Main.zingNewsList.get(i).getTitle());
        }
        return wrapAnchorPane;
    }



}
