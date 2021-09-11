package main;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;

public class DraggableHelper {
    private static double initialX, initialY;

    // Draggable method for root (layout)
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
