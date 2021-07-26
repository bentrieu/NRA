package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    double initialX, initialY;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        addDraggableNode(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Draggable method for root (layout)
    private void addDraggableNode(final Node node) {
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    ((Node)me.getSource()).getScene().getWindow().setOpacity(0.5);
                    initialX = me.getSceneX();
                    initialY = me.getSceneY();
                }
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    node.getScene().getWindow().setX(me.getScreenX() - initialX);
                    node.getScene().getWindow().setY(me.getScreenY() - initialY);
                }
            }
        });

        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                ((Node)me.getSource()).getScene().getWindow().setOpacity(1);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
