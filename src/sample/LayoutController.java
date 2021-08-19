package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    @FXML
    public VBox displayLayoutVbox;

    @FXML
    public AnchorPane anchorPane1, anchorPane2, anchorPane3, anchorPane4, anchorPane5, anchorPane6, anchorPane7, anchorPane8, anchorPane9, anchorPane10;

    @FXML
    public ImageView imageSource1, imageSource2, imageSource3, imageSource4, imageSource5, imageSource6, imageSource7, imageSource8, imageSource9, imageSource10;

    @FXML
    public Text textSource1, textSource2, textSource3, textSource4, textSource5, textSource6, textSource7, textSource8, textSource9, textSource10;

    @FXML
    public Text textTitle1, textTitle2, textTitle3, textTitle4, textTitle5, textTitle6, textTitle7, textTitle8, textTitle9, textTitle10;

    @FXML
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;

    @FXML
    public AnchorPane anchorPaneImage1, anchorPaneImage2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
}
