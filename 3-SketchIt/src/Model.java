import javax.tools.Tool;
import java.io.Serializable;
import java.util.ArrayList;

enum CurrentTool { SELECT, ERASE, LINE, CIRCLE, RECTANGLE, FILL }
enum CurrentThickness implements Serializable { SMALL, MEDIUM, LARGE };
enum CurrentStyle implements Serializable { DOTTED, NORMAL, DASHED };
class ModelColor implements Serializable {
    private double red, green, blue;

    public ModelColor(double red, double green, double blue) {
        this.red = red > 1 ? 1 : (red < 0 ? 0 : red);
        this.green = green > 1 ? 1 : (green < 0 ? 0 : green);
        this.blue = blue > 1 ? 1 : (blue < 0 ? 0 : blue);
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }
}
class ModelPosition implements Serializable {
    private double x, y;

    ModelPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
interface IModelShape extends Serializable {
    void setStartX(double x);
    void setStartY(double y);
    ModelPosition getStartPosition();
    ModelPosition getEndPosition();
    ModelColor getLineColor();
    ModelColor getShadeLineColor();
    CurrentThickness getThickness();
    CurrentStyle getStyle();
    void resizeWidth(double factor);
    void resizeHeight(double factor);
    void updatePosition(ModelPosition movement, ModelPosition startDrag, ModelPosition endDrag);
    void select();
    void unSelect();
    boolean isSelected();
    void updateLineColor(ModelColor color);
    void updateThickness(CurrentThickness thickness);
    void updateStyle(CurrentStyle style);
}
interface IModelFillable extends IModelShape {
    ModelColor getFillColor();
    ModelColor getShadeFillColor();
    void updateFillColor(ModelColor color);
}
class ModelLine implements IModelShape {
    private ModelPosition startPosition, endPosition;
    private ModelColor lineColor;
    private CurrentThickness thickness;
    private CurrentStyle style;
    private boolean selected;

    @Override
    public void setStartX(double x) {
        startPosition = new ModelPosition(x, startPosition.getY());
    }

    @Override
    public void setStartY(double y) {
        startPosition = new ModelPosition(startPosition.getX(), y);
    }

    public ModelLine(ModelPosition startPosition, ModelPosition endPosition, ModelColor lineColor,
                CurrentThickness thickness, CurrentStyle style) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lineColor = lineColor;
        this.thickness = thickness;
        this.style = style;
    }

    @Override
    public ModelPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ModelPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ModelColor getShadeLineColor() {
        if (selected) {
            return new ModelColor(lineColor.getRed() - 5 * TabModel.SELECTED_SHADE, lineColor.getGreen() - 5 * TabModel.SELECTED_SHADE, lineColor.getBlue() - 5 * TabModel.SELECTED_SHADE);
        } else {
            return lineColor;
        }
    }

