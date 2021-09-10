package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeSceneController implements Initializable {

    @FXML
    private Button menuButton, refreshButton, searchButton, nextCategoryButton, previousCategoryButton, backToHomeButton, copyArticleLinkButton;

    @FXML
    private ToggleButton homeButton, videoButton, articlesListButton, settingsButton, darkModeButton;

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
    private ScrollPane scrollPane;

    @FXML
    private BorderPane borderPaneUnderScrollPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private SVGPath darkModeSVGPath;

    public LayoutController layoutController;

    public int currentCategoryIndex = 0, currentArticleIndex = 0;

    public ArrayList<Article> currentCategoryList;

    public Pagination currentPagination;

    public StackPane stackPane1 = new StackPane();
    public StackPane stackPane2 = new StackPane();

    public StackPane loadingStackPane = new StackPane();
    ImageView loadingImageView;
    Image whiteLoadingImage;
    Image darkLoadingImage;

    public VBox displayFullArticleVbox = new VBox();

    public VBox displayLayoutVbox = new VBox();

    public boolean isDarkMode = true;

    public static StopWatch stopWatch = new StopWatch("My Stop Watch");

    public HomeSceneController() {
        // Setup fxml loader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Layout.fxml"));

        // Get the vbox root of the layout.fxml
        try {
            displayLayoutVbox = (VBox) loader.load();
            displayLayoutVbox.setCache(true);
            displayLayoutVbox.setCacheHint(CacheHint.SPEED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isDarkMode) {
            displayLayoutVbox.getStylesheets().clear();
            displayLayoutVbox.getStylesheets().add("/css/cssdarkmode.css");
        } else {
            displayLayoutVbox.getStylesheets().clear();
            displayLayoutVbox.getStylesheets().add("/css/css.css");
        }

        // Get the LayoutController instance of layout.fxml
        layoutController = (LayoutController) loader.getController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set initial focus
        homeButton.setSelected(true);
        newsButton.setSelected(true);

        // Setup loading stack pane
        whiteLoadingImage = new Image("/resource/book_loadinganimation_white.gif");
        darkLoadingImage = new Image("/resource/book_loadinganimation_dark.gif");
        loadingImageView = new ImageView();
        loadingStackPane.getChildren().add(loadingImageView);
        Main.stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                loadingStackPane.setMinHeight(t1.doubleValue() - 95);
                loadingStackPane.setMaxHeight(t1.doubleValue() - 95);
            }
        });
        loadingStackPane.getStyleClass().add("stackpane");

        // Set initial dark mode
        if (isDarkMode) {
            loadingImageView.setImage(darkLoadingImage);
            rootAnchorPane.getStylesheets().clear();
            rootAnchorPane.getStylesheets().add("/css/cssdarkmode.css");
            displayLayoutVbox.getStylesheets().clear();
            displayLayoutVbox.getStylesheets().add("/css/cssdarkmode.css");
            darkModeSVGPath.setContent("M15 3L15 8L17 8L17 3 Z M 7.5 6.09375L6.09375 7.5L9.625 11.0625L11.0625 9.625 Z M 24.5 6.09375L20.9375 9.625L22.375 11.0625L25.90625 7.5 Z M 16 9C12.144531 9 9 12.144531 9 16C9 19.855469 12.144531 23 16 23C19.855469 23 23 19.855469 23 16C23 12.144531 19.855469 9 16 9 Z M 16 11C18.773438 11 21 13.226563 21 16C21 18.773438 18.773438 21 16 21C13.226563 21 11 18.773438 11 16C11 13.226563 13.226563 11 16 11 Z M 3 15L3 17L8 17L8 15 Z M 24 15L24 17L29 17L29 15 Z M 9.625 20.9375L6.09375 24.5L7.5 25.90625L11.0625 22.375 Z M 22.375 20.9375L20.9375 22.375L24.5 25.90625L25.90625 24.5 Z M 15 24L15 29L17 29L17 24Z");
            isDarkMode = true;
        } else {
            loadingImageView.setImage(whiteLoadingImage);
            rootAnchorPane.getStylesheets().clear();
            rootAnchorPane.getStylesheets().add("/css/css.css");
            displayLayoutVbox.getStylesheets().clear();
            displayLayoutVbox.getStylesheets().add("/css/css.css");
            darkModeSVGPath.setContent("M6 .278a.768.768 0 0 1 .08.858 7.208 7.208 0 0 0-.878 3.46c0 4.021 3.278 7.277 7.318 7.277.527 0 1.04-.055 1.533-.16a.787.787 0 0 1 .81.316.733.733 0 0 1-.031.893A8.349 8.349 0 0 1 8.344 16C3.734 16 0 12.286 0 7.71 0 4.266 2.114 1.312 5.124.06A.752.752 0 0 1 6 .278z");
            isDarkMode = false;
        }

        // Set up menuPane + tempPane to be unvisible
        menuPane.setVisible(false);
        menuPane.setLayoutX(-300);
        tempPane.setVisible(false);

        // Set up visible mode to 2 category arrows
        nextCategoryButton.setVisible(false);
        previousCategoryButton.setVisible(false);
        articlesListPane.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            nextCategoryButton.setVisible(true);
            previousCategoryButton.setVisible(true);
            e.consume();
        });
        articlesListPane.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            nextCategoryButton.setVisible(false);
            previousCategoryButton.setVisible(false);
            e.consume();
        });
        nextCategoryButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            try {
                nextCategoryList();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.consume();
        });
        previousCategoryButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            try {
                previousCategoryList();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.consume();
        });

        // Setup vbox for full article display
        displayFullArticleVbox.setMinHeight(985);
        displayFullArticleVbox.setSpacing(20);
        displayFullArticleVbox.setAlignment(Pos.TOP_CENTER);
        displayFullArticleVbox.setPadding(new Insets(100, 0, 100, 0));
        displayFullArticleVbox.setCache(true);
        displayFullArticleVbox.setCacheHint(CacheHint.SPEED);
        // Setup initial width for vbox
        if (Main.stage.getWidth() < 900) {
            displayFullArticleVbox.setMaxWidth(Main.stage.getWidth() - 140);
            displayFullArticleVbox.setMinWidth(Main.stage.getWidth() - 140);
        }
        if (Main.stage.getWidth() >= 900) {
            displayFullArticleVbox.setMaxWidth(800);
            displayFullArticleVbox.setMinWidth(800);
        }
        Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (t1.doubleValue() < 900) {
                    displayFullArticleVbox.setMaxWidth(t1.doubleValue() - 140);
                    displayFullArticleVbox.setMinWidth(t1.doubleValue() - 140);
                }
                if (t1.doubleValue() >= 900) {
                    displayFullArticleVbox.setMaxWidth(800);
                    displayFullArticleVbox.setMinWidth(800);
                }
            }
        });

        // Stack pane 1 for Border pane 2
        stackPane1.setAlignment(Pos.TOP_CENTER);
        stackPane1.getStyleClass().add("stackpane1");
        stackPane1.setVisible(false);

        // Stack pane 2 for displayFullArticleVbox
        stackPane2.setAlignment(Pos.TOP_CENTER);
        stackPane2.setMaxWidth(1200);
        stackPane2.setMinWidth(1200);
        stackPane2.getStyleClass().add("stackpane2");
        stackPane2.getChildren().add(displayFullArticleVbox);
        stackPane1.getChildren().add(stackPane2);

        // Responsive design (full article vbox)
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

        // Setup nextArticleButton and previousArticleButton
        Button previousArticleButton = new Button();
        previousArticleButton.getStyleClass().add("previousAndNextArticleButton");
        previousArticleButton.setPrefSize(30, 100);
        Button nextArticleButton = new Button();
        nextArticleButton.getStyleClass().add("previousAndNextArticleButton");
        nextArticleButton.setPrefSize(30, 100);
        mainPane.getChildren().addAll(previousArticleButton, nextArticleButton);
        // Set graphic for each button
        SVGPath previousSVG = new SVGPath();
        previousSVG.setContent("M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z");
        previousArticleButton.setGraphic(previousSVG);
        SVGPath nextSVG = new SVGPath();
        nextSVG.setContent("M1 8a.5.5 0 0 1 .5-.5h11.793l-3.147-3.146a.5.5 0 0 1 .708-.708l4 4a.5.5 0 0 1 0 .708l-4 4a.5.5 0 0 1-.708-.708L13.293 8.5H1.5A.5.5 0 0 1 1 8z");
        previousArticleButton.setGraphic(previousSVG);
        nextArticleButton.setGraphic(nextSVG);
        // Set constrain layoutX and layoutY for each button
        previousArticleButton.translateXProperty().bind(stackPane2.layoutXProperty().add(1));
        previousArticleButton.translateYProperty().bind(mainPane.heightProperty().divide(2));
        NumberBinding number = Bindings.add(stackPane2.layoutXProperty(), stackPane2.widthProperty());
        nextArticleButton.translateXProperty().bind(number.subtract(30));
        nextArticleButton.translateYProperty().bind(mainPane.heightProperty().divide(2));
        previousArticleButton.visibleProperty().bind(stackPane1.visibleProperty());
        nextArticleButton.visibleProperty().bind(stackPane1.visibleProperty());
        // Setup action event for each button
        previousArticleButton.setOnAction(e -> {
            try {
                previousArticle();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        nextArticleButton.setOnAction(e -> {
            try {
                nextArticle();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        // Setup back to home button visibleProperty
        backToHomeButton.visibleProperty().bind(stackPane1.visibleProperty());

        // Setup copy article link button visibleProperty
        copyArticleLinkButton.visibleProperty().bind(stackPane1.visibleProperty());

        // Setup refresh Button visibleProperty
//        refreshButton.visibleProperty().bind(stackPane1.visibleProperty().not());
        stackPane1.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) refreshButton.setDisable(true);
                else refreshButton.setDisable(false);
            }
        });

        // Setup initial category (pagination)
        try {
            borderPaneUnderScrollPane.setCenter(null);
            borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.newsList, new Pagination()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup text field action
        searchTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                search();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                exitSearch();
            }
        });
    }

    public void menuPaneSetVisible(boolean a) {
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
            menuPaneSetVisible(true);
        }
        else {
            menuPaneSetVisible(false);
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
        menuPaneSetVisible(false);
        home();
    }
    public void videoInMenu() {
        menuPaneSetVisible(false);
        video();
    }
    public void articlesListInMenu() {
        menuPaneSetVisible(false);
        articlesList();
    }
    public void settingsInMenu() {
        menuPaneSetVisible(false);
        settings();
    }
    public void search() {
        String searchText = searchTextField.getText().trim();
        searchText = searchText.replaceAll("[\\s]+", " ");
        System.out.println(searchText);
    }
    public void refresh() throws IOException {
        switch (currentCategoryIndex) {
            case 0:
                displayNewsList();
                break;
            case 1:
                displayCovidList();
                break;
            case 2:
                displayPoliticsList();
                break;
            case 3:
                displayBusinessList();
                break;
            case 4:
                displayTechnologyList();
                break;
            case 5:
                displayHealthList();
                break;
            case 6:
                displaySportsList();
                break;
            case 7:
                displayEntertainmentList();
                break;
            case 8:
                displayWorldList();
                break;
            case 9:
                displayOthersList();
                break;
            default:
                break;
        }
    }
    public void backToHome(ActionEvent event) {
        if (!tempPane.isVisible()) {
            scrollPane.setVvalue(0);
            for (int i = 0; i < ArticlesManager.changeListenerList.size(); i++) {
                Main.stage.widthProperty().removeListener(ArticlesManager.changeListenerList.get(i));
            }
            ArticlesManager.changeListenerList.clear();
            for (int i = 0; i < displayFullArticleVbox.getChildren().size(); i++) {
                if (displayFullArticleVbox.getChildren().get(i) instanceof ImageView) {
                    ((ImageView) displayFullArticleVbox.getChildren().get(i)).setImage(null);
                }
            }
            displayFullArticleVbox.getChildren().clear();
            borderPaneUnderScrollPane.setCenter(null);
            borderPaneUnderScrollPane.setCenter(currentPagination);
            stackPane1.setVisible(false);
            System.gc();
            Runtime.getRuntime().gc();
            homeButton.requestFocus();
        } else {
            tempPane.setVisible(false);
        }
    }
    public void copyArticleLink() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(currentCategoryList.get(currentArticleIndex).getLinkToFullArticles());
