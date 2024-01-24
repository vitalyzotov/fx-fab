package ru.vzotov.fx.fab;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import ru.vzotov.fx.fab.skin.FabButtonSkin;
import ru.vzotov.fx.fab.skin.FabToggleSkin;

public class Fab extends Labeled {

    private static final String DEFAULT_STYLE_CLASS = "fab";

    private final Mode mode;

    public Fab(Mode mode) {
        this(mode, null, FontIcon.of(Material2OutlinedAL.ADD), new Node[0]);
    }

    public Fab(Mode mode, Node graphic, Node... items) {
        this(mode, null, graphic, items);
    }

    public Fab(Mode mode, String text, Node graphic, Node... items) {
        super(text, graphic);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);

        this.mode = mode;

        if (text == null) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        if (mode == Mode.ACTION && items != null && items.length > 0) {
            throw new IllegalArgumentException();
        }

        if (items != null && items.length > 0) {
            getItems().addAll(items);
        }
    }

    /**
     * Items
     */
    private final ObservableList<Node> items = FXCollections.observableArrayList();

    public final ObservableList<Node> getItems() {
        return mode == Mode.ACTION ? FXCollections.unmodifiableObservableList(items) : items;
    }

    /**
     * Selected
     */
    private final BooleanProperty selected = new SimpleBooleanProperty(this, "selected", false);

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return mode == Mode.ACTION ?
                new FabButtonSkin<>(this) :
                new FabToggleSkin<>(this);
    }

    private FabEventHandlerProperty<ActionEvent> onAction;

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        if (onAction == null) {
            onAction = new FabEventHandlerProperty<>("onAction", ActionEvent.ACTION);
        }

        return onAction;
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onAction == null ? null : onActionProperty().get();
    }


    /**
     * События
     */
    public static class FabEvent extends Event {

        public static final EventType<FabEvent> FAB_EVENT = new EventType<>(
                Event.ANY, "FAB_EVENT");

        public FabEvent(EventType<? extends Event> eventType) {
            super(eventType);
        }

    }

    /**
     * Обработчик событий
     */
    private class FabEventHandlerProperty<T extends Event> extends SimpleObjectProperty<EventHandler<T>> {

        private final EventType<T> eventType;

        public FabEventHandlerProperty(final String name, final EventType<T> eventType) {
            super(Fab.this, name);
            this.eventType = eventType;
        }

        @Override
        protected void invalidated() {
            setEventHandler(eventType, get());
        }
    }

    public enum Mode {
        TOGGLE, ACTION;
    }
}
