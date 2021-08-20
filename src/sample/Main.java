package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    public static Stage stage;

    double initialX, initialY;

    public static ArrayList<Article> newsTemp;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage stage = new Stage();
        Main.stage = stage;

        // Load the first news category
        try {
            newsTemp = ArticlesList.getNewsList();
//            ArticlesManager.printSortArticles(newsTemp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        DraggableHelper.addDraggableNode(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
