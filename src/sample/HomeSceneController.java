package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeSceneController implements Initializable {

    @FXML
    private Button menuButton, refreshButton, nextCategoryButton, previousCategoryButton, backToHomeButton, copyArticleLinkButton, openInBrowserButton, darkModeButton;

    @FXML
    private ToggleButton homeButton, videoButton, articlesListButton, settingsButton;

    @FXML
    private ToggleButton newsButton, covidButton, politicsButton, businessButton, technologyButton,
            healthButton, sportsButton, entertainmentButton, worldButton, othersButton;

    @FXML
    private Button homeInMenuButton, videoInMenuButton, articlesListInMenuButton, settingsInMenuButton;

    @FXML
    private RadioButton darkModeRadioButton, lightModeRadioButton, normalFontSizeRadioButton, largeFontSizeRadioButton, veryLargeFontSizeRadioButton;

    @FXML
    private VBox displaySettingsLayoutVbox;

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

    // Get current
    public int currentCategoryIndex = 0, currentArticleIndex = 0;
    public ArrayList<Article> currentCategoryList;
    public Pagination currentPagination;

    // Toggle ground for radio button in setting menu
    public ToggleGroup modeToggleGroup = new ToggleGroup();
    public ToggleGroup fontToggleGroup = new ToggleGroup();
    public ToggleGroup button1ToggleGroup = new ToggleGroup();
    public ToggleGroup button3ToggleGroup = new ToggleGroup();

    // Layout controller instance
    public LayoutController layoutController;

    // All stack panes and vboxs for display layout and full article
    public StackPane stackPane1 = new StackPane();
    public StackPane stackPane2 = new StackPane();
    public VBox displayFullArticleVbox = new VBox();
    public VBox displayLayoutVbox = new VBox();

    // Loading animation stack pane
    public StackPane loadingStackPane = new StackPane();
    ImageView loadingImageView;
    Image whiteLoadingImage;
    Image darkLoadingImage;

    // Initial Mode
    public boolean isDarkMode = true;

    // Check animation
    public boolean animationFinish;

    public String searchText;

    public HomeSceneController() {
        // Setup for layout controller
        // Setup fxml loader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Layout.fxml"));

        // Get the vbox root of the layout.fxml
        try {
            displayLayoutVbox = loader.load();
            displayLayoutVbox.setCache(true);
            displayLayoutVbox.setCacheHint(CacheHint.SPEED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the LayoutController instance of layout.fxml
        layoutController = loader.getController();
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
        loadingImageView.setImage(whiteLoadingImage);
        loadingStackPane.getChildren().add(loadingImageView);
        Main.stage.heightProperty().addListener((observableValue, number, t1) -> {
            loadingStackPane.setMinHeight(t1.doubleValue() - 95);
            loadingStackPane.setMaxHeight(t1.doubleValue() - 95);
        });
        loadingStackPane.getStyleClass().add("stackpane");

        // Set initial dark mode
        if (isDarkMode) {
            loadingImageView.setImage(darkLoadingImage);
            rootAnchorPane.getStylesheets().clear();
            rootAnchorPane.getStylesheets().add("/css/cssdarkmode.css");
            darkModeSVGPath.setContent("M15 3L15 8L17 8L17 3 Z M 7.5 6.09375L6.09375 7.5L9.625 11.0625L11.0625 9.625 Z M 24.5 6.09375L20.9375 9.625L22.375 11.0625L25.90625 7.5 Z M 16 9C12.144531 9 9 12.144531 9 16C9 19.855469 12.144531 23 16 23C19.855469 23 23 19.855469 23 16C23 12.144531 19.855469 9 16 9 Z M 16 11C18.773438 11 21 13.226563 21 16C21 18.773438 18.773438 21 16 21C13.226563 21 11 18.773438 11 16C11 13.226563 13.226563 11 16 11 Z M 3 15L3 17L8 17L8 15 Z M 24 15L24 17L29 17L29 15 Z M 9.625 20.9375L6.09375 24.5L7.5 25.90625L11.0625 22.375 Z M 22.375 20.9375L20.9375 22.375L24.5 25.90625L25.90625 24.5 Z M 15 24L15 29L17 29L17 24Z");
            isDarkMode = true;
        }

        // Setup tool tip
        Tooltip darkModeTooltip = new Tooltip("On/Off Dark Mode");
        darkModeTooltip.setShowDelay(Duration.millis(100));
        darkModeTooltip.setHideDelay(Duration.ZERO);
        darkModeButton.setTooltip(darkModeTooltip);
        Tooltip refreshTooltip = new Tooltip("Refresh Category");
        refreshTooltip.setShowDelay(Duration.millis(100));
        refreshTooltip.setHideDelay(Duration.ZERO);
        refreshButton.setTooltip(refreshTooltip);
        Tooltip copyLinkTooltip = new Tooltip("Copy Link");
        copyLinkTooltip.setShowDelay(Duration.millis(100));
        copyLinkTooltip.setHideDelay(Duration.ZERO);
        copyArticleLinkButton.setTooltip(copyLinkTooltip);
        Tooltip openInBrowserTooltip = new Tooltip("Open In Browser");
        openInBrowserTooltip.setShowDelay(Duration.millis(100));
        openInBrowserTooltip.setHideDelay(Duration.ZERO);
        openInBrowserButton.setTooltip(openInBrowserTooltip);

        // Setup menuPane + tempPane to be unvisible
        menuPane.setVisible(false);
        menuPane.setLayoutX(-300);
        tempPane.setVisible(false);

        // Setup visible mode to 2 category arrows
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
        Main.stage.widthProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() < 900) {
                displayFullArticleVbox.setMaxWidth(t1.doubleValue() - 140);
                displayFullArticleVbox.setMinWidth(t1.doubleValue() - 140);
            }
            if (t1.doubleValue() >= 900) {
                displayFullArticleVbox.setMaxWidth(800);
                displayFullArticleVbox.setMinWidth(800);
            }
        });

        // Stack pane 1 for Border pane 2
        stackPane1.setAlignment(Pos.TOP_CENTER);
        stackPane1.getStyleClass().add("stackpane1");

        // Stack pane 2 for displayFullArticleVbox
        stackPane2.setAlignment(Pos.TOP_CENTER);
        stackPane2.setMaxWidth(1200);
        stackPane2.setMinWidth(1200);
        stackPane2.getStyleClass().add("stackpane2");
        stackPane2.getChildren().add(displayFullArticleVbox);
        stackPane2.setVisible(false);

        stackPane1.getChildren().add(stackPane2);

        // Responsive design (full article vbox)
        Main.stage.widthProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() < 1274) {
                stackPane2.setMaxWidth(t1.doubleValue() - 74);
                stackPane2.setMinWidth(t1.doubleValue() - 74);
            }
            if (t1.doubleValue() >= 1274) {
                stackPane2.setMaxWidth(1200);
                stackPane2.setMinWidth(1200);
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
        previousArticleButton.translateYProperty().bind(mainPane.heightProperty().subtract(80).divide(2));
        NumberBinding number = Bindings.add(stackPane2.layoutXProperty(), stackPane2.widthProperty());
        nextArticleButton.translateXProperty().bind(number.subtract(30));
        nextArticleButton.translateYProperty().bind(mainPane.heightProperty().subtract(80).divide(2));
        previousArticleButton.visibleProperty().bind(stackPane2.visibleProperty());
        nextArticleButton.visibleProperty().bind(stackPane2.visibleProperty());
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

        // Setup copy article link button visibleProperty
        copyArticleLinkButton.visibleProperty().bind(stackPane2.visibleProperty());

        // Setup open in browser button visibleProperty
        openInBrowserButton.visibleProperty().bind(stackPane2.visibleProperty());

        // Refresh button disable property and backtohomebutton visible property
        borderPaneUnderScrollPane.centerProperty().addListener((observableValue, node, t1) -> {
            if (t1 == stackPane1){
                refreshButton.setDisable(true);
            } else {
                refreshButton.setDisable(false);
            }

            if (t1 == currentPagination && !displaySettingsLayoutVbox.isVisible()) {
                backToHomeButton.setVisible(false);
            } else {
                backToHomeButton.setVisible(true);
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

        // Setup toggle group for button 1 and 3
        button1ToggleGroup.getToggles().addAll(homeButton, videoButton, articlesListButton, settingsButton);
        button3ToggleGroup.getToggles().addAll(newsButton, covidButton, politicsButton, businessButton,
                technologyButton, healthButton, sportsButton, entertainmentButton, worldButton, othersButton);
        button1ToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) oldVal.setSelected(true); // always choose 1 toggle to be selected
        });
        button3ToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) oldVal.setSelected(true); // always choose 1 toggle to be selected
        });

        // Setup toggle group for mode and font
        modeToggleGroup.getToggles().addAll(darkModeRadioButton, lightModeRadioButton);
        fontToggleGroup.getToggles().addAll(normalFontSizeRadioButton, largeFontSizeRadioButton, veryLargeFontSizeRadioButton);
        if (isDarkMode) darkModeRadioButton.setSelected(true);
        else lightModeRadioButton.setSelected(true);
        normalFontSizeRadioButton.setSelected(true);

        // Setup listener for toggle group
        fontToggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (fontToggleGroup.getSelectedToggle() == normalFontSizeRadioButton) {
                rootAnchorPane.getStylesheets().remove("/css/largefont.css");
                rootAnchorPane.getStylesheets().remove("/css/verylargefont.css");
            }
            if (fontToggleGroup.getSelectedToggle() == largeFontSizeRadioButton) {
                rootAnchorPane.getStylesheets().remove("/css/verylargefont.css");
                rootAnchorPane.getStylesheets().add("/css/largefont.css");
            }
            if (fontToggleGroup.getSelectedToggle() == veryLargeFontSizeRadioButton) {
                rootAnchorPane.getStylesheets().remove("/css/largefont.css");
                rootAnchorPane.getStylesheets().add("/css/verylargefont.css");
            }
        });

        modeToggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> darkMode(new ActionEvent()));
    }

    public void menuPaneSetVisible(boolean a) {
        if (a) {
            menuPane.setVisible(true);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), menuPane);
            translateTransition1.setByX(+300);
            translateTransition1.play();

            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.2), mainPane);
            translateTransition2.setByX(+212);
            translateTransition2.play();
            translateTransition1.setOnFinished(e -> animationFinish = true);
        }
        else {
            menuPane.setVisible(false);
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.2), menuPane);
            translateTransition1.setByX(-300);
            translateTransition1.play();

            TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.2), mainPane);
            translateTransition2.setByX(-212);
            translateTransition2.play();
            translateTransition1.setOnFinished(e -> animationFinish = false);
        }
    }

    public void menu(ActionEvent event) {
        if (!menuPane.isVisible()) {
            if (!animationFinish) menuPaneSetVisible(true);
        }
        else {
            if (animationFinish) menuPaneSetVisible(false);
        }
    }
    public void home() {
        homeButton.setSelected(true);
        todayLabel.setText("Today");
        backToHome();
    }
    public void video() {
        videoButton.setSelected(true);
        todayLabel.setText("Video Hub");
    }
    public void articlesList() {
        articlesListButton.setSelected(true);
        todayLabel.setText("Articles List");
    }
    public void settings() {
        settingsButton.setSelected(true);
        todayLabel.setText("Settings");
        displaySettingsLayoutVbox.setVisible(true);
        stackPane2.setVisible(false);
        backToHomeButton.setVisible(true);
    }
    public void homeInMenu() {
        if (animationFinish) {
            menuPaneSetVisible(false);
            home();
        }
    }
    public void videoInMenu() {
        if (animationFinish) {
            menuPaneSetVisible(false);
            video();
        }
    }
    public void articlesListInMenu() {
        if (animationFinish) {
            menuPaneSetVisible(false);
            articlesList();
        }
    }
    public void settingsInMenu() {
        if (animationFinish) {
            menuPaneSetVisible(false);
            settings();
        }
    }

    public void search() {
        if (!searchTextField.getText().trim().equals("")) {
            searchText = searchTextField.getText().trim();
            searchText = searchText.replaceAll("[\\s]+", " ");
        }
        if (!searchText.isEmpty()) {
            scrollPane.setVvalue(0);
            scrollPane.setHvalue(0);
            currentCategoryIndex = 10;
            todayLabel.setText("Search");
            displaySettingsLayoutVbox.setVisible(false);
            stackPane2.setVisible(false);
            borderPaneUnderScrollPane.setCenter(null);
            borderPaneUnderScrollPane.setCenter(loadingStackPane);
            String finalSearchText = searchText;
            Thread t1 = new Thread(() -> {
                try {
                    backToHomeButton.setVisible(false);
                    ArticlesList.getSearchList(finalSearchText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    borderPaneUnderScrollPane.setCenter(null);
                    try {
                        borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.searchList, new Pagination()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.gc();
                    Runtime.getRuntime().gc();
                });
            });
            t1.start();
            exitSearch();
        }
    }
    public void refresh() {
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
            case 10:
                search();
                break;
            default:
                break;
        }
    }
    public void backToHome() {
        homeButton.setSelected(true);
        todayLabel.setText("Today");

        if (tempPane.isVisible()) {
            tempPane.setVisible(false);
        } else if (displaySettingsLayoutVbox.isVisible()) {
            if (borderPaneUnderScrollPane.getCenter() == currentPagination) {
                displaySettingsLayoutVbox.setVisible(false);
                backToHomeButton.setVisible(false);
            } else {
                displaySettingsLayoutVbox.setVisible(false);
                stackPane2.setVisible(true);
            }
        } else {
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
            stackPane2.setVisible(false);
            System.gc();
            Runtime.getRuntime().gc();
            homeButton.requestFocus();
        }
    }
    public void copyArticleLink() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(currentCategoryList.get(currentArticleIndex).getLinkToFullArticles());
        clipboard.setContent(content);
    }
    public void openInBrowser() {
        HostServices services = Helper.getInstance().getHostServices();
        services.showDocument(currentCategoryList.get(currentArticleIndex).getLinkToFullArticles());
    }
    public void takeSearchInput(MouseEvent event) {
        tempPane.setVisible(true);
    }
    public void exitSearch() {
        tempPane.setVisible(false);
        menuButton.requestFocus();
        searchTextField.clear();
    }

    public void displayNewsList() {
        homeButton.setSelected(true);
        newsButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 0;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(newsButton));
        todayLabel.setText("Today");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getNewsList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.newsList, new Pagination()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayCovidList() {
        homeButton.setSelected(true);
        covidButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 1;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(covidButton));
        todayLabel.setText("Covid-19");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getCovidList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.covidList, new Pagination()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayPoliticsList() {
        homeButton.setSelected(true);
        politicsButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 2;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(politicsButton));
        todayLabel.setText("Politics");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getPoliticsList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.politicsList, new Pagination()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayBusinessList() {
        homeButton.setSelected(true);
        businessButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 3;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(businessButton));
        todayLabel.setText("Business");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getBusinessList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.businessList, new Pagination()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayTechnologyList() {
        homeButton.setSelected(true);
        technologyButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 4;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(technologyButton));
        todayLabel.setText("Technology");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
        borderPaneUnderScrollPane.setCenter(null);
        borderPaneUnderScrollPane.setCenter(loadingStackPane);
        Thread t1 = new Thread(() -> {
            try {
                ArticlesList.getTechnologyList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                borderPaneUnderScrollPane.setCenter(null);
                try {
                    borderPaneUnderScrollPane.setCenter(setPaginationList(ArticlesList.technologyList, new Pagination()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.gc();
                Runtime.getRuntime().gc();
            });
        });
        t1.start();
    }
    public void displayHealthList() {
        homeButton.setSelected(true);
        healthButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 5;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(healthButton));
        todayLabel.setText("Health");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
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
    public void displaySportsList() {
        homeButton.setSelected(true);
        sportsButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 6;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(sportsButton));
        todayLabel.setText("Sports");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
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
    public void displayEntertainmentList() {
        homeButton.setSelected(true);
        entertainmentButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 7;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(entertainmentButton));
        todayLabel.setText("Entertainment");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
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
    public void displayWorldList() {
        homeButton.setSelected(true);
        worldButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 8;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(worldButton));
        todayLabel.setText("World");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
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
    public void displayOthersList() {
        homeButton.setSelected(true);
        othersButton.setSelected(true);

        scrollPane.setVvalue(0);
        scrollPane.setHvalue(0);
        currentCategoryIndex = 9;
        movePosHbox(categoryHbox, categoryHbox.getChildren().indexOf(othersButton));
        todayLabel.setText("Others");
        displaySettingsLayoutVbox.setVisible(false);
        stackPane2.setVisible(false);
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
        // Move hbox
        ToggleButton b = (ToggleButton) categoryHbox.getChildren().get(categoryHbox.getChildren().size() - 1);
        categoryHbox.getChildren().remove(categoryHbox.getChildren().size() - 1);
        categoryHbox.getChildren().add(0, b);

        // Display previousCategoryList
        if (newsButton.equals(categoryHbox.getChildren().get(0))) {
            displayNewsList();
        }
        if (covidButton.equals(categoryHbox.getChildren().get(0))) {
            displayCovidList();
        }
        if (politicsButton.equals(categoryHbox.getChildren().get(0))) {
            displayPoliticsList();
        }
        if (businessButton.equals(categoryHbox.getChildren().get(0))) {
            displayBusinessList();
        }
        if (technologyButton.equals(categoryHbox.getChildren().get(0))) {
            displayTechnologyList();
        }
        if (healthButton.equals(categoryHbox.getChildren().get(0))) {
            displayHealthList();
        }
        if (sportsButton.equals(categoryHbox.getChildren().get(0))) {
            displaySportsList();
        }
        if (entertainmentButton.equals(categoryHbox.getChildren().get(0))) {
            displayEntertainmentList();
        }
        if (worldButton.equals(categoryHbox.getChildren().get(0))) {
            displayWorldList();
        }
        if (othersButton.equals(categoryHbox.getChildren().get(0))) {
            displayOthersList();
        }
    }
    public void nextCategoryList() throws IOException {
        // Move hbox
        ToggleButton b = (ToggleButton) categoryHbox.getChildren().get(0);
        categoryHbox.getChildren().remove(0);
        categoryHbox.getChildren().add(b);

        // Display nextCategoryList
        if (newsButton.equals(categoryHbox.getChildren().get(0))) {
            displayNewsList();
        }
        if (covidButton.equals(categoryHbox.getChildren().get(0))) {
            displayCovidList();
        }
        if (politicsButton.equals(categoryHbox.getChildren().get(0))) {
            displayPoliticsList();
        }
        if (businessButton.equals(categoryHbox.getChildren().get(0))) {
            displayBusinessList();
        }
        if (technologyButton.equals(categoryHbox.getChildren().get(0))) {
            displayTechnologyList();
        }
        if (healthButton.equals(categoryHbox.getChildren().get(0))) {
            displayHealthList();
        }
        if (sportsButton.equals(categoryHbox.getChildren().get(0))) {
            displaySportsList();
        }
        if (entertainmentButton.equals(categoryHbox.getChildren().get(0))) {
            displayEntertainmentList();
        }
        if (worldButton.equals(categoryHbox.getChildren().get(0))) {
            displayWorldList();
        }
        if (othersButton.equals(categoryHbox.getChildren().get(0))) {
            displayOthersList();
        }
    }

    public void previousArticle() throws IOException {
        homeButton.setSelected(true);

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

            borderPaneUnderScrollPane.setCenter(null);
            borderPaneUnderScrollPane.setCenter(loadingStackPane);
            Thread t1 = new Thread(() -> {
                // Load new article
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

                Platform.runLater(() -> {
                    borderPaneUnderScrollPane.setCenter(null);
                    borderPaneUnderScrollPane.setCenter(stackPane1);
                    System.gc();
                    Runtime.getRuntime().gc();
                });
            });
            t1.start();
        }
    }
    public void nextArticle() throws IOException {
        homeButton.setSelected(true);

        if (currentArticleIndex < currentCategoryList.size() - 1) {
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

            borderPaneUnderScrollPane.setCenter(null);
            borderPaneUnderScrollPane.setCenter(loadingStackPane);
            Thread t1 = new Thread(() -> {
                // Load new article
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

                Platform.runLater(() -> {
                    borderPaneUnderScrollPane.setCenter(null);
                    borderPaneUnderScrollPane.setCenter(stackPane1);
                    System.gc();
                    Runtime.getRuntime().gc();
                });
            });
            t1.start();
        }
    }

    public void darkMode(ActionEvent e) {
        String currentStyleSheet = "";
        if (rootAnchorPane.getStylesheets().size() > 1) currentStyleSheet = rootAnchorPane.getStylesheets().get(1);

        if (isDarkMode) {
            lightModeRadioButton.setSelected(true);
            loadingImageView.setImage(whiteLoadingImage);
            rootAnchorPane.getStylesheets().clear();
            rootAnchorPane.getStylesheets().add("/css/css.css");
            if (!currentStyleSheet.isEmpty()) {
                rootAnchorPane.getStylesheets().add(currentStyleSheet);
            }
            darkModeSVGPath.setContent("M6 .278a.768.768 0 0 1 .08.858 7.208 7.208 0 0 0-.878 3.46c0 4.021 3.278 7.277 7.318 7.277.527 0 1.04-.055 1.533-.16a.787.787 0 0 1 .81.316.733.733 0 0 1-.031.893A8.349 8.349 0 0 1 8.344 16C3.734 16 0 12.286 0 7.71 0 4.266 2.114 1.312 5.124.06A.752.752 0 0 1 6 .278z");
            isDarkMode = false;
        } else {
            darkModeRadioButton.setSelected(true);
            loadingImageView.setImage(darkLoadingImage);
            rootAnchorPane.getStylesheets().clear();
            rootAnchorPane.getStylesheets().add("/css/cssdarkmode.css");
            if (!currentStyleSheet.isEmpty()) {
                rootAnchorPane.getStylesheets().add(currentStyleSheet);
            }
            darkModeSVGPath.setContent("M15 3L15 8L17 8L17 3 Z M 7.5 6.09375L6.09375 7.5L9.625 11.0625L11.0625 9.625 Z M 24.5 6.09375L20.9375 9.625L22.375 11.0625L25.90625 7.5 Z M 16 9C12.144531 9 9 12.144531 9 16C9 19.855469 12.144531 23 16 23C19.855469 23 23 19.855469 23 16C23 12.144531 19.855469 9 16 9 Z M 16 11C18.773438 11 21 13.226563 21 16C21 18.773438 18.773438 21 16 21C13.226563 21 11 18.773438 11 16C11 13.226563 13.226563 11 16 11 Z M 3 15L3 17L8 17L8 15 Z M 24 15L24 17L29 17L29 15 Z M 9.625 20.9375L6.09375 24.5L7.5 25.90625L11.0625 22.375 Z M 22.375 20.9375L20.9375 22.375L24.5 25.90625L25.90625 24.5 Z M 15 24L15 29L17 29L17 24Z");
            isDarkMode = true;
        }

        e.consume();
    }

    public Pagination setPaginationList(ArrayList<Article> articlesList, Pagination newPagination) throws IOException {
        newPagination.setMaxHeight(983);
        newPagination.setMinHeight(983);
        newPagination.setMaxPageIndicatorCount(5);

        currentCategoryList = articlesList;
        int size = (int) Math.floor((double) currentCategoryList.size() / 10.0);
        newPagination.setPageCount(size);
//        if (size >= 5) newPagination.setPageCount(5);
//        else newPagination.setPageCount(size);
        if (size < 1) {
            newPagination.setPageCount(1);
            newPagination.setPageFactory(pageindex -> {
                Text text = new Text("There is nothing to show!");
                text.getStyleClass().add("textnormal");
                return text;
            });

            System.gc();
            Runtime.getRuntime().gc();
            return newPagination;
        }

        currentPagination = newPagination;

        AnchorPane[] anchorPaneList = {layoutController.anchorPane1, layoutController.anchorPane2, layoutController.anchorPane3, layoutController.anchorPane4, layoutController.anchorPane5, layoutController.anchorPane6, layoutController.anchorPane7, layoutController.anchorPane8, layoutController.anchorPane9, layoutController.anchorPane10};
        ImageView[] imageSourceList = {layoutController.imageSource1, layoutController.imageSource2, layoutController.imageSource3, layoutController.imageSource4, layoutController.imageSource5, layoutController.imageSource6, layoutController.imageSource7, layoutController.imageSource8, layoutController.imageSource9, layoutController.imageSource10};
        Text[] textSourceList = {layoutController.textSource1, layoutController.textSource2, layoutController.textSource3, layoutController.textSource4, layoutController.textSource5, layoutController.textSource6, layoutController.textSource7, layoutController.textSource8, layoutController.textSource9, layoutController.textSource10};
        Text[] textTitleList = {layoutController.textTitle1, layoutController.textTitle2, layoutController.textTitle3, layoutController.textTitle4, layoutController.textTitle5, layoutController.textTitle6, layoutController.textTitle7, layoutController.textTitle8, layoutController.textTitle9, layoutController.textTitle10};
        Button[] buttonList = {layoutController.button1, layoutController.button2, layoutController.button3, layoutController.button4, layoutController.button5, layoutController.button6, layoutController.button7, layoutController.button8, layoutController.button9, layoutController.button10};

        newPagination.setPageFactory(pageindex -> {
            for (int i = 10 * pageindex, k = 0; i < 10 + 10 * pageindex; i++, k++) {
                int finalI = i;
                // Set title text for each article
                textTitleList[k].setText(articlesList.get(i).getTitle());
                // Set source text + time ago + source image + action event for each button for each article
                switch (articlesList.get(i).getSource()) {
                    case "vnexpress":
                        textSourceList[k].setText("VnExpress - " + articlesList.get(i).getTimeAgo());
                        imageSourceList[k].setImage(new Image("resource/vnexpress_small.png"));
                        buttonList[k].setOnAction(e -> {
                            displayFullArticleVbox.getChildren().clear();
                            borderPaneUnderScrollPane.setCenter(null);
                            borderPaneUnderScrollPane.setCenter(loadingStackPane);
                            Thread t1 = new Thread(() -> {
                                try {
                                    ArticlesManager.displayVnexpressFullArticle(articlesList.get(finalI), displayFullArticleVbox);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    borderPaneUnderScrollPane.setCenter(null);
                                    borderPaneUnderScrollPane.setCenter(stackPane1);
                                    stackPane2.setVisible(true);
                                    System.gc();
                                    Runtime.getRuntime().gc();
                                });
                            });
                            t1.start();
                            currentArticleIndex = finalI;
                        });
                        break;
                    case "zingnews":
                        textSourceList[k].setText("ZingNews - " + articlesList.get(i).getTimeAgo());
                        imageSourceList[k].setImage(new Image("resource/zingnews_small.png"));
                        buttonList[k].setOnAction(e -> {
                            displayFullArticleVbox.getChildren().clear();
                            borderPaneUnderScrollPane.setCenter(null);
                            borderPaneUnderScrollPane.setCenter(loadingStackPane);
                            Thread t1 = new Thread(() -> {
                                try {
                                    ArticlesManager.displayZingFullArticle(articlesList.get(finalI), displayFullArticleVbox);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    borderPaneUnderScrollPane.setCenter(null);
                                    borderPaneUnderScrollPane.setCenter(stackPane1);
                                    stackPane2.setVisible(true);
                                    System.gc();
                                    Runtime.getRuntime().gc();
                                });
                            });
                            t1.start();
                            currentArticleIndex = finalI;
                        });
                        break;
                    case "tuoitre":
                        textSourceList[k].setText("Tuổi trẻ - " + articlesList.get(i).getTimeAgo());
                        imageSourceList[k].setImage(new Image("resource/tuoitre_small.png"));
                        buttonList[k].setOnAction(e -> {
                            displayFullArticleVbox.getChildren().clear();
                            borderPaneUnderScrollPane.setCenter(null);
                            borderPaneUnderScrollPane.setCenter(loadingStackPane);
                            Thread t1 = new Thread(() -> {
                                try {
                                    ArticlesManager.displayTuoiTreFullArticle(articlesList.get(finalI), displayFullArticleVbox);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    borderPaneUnderScrollPane.setCenter(null);
                                    borderPaneUnderScrollPane.setCenter(stackPane1);
                                    stackPane2.setVisible(true);
                                    System.gc();
                                    Runtime.getRuntime().gc();
                                });
                            });
                            t1.start();
                            currentArticleIndex = finalI;
                        });
                        break;
                    case "nhandan":
                        textSourceList[k].setText("Nhân dân - " + articlesList.get(i).getTimeAgo());
                        imageSourceList[k].setImage(new Image("resource/nhandan_small.png"));
                        buttonList[k].setOnAction(e -> {
                            displayFullArticleVbox.getChildren().clear();
                            borderPaneUnderScrollPane.setCenter(null);
                            borderPaneUnderScrollPane.setCenter(loadingStackPane);
                            Thread t1 = new Thread(() -> {
                                try {
                                    ArticlesManager.displayNhanDanFullArticle(articlesList.get(finalI), displayFullArticleVbox);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    borderPaneUnderScrollPane.setCenter(null);
                                    borderPaneUnderScrollPane.setCenter(stackPane1);
                                    stackPane2.setVisible(true);
                                    System.gc();
                                    Runtime.getRuntime().gc();
                                });
                            });
                            t1.start();
                            currentArticleIndex = finalI;
                        });
                        break;
                    case "thanhnien":
                        textSourceList[k].setText("Thanh niên - " + articlesList.get(i).getTimeAgo());
                        imageSourceList[k].setImage(new Image("resource/thanhnien_small.png"));
                        buttonList[k].setOnAction(e -> {
                            displayFullArticleVbox.getChildren().clear();
                            borderPaneUnderScrollPane.setCenter(null);
                            borderPaneUnderScrollPane.setCenter(loadingStackPane);
                            Thread t1 = new Thread(() -> {
                                try {
                                    ArticlesManager.displayThanhNienFullArticle(articlesList.get(finalI), displayFullArticleVbox);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    borderPaneUnderScrollPane.setCenter(null);
                                    borderPaneUnderScrollPane.setCenter(stackPane1);
                                    stackPane2.setVisible(true);
                                    System.gc();
                                    Runtime.getRuntime().gc();
                                });
                            });
                            t1.start();
                            currentArticleIndex = finalI;
                        });
                        break;
                }

                // Set thumb image for each object
                if (k != 3 && k != 7) {
                    Image image = new Image(articlesList.get(i).getThumb(), 600, 0, true, true, true);
                    BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
                    Background background = new Background(backgroundImage);
                    anchorPaneList[k].setCache(true);
                    anchorPaneList[k].setCacheHint(CacheHint.SPEED);
                    anchorPaneList[k].setBackground(background);
                } else {
                    Image image2 = new Image(articlesList.get(i).getThumb(), 600, 0, true, true, true);
                    BackgroundImage backgroundImage2 = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
                    Background background2 = new Background(backgroundImage2);
                    layoutController.anchorPaneImage1.setCache(true);
                    layoutController.anchorPaneImage1.setCacheHint(CacheHint.SPEED);
                    layoutController.anchorPaneImage2.setCache(true);
                    layoutController.anchorPaneImage2.setCacheHint(CacheHint.SPEED);
                    if (k == 3) layoutController.anchorPaneImage1.setBackground(background2);
                    else layoutController.anchorPaneImage2.setBackground(background2);
                }

            }

            // Animation for each transition of pagination
//            Timeline timeline = new Timeline();
//            timeline.getKeyFrames().add(
//                    new KeyFrame(Duration.ZERO,
//                            new KeyValue(displayLayoutVbox.opacityProperty(), 0)
//                    )
//            );
//            for (int i = 1; i < 10; i++) {
//                timeline.getKeyFrames().add(
//                        new KeyFrame(new Duration(i * 60),
//                                new KeyValue(displayLayoutVbox.opacityProperty(), i / 10.0)
//                        )
//                );
//            }
//            timeline.play();

            return displayLayoutVbox;
        });

        System.gc();
        Runtime.getRuntime().gc();
        return newPagination;
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

        Timeline timeline = new Timeline();
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, e -> categoryHbox.setOpacity(0));
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.1), e -> categoryHbox.setOpacity(1));
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1);
        timeline.play();
    }
}
