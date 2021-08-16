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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private BorderPane borderPaneUnderScrollPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private LayoutController layoutController;

    public int currentCategory = 0;

    public ArticlesManager articlesManager = new ArticlesManager();

    public StackPane stackPane1 = new StackPane();

    public VBox displayFullArticleVbox = new VBox();

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

        // Vbox for full article
        displayFullArticleVbox.setMinHeight(985);
        displayFullArticleVbox.setSpacing(20);
        displayFullArticleVbox.setAlignment(Pos.TOP_CENTER);
        displayFullArticleVbox.setPadding(new Insets(0, 0, 100, 0));
        if (Main.stage.getWidth() < 900) {
            displayFullArticleVbox.setMaxWidth(Main.stage.getWidth() - 80);
            displayFullArticleVbox.setMinWidth(Main.stage.getWidth() - 80);
        }
        if (Main.stage.getWidth() >= 900) {
            displayFullArticleVbox.setMaxWidth(800);
            displayFullArticleVbox.setMinWidth(800);
        }
        Button backButton = new Button("back");
        Button nothingButton = new Button("nothing");
        displayFullArticleVbox.getChildren().addAll(backButton, nothingButton);
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            borderPaneUnderScrollPane.setCenter(null);
            borderPaneUnderScrollPane.setCenter(pagination);
        });

        // Stack pane 1 for Border pane 2
        stackPane1.setAlignment(Pos.TOP_CENTER);
        stackPane1.getStyleClass().add("stackpane1");

        // Stack pane 2 for displayFullArticleVbox
        StackPane stackPane2 = new StackPane();
        stackPane2.setAlignment(Pos.TOP_CENTER);
        stackPane2.setMaxWidth(1200);
        stackPane2.setMinWidth(1200);
        stackPane2.getStyleClass().add("stackpane2");
        stackPane2.getChildren().add(displayFullArticleVbox);
        stackPane1.getChildren().add(stackPane2);

        // Next and previous border pane
        BorderPane nextAndPrevBorderPane = new BorderPane();
