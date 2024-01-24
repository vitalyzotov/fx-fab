package ru.vzotov.fx.fab.skin;

import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import ru.vzotov.fx.fab.Fab;

import static ru.vzotov.fx.utils.LayoutUtils.styled;


public class FabButtonSkin<T extends Fab> extends SkinBase<T> {

    private final Button actionButton;

    public FabButtonSkin(T control) {
        super(control);

        actionButton = styled("fab-button", new Button(control.getText(), control.getGraphic()));
        actionButton.textProperty().bind(control.textProperty());
        actionButton.graphicProperty().bind(control.graphicProperty());
        getChildren().add(actionButton);

        control.requestLayout();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
        actionButton.resizeRelocate(contentX, contentY, contentWidth, 56);
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