//        content.putHtml("<b>Some</b> text");
        clipboard.setContent(content);
    }
    public void takeSearchInput(MouseEvent event) {
        tempPane.setVisible(true);
    }
    public void exitSearch() {
        tempPane.setVisible(false);
        menuButton.requestFocus();
        searchTextField.clear();
    }
    
    public void displayHealthList() throws IOException {
        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 5;
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
        stackPane1.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getHealthList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.healthList, new Pagination()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displaySportsList() throws IOException {
        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 6;
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
        stackPane1.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getSportsList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.sportsList, new Pagination()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayEntertainmentList() throws IOException {
        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 7;
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
        stackPane1.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getEntertainmentList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.entertainmentList, new Pagination()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayWorldList() throws IOException {
        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 8;
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
        stackPane1.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getWorldList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.worldList, new Pagination()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayOthersList() throws IOException {
        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 9;
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
        stackPane1.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getOthersList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.othersList, new Pagination()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }

    public void previousCategoryList() throws IOException {
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
    public void nextCategoryList() throws IOException {
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

    public void previousArticle() throws IOException {

        if (currentArticleIndex > 0) {
            scrollPane.setVvalue(0);
            scrollPane.setHvalue(0);

            for (int i = 0; i < ArticlesManager.changeListenerList.size(); i++) {
                Main.stage.widthProperty().removeListener(ArticlesManager.changeListenerList.get(i));
            }

            ArticlesManager.changeListenerList.clear();

            for (int i = 0; i < displayFullArticleVbox.getChildren().size(); i++) {
                if (displayFullArticleVbox.getChildren().get(i) instanceof ImageView) {
                    ((ImageView) displayFullArticleVbox.getChildren().get(i)).setImage(null);
                }
            }

            displayFullArticleVbox.getChildren().clear();

            switch (currentCategoryList.get(currentArticleIndex - 1).getSource()) {
                case "vnexpress":
                    try {
                        ArticlesManager.displayVnexpressFullArticle(currentCategoryList.get(currentArticleIndex - 1), displayFullArticleVbox);
                        currentArticleIndex--;
                        break;
                    } catch (Exception e) {
                        currentCategoryIndex--;
                        break;
                    }
                case "zingnews":
                    try {
                        ArticlesManager.displayZingFullArticle(currentCategoryList.get(currentArticleIndex - 1), displayFullArticleVbox);
                        currentArticleIndex--;
                        break;
                    } catch (Exception e) {
                        currentCategoryIndex--;
                        break;
                    }
                case "tuoitre":
                    try {
                        ArticlesManager.displayTuoiTreFullArticle(currentCategoryList.get(currentArticleIndex - 1), displayFullArticleVbox);
                        currentArticleIndex--;
                        break;
                    } catch (Exception e) {
                        currentCategoryIndex--;
                        break;
                    }
                case "nhandan":
                    try {
                        ArticlesManager.displayNhanDanFullArticle(currentCategoryList.get(currentArticleIndex - 1), displayFullArticleVbox);
                        currentArticleIndex--;
                        break;
                    } catch (Exception e) {
                        currentCategoryIndex--;
                        break;
                    }
                case "thanhnien":
                    try {
                        ArticlesManager.displayThanhNienFullArticle(currentCategoryList.get(currentArticleIndex - 1), displayFullArticleVbox);
                        currentArticleIndex--;
                        break;
                    } catch (Exception e) {
                        currentCategoryIndex--;
                        break;
                    }
            }

            System.gc();
            Runtime.getRuntime().gc();
        }
    }
    public void nextArticle() throws IOException {

        if (currentArticleIndex < 49) {
            scrollPane.setVvalue(0);
            scrollPane.setHvalue(0);

            for (int i = 0; i < ArticlesManager.changeListenerList.size(); i++) {
                Main.stage.widthProperty().removeListener(ArticlesManager.changeListenerList.get(i));
            }
            ArticlesManager.changeListenerList.clear();

            for (int i = 0; i < displayFullArticleVbox.getChildren().size(); i++) {
                if (displayFullArticleVbox.getChildren().get(i) instanceof ImageView) {
                    ((ImageView) displayFullArticleVbox.getChildren().get(i)).setImage(null);
                }
            }

            displayFullArticleVbox.getChildren().clear();
            System.gc();
            Runtime.getRuntime().gc();

            switch (currentCategoryList.get(currentArticleIndex + 1).getSource()) {
                case "vnexpress":
                    try {
                        ArticlesManager.displayVnexpressFullArticle(currentCategoryList.get(currentArticleIndex + 1), displayFullArticleVbox);
                        currentArticleIndex++;
                        break;
                    } catch (Exception e) {
                        currentArticleIndex++;
                        break;
                    }
                case "zingnews":
                    try {
                        ArticlesManager.displayZingFullArticle(currentCategoryList.get(currentArticleIndex + 1), displayFullArticleVbox);
                        currentArticleIndex++;
                        break;
                    } catch (Exception e) {
                        currentArticleIndex++;
                        break;
                    }
                case "tuoitre":
                    try {
                        ArticlesManager.displayTuoiTreFullArticle(currentCategoryList.get(currentArticleIndex + 1), displayFullArticleVbox);
                        currentArticleIndex++;
                        break;
                    } catch (Exception e) {
                        currentArticleIndex++;
                        break;
                    }
                case "nhandan":
                    try {
                        ArticlesManager.displayNhanDanFullArticle(currentCategoryList.get(currentArticleIndex + 1), displayFullArticleVbox);
                        currentArticleIndex++;
                        break;
                    } catch (Exception e) {
                        currentArticleIndex++;
                        break;
                    }
                case "thanhnien":
                    try {
                        ArticlesManager.displayThanhNienFullArticle(currentCategoryList.get(currentArticleIndex + 1), displayFullArticleVbox);
                        currentArticleIndex++;
                        break;
                    } catch (Exception e) {
                        currentArticleIndex++;
                        break;
                    }
            }

        }
    }

    public void darkMode(ActionEvent event) {
        if (isDarkMode) {
            loadingImageView.setImage(whiteLoadingImage);
            rootAnchorPane.getStylesheets().clear();
            rootAnchorPane.getStylesheets().add("/css/css.css");
            displayLayoutVbox.getStylesheets().clear();
            displayLayoutVbox.getStylesheets().add("/css/css.css");
            darkModeSVGPath.setContent("M6 .278a.768.768 0 0 1 .08.858 7.208 7.208 0 0 0-.878 3.46c0 4.021 3.278 7.277 7.318 7.277.527 0 1.04-.055 1.533-.16a.787.787 0 0 1 .81.316.733.733 0 0 1-.031.893A8.349 8.349 0 0 1 8.344 16C3.734 16 0 12.286 0 7.71 0 4.266 2.114 1.312 5.124.06A.752.752 0 0 1 6 .278z");
            isDarkMode = false;
        } else {
            loadingImageView.setImage(darkLoadingImage);
            rootAnchorPane.getStylesheets().clear();
            rootAnchorPane.getStylesheets().add("/css/cssdarkmode.css");
            displayLayoutVbox.getStylesheets().clear();
            displayLayoutVbox.getStylesheets().add("/css/cssdarkmode.css");
            darkModeSVGPath.setContent("M15 3L15 8L17 8L17 3 Z M 7.5 6.09375L6.09375 7.5L9.625 11.0625L11.0625 9.625 Z M 24.5 6.09375L20.9375 9.625L22.375 11.0625L25.90625 7.5 Z M 16 9C12.144531 9 9 12.144531 9 16C9 19.855469 12.144531 23 16 23C19.855469 23 23 19.855469 23 16C23 12.144531 19.855469 9 16 9 Z M 16 11C18.773438 11 21 13.226563 21 16C21 18.773438 18.773438 21 16 21C13.226563 21 11 18.773438 11 16C11 13.226563 13.226563 11 16 11 Z M 3 15L3 17L8 17L8 15 Z M 24 15L24 17L29 17L29 15 Z M 9.625 20.9375L6.09375 24.5L7.5 25.90625L11.0625 22.375 Z M 22.375 20.9375L20.9375 22.375L24.5 25.90625L25.90625 24.5 Z M 15 24L15 29L17 29L17 24Z");
            isDarkMode = true;
        }
    }
}