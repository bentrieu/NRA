/*
    RMIT University Vietnam
    Course: INTE2512 Object-Oriented Programming
    Semester: 2021B
    Assessment: Final Project
    Created  date: 15/07/2021
    Author: Ngo Ngoc Thinh - s3879364, Trieu Hoang Khang - s3878466, Nguyen Van Quy - s3878636, Nguyen Tran Duy Khang - s3836280
    Last modified date: 19/9/2021
    Acknowledgement:
    [1] J. Hedley, "jsoup: Java HTML parser, built for HTML editing, cleaning, scraping, and XSS safety", Jsoup.org, 2021. [Online]. Available: https://jsoup.org/. [Accessed: 11- Sep- 2021].
    [2] "VnExpress - Báo tiếng Việt nhiều người xem nhất", vnexpress.net, 2021. [Online]. Available: https://vnexpress.net/. [Accessed: 11- Sep- 2021].
    [3] Zingnews.vn, 2021. [Online]. Available: https://zingnews.vn/. [Accessed: 11- Sep- 2021].
    [4] "Tin tức 24h mới nhất, tin nhanh, tin nóng hàng ngày", Thanh Niên, 2021. [Online]. Available: https://thanhnien.vn/. [Accessed: 11- Sep- 2021].
    [5] "Báo Nhân Dân điện tử", Báo Nhân Dân, 2021. [Online]. Available: https://nhandan.vn/. [Accessed: 11- Sep- 2021].
    [6] T. ONLINE, "Tin tức, tin nóng, đọc báo điện tử - Tuổi Trẻ Online", TUOI TRE ONLINE, 2021. [Online]. Available: https://tuoitre.vn/. [Accessed: 11- Sep- 2021].
    [7] "Download OpenJDK builds of Liberica JDK, Java 8, 11, Java 16 Linux, Windows, macOS", BellSoft LTD, 2021. [Online]. Available: https://bell-sw.com/pages/downloads/. [Accessed: 11- Sep- 2021].
    [8] Tutorials.jenkov.com,2021.[Online].Available:http://tutorials.jenkov.com/javafx/index.html?fbclid=IwAR00PfMH6tVx6fBOUIzHP3LbNIwAeW-o-DlaxO9zMyD8YijM_Z3i2XqtYIY. [Accessed: 12- Sep- 2021].
    [9] J. Redmond, "Maven – Maven Documentation", Maven.apache.org, 2021. [Online]. Available: https://maven.apache.org/guides/?fbclid=IwAR2J6wZokvEVKj-RqRTw8qH3xJN1kr5TQY_WqnfzBsv4JoCXN1bOqEX6Bzw. [Accessed: 12- Sep- 2021].
    [10] All lecture and lab slides from RMIT univeristy: https://rmit.instructure.com/courses/88207
*/

package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomeSceneController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button gettingStartedButton;

    public static ProgressBar progressBar = new ProgressBar();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gettingStartedButton.setText("Loading now");
        AnchorPane.setLeftAnchor(progressBar, 50.0);
        AnchorPane.setRightAnchor(progressBar, 50.0);
        AnchorPane.setBottomAnchor(progressBar, 20.0);
        anchorPane.getChildren().add(progressBar);
        progressBar.setProgress(0.0);
        progressBar.getStylesheets().add("/css/cssdarkmode.css");
        progressBar.getStyleClass().add("progress-bar");
        progressBar.setPrefHeight(5);
        progressBar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() == 1) {
                    Platform.runLater(() -> {
                        gettingStartedButton.setText("Getting Started");
                    });
                }
            }
        });
    }

    public void close() {
        System.exit(1);
    }

    public void gettingStarted() {
        if (progressBar.getProgress() == 1) {
            Parent nextRoot;
            try {
                Main.stage.setMinWidth(600);
                Main.stage.setMinHeight(500);
                Main.stage.getIcons().add(new Image("/images/newsicon.png"));
                Main.stage.setTitle("NRA News");
                Main.stage.setFullScreen(true);

                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                int width = gd.getDisplayMode().getWidth();
                int height = gd.getDisplayMode().getHeight();
                if (width < 1300) {
                    Main.stage.setWidth(width - 100);
                    Main.stage.setHeight(height - 100);
                }

                // Change the stage
                nextRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("layouts/HomeScene.fxml")));
                Main.stage.setScene(new Scene(nextRoot));
                ((Stage) ((Node) progressBar).getScene().getWindow()).close();
                Main.stage.show();

                // Set full screen using F11
                Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, e1 -> {
                    if (KeyCode.F11.equals(e1.getCode())) {
                        Main.stage.setFullScreen(!Main.stage.isFullScreen());
                    }
                });
                System.gc();
                Runtime.getRuntime().gc();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
