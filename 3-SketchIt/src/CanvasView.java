import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

class CanvasView extends Canvas implements IView {
    private Model model;

    CanvasView(Model model, Controller controller) {
        this.model = model;
        model.addView(this);
    }

    public void updateView() {
        // update view from the model
    }
}
