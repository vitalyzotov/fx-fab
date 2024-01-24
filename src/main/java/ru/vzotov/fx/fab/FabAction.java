package ru.vzotov.fx.fab;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Shape;
import ru.vzotov.fx.utils.AdjustableAction;

public interface FabAction extends EventHandler<ActionEvent>, AdjustableAction {

    Shape createIcon();

    String getFabLabel();
}
