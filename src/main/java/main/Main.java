package main;

import article.ArticlesList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    public static Stage stage;
    public static ExecutorService es;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.stage = new Stage();

        // Load the first news category
        es = Executors.newCachedThreadPool();
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
        DraggableHelper.addDraggableNode(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