    @Override
    public ModelColor getLineColor() {
        return lineColor;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void updateLineColor(ModelColor color) {
        this.lineColor = color;
    }

    @Override
    public void updateThickness(CurrentThickness thickness) {
        this.thickness = thickness;
    }

    @Override
    public void updateStyle(CurrentStyle style) {
        this.style = style;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public void unSelect() {
        selected = false;
    }

    @Override
    public CurrentThickness getThickness() {
        return thickness;
    }

    @Override
    public CurrentStyle getStyle() {
        return style;
    }

    @Override
    public void resizeWidth(double factor) {
        startPosition = new ModelPosition(startPosition.getX() * factor, startPosition.getY());
        endPosition = new ModelPosition(endPosition.getX() * factor, endPosition.getY());
    }

    @Override
    public void resizeHeight(double factor) {
        startPosition = new ModelPosition(startPosition.getX(), startPosition.getY() * factor);
        endPosition = new ModelPosition(endPosition.getX(), endPosition.getY() * factor);
    }

    @Override
    public void updatePosition(ModelPosition movement, ModelPosition startDrag, ModelPosition endDrag) {
        startPosition = new ModelPosition(startDrag.getX() + movement.getX(), startDrag.getY() + movement.getY());
        endPosition = new ModelPosition(endDrag.getX() + movement.getX(), endDrag.getY() + movement.getY());
    }
}
class ModelCircle implements IModelFillable {
    private ModelPosition startPosition, endPosition;
    private ModelColor lineColor, fillColor;
    private CurrentThickness thickness;
    private CurrentStyle style;
    private boolean selected;

    @Override
    public void setStartX(double x) {
        startPosition = new ModelPosition(x, startPosition.getY());
    }

    @Override
    public void setStartY(double y) {
        startPosition = new ModelPosition(startPosition.getX(), y);
    }

    public ModelCircle(ModelPosition startPosition, ModelPosition endPosition, ModelColor lineColor, ModelColor fillColor,
                  CurrentThickness thickness, CurrentStyle style) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lineColor = lineColor;
        this.fillColor = fillColor;
        this.thickness = thickness;
        this.style = style;
    }

    @Override
    public void updateLineColor(ModelColor color) {
        this.lineColor = color;
    }

    @Override
    public void updateThickness(CurrentThickness thickness) {
        this.thickness = thickness;
    }

    @Override
    public void updateStyle(CurrentStyle style) {
        this.style = style;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public void unSelect() {
        selected = false;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public ModelPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ModelPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ModelColor getShadeLineColor() {
        if (selected) {
            return new ModelColor(lineColor.getRed() - TabModel.SELECTED_SHADE, lineColor.getGreen() - TabModel.SELECTED_SHADE, lineColor.getBlue() - TabModel.SELECTED_SHADE);
        } else {
            return lineColor;
        }
    }

    @Override
    public ModelColor getLineColor() {
        return lineColor;
    }

    @Override
    public ModelColor getShadeFillColor() {
        if (selected) {
            return new ModelColor(fillColor.getRed() + TabModel.SELECTED_SHADE, fillColor.getGreen() + TabModel.SELECTED_SHADE, fillColor.getBlue() + TabModel.SELECTED_SHADE);
        } else {
            return fillColor;
        }
    }

    @Override
    public ModelColor getFillColor() {
        return fillColor;
    }

    @Override
    public void updateFillColor(ModelColor color) {
        this.fillColor = color;
    }

    @Override
    public CurrentThickness getThickness() {
        return thickness;
    }

    @Override
    public CurrentStyle getStyle() {
        return style;
    }

    @Override
    public void resizeWidth(double factor) {
        startPosition = new ModelPosition(startPosition.getX() * factor, startPosition.getY());
        endPosition = new ModelPosition(endPosition.getX() * factor, endPosition.getY());
    }

    @Override
    public void resizeHeight(double factor) {
        startPosition = new ModelPosition(startPosition.getX(), startPosition.getY() * factor);
        endPosition = new ModelPosition(endPosition.getX(), endPosition.getY() * factor);
    }

    @Override
    public void updatePosition(ModelPosition movement, ModelPosition startDrag, ModelPosition endDrag) {
        startPosition = new ModelPosition(startDrag.getX() + movement.getX(), startDrag.getY() + movement.getY());
        endPosition = new ModelPosition(endDrag.getX() + movement.getX(), endDrag.getY() + movement.getY());
    }
}
class ModelRectangle implements IModelFillable {
    private ModelPosition startPosition, endPosition;
    private ModelColor lineColor, fillColor;
    private CurrentThickness thickness;
    private CurrentStyle style;
    private boolean selected;

    @Override
    public void setStartX(double x) {
        startPosition = new ModelPosition(x, startPosition.getY());
    }

    @Override
    public void setStartY(double y) {
        startPosition = new ModelPosition(startPosition.getX(), y);
    }

    public ModelRectangle(ModelPosition startPosition, ModelPosition endPosition, ModelColor lineColor, ModelColor fillColor,
                     CurrentThickness thickness, CurrentStyle style) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.lineColor = lineColor;
        this.fillColor = fillColor;
        this.thickness = thickness;
        this.style = style;
    }

    @Override
    public void updateLineColor(ModelColor color) {
        this.lineColor = color;
    }

    @Override
    public void updateFillColor(ModelColor color) {
        this.fillColor = color;
    }

    @Override
    public void updateThickness(CurrentThickness thickness) {
        this.thickness = thickness;
    }

    @Override
    public void updateStyle(CurrentStyle style) {
        this.style = style;
    }

    @Override
    public void select() {
        selected = true;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void unSelect() {
        selected = false;
    }

    @Override
    public ModelPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ModelPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ModelColor getShadeLineColor() {
        if (selected) {
            return new ModelColor(lineColor.getRed() - TabModel.SELECTED_SHADE, lineColor.getGreen() - TabModel.SELECTED_SHADE, lineColor.getBlue() - TabModel.SELECTED_SHADE);
        } else {
            return lineColor;
        }
    }

    @Override
    public ModelColor getLineColor() {
        return lineColor;
    }

    @Override
    public ModelColor getShadeFillColor() {
        if (selected) {
            return new ModelColor(fillColor.getRed() + TabModel.SELECTED_SHADE, fillColor.getGreen() + TabModel.SELECTED_SHADE, fillColor.getBlue() + TabModel.SELECTED_SHADE);
        } else {
            return fillColor;
        }
    }

    @Override
    public ModelColor getFillColor() {
        return fillColor;
    }

    @Override
    public CurrentThickness getThickness() {
        return thickness;
    }

    @Override
    public CurrentStyle getStyle() {
        return style;
    }

    @Override
    public void resizeWidth(double factor) {
        startPosition = new ModelPosition(startPosition.getX() * factor, startPosition.getY());
        endPosition = new ModelPosition(endPosition.getX() * factor, endPosition.getY());
    }

    @Override
    public void resizeHeight(double factor) {
        startPosition = new ModelPosition(startPosition.getX(), startPosition.getY() * factor);
        endPosition = new ModelPosition(endPosition.getX(), endPosition.getY() * factor);
    }

    @Override
    public void updatePosition(ModelPosition movement, ModelPosition startDrag, ModelPosition endDrag) {
        startPosition = new ModelPosition(startDrag.getX() + movement.getX(), startDrag.getY() + movement.getY());
        endPosition = new ModelPosition(endDrag.getX() + movement.getX(), endDrag.getY() + movement.getY());
    }
}

public class Model {
    ArrayList<TabModel> tabs;
    TabModel fake;

    public Model() {
        tabs = new ArrayList<TabModel>();
        fake = new TabModel(-1);
    }

    public int getNumTabs() {
        return tabs.size();
    }

    public void addTab() {
        tabs.add(new TabModel(tabs.size()));
    }

    public void removeTab(int index) {
        tabs.remove(index);
    }

    public TabModel getTabModel(int index) {
        if (index >= tabs.size() || index < 0) {
            return fake;
        } else {
            return tabs.get(index);
        }
    }
}

class TabModel {
    static double SELECTED_SHADE = -0.1;
    static String DEFAULT_NAME = "Untitled";
    static String DEFAULT_FILE_NAME = DEFAULT_NAME + ToolbarView.SKETCHIT_EXTENSION;
    private ArrayList<IView> views = new ArrayList<IView>();
    private CurrentTool currentTool;
    private CurrentThickness currentThickness;
    private CurrentStyle currentStyle;
    private ModelColor fillColor;
    private ModelColor lineColor;
    private boolean onHold;
    private ModelPosition startPosition;
    private ArrayList<IModelShape> shapes;
    private IModelShape shapeOnHold;
    private int index;

    public TabModel(int index) {
        this.index = index;
        nameSet = false;
        saved = true;
        currentTool = CurrentTool.SELECT;
        currentThickness = CurrentThickness.MEDIUM;
        currentStyle = CurrentStyle.NORMAL;
        fillColor = new ModelColor(1, 1, 1);
        lineColor = new ModelColor(0, 0, 0);
        onHold = false;
        shapes = new ArrayList<IModelShape>();
        fileName = DEFAULT_FILE_NAME;
        if (index >= 1) {
            fileName = DEFAULT_NAME + Integer.toString(index) + ToolbarView.SKETCHIT_EXTENSION;
        }
    }

    public void addView(IView view) {
        views.add(view);
        view.updateView();
    }

    ModelPosition startDrag, endDrag;
    public void holdMouse(double x, double y, IModelShape shape) {
        //onHold = true;
        for (IModelShape s : shapes) {
            s.unSelect();
        }
        /*if (shapeOnHold != null) {
            shapeOnHold.unSelect();
        }*/
        if (currentTool == CurrentTool.LINE) {
            onHold = true;
            shapeOnHold = new ModelLine(new ModelPosition(x, y), new ModelPosition(x, y), new ModelColor(lineColor.getRed(),
                    lineColor.getGreen(), lineColor.getBlue()), currentThickness, currentStyle);
        } else if (currentTool == CurrentTool.CIRCLE) {
            onHold = true;
            shapeOnHold = new ModelCircle(new ModelPosition(x, y), new ModelPosition(x, y), new ModelColor(lineColor.getRed(),
                    lineColor.getGreen(), lineColor.getBlue()), new ModelColor(fillColor.getRed(),
                    fillColor.getGreen(), fillColor.getBlue()), currentThickness, currentStyle);
        } else if (currentTool == CurrentTool.RECTANGLE) {
            onHold = true;
            shapeOnHold = new ModelRectangle(new ModelPosition(x, y), new ModelPosition(x, y), new ModelColor(lineColor.getRed(),
                    lineColor.getGreen(), lineColor.getBlue()), new ModelColor(fillColor.getRed(),
                    fillColor.getGreen(), fillColor.getBlue()), currentThickness, currentStyle);
        } else if (currentTool == CurrentTool.SELECT && shape != null) {
            onHold = true;
            shape.select();
            shapeOnHold = shape;
            startDrag = shape.getStartPosition();
            endDrag = shape.getEndPosition();
            shapes.remove(shape);
            currentThickness = shape.getThickness();
            currentStyle = shape.getStyle();
            lineColor = shape.getLineColor();
            if (shape instanceof ModelCircle) {
                fillColor = ((ModelCircle)shape).getFillColor();
            } else if (shape instanceof ModelRectangle) {
                fillColor = ((ModelRectangle)shape).getFillColor();
            }
        } else if (currentTool == CurrentTool.ERASE && shape != null) {
            shapes.remove(shape);
        } else if (currentTool == CurrentTool.FILL && shape != null && shape instanceof IModelFillable) {
            ((IModelFillable)shape).updateFillColor(fillColor);
        }
        startPosition = new ModelPosition(x, y);
        notifyObservers();
    }

    public void moveMouse(double x, double y, IModelShape shape) {
        if (onHold) {
            if (currentTool == CurrentTool.LINE) {
                shapeOnHold = new ModelLine(shapeOnHold.getStartPosition(), new ModelPosition(x, y), shapeOnHold.getLineColor(),
                        currentThickness, currentStyle);
            } else if (currentTool == CurrentTool.CIRCLE) {
                shapeOnHold = new ModelCircle(shapeOnHold.getStartPosition(), new ModelPosition(x, y), shapeOnHold.getLineColor(),
                        ((ModelCircle)shapeOnHold).getFillColor(), currentThickness, currentStyle);
            } else if (currentTool == CurrentTool.RECTANGLE) {
                shapeOnHold = new ModelRectangle(shapeOnHold.getStartPosition(), new ModelPosition(x, y), shapeOnHold.getLineColor(),
                        ((ModelRectangle)shapeOnHold).getFillColor(), currentThickness, currentStyle);
            } else if (currentTool == CurrentTool.SELECT) {
                if (shapeOnHold != null) {
                    shapeOnHold.updatePosition(new ModelPosition(x - startPosition.getX(), y - startPosition.getY()), startDrag, endDrag);
                }
            }
        }
        notifyObservers();
    }

    public void releaseMouse(double x, double y, IModelShape shape) {
        if (onHold) {
            onHold = false;
            if (currentTool == CurrentTool.LINE) {
                shapes.add(new ModelLine(new ModelPosition(startPosition.getX(), startPosition.getY()),
                        new ModelPosition(x, y), new ModelColor(lineColor.getRed(),
                        lineColor.getGreen(), lineColor.getBlue()), currentThickness, currentStyle));
                saved = false;
            } else if (currentTool == CurrentTool.CIRCLE) {
                shapes.add(new ModelCircle(new ModelPosition(startPosition.getX(), startPosition.getY()),
                        new ModelPosition(x, y), new ModelColor(lineColor.getRed(),
                        lineColor.getGreen(), lineColor.getBlue()), new ModelColor(fillColor.getRed(),
                        fillColor.getGreen(), fillColor.getBlue()), currentThickness, currentStyle));
                saved = false;
            } else if (currentTool == CurrentTool.RECTANGLE) {
                shapes.add(new ModelRectangle(new ModelPosition(startPosition.getX(), startPosition.getY()),
                        new ModelPosition(x, y), new ModelColor(lineColor.getRed(),
                        lineColor.getGreen(), lineColor.getBlue()), new ModelColor(fillColor.getRed(),
                        fillColor.getGreen(), fillColor.getBlue()), currentThickness, currentStyle));
                saved = false;
            } else if (currentTool == CurrentTool.SELECT) {
                shapes.add(shapeOnHold);
                //shapeOnHold = null;
                saved = false;
            }
        }
        notifyObservers();
    }

    public void selectSelection() {
        currentTool = CurrentTool.SELECT;
    }

    public void selectEraser() {
        currentTool = CurrentTool.ERASE;
    }

    public void selectLine() {
        currentTool = CurrentTool.LINE;
    }

    public void selectCircle() {
        currentTool = CurrentTool.CIRCLE;
    }

    public void selectRectangle() {
        currentTool = CurrentTool.RECTANGLE;
    }

    public void selectFill() {
        currentTool = CurrentTool.FILL;
    }

    public void setLineColor(double red, double green, double blue) {
        lineColor = new ModelColor(red, green, blue);
        if (shapeOnHold != null && shapeOnHold.isSelected()) {
            shapeOnHold.updateLineColor(lineColor);
        }
        notifyObservers();
    }

    boolean saved;
    public boolean isSaved() {
        return saved;
    }

    public void save() {
        saved = true;
    }

    String fileName;
    boolean nameSet;
    public String getFileName() { return fileName; }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        nameSet = true;
    }

    public boolean isNameSet() {
        return nameSet;
    }

    public void newSketch() {
        nameSet = false;
        shapes.clear();
        shapeOnHold = null;
        saved = true;
        //currentTool = CurrentTool.SELECT;
        currentThickness = CurrentThickness.MEDIUM;
        currentStyle = CurrentStyle.NORMAL;
        fillColor = new ModelColor(1, 1, 1);
        lineColor = new ModelColor(0, 0, 0);
        onHold = false;
        fileName = DEFAULT_FILE_NAME;
        notifyObservers();
    }

    public void loadSketch(ArrayList<IModelShape> shapes) {
        newSketch();
        this.shapes = shapes;
        notifyObservers();
    }

    public void setFillColor(double red, double green, double blue) {
        fillColor = new ModelColor(red, green, blue);
        if (shapeOnHold != null && shapeOnHold.isSelected() && shapeOnHold instanceof IModelFillable) {
            ((IModelFillable)shapeOnHold).updateFillColor(fillColor);
        }
        notifyObservers();
    }

    public void selectSmall() {
        currentThickness = CurrentThickness.SMALL;
        if (shapeOnHold != null && shapeOnHold.isSelected()) {
            shapeOnHold.updateThickness(CurrentThickness.SMALL);
        }
        notifyObservers();
    }

    public void selectMedium() {
        currentThickness = CurrentThickness.MEDIUM;
        if (shapeOnHold != null && shapeOnHold.isSelected()) {
            shapeOnHold.updateThickness(CurrentThickness.MEDIUM);
        }
        notifyObservers();
    }

    public void selectLarge() {
        currentThickness = CurrentThickness.LARGE;
        if (shapeOnHold != null && shapeOnHold.isSelected()) {
            shapeOnHold.updateThickness(CurrentThickness.LARGE);
        }
        notifyObservers();
    }

    public void selectDotted() {
        currentStyle = CurrentStyle.DOTTED;
        if (shapeOnHold != null && shapeOnHold.isSelected()) {
            shapeOnHold.updateStyle(CurrentStyle.DOTTED);
        }
        notifyObservers();
    }

    public void selectNormal() {
        currentStyle = CurrentStyle.NORMAL;
        if (shapeOnHold != null && shapeOnHold.isSelected()) {
            shapeOnHold.updateStyle(CurrentStyle.NORMAL);
        }
        notifyObservers();
    }

    public void selectDashed() {
        currentStyle = CurrentStyle.DASHED;
        if (shapeOnHold != null && shapeOnHold.isSelected()) {
            shapeOnHold.updateStyle(CurrentStyle.DASHED);
        }
        notifyObservers();
    }

    public ArrayList<IModelShape> getShapes() {
        return new ArrayList<IModelShape>(shapes);
    }

    public IModelShape getShapeOnHold() {
        if (onHold) {
            return shapeOnHold;
        } else {
            return null;
        }
    }

    public void resizeWidth(double factor) {
        for (IModelShape shape : shapes) {
            shape.resizeWidth(factor);
        }
        notifyObservers();
    }

    public void resizeHeight(double factor) {
        for (IModelShape shape : shapes) {
            shape.resizeHeight(factor);
        }
        notifyObservers();
    }

    public CurrentThickness getCurrentThickness() {
        return currentThickness;
    }

    public CurrentStyle getCurrentStyle() {
        return currentStyle;
    }

    public ModelColor getLineColor() {
        return lineColor;
    }

    public ModelColor getFillColor() {
        return fillColor;
    }

    private void notifyObservers() {
        for (IView view : this.views) {
            view.updateView();
        }
    }
}
