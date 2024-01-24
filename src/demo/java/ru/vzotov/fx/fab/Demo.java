package ru.vzotov.fx.fab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.vzotov.fxtheme.FxTheme;

import static ru.vzotov.fx.utils.LayoutUtils.styled;

public class Demo extends Application {
    public static void main(String[] args) {
        FxTheme.loadFonts();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final Fab fab = new Fab(Fab.Mode.ACTION);
        fab.setOnAction(action -> {
            System.out.println("Action performed");
        });

        final AnchorPane anchorPane = styled("demo", new AnchorPane(fab));
        //final Pane root = new Pane(anchorPane);

        //anchorPane.setPickOnBounds(false);
        AnchorPane.setRightAnchor(fab, 32.0);
        AnchorPane.setBottomAnchor(fab, 16.0);

        Scene scene = new Scene(anchorPane, 800, 600);
        scene.getStylesheets().addAll(FxTheme.stylesheet(), "fab.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
