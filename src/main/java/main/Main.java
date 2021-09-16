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

package main;

import article.ArticlesList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    private static double initialX, initialY;
    public static Stage stage;
    public static ExecutorService es;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.stage = new Stage();

        // Load the first news category
        es = Executors.newFixedThreadPool(1);
        es.execute(() -> {
            try {
                ArticlesList.getNewsList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.shutdown();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("layouts/WelcomeScene.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        addDraggableNode(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    static void addDraggableNode(final Node node) {
        node.setOnMousePressed(me -> {
            if (me.getButton() != MouseButton.MIDDLE) {
                ((Node)me.getSource()).getScene().getWindow().setOpacity(0.5);
                initialX = me.getSceneX();
                initialY = me.getSceneY();
            }
        });

        node.setOnMouseDragged(me -> {
            if (me.getButton() != MouseButton.MIDDLE) {
                node.getScene().getWindow().setX(me.getScreenX() - initialX);
                node.getScene().getWindow().setY(me.getScreenY() - initialY);
            }
        });

        node.setOnMouseReleased(me -> ((Node)me.getSource()).getScene().getWindow().setOpacity(1));
    }
}
