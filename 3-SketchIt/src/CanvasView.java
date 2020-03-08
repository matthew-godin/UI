import com.sun.prism.Graphics;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.io.Serializable;

import java.util.ArrayList;

class CanvasView extends Group implements IView {
    private TabModel model;
    private Canvas canvas;
    class vmShape {
        private Shape shape;
        private IModelShape modelShape;

        vmShape(Shape shape, IModelShape modelShape) {
            this.shape = shape;
            this.modelShape = modelShape;
        }

        public Shape getShape() {
            return shape;
        }

        public IModelShape getModelShape() {
            return modelShape;
        }
    }
    private ArrayList<vmShape> shapes;
    boolean draggingShape;

    CanvasView(TabModel model) {
        draggingShape = false;
        widthFactor = 1;
        heightFactor = 1;
        shapes = new ArrayList<vmShape>();
        this.model = model;
        canvas = new Canvas(SketchIt.WINDOW_WIDTH - ToolbarView.TOOLBAR_WIDTH, SketchIt.WINDOW_HEIGHT);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        canvas.setOnMousePressed(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            model.holdMouse(mouseEvent.getX() < thicknessOffset ? thicknessOffset
                    : (mouseEvent.getX() > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : mouseEvent.getX() / widthFactor),
                    mouseEvent.getY() < thicknessOffset ? thicknessOffset
                            : (mouseEvent.getY() > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : mouseEvent.getY() / heightFactor),
                    null);
        });
        canvas.setOnMouseReleased(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            model.releaseMouse(mouseEvent.getX() < thicknessOffset ? thicknessOffset
                            : (mouseEvent.getX() > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : mouseEvent.getX() / widthFactor),
                    mouseEvent.getY() < thicknessOffset ? thicknessOffset
                            : (mouseEvent.getY() > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : mouseEvent.getY() / heightFactor),
                    null);
        });
        canvas.setOnMouseDragged(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            model.moveMouse(mouseEvent.getX() < thicknessOffset ? thicknessOffset
                            : (mouseEvent.getX() > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : mouseEvent.getX() / widthFactor),
                    mouseEvent.getY() < thicknessOffset ? thicknessOffset
                            : (mouseEvent.getY() > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : mouseEvent.getY() / heightFactor),
                    null);
        });
        model.addView(this);
        this.getChildren().add(canvas);
    }

    public void updateView() {
        clearShapes();
        shapes.clear();
        ArrayList<IModelShape> shapes = model.getShapes();
        for (IModelShape shape : shapes) {
            if (shape instanceof ModelLine) {
                drawLine((ModelLine)shape);
            } else if (shape instanceof ModelCircle) {
                drawCircle((ModelCircle)shape);
            } else if (shape instanceof ModelRectangle) {
                drawRectangle((ModelRectangle)shape);
            }
        }
        IModelShape shapeOnHold = model.getShapeOnHold();
        if (shapeOnHold instanceof ModelLine) {
            drawLine((ModelLine)shapeOnHold);
        } else if (shapeOnHold instanceof ModelCircle) {
            drawCircle((ModelCircle)shapeOnHold);
        } else if (shapeOnHold instanceof ModelRectangle) {
            drawRectangle((ModelRectangle)shapeOnHold);
        }
    }

    private void clearShapes() {
        for (vmShape shape : shapes) {
            this.getChildren().remove(shape.getShape());
        }
    }

    static final int SMALL_SIZE = 1;
    static final int MEDIUM_SIZE = 3;
    static final int LARGE_SIZE = 9;
    static final double DOTTED_GAP = 20;
    static final double DOTTED_DASH = 1;
    static final double DASHED_GAP = 20;
    static final double DASHED_DASH = 20;

    private void drawLine(ModelLine modelLine) {
        Line line = new Line();
        double startX = modelLine.getStartPosition().getX() * widthFactor,
                startY = modelLine.getStartPosition().getY() * heightFactor,
                endX = modelLine.getEndPosition().getX() * widthFactor,
                endY = modelLine.getEndPosition().getY() * heightFactor;
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStroke(Color.color(modelLine.getShadeLineColor().getRed(),
                modelLine.getShadeLineColor().getGreen(),
                modelLine.getShadeLineColor().getBlue()));
        if (modelLine.getThickness() == CurrentThickness.SMALL) {
            line.setStrokeWidth(SMALL_SIZE);
        } else if (modelLine.getThickness() == CurrentThickness.MEDIUM) {
            line.setStrokeWidth(MEDIUM_SIZE);
        } else {
            line.setStrokeWidth(LARGE_SIZE);
        }
        if (modelLine.getStyle() == CurrentStyle.DOTTED) {
            line.getStrokeDashArray().addAll(DOTTED_DASH, DOTTED_GAP);
        } else if (modelLine.getStyle() == CurrentStyle.DASHED) {
            line.getStrokeDashArray().addAll(DASHED_DASH, DASHED_GAP);
        }
        line.setOnMousePressed(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX();//model.getShapeOnHold().getStartPosition().getX();
            double realY = mouseEvent.getY();//model.getShapeOnHold().getStartPosition().getY();
            model.holdMouse(realX < thicknessOffset ? thicknessOffset
                            : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                    realY < thicknessOffset ? thicknessOffset
                            : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                    modelLine);
        });
        line.setOnMouseReleased(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX();//model.getShapeOnHold().getStartPosition().getX();
            double realY = mouseEvent.getY();//model.getShapeOnHold().getStartPosition().getY();
            model.releaseMouse(realX < thicknessOffset ? thicknessOffset
                            : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                    realY < thicknessOffset ? thicknessOffset
                            : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                    modelLine);
        });
        line.setOnMouseDragged(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX() - ToolbarView.TOOLBAR_WIDTH;
            double realY = mouseEvent.getY() - ToolbarView.TOP_HEIGHT;
            if (model.getShapeOnHold() != null && (model.getShapeOnHold().getStartPosition().getX() > thicknessOffset
                    && model.getShapeOnHold().getStartPosition().getY() > thicknessOffset)) {
                model.moveMouse(realX < thicknessOffset ? thicknessOffset
                                : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                        realY < thicknessOffset ? thicknessOffset
                                : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                        modelLine);
            } else if (model.getShapeOnHold() != null) {
                if (model.getShapeOnHold().getStartPosition().getX() <= thicknessOffset) {
                    model.getShapeOnHold().setStartX(thicknessOffset + 1);
                }
                if (model.getShapeOnHold().getStartPosition().getY() <= thicknessOffset) {
                    model.getShapeOnHold().setStartY(thicknessOffset + 1);
                }
            }
        });
        this.getChildren().add(line);
        shapes.add(new vmShape(line, modelLine));
    }

    private void drawCircle(ModelCircle modelCircle) {
        Circle circle = new Circle();
        double startX = modelCircle.getStartPosition().getX() * widthFactor,
                startY = modelCircle.getStartPosition().getY() * heightFactor,
                endX = modelCircle.getEndPosition().getX() * widthFactor,
                endY = modelCircle.getEndPosition().getY() * heightFactor;
        double xDist = (endX - startX) / 2, yDist = (endY - startY) / 2;
        if (startX > endX) {
            circle.setCenterX(-xDist + endX);
        } else {
            circle.setCenterX(xDist + startX);
        }
        if (startY > endY) {
            circle.setCenterY(-yDist + endY);
        } else {
            circle.setCenterY(yDist + startY);
        }
        circle.setRadius(Math.abs(xDist > yDist ? yDist : xDist));
        circle.setStroke(Color.color(modelCircle.getShadeLineColor().getRed(),
                modelCircle.getShadeLineColor().getGreen(),
                modelCircle.getShadeLineColor().getBlue()));
        circle.setFill(Color.color(modelCircle.getShadeFillColor().getRed(),
                modelCircle.getShadeFillColor().getGreen(),
                modelCircle.getShadeFillColor().getBlue()));
        if (modelCircle.getThickness() == CurrentThickness.SMALL) {
            circle.setStrokeWidth(SMALL_SIZE);
        } else if (modelCircle.getThickness() == CurrentThickness.MEDIUM) {
            circle.setStrokeWidth(MEDIUM_SIZE);
        } else {
            circle.setStrokeWidth(LARGE_SIZE);
        }
        if (modelCircle.getStyle() == CurrentStyle.DOTTED) {
            circle.getStrokeDashArray().addAll(DOTTED_DASH, DOTTED_GAP);
        } else if (modelCircle.getStyle() == CurrentStyle.DASHED) {
            circle.getStrokeDashArray().addAll(DASHED_DASH, DASHED_GAP);
        }
        circle.setOnMousePressed(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX();//model.getShapeOnHold().getStartPosition().getX();
            double realY = mouseEvent.getY();//model.getShapeOnHold().getStartPosition().getY();
            model.holdMouse(realX < thicknessOffset ? thicknessOffset
                            : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                    realY < thicknessOffset ? thicknessOffset
                            : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                    modelCircle);
        });
        circle.setOnMouseReleased(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX();//model.getShapeOnHold().getStartPosition().getX();
            double realY = mouseEvent.getY();//model.getShapeOnHold().getStartPosition().getY();
            model.releaseMouse(realX < thicknessOffset ? thicknessOffset
                            : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                    realY < thicknessOffset ? thicknessOffset
                            : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                    modelCircle);
        });
        circle.setOnMouseDragged(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX() - ToolbarView.TOOLBAR_WIDTH;
            double realY = mouseEvent.getY() - ToolbarView.TOP_HEIGHT;
            if (model.getShapeOnHold() != null && (model.getShapeOnHold().getStartPosition().getX() > thicknessOffset
                    && model.getShapeOnHold().getStartPosition().getY() > thicknessOffset)) {
                model.moveMouse(realX < thicknessOffset ? thicknessOffset
                                : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                        realY < thicknessOffset ? thicknessOffset
                                : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                        modelCircle);
            } else if (model.getShapeOnHold() != null) {
                if (model.getShapeOnHold().getStartPosition().getX() <= thicknessOffset) {
                    model.getShapeOnHold().setStartX(thicknessOffset + 1);
                }
                if (model.getShapeOnHold().getStartPosition().getY() <= thicknessOffset) {
                    model.getShapeOnHold().setStartY(thicknessOffset + 1);
                }
            }
        });
        this.getChildren().add(circle);
        shapes.add(new vmShape(circle, modelCircle));
    }

    private void drawRectangle(ModelRectangle modelRectangle) {
        Rectangle rectangle = new Rectangle();
        double startX = modelRectangle.getStartPosition().getX() * widthFactor,
                startY = modelRectangle.getStartPosition().getY() * heightFactor,
                endX = modelRectangle.getEndPosition().getX() * widthFactor,
                endY = modelRectangle.getEndPosition().getY() * heightFactor;
        if (startX > endX) {
            rectangle.setX(endX);
        } else {
            rectangle.setX(startX);
        }
        if (startY > endY) {
            rectangle.setY(endY);
        } else {
            rectangle.setY(startY);
        }
        rectangle.setWidth(Math.abs(endX - startX));
        rectangle.setHeight(Math.abs(endY - startY));
        rectangle.setStroke(Color.color(modelRectangle.getShadeLineColor().getRed(),
                modelRectangle.getShadeLineColor().getGreen(),
                modelRectangle.getShadeLineColor().getBlue()));
        rectangle.setFill(Color.color(modelRectangle.getShadeFillColor().getRed(),
                modelRectangle.getShadeFillColor().getGreen(),
                modelRectangle.getShadeFillColor().getBlue()));
        if (modelRectangle.getThickness() == CurrentThickness.SMALL) {
            rectangle.setStrokeWidth(SMALL_SIZE);
        } else if (modelRectangle.getThickness() == CurrentThickness.MEDIUM) {
            rectangle.setStrokeWidth(MEDIUM_SIZE);
        } else {
            rectangle.setStrokeWidth(LARGE_SIZE);
        }
        if (modelRectangle.getStyle() == CurrentStyle.DOTTED) {
            rectangle.getStrokeDashArray().addAll(DOTTED_DASH, DOTTED_GAP);
        } else if (modelRectangle.getStyle() == CurrentStyle.DASHED) {
            rectangle.getStrokeDashArray().addAll(DASHED_DASH, DASHED_GAP);
        }
        rectangle.setOnMousePressed(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX();//model.getShapeOnHold().getStartPosition().getX();
            double realY = mouseEvent.getY();//model.getShapeOnHold().getStartPosition().getY();
            model.holdMouse(realX < thicknessOffset ? thicknessOffset
                            : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                    realY < thicknessOffset ? thicknessOffset
                            : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                    modelRectangle);
        });
        rectangle.setOnMouseReleased(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX();//model.getShapeOnHold().getStartPosition().getX();
            double realY = mouseEvent.getY();//model.getShapeOnHold().getStartPosition().getY();
            model.releaseMouse(realX < thicknessOffset ? thicknessOffset
                            : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                    realY < thicknessOffset ? thicknessOffset
                            : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                    modelRectangle);
        });
        rectangle.setOnMouseDragged(mouseEvent -> {
            double thicknessOffset = MEDIUM_SIZE;
            if (model.getCurrentThickness() == CurrentThickness.SMALL) {
                thicknessOffset = SMALL_SIZE;
            } else if (model.getCurrentThickness() == CurrentThickness.LARGE) {
                thicknessOffset = LARGE_SIZE;
            }
            double realX = mouseEvent.getX() - ToolbarView.TOOLBAR_WIDTH;
            double realY = mouseEvent.getY() - ToolbarView.TOP_HEIGHT;
            if (model.getShapeOnHold() != null && (model.getShapeOnHold().getStartPosition().getX() > thicknessOffset
            && model.getShapeOnHold().getStartPosition().getY() > thicknessOffset)) {
                model.moveMouse(realX < thicknessOffset ? thicknessOffset
                                : (realX > canvas.getWidth() - thicknessOffset ? (canvas.getWidth() - thicknessOffset) / widthFactor : realX / widthFactor),
                        realY < thicknessOffset ? thicknessOffset
                                : (realY > canvas.getHeight() - thicknessOffset ? (canvas.getHeight() - thicknessOffset) / heightFactor : realY / heightFactor),
                        modelRectangle);
            } else if (model.getShapeOnHold() != null) {
                if (model.getShapeOnHold().getStartPosition().getX() <= thicknessOffset) {
                    model.getShapeOnHold().setStartX(thicknessOffset + 1);
                }
                if (model.getShapeOnHold().getStartPosition().getY() <= thicknessOffset) {
                    model.getShapeOnHold().setStartY(thicknessOffset + 1);
                }
            }
        });
        this.getChildren().add(rectangle);
        shapes.add(new vmShape(rectangle, modelRectangle));
    }
    Shape draggedShape;

    double widthFactor, heightFactor;
    public void resizeWidth(double width) {
        canvas.setWidth(width - ToolbarView.TOOLBAR_WIDTH);
        widthFactor = canvas.getWidth() / (SketchIt.WINDOW_WIDTH - ToolbarView.TOOLBAR_WIDTH);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        updateView();
    }

    public void resizeHeight(double height) {
        canvas.setHeight(height - menuBarHeight);
        heightFactor = canvas.getHeight() / (SketchIt.WINDOW_HEIGHT - menuBarHeight);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        updateView();
    }

    double menuBarHeight;
    public void setCanvasHeight(double height) {
        menuBarHeight = height;
        canvas.setHeight(SketchIt.WINDOW_HEIGHT - menuBarHeight);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0,0, canvas.getWidth(), canvas.getHeight());
    }
}
