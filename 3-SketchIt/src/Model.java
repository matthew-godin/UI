import java.util.ArrayList;

public class Model {
    private ArrayList<IView> views = new ArrayList<IView>();

    public void addView(IView view) {
        views.add(view);
        view.updateView();
    }
}
