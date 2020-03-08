// Author: Matthew Godin
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.tools.Tool;

public class SketchIt extends Application {
    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;
    static final int WINDOW_MIN_WIDTH = 640;
    static final int WINDOW_MIN_HEIGHT = 480;
    static final int WINDOW_MAX_WIDTH = 1920;
    static final int WINDOW_MAX_HEIGHT = 1440;

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();
        ToolbarView toolbarView = new ToolbarView(model, stage);
        Scene scene = new Scene(toolbarView, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setMinWidth(WINDOW_MIN_WIDTH);
        stage.setMinHeight(WINDOW_MIN_HEIGHT);
        stage.setMaxWidth(WINDOW_MAX_WIDTH);
        stage.setMaxHeight(WINDOW_MAX_HEIGHT);
        stage.show();
    }
}