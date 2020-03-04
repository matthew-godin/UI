import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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

import javax.swing.*;
import java.text.SimpleDateFormat;
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
    static final int TOOL_BUTTON_SIZE = 30;
    static final int BOTTOM_BUTTON_SIZE = 20;
    static final double TOOLBAR_SEPARATOR_PADDING = 10;
    static final double TOOLBAR_WIDTH = TOOL_BUTTON_SIZE * 2 + 10;
    static final double TOOLBAR_HEIGHT = 1440;
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

    ToolbarView(Model model, Controller controller, CanvasView canvasView) {
        this.model = model;
        final MenuBar menuBar = new MenuBar();
        final Menu fileMenu = makeMenu("_File");
        MenuItem fileMenuNew = new MenuItem("_New");
        MenuItem fileMenuLoad = new MenuItem("_Load");
        MenuItem fileMenuSave = new MenuItem("_Save");
        MenuItem fileMenuQuit = new MenuItem("_Quit");
        fileMenu.getItems().addAll(fileMenuNew, fileMenuLoad, fileMenuSave,
                                    fileMenuQuit);
        menuBar.getMenus().add(fileMenu);
        this.getChildren().add(menuBar);
        HBox bottom = new HBox();
        final ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setMinWidth(TOOLBAR_WIDTH);
        toolBar.setMinHeight(TOOLBAR_HEIGHT);
        GridPane toolBarGrid = new GridPane();
        final ToggleGroup toolGroup = new ToggleGroup();
        //toolGroup.selectedToggleProperty().addListener(...)
        ToggleButton selectButton = new ToggleButton();
        selectButton.setToggleGroup(toolGroup);
        ToggleButton eraseButton = new ToggleButton();
        eraseButton.setToggleGroup(toolGroup);
        ToggleButton lineButton = new ToggleButton();
        lineButton.setToggleGroup(toolGroup);
        ToggleButton circleButton = new ToggleButton();
        circleButton.setToggleGroup(toolGroup);
        ToggleButton rectangleButton = new ToggleButton();
        rectangleButton.setToggleGroup(toolGroup);
        ToggleButton fillButton = new ToggleButton();
        fillButton.setToggleGroup(toolGroup);
        Image selectImage = new Image(SELECT_TOOL_PATH);
        Image eraseImage = new Image(ERASE_TOOL_PATH);
        Image lineImage = new Image(LINE_TOOL_PATH);
        Image circleImage = new Image(CIRCLE_TOOL_PATH);
        Image rectangleImage = new Image(RECTANGLE_TOOL_PATH);
        Image fillImage = new Image(FILL_TOOL_PATH);
        ImageView selectImageView = new ImageView(selectImage);
        selectImageView.setFitWidth(TOOL_BUTTON_SIZE);
        selectImageView.setFitHeight(TOOL_BUTTON_SIZE);
        selectButton.setMaxWidth(TOOL_BUTTON_SIZE);
        selectButton.setMaxHeight(TOOL_BUTTON_SIZE);
        selectButton.setMinWidth(TOOL_BUTTON_SIZE);
        selectButton.setMinHeight(TOOL_BUTTON_SIZE);
        selectButton.setGraphic(selectImageView);
        ImageView eraseImageView = new ImageView(eraseImage);
        eraseImageView.setFitWidth(TOOL_BUTTON_SIZE);
        eraseImageView.setFitHeight(TOOL_BUTTON_SIZE);
        eraseButton.setMaxWidth(TOOL_BUTTON_SIZE);
        eraseButton.setMaxHeight(TOOL_BUTTON_SIZE);
        eraseButton.setMinWidth(TOOL_BUTTON_SIZE);
        eraseButton.setMinHeight(TOOL_BUTTON_SIZE);
        eraseButton.setGraphic(eraseImageView);
        ImageView lineImageView = new ImageView(lineImage);
        lineImageView.setFitWidth(TOOL_BUTTON_SIZE);
        lineImageView.setFitHeight(TOOL_BUTTON_SIZE);
        lineButton.setMaxWidth(TOOL_BUTTON_SIZE);
        lineButton.setMaxHeight(TOOL_BUTTON_SIZE);
        lineButton.setMinWidth(TOOL_BUTTON_SIZE);
        lineButton.setMinHeight(TOOL_BUTTON_SIZE);
        lineButton.setGraphic(lineImageView);
        ImageView circleImageView = new ImageView(circleImage);
        circleImageView.setFitWidth(TOOL_BUTTON_SIZE);
        circleImageView.setFitHeight(TOOL_BUTTON_SIZE);
        circleButton.setMaxWidth(TOOL_BUTTON_SIZE);
        circleButton.setMaxHeight(TOOL_BUTTON_SIZE);
        circleButton.setMinWidth(TOOL_BUTTON_SIZE);
        circleButton.setMinHeight(TOOL_BUTTON_SIZE);
        circleButton.setGraphic(circleImageView);
        ImageView rectangleImageView = new ImageView(rectangleImage);
        rectangleImageView.setFitWidth(TOOL_BUTTON_SIZE);
        rectangleImageView.setFitHeight(TOOL_BUTTON_SIZE);
        rectangleButton.setMaxWidth(TOOL_BUTTON_SIZE);
        rectangleButton.setMaxHeight(TOOL_BUTTON_SIZE);
        rectangleButton.setMinWidth(TOOL_BUTTON_SIZE);
        rectangleButton.setMinHeight(TOOL_BUTTON_SIZE);
        rectangleButton.setGraphic(rectangleImageView);
        ImageView fillImageView = new ImageView(fillImage);
        fillImageView.setFitWidth(TOOL_BUTTON_SIZE);
        fillImageView.setFitHeight(TOOL_BUTTON_SIZE);
        fillButton.setMaxWidth(TOOL_BUTTON_SIZE);
        fillButton.setMaxHeight(TOOL_BUTTON_SIZE);
        fillButton.setMinWidth(TOOL_BUTTON_SIZE);
        fillButton.setMinHeight(TOOL_BUTTON_SIZE);
        fillButton.setGraphic(fillImageView);
        toolBarGrid.add(selectButton, 0, 0);
        toolBarGrid.add(eraseButton, 1, 0);
        toolBarGrid.add(lineButton, 0, 1);
        toolBarGrid.add(circleButton, 1, 1);
        toolBarGrid.add(rectangleButton, 0, 2);
        toolBarGrid.add(fillButton, 1, 2);
        VBox toolBarVBox = new VBox();
        Separator toolBarSeparator = new Separator();
        toolBarSeparator.setPadding(new Insets(TOOLBAR_SEPARATOR_PADDING,TOOLBAR_SEPARATOR_PADDING,TOOLBAR_SEPARATOR_PADDING,TOOLBAR_SEPARATOR_PADDING));
        toolBarVBox.getChildren().add(toolBarGrid);
        toolBarVBox.getChildren().add(toolBarSeparator);
        HBox colorPickers = new HBox();
        final ColorPicker lineColorPicker = new ColorPicker();
        lineColorPicker.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
        lineColorPicker.setStyle("-fx-color-label-visible: false;");
        lineColorPicker.setMinWidth(TOOL_BUTTON_SIZE);
        lineColorPicker.setMinHeight(TOOL_BUTTON_SIZE);
        final ColorPicker fillColorPicker = new ColorPicker();
        fillColorPicker.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
        fillColorPicker.setStyle("-fx-color-label-visible: false;");
        fillColorPicker.setMinWidth(TOOL_BUTTON_SIZE);
        fillColorPicker.setMinHeight(TOOL_BUTTON_SIZE);
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
        ToggleButton smallLineButton = new ToggleButton();
        setupButton(smallLineButton, BOTTOM_BUTTON_SIZE, SMALL_IMAGE_PATH,
                        lineSizeGroup);
        ToggleButton mediumLineButton = new ToggleButton();
        setupButton(mediumLineButton, BOTTOM_BUTTON_SIZE, MEDIUM_IMAGE_PATH,
                lineSizeGroup);
        ToggleButton largeLineButton = new ToggleButton();
        setupButton(largeLineButton, BOTTOM_BUTTON_SIZE, LARGE_IMAGE_PATH,
                lineSizeGroup);
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
        ToggleButton dottedLineButton = new ToggleButton();
        setupButton(dottedLineButton, BOTTOM_BUTTON_SIZE, DOTTED_IMAGE_PATH,
                lineStyleGroup);
        ToggleButton normalLineButton = new ToggleButton();
        setupButton(normalLineButton, BOTTOM_BUTTON_SIZE, MEDIUM_IMAGE_PATH,
                lineStyleGroup);
        ToggleButton dashedLineButton = new ToggleButton();
        setupButton(dashedLineButton, BOTTOM_BUTTON_SIZE, DASHED_IMAGE_PATH,
                lineStyleGroup);
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

    public void updateView() {
        // update view from the model
    }
}