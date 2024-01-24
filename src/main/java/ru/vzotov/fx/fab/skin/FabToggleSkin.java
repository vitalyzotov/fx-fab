package ru.vzotov.fx.fab.skin;

import javafx.animation.RotateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.util.Duration;
import ru.vzotov.fx.fab.Fab;

import static ru.vzotov.fx.utils.LayoutUtils.styled;


public class FabToggleSkin<T extends Fab> extends SkinBase<T> {

    private final RotateTransition whenSelected;
    private final RotateTransition whenDeselected;

    private final VBox content;
    private final Popup popup;
    private final ToggleButton toggleButton;

    private ListChangeListener<Node> itemsChangedListener;

    public FabToggleSkin(T control) {
        super(control);

        whenSelected = new RotateTransition(Duration.millis(100), control.getGraphic());
        whenSelected.setFromAngle(0.0);
        whenSelected.setToAngle(45.0);

        whenDeselected = new RotateTransition(Duration.millis(100), control.getGraphic());
        whenDeselected.setFromAngle(45.0);
        whenDeselected.setToAngle(0);

        toggleButton = styled("fab-button", new ToggleButton(control.getText(), control.getGraphic()));
        toggleButton.textProperty().bind(control.textProperty());
        toggleButton.graphicProperty().bind(control.graphicProperty());
        getChildren().add(toggleButton);

        content = styled(new VBox(), "compose-menu");
        content.getChildren().addAll(control.getItems());

        popup = new Popup(); // для отслеживания автоматического сокрытия
        popup.setAutoHide(true);
        popup.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
        popup.getContent().add(content);

        popup.setOnShown(windowEvent -> {
            if (!control.getItems().isEmpty()) {
                control.getItems().get(0).requestFocus();
            }
        });

        popup.setOnShowing(windowEvent -> {
            whenSelected.playFromStart();
        });

        popup.setOnHiding(windowEvent -> {
            toggleButton.setSelected(false);
            whenDeselected.playFromStart();
        });

        popup.addEventFilter(ActionEvent.ACTION, action -> {
            popup.hide();
        });

        toggleButton.selectedProperty().addListener((observableValue, wasSelected, isSelected) -> {
            if (isSelected) {
                final Bounds localBounds = toggleButton.getBoundsInLocal();
                final Point2D p = toggleButton.localToScreen(localBounds.getMinX(), localBounds.getMinY());
                popup.show(toggleButton, p.getX(), p.getY());
            } else {
                popup.hide();
            }
        });

        control.selectedProperty().bindBidirectional(toggleButton.selectedProperty());

        itemsChangedListener = c -> {
            while (c.next()) {
                content.getChildren().removeAll(c.getRemoved());
                content.getChildren().addAll(c.getFrom(), c.getAddedSubList());
            }
        };
        control.getItems().addListener(itemsChangedListener);

        control.requestLayout();
    }

    @Override
    public void dispose() {
        getSkinnable().getItems().removeListener(itemsChangedListener);
        getSkinnable().selectedProperty().unbindBidirectional(toggleButton.selectedProperty());
        super.dispose();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
        toggleButton.resizeRelocate(contentX, contentY, contentWidth, 56);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 56;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 56;
    }

}
