// Author: Matthew Godin
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.tools.Tool;

public class SketchIt extends Application {
    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();
        Controller controller = new Controller(model);
        CanvasView canvasView = new CanvasView(model, controller);
        ToolbarView toolbarView = new ToolbarView(model, controller,
                                                    canvasView);

        Scene scene = new Scene(toolbarView, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}