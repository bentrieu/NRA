package sample;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HomeSceneController implements Initializable {

    @FXML
    private Button menuButton, refreshButton, searchButton, rightArrowButton, leftArrowButton;

    @FXML
    private ToggleButton homeButton, videoButton, articlesListButton, settingsButton;

    @FXML
    private ToggleButton newsButton, covidButton, politicsButton, businessButton, technologyButton,
            healthButton, sportsButton, entertainmentButton, worldButton, othersButton;

    @FXML
    private Button homeInMenuButton, videoInMenuButton, articlesListInMenuButton, settingsInMenuButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private AnchorPane menuPane, mainPane, tempPane, articlesListPane;

    @FXML
    private Label todayLabel;

    @FXML
    private HBox categoryHbox;

    @FXML
    private Pagination pagination;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane anchorPaneUnderScrollPane, anchorPaneAboveScrollPane, rootAnchorPane;

    int currentCategory = 0;

    ArticlesManager articlesManager = new ArticlesManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set initial category
        homeButton.setSelected(true);
        displayNewsList();

        // Set up menuPane + tempPane to be unvisible
        menuPane.setVisible(false);
        menuPane.setLayoutX(-300);
        tempPane.setVisible(false);

        // Set up visible mode to 2 arrows
        rightArrowButton.setVisible(false);
        leftArrowButton.setVisible(false);
        articlesListPane.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            rightArrowButton.setVisible(true);
            leftArrowButton.setVisible(true);
            e.consume();
        });
        articlesListPane.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            rightArrowButton.setVisible(false);
            leftArrowButton.setVisible(false);
            e.consume();
        });
        rightArrowButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            nextCategoryList();
            e.consume();
        });
        leftArrowButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            previousCategoryList();
            e.consume();
        });

        // Set up pagination
        AnchorPane wrapAnchorPane = new AnchorPane();

        // Vbox setup
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        AnchorPane.setTopAnchor(vBox, 10.0);
        AnchorPane.setRightAnchor(vBox, 10.0);
        AnchorPane.setBottomAnchor(vBox, 10.0);
        AnchorPane.setLeftAnchor(vBox, 10.0);
        wrapAnchorPane.getChildren().add(vBox);

        // Hbox set up
        HBox hBox1 = new HBox();
        hBox1.setSpacing(5);
        hBox1.setAlignment(Pos.CENTER);
        HBox hBox2 = new HBox();
        hBox2.setSpacing(5);
        hBox2.setAlignment(Pos.CENTER);
        HBox hBox3 = new HBox();
        hBox3.setSpacing(5);
        hBox3.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hBox1, hBox2, hBox3);

        // inside anchor pane set up
        AnchorPane anchorPane1 = new AnchorPane();
        anchorPane1.setPrefSize(300,300);
        AnchorPane anchorPane2 = new AnchorPane();
        anchorPane2.setPrefSize(300,300);
        AnchorPane anchorPane3 = new AnchorPane();
        anchorPane3.setPrefSize(300,300);
        AnchorPane anchorPane4 = new AnchorPane();
        anchorPane4.setPrefSize(300,300);
        AnchorPane anchorPane5 = new AnchorPane();
        anchorPane5.setPrefSize(300,300);
        AnchorPane anchorPane6 = new AnchorPane();
        anchorPane6.setPrefSize(300,300);
        AnchorPane anchorPane7 = new AnchorPane();
        anchorPane7.setPrefSize(300,300);
        AnchorPane anchorPane8 = new AnchorPane();
        anchorPane8.setPrefSize(300,300);
        hBox1.getChildren().addAll(anchorPane1, anchorPane2);
        hBox2.getChildren().addAll(anchorPane3, anchorPane4, anchorPane5);
        hBox3.getChildren().addAll(anchorPane6, anchorPane7, anchorPane8);

        // Button
        Button button1 = new Button();
        button1.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button1, 0.0);
        AnchorPane.setRightAnchor(button1, 0.0);
        AnchorPane.setBottomAnchor(button1, 0.0);
        AnchorPane.setLeftAnchor(button1, 0.0);

        Button button2 = new Button();
        button2.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button2, 0.0);
        AnchorPane.setRightAnchor(button2, 0.0);
        AnchorPane.setBottomAnchor(button2, 0.0);
        AnchorPane.setLeftAnchor(button2, 0.0);

        Button button3 = new Button();
        button3.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button3, 0.0);
        AnchorPane.setRightAnchor(button3, 0.0);
        AnchorPane.setBottomAnchor(button3, 0.0);
        AnchorPane.setLeftAnchor(button3, 0.0);

        Button button4 = new Button();
        button4.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button4, 0.0);
        AnchorPane.setRightAnchor(button4, 0.0);
        AnchorPane.setBottomAnchor(button4, 0.0);
        AnchorPane.setLeftAnchor(button4, 0.0);

        Button button5 = new Button();
        button5.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button5, 0.0);
        AnchorPane.setRightAnchor(button5, 0.0);
        AnchorPane.setBottomAnchor(button5, 0.0);
        AnchorPane.setLeftAnchor(button5, 0.0);

        Button button6 = new Button();
        button6.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button6, 0.0);
        AnchorPane.setRightAnchor(button6, 0.0);
        AnchorPane.setBottomAnchor(button6, 0.0);
        AnchorPane.setLeftAnchor(button6, 0.0);

        Button button7 = new Button();
        button5.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button7, 0.0);
        AnchorPane.setRightAnchor(button7, 0.0);
        AnchorPane.setBottomAnchor(button7, 0.0);
        AnchorPane.setLeftAnchor(button7, 0.0);

        Button button8 = new Button();
        button5.setPadding(new Insets(0, 20, 20, 20));
        AnchorPane.setTopAnchor(button8, 0.0);
        AnchorPane.setRightAnchor(button8, 0.0);
        AnchorPane.setBottomAnchor(button8, 0.0);
        AnchorPane.setLeftAnchor(button8, 0.0);

        // Label 1 + 2
        Label label1 = new Label();
        label1.setWrapText(true);
        AnchorPane.setRightAnchor(label1, 10.0);
        AnchorPane.setBottomAnchor(label1, 10.0);
        AnchorPane.setLeftAnchor(label1, 10.0);

        Label label2 = new Label();
        label2.setWrapText(true);
        AnchorPane.setRightAnchor(label2, 10.0);
        AnchorPane.setBottomAnchor(label2, 10.0);
        AnchorPane.setLeftAnchor(label2, 10.0);

        Label label3 = new Label();
        label3.setWrapText(true);
        AnchorPane.setRightAnchor(label3, 10.0);
        AnchorPane.setBottomAnchor(label3, 10.0);
        AnchorPane.setLeftAnchor(label3, 10.0);

        Label label4 = new Label();
        label4.setWrapText(true);
        AnchorPane.setRightAnchor(label4, 10.0);
        AnchorPane.setBottomAnchor(label4, 10.0);
        AnchorPane.setLeftAnchor(label4, 10.0);

        Label label5 = new Label();
        label5.setWrapText(true);
        AnchorPane.setRightAnchor(label5, 10.0);
        AnchorPane.setBottomAnchor(label5, 10.0);
        AnchorPane.setLeftAnchor(label5, 10.0);

        Label label6 = new Label();
        label6.setWrapText(true);
        AnchorPane.setRightAnchor(label6, 10.0);
        AnchorPane.setBottomAnchor(label6, 10.0);
        AnchorPane.setLeftAnchor(label6, 10.0);

        Label label7 = new Label();
        label7.setWrapText(true);
        AnchorPane.setRightAnchor(label7, 10.0);
        AnchorPane.setBottomAnchor(label7, 10.0);
        AnchorPane.setLeftAnchor(label7, 10.0);

        Label label8 = new Label();
        label8.setWrapText(true);
        AnchorPane.setRightAnchor(label8, 10.0);
        AnchorPane.setBottomAnchor(label8, 10.0);
        AnchorPane.setLeftAnchor(label8, 10.0);

        anchorPane1.getChildren().addAll(button1, label1);
        anchorPane2.getChildren().addAll(button2, label2);
        anchorPane3.getChildren().addAll(button3, label3);
        anchorPane4.getChildren().addAll(button4, label4);
        anchorPane5.getChildren().addAll(button5, label5);
        anchorPane6.getChildren().addAll(button6, label6);
        anchorPane7.getChildren().addAll(button7, label7);
        anchorPane8.getChildren().addAll(button8, label8);

        Button[] buttonList = {button1, button2, button3, button4, button5, button6, button7, button8};
        Label[] labelList = {label1, label2, label3, label4, label5, label6, label7, label8};
        AnchorPane[] anchorPaneList = {anchorPane1, anchorPane2, anchorPane3, anchorPane4, anchorPane5, anchorPane6, anchorPane7, anchorPane8};
        ArrayList<Article> articleList = new ArrayList<>();

        // Set up display vbox
        VBox displayVbox = new VBox();
        displayVbox.setPadding(new Insets(100, 150, 100, 150));
        displayVbox.setSpacing(15);
        displayVbox.setFillWidth(true);
        displayVbox.setAlignment(Pos.TOP_CENTER);
        displayVbox.setVisible(false);
        Button backButton = new Button("Back");
        displayVbox.getChildren().addAll(backButton, new Button());
        ScrollPane scrollPane2 = new ScrollPane();
        scrollPane2.setContent(displayVbox);
        scrollPane2.setFitToWidth(true);
        AnchorPane.setTopAnchor(scrollPane2, 0.0);
        AnchorPane.setRightAnchor(scrollPane2, 0.0);
        AnchorPane.setBottomAnchor(scrollPane2, 0.0);
        AnchorPane.setLeftAnchor(scrollPane2, 0.0);
        /*displayVbox.setMaxWidth(Main.stage.getWidth() - 80);
        displayVbox.setMinWidth(Main.stage.getWidth() - 80);*/
        Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                displayVbox.setMaxWidth(Main.stage.getWidth() - 80);
                displayVbox.setMinWidth(Main.stage.getWidth() - 80);
                if (t1.doubleValue() < 900) {
                    displayVbox.setPadding(new Insets(150, 30, 150, 30));
                }
                if (t1.doubleValue() >= 900) {
                    displayVbox.setPadding(new Insets(150, 200, 150, 200));
                }
                if (t1.doubleValue() > 1400) {
                    displayVbox.setPadding(new Insets(150, 400, 150, 400));
                }
            }
        });
        Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                anchorPaneAboveScrollPane.setMaxWidth(t1.doubleValue() - 50);
            }
        });

        pagination.setPageFactory(pageIndex -> {
            for (int i = 0; i < 8; i++) {
                buttonList[i].getStyleClass().add("buttonhome");
                labelList[i].getStyleClass().add("labelhomelayoutbutton");
                anchorPaneList[i].getStyleClass().add("anchorpanebutton");
                BackgroundImage backgroundImage = new BackgroundImage(new Image(Main.zingNewsList.get(i).getThumb()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
                Background background = new Background(backgroundImage);
                anchorPaneList[i].setBackground(background);
                labelList[i].setText(Main.zingNewsList.get(i).getTitle());
                articleList.add(Main.zingNewsList.get(i));
            }
            for (int i = 0; i < 8; i++) {
                int finalI = i;
                anchorPaneList[i].addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    try {
                        articlesManager.displayZingFullArticle(articleList.get(finalI), displayVbox);
                        displayVbox.setVisible(true);
                        anchorPaneAboveScrollPane.getChildren().remove(scrollPane);
                        anchorPaneAboveScrollPane.getChildren().add(scrollPane2);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    e.consume();
                });
            }
            return wrapAnchorPane;
        });

        backButton.setOnAction(e -> {
            anchorPaneAboveScrollPane.getChildren().remove(scrollPane2);
            anchorPaneAboveScrollPane.getChildren().add(scrollPane);
        });
    }

    public void menuPaneSetVisble(boolean a) {
        if (a) {
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

    public void menu(ActionEvent event) {
        if (!menuPane.isVisible()) {
            menuPaneSetVisble(true);
        }
        else {
            menuPaneSetVisble(false);
        }
    }

    public void home() {
        homeButton.setSelected(true);
        videoButton.setSelected(false);
        articlesListButton.setSelected(false);
        settingsButton.setSelected(false);
        todayLabel.setText("Today");
    }
    public void video() {
        homeButton.setSelected(false);
        videoButton.setSelected(true);
        articlesListButton.setSelected(false);
        settingsButton.setSelected(false);
        todayLabel.setText("Video Hub");
    }
    public void articlesList() {
        homeButton.setSelected(false);
        videoButton.setSelected(false);
        articlesListButton.setSelected(true);
        settingsButton.setSelected(false);
        todayLabel.setText("Articles List");
    }
    public void settings() {
        homeButton.setSelected(false);
        videoButton.setSelected(false);
        articlesListButton.setSelected(false);
        settingsButton.setSelected(true);
        todayLabel.setText("Settings");
    }
    public void homeInMenu() {
        menuPaneSetVisble(false);
        home();
    }
    public void videoInMenu() {
        menuPaneSetVisble(false);
        video();
    }
    public void articlesListInMenu() {
        menuPaneSetVisble(false);
        articlesList();
    }
    public void settingsInMenu() {
        menuPaneSetVisble(false);
        settings();
    }
    public void search() {

    }
    public void refresh() {

    }
    public void takeSearchInput(MouseEvent event) {
        tempPane.setVisible(true);
    }
    public void exitSearch(MouseEvent event) {
        tempPane.setVisible(false);
        menuButton.requestFocus();
    }
    public void displayNewsList() {
        currentCategory = 0;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(newsButton));
        newsButton.setSelected(true);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Today");
    }
    public void displayCovidList() {
        currentCategory = 1;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(covidButton));
        newsButton.setSelected(false);
        covidButton.setSelected(true);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Covid-19");
    }
    public void displayPoliticsList() {
        currentCategory = 2;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(politicsButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(true);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Politics");
    }
    public void displayBusinessList() {
        currentCategory = 3;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(businessButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(true);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Business");
    }
    public void displayTechnologyList() {
        currentCategory = 4;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(technologyButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(true);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Technology");
    }
    public void displayHealthList() {
        currentCategory = 5;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(healthButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(true);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Health");
    }
    public void displaySportsList() {
        currentCategory = 6;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(sportsButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(true);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Sport");
    }
    public void displayEntertainmentList() {
        currentCategory = 7;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(entertainmentButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(true);
        worldButton.setSelected(false);
        othersButton.setSelected(false);
        todayLabel.setText("Entertainment");
    }
    public void displayWorldList() {
        currentCategory = 8;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(worldButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(true);
        othersButton.setSelected(false);
        todayLabel.setText("World");
    }
    public void displayOthersList() {
        currentCategory = 9;
        removePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(othersButton));
        newsButton.setSelected(false);
        covidButton.setSelected(false);
        politicsButton.setSelected(false);
        businessButton.setSelected(false);
        technologyButton.setSelected(false);
        healthButton.setSelected(false);
        sportsButton.setSelected(false);
        entertainmentButton.setSelected(false);
        worldButton.setSelected(false);
        othersButton.setSelected(true);
        todayLabel.setText("Others");
    }
    public void nextCategoryList() {
        ToggleButton b = (ToggleButton) categoryHbox.getChildren().get(0);
        categoryHbox.getChildren().remove(0);
        categoryHbox.getChildren().add(b);
        if (newsButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayNewsList();
        }
        if (covidButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayCovidList();
        }
        if (politicsButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayPoliticsList();
        }
        if (businessButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayBusinessList();
        }
        if (technologyButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayTechnologyList();
        }
        if (healthButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayHealthList();
        }
        if (sportsButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displaySportsList();
        }
        if (entertainmentButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayEntertainmentList();
        }
        if (worldButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayWorldList();
        }
        if (othersButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayOthersList();
        }
    }
    public void previousCategoryList() {
        ToggleButton b = (ToggleButton) categoryHbox.getChildren().get(categoryHbox.getChildren().size() - 1);
        categoryHbox.getChildren().remove(categoryHbox.getChildren().size() - 1);
        categoryHbox.getChildren().add(0, b);
        if (newsButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayNewsList();
        }
        if (covidButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayCovidList();
        }
        if (politicsButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayPoliticsList();
        }
        if (businessButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayBusinessList();
        }
        if (technologyButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayTechnologyList();
        }
        if (healthButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayHealthList();
        }
        if (sportsButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displaySportsList();
        }
        if (entertainmentButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayEntertainmentList();
        }
        if (worldButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayWorldList();
        }
        if (othersButton.equals((ToggleButton) categoryHbox.getChildren().get(0))) {
            displayOthersList();
        }
    }

    // This function will help to move the position of user's selection category node to the first node
    public void removePosHbox(HBox categoryHbox, int currentPosition) {
        int initialSize = categoryHbox.getChildren().size();
        if (currentPosition != 0) {
            for (int i = 0; i < initialSize - currentPosition; i++) {
                ToggleButton b = (ToggleButton) categoryHbox.getChildren().get(categoryHbox.getChildren().size() - 1);
                categoryHbox.getChildren().remove(categoryHbox.getChildren().size() - 1);
                categoryHbox.getChildren().add(0, b);
            }
        }
    }

}
