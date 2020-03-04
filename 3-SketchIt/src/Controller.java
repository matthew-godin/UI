import javafx.event.Event;
import javafx.event.EventHandler;

public class Controller implements EventHandler {
    Model model;
    Controller(Model model) {
        this.model = model;
    }

    @Override
    public void handle(Event event) {
        // do something with the model
    }
}
