package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;


public class Controller {

    private Stage stage;
    private Scene scene;
    @FXML
    private Pane pnl_covid,pnl_new,pnl_business,temp;
    @FXML
    private Button bt_covid,bt_new,bt_business;
    @FXML
    public void buttonAction (ActionEvent event)
    {
        if(event.getSource()==bt_covid)
        {
            pnl_covid.toFront();
        }
        else if(event.getSource()==bt_new)
        {
            pnl_new.toFront();
        }
        else if(event.getSource()==bt_business)
        {
            pnl_business.toFront();
        }
    }

}
