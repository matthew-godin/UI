// Author: Matthew Godin
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import java.util.Random;

public class SpaceInvaders extends Application {
    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;
    static final String WINDOW_TITLE = "Space Invaders";
    static final String SPACE_INVADERS_LOGO_PATH = "images/si_logo.png";
    static final boolean CAN_RESIZE_WINDOW = false;
    static final int HALF_SIZE = 2;
    static final double LOGO_SCALE = 0.75;
    static final int FOURTH_SIZE = 4;
    static final int EIGHT_SIZE = 8;
    static final int SIXTEENTH_SIZE = 16;
    static final String INSTRUCTIONS_FONT_PATH =
            "fonts/MachineStd-Medium.otf";
    static final String GAME_FONT_PATH = "fonts/space_invaders.ttf";
    static final int GAME_FONT_SIZE = 18;
    static final int GAME_TEXT_SPACING = WINDOW_WIDTH / SIXTEENTH_SIZE;
    static final int INSTRUCTIONS_FONT_SIZE = 36;
    static final int INSTRUCTIONS_CONTENT_FONT_SIZE = 18;
    static final int IMPLEMENTED_FONT_SIZE = 12;
    static final String IMPLEMENTED_FONT = "Verdana";
    static final double IMPLEMENTED_SPACING = 1.0;
    static final String INSTRUCTIONS_TEXT = "Instructions";
    static final String INSTRUCTIONS_LINES[] = {"ENTER - Start Game",
                                            "A, D - Move ship left "
                                                + "or right",
                                                "SPACE - Fire",
                                                "Q - Quit Game",
                                        "1, 2 or 3 - Start game "
                                            + "at a specific level"};
    static final String IMPLEMENTED_STRING = "Implemented by Matthew Godin"
            + " for CS 349, University of Waterloo, W20";
    static final double INSTRUCTIONS_SPACING = 1.5;
    static final int NUM_INSTRUCTION_LINES = 5;
    static final int INIT_I = 0;
    static final int LEVEL_1 = 1, LEVEL_2 = 2, LEVEL_3 = 3;
    static final String SCORE_STRING = "Score: ";
    static final String LIVES_STRING = "Lives: ";
    static final String LEVEL_STRING = "Level: ";
    static int START_SCORE = 0;
    static int START_LIVES = 3;
    static int START_LEVEL = LEVEL_1;
    static int currentScore = START_SCORE;
    static int currentLives = START_LIVES;
    static int currentLevel = START_LEVEL;
    static int ALIEN_ROWS = 5;
    static int ALIEN_COLUMNS = 10;
    static int NUM_ALIENS = ALIEN_ROWS * ALIEN_COLUMNS;
    static int NUM_GREEN_ALIENS = ALIEN_COLUMNS;
    static int NUM_BLUE_ALIENS = ALIEN_COLUMNS * 2;
    static int NUM_PINK_ALIENS = ALIEN_COLUMNS * 2;
    static int FIRST_ALIEN = 0;
    enum AlienType { GREEN, BLUE, PINK };
    Alien[] aliens;
    Ship ship;
    Random rand;
    AudioClip clip, clip1, clip2, clip3, clip4;
    @Override
    public void start(Stage stage) {
        clip = new AudioClip(getClass().getClassLoader().getResource(
                "sounds/invaderkilled.wav"
        ).toString());
        clip1 = new AudioClip(getClass().getClassLoader().getResource(
                "sounds/fastinvader1.wav"
        ).toString());
        clip2 = new AudioClip(getClass().getClassLoader().getResource(
                "sounds/fastinvader2.wav"
        ).toString());
        clip3 = new AudioClip(getClass().getClassLoader().getResource(
                "sounds/fastinvader3.wav"
        ).toString());
        clip4 = new AudioClip(getClass().getClassLoader().getResource(
                "sounds/fastinvader4.wav"
        ).toString());
        rand = new Random();
        stage.setResizable(CAN_RESIZE_WINDOW);
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH,
                                WINDOW_HEIGHT, Color.BLACK);
        Image spaceInvadersLogo = new Image(SPACE_INVADERS_LOGO_PATH);
        ImageView spaceInvadersLogoView = new ImageView(spaceInvadersLogo);
        spaceInvadersLogoView.setScaleX(LOGO_SCALE);
        spaceInvadersLogoView.setScaleY(LOGO_SCALE);
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
                            + spaceInvadersLogo.getHeight() / FOURTH_SIZE);
        Text[] instructionsContent = new Text[NUM_INSTRUCTION_LINES];
        for (int i = INIT_I; i < NUM_INSTRUCTION_LINES; ++i) {
            instructionsContent[i] = new Text(INSTRUCTIONS_LINES[i]);
            instructionsContent[i].setFill(Color.WHITE);
            instructionsContent[i].setFont(Font.loadFont(getClass()
                            .getResourceAsStream(
                                    INSTRUCTIONS_FONT_PATH
                            ),
                    INSTRUCTIONS_CONTENT_FONT_SIZE));
            instructionsContent[i].setX(WINDOW_WIDTH / HALF_SIZE
                    - instructionsContent[i].getLayoutBounds()
                    .getWidth() / HALF_SIZE);
            instructionsContent[i].setY(instructions.getLayoutBounds()
                    .getHeight()
                    + instructions.getY()
                    + instructionsContent[i].getLayoutBounds().getHeight()
                    * i * INSTRUCTIONS_SPACING);
            root.getChildren().add(instructionsContent[i]);
        }
        Text implemented = new Text(IMPLEMENTED_STRING);
        implemented.setFill(Color.WHITE);
        implemented.setFont(Font.font(IMPLEMENTED_FONT,
                                        IMPLEMENTED_FONT_SIZE));
        implemented.setX(WINDOW_WIDTH / HALF_SIZE
                - implemented.getLayoutBounds()
                .getWidth() / HALF_SIZE);
        implemented.setY(WINDOW_HEIGHT - IMPLEMENTED_SPACING
                            * implemented.getLayoutBounds().getHeight());
        root.getChildren().add(implemented);
        root.getChildren().add(spaceInvadersLogoView);
        root.getChildren().add(instructions);
        stage.setTitle(WINDOW_TITLE);
        scene.setOnKeyPressed(keyEvent -> titleScreenKeyListener(
                keyEvent, stage
        ));
        stage.setScene(scene);
        stage.show();
    }

    void exitProgram() {
        Platform.exit();
        System.exit(0);
    }

    void titleScreenKeyListener(KeyEvent event, Stage stage) {
        if (event.getCode() == KeyCode.ENTER) {
            currentLevel = START_LEVEL;
            level(stage);
        } else if (event.getCode() == KeyCode.DIGIT1
                    || event.getCode() == KeyCode.NUMPAD1
                    || event.getCode() == KeyCode.SOFTKEY_1) {
            currentLevel = LEVEL_1;
            level(stage);
        } else if (event.getCode() == KeyCode.DIGIT2
                || event.getCode() == KeyCode.NUMPAD2
                || event.getCode() == KeyCode.SOFTKEY_2) {
            currentLevel = LEVEL_2;
            level(stage);
        } else if (event.getCode() == KeyCode.DIGIT3
                || event.getCode() == KeyCode.NUMPAD3
                || event.getCode() == KeyCode.SOFTKEY_3) {
            currentLevel = LEVEL_3;
            level(stage);
        } else if (event.getCode() == KeyCode.Q) {
            exitProgram();
        }
    }
    AnimationTimer timer;
    int shootTimer, musicTimer;
    static final double MUSIC_SPEED = 20;
    static final double ALIEN_START_SPEED = 0.3;
    void level(Stage stage) {
        Alien.speed = ALIEN_START_SPEED * currentLevel * 2;
        Alien.numAliensAlive = NUM_ALIENS;
        shootTimer = 30;
        musicTimer = 0;
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH,
                WINDOW_HEIGHT, Color.BLACK);
        Text scoreText = new Text(SCORE_STRING + Integer.toString(
                currentScore
        ));
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.loadFont(getClass().getResourceAsStream(
                GAME_FONT_PATH
        ), GAME_FONT_SIZE));
        scoreText.setX(GAME_TEXT_SPACING);
        scoreText.setY(GAME_TEXT_SPACING);
        Text levelText = new Text(LEVEL_STRING + Integer.toString(
                currentLevel
        ));
        levelText.setFill(Color.WHITE);
        levelText.setFont(Font.loadFont(getClass().getResourceAsStream(
                GAME_FONT_PATH
        ), GAME_FONT_SIZE));
        levelText.setX(WINDOW_WIDTH - GAME_TEXT_SPACING
                - levelText.getLayoutBounds().getWidth());
        levelText.setY(GAME_TEXT_SPACING);
        Text livesText = new Text(LIVES_STRING + Integer.toString(
                currentLives
        ));
        livesText.setFill(Color.WHITE);
        livesText.setFont(Font.loadFont(getClass().getResourceAsStream(
                GAME_FONT_PATH
        ), GAME_FONT_SIZE));
        livesText.setX(WINDOW_WIDTH - levelText.getLayoutBounds()
                .getWidth() - GAME_TEXT_SPACING
                - livesText.getLayoutBounds().getWidth()
                - GAME_TEXT_SPACING);
        livesText.setY(GAME_TEXT_SPACING);
        aliens = new Alien[NUM_ALIENS];
        aliens[FIRST_ALIEN] = new Alien(AlienType.GREEN);
        double startX = WINDOW_WIDTH / 2 - ALIEN_COLUMNS
                * aliens[FIRST_ALIEN].getImageWidth() / 2
                - aliens[FIRST_ALIEN].getWidthSpacing();
        double startY = WINDOW_HEIGHT / SIXTEENTH_SIZE;
        for (int i = 0; i < NUM_GREEN_ALIENS; ++i) {
            aliens[i] = new Alien(AlienType.GREEN);
            aliens[i].setPosition(i
                    * aliens[i].getImageWidth()
            + startX, startY);
        }
        for (int i = 0; i < NUM_BLUE_ALIENS; ++i) {
            aliens[i + NUM_GREEN_ALIENS] = new Alien(AlienType.BLUE);
            aliens[i + NUM_GREEN_ALIENS].setPosition(i % ALIEN_COLUMNS
                    * aliens[i].getImageWidth()
                    + startX, startY
                    + aliens[FIRST_ALIEN].getImageHeight()
                    + i / ALIEN_COLUMNS
            * aliens[FIRST_ALIEN].getImageHeight());
        }
        for (int i = 0; i < NUM_PINK_ALIENS; ++i) {
            aliens[i + NUM_GREEN_ALIENS
                    + NUM_BLUE_ALIENS] = new Alien(AlienType.PINK);
            aliens[i + NUM_GREEN_ALIENS
                    + NUM_BLUE_ALIENS].setPosition(i % ALIEN_COLUMNS
                    * aliens[i].getImageWidth()
                    + startX, startY
                    + aliens[FIRST_ALIEN].getImageHeight()
                    + 2 * aliens[NUM_GREEN_ALIENS].getImageHeight()
                    + i / ALIEN_COLUMNS
                    * aliens[NUM_GREEN_ALIENS + NUM_BLUE_ALIENS]
                    .getImageHeight());
        }
        ship = new Ship();

        for (int i = 0; i < NUM_ALIENS; ++i) {
            root.getChildren().add(aliens[i].getImageView());
        }
        root.getChildren().add(ship.getImageView());
        root.getChildren().add(scoreText);
        root.getChildren().add(livesText);
        root.getChildren().add(levelText);
        scene.setOnKeyPressed(keyEvent -> levelKeyPressedListener(
                keyEvent, stage
        ));
        scene.setOnKeyReleased(keyEvent -> levelKeyReleasedListener(
                keyEvent, stage
        ));
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                handle_animation(root, scoreText, livesText, stage);
            }
        };
        timer.start();
        stage.setScene(scene);
        stage.show();
    }
    boolean APressed, DPressed, SpacePressed;
    void levelKeyPressedListener(KeyEvent event, Stage stage) {
        if (event.getCode() == KeyCode.A) {
            APressed = true; DPressed = false;
        } else if (event.getCode() == KeyCode.D) {
            DPressed = true; APressed = false;
        } else if (event.getCode() == KeyCode.SPACE) {
            SpacePressed = true;
        }
    }

    void levelKeyReleasedListener(KeyEvent event, Stage stage) {
        if (event.getCode() == KeyCode.A) {
            APressed = false;
        } else if (event.getCode() == KeyCode.D) {
            DPressed = false;
        } else if (event.getCode() == KeyCode.SPACE) {
            SpacePressed = false;
        }
    }

    void handle_animation(Group root, Text scoreText, Text livesText,
                          Stage stage) {
        ship.move(root, aliens, clip, scoreText);
        if (aliens[FIRST_ALIEN].getX()
                + aliens[FIRST_ALIEN].getWidthSpacing() <= 0
                || aliens[FIRST_ALIEN].getX()
                + aliens[FIRST_ALIEN].getWidthSpacing()
                + aliens[FIRST_ALIEN].getImageWidth() * ALIEN_COLUMNS
                >= WINDOW_WIDTH) {
            for (int i = 0; i < NUM_ALIENS; ++i) {
                if (/*aliens[i].isAlive()*/true) {
                    aliens[i].invert();
                    int a = aliens[i].move(ship.getX() + ship.getWidthSpacing(),
                            ship.getY() + ship.getHeightSpacing(),
                            ship.getX() + ship.getWidthSpacing()
                                    + ship.getImageWidth(),
                            ship.getY() + ship.getHeightSpacing()
                                    + ship.getImageHeight(), root, clip,
                            scoreText);
                    switch (a) {
                        case 1:
                            break;
                        case 2:
                            ship.shot();
                            --currentLives;
                            if (currentLives == 0) {
                                timer.stop();
                                gameOver(stage);
                            } else {
                                ship.shot();
                                livesText.setText(LIVES_STRING + Integer.toString(
                                        currentLives
                                ));
                            }
                            break;
                        case 3:
                            ship.playExplosion();
                            timer.stop();
                            gameOver(stage);
                            break;
                        case 4:
                            break;
                    }
                }
            }
            int alienShooting = rand.nextInt(Alien.numAliensAlive);
            for (int i = 0; i < NUM_ALIENS; ++i) {
                if (aliens[i].isAlive()) {
                    --alienShooting;
                }
                if (alienShooting == 0) {
                    aliens[i].prepareMissile(root);
                    break;
                }
            }
        } else {
            for (int i = 0; i < NUM_ALIENS; ++i) {
                if (/*aliens[i].isAlive()*/true) {
                    int a = aliens[i].move(ship.getX() + ship.getWidthSpacing(),
                            ship.getY() + ship.getHeightSpacing(),
                            ship.getX() + ship.getWidthSpacing()
                                    + ship.getImageWidth(),
                            ship.getY() + ship.getHeightSpacing()
                                    + ship.getImageHeight(), root, clip,
                            scoreText);
                    switch (a) {
                        case 1:
                            break;
                        case 2:
                            --currentLives;
                            if (currentLives == 0) {
                                timer.stop();
                                gameOver(stage);
                            } else {
                                ship.shot();
                                livesText.setText(LIVES_STRING + Integer.toString(
                                        currentLives
                                ));
                            }
                            break;
                        case 3:
                            ship.playExplosion();
                            timer.stop();
                            gameOver(stage);
                            break;
                        case 4:
                            break;
                    }
                }
            }
        }
        for (int i = 0; i < NUM_ALIENS; ++i) {
            if (aliens[i].isAlive()) {
                int alienShootingMoving =
                        rand.nextInt((int)(ALIEN_START_SPEED
                                / Alien.speed * 1000 * 60));
                if (alienShootingMoving == 0) {
                    aliens[i].prepareMissile(root);
                }
            }
        }
        if (APressed) {
            ship.moveLeft();
        } else if (DPressed) {
            ship.moveRight();
        }
        if (SpacePressed && shootTimer == 30) {
            ship.prepareMissile(root);
            shootTimer = 0;
        }
        if (shootTimer < 30) {
            shootTimer += 1;
        }
        int musicSpeed = (int)(MUSIC_SPEED / Alien.speed);
        if (musicSpeed < 20) {
            musicSpeed = 20;
        }
        /*double newRate = 3.0 * Alien.speed;
        clip1.setRate(newRate);
        clip2.setRate(newRate);
        clip3.setRate(newRate);
        clip4.setRate(newRate);*/
        if (musicTimer >= musicSpeed * 4) {
            clip4.play();
            musicTimer = 0;
        } else if (musicTimer == musicSpeed * 3) {
            clip3.play();
        } else if (musicTimer == musicSpeed * 2) {
            clip2.play();
        } else if (musicTimer == musicSpeed) {
            clip1.play();
        }
        ++musicTimer;
        if (Alien.numAliensAlive == 0) {
            timer.stop();
            Alien.numAliensAlive = NUM_ALIENS;
            if (currentLevel < 3) {
                ++currentLevel;
                level(stage);
            } else {
                wonGame(stage);
            }
        }
    }
    final String WON_STRING = "Congrats! You won the game!";
    final String PROMPT_STRING = "Press ENTER to restart the game"
            + " or Q to quit";
    final int WON_SIZE = 18;
    public void wonGame(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH,
                WINDOW_HEIGHT, Color.BLACK);
        Text wonText = new Text(WON_STRING);
        wonText.setFill(Color.WHITE);
        wonText.setFont(Font.loadFont(getClass()
                .getResourceAsStream(
                        GAME_FONT_PATH
                ), WON_SIZE));
        wonText.setX(WINDOW_WIDTH / HALF_SIZE
                - wonText.getLayoutBounds()
                .getWidth() / HALF_SIZE);
        wonText.setY(WINDOW_HEIGHT / HALF_SIZE
                - wonText.getLayoutBounds()
                .getHeight() / HALF_SIZE);
        Text scoreText = new Text(SCORE_STRING + Integer.toString(
                currentScore
        ));
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.loadFont(getClass()
                .getResourceAsStream(
                        GAME_FONT_PATH
                ), 14));
        scoreText.setX(WINDOW_WIDTH / HALF_SIZE
                - scoreText.getLayoutBounds()
                .getWidth() / HALF_SIZE);
        scoreText.setY(WINDOW_HEIGHT / HALF_SIZE
                - scoreText.getLayoutBounds()
                .getHeight() / HALF_SIZE + scoreText.getLayoutBounds()
        .getHeight() * 2);
        Text promptText = new Text(PROMPT_STRING);
        promptText.setFill(Color.WHITE);
        promptText.setFont(Font.loadFont(getClass()
                .getResourceAsStream(
                        GAME_FONT_PATH
                ), 14));
        promptText.setX(WINDOW_WIDTH / HALF_SIZE
                - promptText.getLayoutBounds()
                .getWidth() / HALF_SIZE);
        promptText.setY(WINDOW_HEIGHT / HALF_SIZE
                - promptText.getLayoutBounds()
                .getHeight() / HALF_SIZE + promptText.getLayoutBounds()
                .getHeight() * 3);
        root.getChildren().add(wonText);
        root.getChildren().add(scoreText);
        root.getChildren().add(promptText);
        scene.setOnKeyPressed(keyEvent -> wonKeyListener(
                keyEvent, stage
        ));
        stage.setScene(scene);
        stage.show();
    }

    void wonKeyListener(KeyEvent keyEvent, Stage stage) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            currentLevel = 1;
            currentScore = 0;
            currentLives = START_LIVES;
            DPressed = false;
            SpacePressed = false;
            APressed = false;
            level(stage);
        } else if (keyEvent.getCode() == KeyCode.Q) {
            exitProgram();
        }
    }

    final String GAME_OVER_STRING = "GAME OVER";
    final int GAME_OVER_SIZE = 36;
    public void gameOver(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH,
                WINDOW_HEIGHT, Color.BLACK);
        Text gameOverText = new Text(GAME_OVER_STRING);
        gameOverText.setFill(Color.WHITE);
        gameOverText.setFont(Font.loadFont(getClass()
                .getResourceAsStream(
                        GAME_FONT_PATH
                ), GAME_OVER_SIZE));
        gameOverText.setX(WINDOW_WIDTH / HALF_SIZE
                - gameOverText.getLayoutBounds()
                .getWidth() / HALF_SIZE);
        gameOverText.setY(WINDOW_HEIGHT / HALF_SIZE
                - gameOverText.getLayoutBounds()
                .getHeight() / HALF_SIZE);
        Text scoreText = new Text(SCORE_STRING + Integer.toString(
                currentScore
        ));
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.loadFont(getClass()
                .getResourceAsStream(
                        GAME_FONT_PATH
                ), 14));
        scoreText.setX(WINDOW_WIDTH / HALF_SIZE
                - scoreText.getLayoutBounds()
                .getWidth() / HALF_SIZE);
        scoreText.setY(WINDOW_HEIGHT / HALF_SIZE
                - scoreText.getLayoutBounds()
                .getHeight() / HALF_SIZE + scoreText.getLayoutBounds()
                .getHeight() * 1);
        Text promptText = new Text(PROMPT_STRING);
        promptText.setFill(Color.WHITE);
        promptText.setFont(Font.loadFont(getClass()
                .getResourceAsStream(
                        GAME_FONT_PATH
                ), 14));
        promptText.setX(WINDOW_WIDTH / HALF_SIZE
                - promptText.getLayoutBounds()
                .getWidth() / HALF_SIZE);
        promptText.setY(WINDOW_HEIGHT / HALF_SIZE
                - promptText.getLayoutBounds()
                .getHeight() / HALF_SIZE + promptText.getLayoutBounds()
                .getHeight() * 3);
        root.getChildren().add(gameOverText);
        root.getChildren().add(scoreText);
        root.getChildren().add(promptText);
        scene.setOnKeyPressed(keyEvent -> wonKeyListener(
                keyEvent, stage
        ));
        stage.setScene(scene);
        stage.show();
    }
}