//            nextAndPrevBorderPane.setMinHeight(985);
        nextAndPrevBorderPane.setMaxHeight(985);
        Button nextArticleButton = new Button("Next");
        nextArticleButton.setPrefSize(30, 65);
        Button previousArticleButton = new Button("Prev");
        previousArticleButton.setPrefSize(30, 65);
        nextAndPrevBorderPane.setRight(nextArticleButton);
        nextAndPrevBorderPane.setLeft(previousArticleButton);
        BorderPane.setAlignment(nextArticleButton, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(previousArticleButton, Pos.CENTER_LEFT);
//            stackPane2.getChildren().add(nextAndPrevBorderPane);

        // Responsive design
        Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (t1.doubleValue() < 1274) {
                    stackPane2.setMaxWidth(t1.doubleValue() - 74);
                    stackPane2.setMinWidth(t1.doubleValue() - 74);
                }
                if (t1.doubleValue() >= 1274) {
                    stackPane2.setMaxWidth(1200);
                    stackPane2.setMinWidth(1200);
                }
            }
        });

        // Setup pagination
        try {
            pagination.setMaxHeight(980);
            pagination.setMinHeight(980);
            setPaginationList(Main.zingBusinessList, stackPane1, displayFullArticleVbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void refresh() throws IOException {
        switch (currentCategory) {
            case 0:
                setPaginationList(Main.zingBusinessList, stackPane1, displayFullArticleVbox);
                break;
            default:
                break;
        }
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(newsButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(covidButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(politicsButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(businessButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(technologyButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(healthButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(sportsButton));
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
        todayLabel.setText("Sports");
    }
    public void displayEntertainmentList() {
        currentCategory = 7;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(entertainmentButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(worldButton));
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
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(othersButton));
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

    public void setPaginationList(ArrayList<Article> articlesList, StackPane stackPane1, VBox displayFullArticleVbox) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("layout.fxml"));

        // Get the vbox root of the layout.fxml
        VBox displayLayoutVbox = (VBox) loader.load();

        // Get the LayoutController instance of layout.fxml
        layoutController = loader.getController();

        // Setup the lists of childrens in layoutController instance
        AnchorPane[] anchorPaneList = {layoutController.anchorPane1, layoutController.anchorPane2, layoutController.anchorPane3, layoutController.anchorPane4, layoutController.anchorPane5, layoutController.anchorPane6, layoutController.anchorPane7, layoutController.anchorPane8, layoutController.anchorPane9, layoutController.anchorPane10};
        ImageView[] imageSourceList = {layoutController.imageSource1, layoutController.imageSource2, layoutController.imageSource3, layoutController.imageSource4, layoutController.imageSource5, layoutController.imageSource6, layoutController.imageSource7, layoutController.imageSource8, layoutController.imageSource9, layoutController.imageSource10};
        Text[] textSourceList = {layoutController.textSource1, layoutController.textSource2, layoutController.textSource3, layoutController.textSource4, layoutController.textSource5, layoutController.textSource6, layoutController.textSource7, layoutController.textSource8, layoutController.textSource9, layoutController.textSource10};
        Text[] textTitleList = {layoutController.textTitle1, layoutController.textTitle2, layoutController.textTitle3, layoutController.textTitle4, layoutController.textTitle5, layoutController.textTitle6, layoutController.textTitle7, layoutController.textTitle8, layoutController.textTitle9, layoutController.textTitle10};
        Button[] buttonList = {layoutController.button1, layoutController.button2, layoutController.button3, layoutController.button4, layoutController.button5, layoutController.button6, layoutController.button7, layoutController.button8, layoutController.button9, layoutController.button10};

        pagination.setPageFactory(pageindex -> {
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                // Set title text for each article
                textTitleList[i].setText(articlesList.get(i).getTitle());
                textTitleList[i].setDisable(true);
                // Set source text + time ago + source image + action event for each button for each article
                switch (articlesList.get(i).getSource()) {
                    case "vnexpress":
                        textSourceList[i].setText("VnExpress - " + articlesList.get(i).getTimeAgo());
                        imageSourceList[i].setImage(new Image("resource/vnexpress_small.png"));
                        buttonList[i].addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                            try {
                                articlesManager.displayVnexpressFullArticle(articlesList.get(finalI), displayFullArticleVbox);
                                borderPaneUnderScrollPane.setCenter(null);
                                borderPaneUnderScrollPane.setCenter(stackPane1);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        });
                        break;
                    case "zingnews":
                        textSourceList[i].setText("ZingNews - " + articlesList.get(i).getTimeAgo());
                        imageSourceList[i].setImage(new Image("resource/zingnews_small.png"));
                        buttonList[i].addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                            try {
                                articlesManager.displayZingFullArticle(articlesList.get(finalI), displayFullArticleVbox);
                                borderPaneUnderScrollPane.setCenter(null);
                                borderPaneUnderScrollPane.setCenter(stackPane1);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        });
                        break;
                    case "tuoitre":
                        textSourceList[i].setText("Tuổi trẻ - " + Main.vnexpressNewsList.get(i).getTimeAgo());
                        imageSourceList[i].setImage(new Image("resource/tuoitre_small.png"));
                        break;
                    case "nhandan":
                        textSourceList[i].setText("Nhân dân - " + Main.vnexpressNewsList.get(i).getTimeAgo());
                        imageSourceList[i].setImage(new Image("resource/nhandan_small.png")); break;
                    case "thanhnien":
                        textSourceList[i].setText("Thanh niên - " + Main.vnexpressNewsList.get(i).getTimeAgo());
                        imageSourceList[i].setImage(new Image("resource/thanhnien_small.png")); break;
                }
                // Set thumb image for each object
                if (i != 3 && i != 7) {
                    BackgroundImage backgroundImage = new BackgroundImage(new Image(articlesList.get(i).getThumb()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
                    Background background = new Background(backgroundImage);
                    anchorPaneList[i].setBackground(background);
                } else {
                    BackgroundImage backgroundImage = new BackgroundImage(new Image(articlesList.get(i).getThumb()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
                    Background background = new Background(backgroundImage);
                    if (i == 3) layoutController.anchorPaneImage1.setBackground(background);
                    else layoutController.anchorPaneImage2.setBackground(background);
                }
            }
            return displayLayoutVbox;
        });
    }

    // This function will help to move the position of user's selection category node to the first node
    public void movePosHbox(HBox categoryHbox, int currentPosition) {
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
