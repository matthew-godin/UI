import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class ToolbarView extends VBox implements IView {
    static final String SELECT_TOOL_PATH = "images/mouse.png";
    static final String ERASE_TOOL_PATH = "images/eraser.png";
    static final String LINE_TOOL_PATH = "images/line.png";
    static final String CIRCLE_TOOL_PATH = "images/circle.png";
    static final String RECTANGLE_TOOL_PATH = "images/rectangle.png";
    static final String FILL_TOOL_PATH = "images/bucket.png";
    static final String SMALL_IMAGE_PATH = "images/smallLine.png";
    static final String MEDIUM_IMAGE_PATH = "images/mediumLine.png";
    static final String LARGE_IMAGE_PATH = "images/largeLine.png";
    static final String DOTTED_IMAGE_PATH = "images/dottedLine.png";
    static final String DASHED_IMAGE_PATH = "images/dashedLine.png";
    static final String SKETCHIT_EXTENSION = ".skt";
    static final String SKETCHIT_FILE_EXTENSION = "*" + SKETCHIT_EXTENSION;
    static final String SKETCHIT_NAME = "SketchIt files (" + SKETCHIT_FILE_EXTENSION + ")";
    static final int TOOL_BUTTON_SIZE = 30;
    static final int BOTTOM_BUTTON_SIZE = 20;
    static final double TOOLBAR_SEPARATOR_PADDING = 10;
    static final double TOOLBAR_WIDTH = TOOL_BUTTON_SIZE * 2 + 10;
    static final double TOOLBAR_HEIGHT = SketchIt.WINDOW_MAX_HEIGHT;
    private double menuBarHeight;
    private Model model;
    private EventHandler showHideHandler = t -> { // Taken from HelloMenu.java
        Menu menu = (Menu)t.getSource();          // in the course examples
        if (t.getEventType() == Menu.ON_SHOWING &&
                menu.getText().equals("_Submenu")) {
            Date date = new Date();
            String time = new SimpleDateFormat("HH:mm:ss").format(date);
            menu.getItems().get(0).setText("The time is " + time);
        }
    };
    final ColorPicker lineColorPicker, fillColorPicker;
    ToggleButton smallLineButton, mediumLineButton, largeLineButton, dottedLineButton, normalLineButton, dashedLineButton;

    ToolbarView(Model model, CanvasView canvasView, Stage stage) {
        this.model = model;
        final MenuBar menuBar = new MenuBar();
        final Menu fileMenu = makeMenu("_File");
        MenuItem fileMenuNew = new MenuItem("_New");
        fileMenuNew.setOnAction((ActionEvent e) -> {
            if (!model.isSaved()) {
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "Do you want to save changes to " + model.getFileName() + "?");
                alert.setTitle("SketchIt");
                ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE),
                        dont = new ButtonType("Don't Save", ButtonBar.ButtonData.OK_DONE),
                        cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(save, dont, cancel);
                alert.initOwner(stage);
                ButtonType result = alert.showAndWait().get();
                if (result == save) {
                    if (model.isNameSet()) {
                        // save it
                        saveFile();
                        model.newSketch();
                        defaultSelection();
                    } else {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialFileName(model.getFileName());
                        fileChooser.getExtensionFilters().add(
                                new FileChooser.ExtensionFilter(SKETCHIT_NAME, SKETCHIT_FILE_EXTENSION));
                        File f = fileChooser.showSaveDialog(stage);
                        if(f != null && f.getName().endsWith(SKETCHIT_EXTENSION)) {
                            // save the file
                            model.setFileName(f.getAbsolutePath());
                            saveFile();
                            model.newSketch();
                            defaultSelection();
                        }
                    }
                } else if (result == dont) {
                    model.newSketch();
                }
            } else {
                model.newSketch();
                defaultSelection();
            }
        });
        MenuItem fileMenuLoad = new MenuItem("_Load");
        fileMenuLoad.setOnAction((ActionEvent e) -> {
            FileChooser fileChooser = new FileChooser();
            //fileChooser.setInitialFileName(model.getFileName());
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(SKETCHIT_NAME, SKETCHIT_FILE_EXTENSION));
            File f = fileChooser.showOpenDialog(stage);
            if(f != null && f.getName().endsWith(SKETCHIT_EXTENSION)) {
                try {
                    FileInputStream file = new FileInputStream(f.getAbsolutePath());
                    ObjectInputStream in = new ObjectInputStream(file);
                    ArrayList<IModelShape> shapes = (ArrayList<IModelShape>)in.readObject();
                    in.close();
                    file.close();
                    System.out.println(f.getAbsolutePath());
                    if (!model.isSaved()) {
                        Alert alert = new Alert(Alert.AlertType.NONE,
                                "Do you want to save changes to " + model.getFileName() + "?");
                        alert.setTitle("SketchIt");
                        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE),
                                dont = new ButtonType("Don't Save", ButtonBar.ButtonData.OK_DONE),
                                cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                        alert.getButtonTypes().setAll(save, dont, cancel);
                        alert.initOwner(stage);
                        ButtonType result = alert.showAndWait().get();
                        if (result == save) {
                            if (model.isNameSet()) {
                                // save it
                                saveFile();
                                model.newSketch();
                                defaultSelection();
                            } else {
                                FileChooser fileChooser2 = new FileChooser();
                                fileChooser2.setInitialFileName(model.getFileName());
                                fileChooser2.getExtensionFilters().add(
                                        new FileChooser.ExtensionFilter(SKETCHIT_NAME, SKETCHIT_FILE_EXTENSION));
                                File f2 = fileChooser2.showSaveDialog(stage);
                                if(f2 != null && f2.getName().endsWith(SKETCHIT_EXTENSION)) {
                                    // save the file
                                    model.setFileName(f2.getAbsolutePath());
                                    saveFile();
                                    model.loadSketch(shapes);
                                    defaultSelection();
                                }
                            }
                        } else if (result == dont) {
                            model.loadSketch(shapes);
                        }
                    } else {
                        model.loadSketch(shapes);
                        defaultSelection();
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
        MenuItem fileMenuSave = new MenuItem("_Save");
        fileMenuSave.setOnAction((ActionEvent e) -> {
            if (!model.isSaved()) {
                if (model.isNameSet()) {
                    // save it
                    saveFile();
                    model.newSketch();
                    defaultSelection();
                } else {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(model.getFileName());
                    fileChooser.getExtensionFilters().add(
                            new FileChooser.ExtensionFilter(SKETCHIT_NAME, SKETCHIT_FILE_EXTENSION));
                    File f = fileChooser.showSaveDialog(stage);
                    if(f != null && f.getName().endsWith(SKETCHIT_EXTENSION)) {
                        // save the file
                        model.setFileName(f.getAbsolutePath());
                        saveFile();
                        model.newSketch();
                        defaultSelection();
                    }
                }
            }
        });
        MenuItem fileMenuQuit = new MenuItem("_Quit");
        fileMenuQuit.setOnAction((ActionEvent e) -> {
            if (!model.isSaved()) {
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "Do you want to save changes to " + model.getFileName() + "?");
                alert.setTitle("SketchIt");
                ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE),
                        dont = new ButtonType("Don't Save", ButtonBar.ButtonData.OK_DONE),
                        cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(save, dont, cancel);
                alert.initOwner(stage);
                ButtonType result = alert.showAndWait().get();
                if (result == save) {
                    if (model.isNameSet()) {
                        // save it
                        saveFile();
                        model.newSketch();
                        defaultSelection();
                    } else {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialFileName(model.getFileName());
                        fileChooser.getExtensionFilters().add(
                                new FileChooser.ExtensionFilter(SKETCHIT_NAME, SKETCHIT_FILE_EXTENSION));
                        File f = fileChooser.showSaveDialog(stage);
                        if(f != null && f.getName().endsWith(SKETCHIT_EXTENSION)) {
                            // save the file
                            model.setFileName(f.getAbsolutePath());
                            saveFile();
                            exitProgram();
                        }
                    }
                } else if (result == dont) {
                    exitProgram();
                }
            } else {
                exitProgram();
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                windowEvent.consume();
                if (!model.isSaved()) {
                    Alert alert = new Alert(Alert.AlertType.NONE,
                            "Do you want to save changes to " + model.getFileName() + "?");
                    alert.setTitle("SketchIt");
                    ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE),
                            dont = new ButtonType("Don't Save", ButtonBar.ButtonData.OK_DONE),
                            cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(save, dont, cancel);
                    alert.initOwner(stage);
                    ButtonType result = alert.showAndWait().get();
                    if (result == save) {
                        if (model.isNameSet()) {
                            // save it
                            saveFile();
                            model.newSketch();
                            defaultSelection();
                        } else {
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setInitialFileName(model.getFileName());
                            fileChooser.getExtensionFilters().add(
                                    new FileChooser.ExtensionFilter(SKETCHIT_NAME, SKETCHIT_FILE_EXTENSION));
                            File f = fileChooser.showSaveDialog(stage);
                            if(f != null && f.getName().endsWith(SKETCHIT_EXTENSION)) {
                                // save the file
                                model.setFileName(f.getAbsolutePath());
                                saveFile();
                                exitProgram();
                            }
                        }
                    } else if (result == dont) {
                        exitProgram();
                    }
                } else {
                    exitProgram();
                }
            }
        });
        fileMenu.getItems().addAll(fileMenuNew, fileMenuLoad, fileMenuSave,
                                    fileMenuQuit);
        menuBar.getMenus().add(fileMenu);
        this.getChildren().add(menuBar);
        menuBarHeight = menuBar.getHeight();
        HBox bottom = new HBox();
        final ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setMinWidth(TOOLBAR_WIDTH);
        toolBar.setMinHeight(TOOLBAR_HEIGHT);
        GridPane toolBarGrid = new GridPane();
        final ToggleGroup toolGroup = new ToggleGroup();
        //toolGroup.selectedToggleProperty().addListener(...)
        ToggleButton selectButton = new ToggleButton();
        setupButton(selectButton, TOOL_BUTTON_SIZE, SELECT_TOOL_PATH, toolGroup);
        selectButton.setOnAction((ActionEvent e) -> {
            if (selectButton.isSelected()) {
                model.selectSelection();
            }
        });
        ToggleButton eraseButton = new ToggleButton();
        setupButton(eraseButton, TOOL_BUTTON_SIZE, ERASE_TOOL_PATH, toolGroup);
        eraseButton.setOnAction((ActionEvent e) -> {
            if (eraseButton.isSelected()) {
                model.selectEraser();
            }
        });
        ToggleButton lineButton = new ToggleButton();
        setupButton(lineButton, TOOL_BUTTON_SIZE, LINE_TOOL_PATH, toolGroup);
        lineButton.setOnAction((ActionEvent e) -> {
            if (lineButton.isSelected()) {
                model.selectLine();
            }
        });
        ToggleButton circleButton = new ToggleButton();
        setupButton(circleButton, TOOL_BUTTON_SIZE, CIRCLE_TOOL_PATH, toolGroup);
        circleButton.setOnAction((ActionEvent e) -> {
            if (circleButton.isSelected()) {
                model.selectCircle();
            }
        });
        ToggleButton rectangleButton = new ToggleButton();
        setupButton(rectangleButton, TOOL_BUTTON_SIZE, RECTANGLE_TOOL_PATH, toolGroup);
        rectangleButton.setOnAction((ActionEvent e) -> {
            if (rectangleButton.isSelected()) {
                model.selectRectangle();
            }
        });
        ToggleButton fillButton = new ToggleButton();
        setupButton(fillButton, TOOL_BUTTON_SIZE, FILL_TOOL_PATH, toolGroup);
        fillButton.setOnAction((ActionEvent e) -> {
            if (fillButton.isSelected()) {
                model.selectFill();
            }
        });
        toolBarGrid.add(selectButton, 0, 0);
        toolBarGrid.add(eraseButton, 1, 0);
        toolBarGrid.add(lineButton, 0, 1);
        toolBarGrid.add(circleButton, 1, 1);
        toolBarGrid.add(rectangleButton, 0, 2);
        toolBarGrid.add(fillButton, 1, 2);
        VBox toolBarVBox = new VBox();
        Separator toolBarSeparator = new Separator();
        toolBarSeparator.setPadding(new Insets(TOOLBAR_SEPARATOR_PADDING,
                TOOLBAR_SEPARATOR_PADDING,TOOLBAR_SEPARATOR_PADDING,
                TOOLBAR_SEPARATOR_PADDING));
        toolBarVBox.getChildren().add(toolBarGrid);
        toolBarVBox.getChildren().add(toolBarSeparator);
        HBox colorPickers = new HBox();
        lineColorPicker = new ColorPicker();
        lineColorPicker.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
        lineColorPicker.setStyle("-fx-color-label-visible: false;");
        lineColorPicker.setMinWidth(TOOL_BUTTON_SIZE);
        lineColorPicker.setMinHeight(TOOL_BUTTON_SIZE);
        lineColorPicker.setOnAction((ActionEvent e) -> model.setLineColor(lineColorPicker.getValue().getRed(),
                lineColorPicker.getValue().getGreen(),
                lineColorPicker.getValue().getBlue()));
        fillColorPicker = new ColorPicker();
        fillColorPicker.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
        fillColorPicker.setStyle("-fx-color-label-visible: false;");
        fillColorPicker.setMinWidth(TOOL_BUTTON_SIZE);
        fillColorPicker.setMinHeight(TOOL_BUTTON_SIZE);
        fillColorPicker.setOnAction((ActionEvent e) -> {
            model.setFillColor(fillColorPicker.getValue().getRed(),
                    fillColorPicker.getValue().getGreen(),
                    fillColorPicker.getValue().getBlue());
        });
        VBox lineColorVBox = new VBox();
        Label lineColorLabel = new Label("Line");
        lineColorVBox.setAlignment(Pos.CENTER);
        VBox fillColorVBox = new VBox();
        Label fillColorLabel = new Label("Fill");
        fillColorVBox.setAlignment(Pos.CENTER);
        lineColorVBox.getChildren().add(lineColorLabel);
        lineColorVBox.getChildren().add(lineColorPicker);
        fillColorVBox.getChildren().add(fillColorLabel);
        fillColorVBox.getChildren().add(fillColorPicker);
        colorPickers.getChildren().add(lineColorVBox);
        colorPickers.getChildren().add(fillColorVBox);
        toolBarVBox.getChildren().add(colorPickers);
        VBox lineSizeVBox = new VBox();
        lineSizeVBox.setAlignment(Pos.CENTER);
        Label lineSizeLabel = new Label("Line Size");
        lineSizeVBox.getChildren().add(lineSizeLabel);
        HBox lineSizeHBox = new HBox();
        final ToggleGroup lineSizeGroup = new ToggleGroup();
        smallLineButton = new ToggleButton();
        setupButton(smallLineButton, BOTTOM_BUTTON_SIZE, SMALL_IMAGE_PATH,
                        lineSizeGroup);
        smallLineButton.setOnAction((ActionEvent e) -> {
            if (smallLineButton.isSelected()) {
                model.selectSmall();
            }
        });
        mediumLineButton = new ToggleButton();
        setupButton(mediumLineButton, BOTTOM_BUTTON_SIZE, MEDIUM_IMAGE_PATH,
                lineSizeGroup);
        mediumLineButton.setOnAction((ActionEvent e) -> {
            if (mediumLineButton.isSelected()) {
                model.selectMedium();
            }
        });
        largeLineButton = new ToggleButton();
        setupButton(largeLineButton, BOTTOM_BUTTON_SIZE, LARGE_IMAGE_PATH,
                lineSizeGroup);
        largeLineButton.setOnAction((ActionEvent e) -> {
            if (largeLineButton.isSelected()) {
                model.selectLarge();
            }
        });
        lineSizeHBox.getChildren().add(smallLineButton);
        lineSizeHBox.getChildren().add(mediumLineButton);
        lineSizeHBox.getChildren().add(largeLineButton);
        lineSizeVBox.getChildren().add(lineSizeHBox);
        VBox lineStyleVBox = new VBox();
        lineStyleVBox.setAlignment(Pos.CENTER);
        Label lineStyleLabel = new Label("Line Style");
        lineStyleVBox.getChildren().add(lineStyleLabel);
        HBox lineStyleHBox = new HBox();
        final ToggleGroup lineStyleGroup = new ToggleGroup();
        dottedLineButton = new ToggleButton();
        setupButton(dottedLineButton, BOTTOM_BUTTON_SIZE, DOTTED_IMAGE_PATH,
                lineStyleGroup);
        final ToggleButton dottedButton = dottedLineButton;
        dottedLineButton.setOnAction((ActionEvent e) -> {
            if (dottedButton.isSelected()) {
                model.selectDotted();
            }
        });
        normalLineButton = new ToggleButton();
        setupButton(normalLineButton, BOTTOM_BUTTON_SIZE, MEDIUM_IMAGE_PATH,
                lineStyleGroup);
        normalLineButton.setOnAction((ActionEvent e) -> {
            if (normalLineButton.isSelected()) {
                model.selectNormal();
            }
        });
        dashedLineButton = new ToggleButton();
        setupButton(dashedLineButton, BOTTOM_BUTTON_SIZE, DASHED_IMAGE_PATH,
                lineStyleGroup);
        dashedLineButton.setOnAction((ActionEvent e) -> {
            if (dashedLineButton.isSelected()) {
                model.selectDashed();
            }
        });
        lineStyleHBox.getChildren().add(dottedLineButton);
        lineStyleHBox.getChildren().add(normalLineButton);
        lineStyleHBox.getChildren().add(dashedLineButton);
        lineStyleVBox.getChildren().add(lineStyleHBox);
        toolBarVBox.getChildren().add(lineSizeVBox);
        toolBarVBox.getChildren().add(lineStyleVBox);
        toolBar.getItems().add(toolBarVBox);
        bottom.getChildren().add(toolBar);
        bottom.getChildren().add(canvasView);
        this.getChildren().add(bottom);
        model.addView(this);
        lineColorPicker.setValue(Color.BLACK);
    }

    public void defaultSelection() {

    }

    private void setupButton(ToggleButton button, int size, String path,
                             ToggleGroup group) {
        button.setToggleGroup(group);
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        button.setMaxWidth(size);
        button.setMaxHeight(size);
        button.setMinWidth(size);
        button.setMinHeight(size);
        button.setGraphic(imageView);
    }

    private Menu makeMenu(String text) { // makeMenu was taken from the
        return makeMenu(text, null); // HelloMenu.java example
    }

    private Menu makeMenu(String text, Node graphic) {
        Menu menu = new Menu(text, graphic);
        menu.setOnShowing(showHideHandler);
        menu.setOnShown(showHideHandler);
        menu.setOnHiding(showHideHandler);
        menu.setOnHidden(showHideHandler);
        return menu;
    }

    public double getMenuBarHeight() {
        return menuBarHeight;
    }

    private void saveFile() {
        try {
            FileOutputStream file = new FileOutputStream(
                    model.getFileName()
            );
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(model.getShapes());
            out.close();
            file.close();
            System.out.println(model.getFileName());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public void updateView() {
        lineColorPicker.setValue(Color.color(model.getLineColor().getRed(), model.getLineColor().getGreen(), model.getLineColor().getBlue()));
        fillColorPicker.setValue(Color.color(model.getFillColor().getRed(), model.getFillColor().getGreen(), model.getFillColor().getBlue()));
        if (model.getCurrentThickness() == CurrentThickness.SMALL) {
            smallLineButton.setSelected(true);
        } else if (model.getCurrentThickness() == CurrentThickness.MEDIUM) {
            mediumLineButton.setSelected(true);
        } else {
            largeLineButton.setSelected(true);
        }
        if (model.getCurrentStyle() == CurrentStyle.DOTTED) {
            dottedLineButton.setSelected(true);
        } else if (model.getCurrentStyle() == CurrentStyle.NORMAL) {
            normalLineButton.setSelected(true);
        } else {
            dashedLineButton.setSelected(true);
        }
    }

    void exitProgram() {
        Platform.exit();
        System.exit(0);
    }
}