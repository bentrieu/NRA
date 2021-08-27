package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsLayoutController implements Initializable {

    @FXML
    public RadioButton darkModeRadioButton;

    @FXML
    public RadioButton lightModeRadioButton;

    @FXML
    public RadioButton normalFontSizeRadioButton;

    @FXML
    public RadioButton largeFontSizeRadioButton;

    @FXML
    public RadioButton veryLargeFontSizeRadioButton;

    public ToggleGroup modeToggleGroup = new ToggleGroup();

    public ToggleGroup fontToggleGroup = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        darkModeRadioButton.setSelected(true);
        normalFontSizeRadioButton.setSelected(true);

        darkModeRadioButton.setToggleGroup(modeToggleGroup);
        lightModeRadioButton.setToggleGroup(modeToggleGroup);

        normalFontSizeRadioButton.setToggleGroup(fontToggleGroup);
        largeFontSizeRadioButton.setToggleGroup(fontToggleGroup);
        veryLargeFontSizeRadioButton.setToggleGroup(fontToggleGroup);
    }

}

