package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.stage = new Stage();

        // Load the first news category
        try {
            ArticlesList.getNewsList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("WelcomeScene.fxml")));
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
