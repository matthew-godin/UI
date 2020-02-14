// Author: Matthew Godin
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SpaceInvaders extends Application {
    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;
    static final String WINDOW_TITLE = "Space Invaders";
    static final String SPACE_INVADERS_LOGO_PATH = "images/si_logo.png";
    static final boolean CAN_RESIZE_WINDOW = false;
    static final int HALF_SIZE = 2;
    static final int FOURTH_SIZE = 4;
    static final int EIGHT_SIZE = 8;
    static final String INSTRUCTIONS_FONT_PATH =
            "fonts/MachineStd-Medium.otf";
    static final int INSTRUCTIONS_FONT_SIZE = 36;
    static final String INSTRUCTIONS_TEXT = "Instructions";
    @Override
    public void start(Stage stage) {
        stage.setResizable(CAN_RESIZE_WINDOW);
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH,
                                WINDOW_HEIGHT, Color.BLACK);
        Image spaceInvadersLogo = new Image(SPACE_INVADERS_LOGO_PATH);
        ImageView spaceInvadersLogoView = new ImageView(spaceInvadersLogo);
        spaceInvadersLogoView.setX(WINDOW_WIDTH / HALF_SIZE
                                    - spaceInvadersLogo.getWidth()
                                    / HALF_SIZE);
        spaceInvadersLogoView.setY(spaceInvadersLogo.getHeight()
                                    / FOURTH_SIZE);
        Text instructions = new Text(INSTRUCTIONS_TEXT);
        instructions.setFill(Color.WHITE);
        instructions.setFont(Font.loadFont(getClass()
                                            .getResourceAsStream(
                                                    INSTRUCTIONS_FONT_PATH
                                            ),
                                            INSTRUCTIONS_FONT_SIZE));
        instructions.setX(WINDOW_WIDTH / HALF_SIZE
                            - instructions.getLayoutBounds()
                                .getWidth() / HALF_SIZE);
        instructions.setY(spaceInvadersLogo.getHeight()
                            + spaceInvadersLogoView.getY()
                            + spaceInvadersLogo.getHeight() / HALF_SIZE);
        root.getChildren().add(spaceInvadersLogoView);
        root.getChildren().add(instructions);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }
}
