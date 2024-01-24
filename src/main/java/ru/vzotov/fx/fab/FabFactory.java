package ru.vzotov.fx.fab;

import javafx.scene.control.Button;

public interface FabFactory {
    Button createComposeButton(FabAction onAction);
}
