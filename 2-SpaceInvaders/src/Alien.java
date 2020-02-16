import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Alien {
    static final double ALIEN_SCALE = 0.3, MISSILE_SPEED = 5;
    SpaceInvaders.AlienType alienType;
    Image image, missileImage;
    ImageView imageView;
    ArrayList<ImageView> missiles;
    double x, y;
    boolean alive;
    static int numAliensAlive = SpaceInvaders.NUM_ALIENS;
    public Alien(SpaceInvaders.AlienType alienType) {
        alive = true;
        this.alienType = alienType;
        if (alienType == SpaceInvaders.AlienType.GREEN) {
            image = new Image("images/greent.png");
            missileImage = new Image("images/green_missile.png");
        } else if (alienType == SpaceInvaders.AlienType.BLUE) {
            image = new Image("images/bluet.png");
            missileImage = new Image("images/blue_missile.png");
        } else if (alienType == SpaceInvaders.AlienType.PINK) {
            image = new Image("images/pinkt.png");
            missileImage = new Image("images/pink_missile.png");
        }
        imageView = new ImageView(image);
        imageView.setScaleX(ALIEN_SCALE);
        imageView.setScaleY(ALIEN_SCALE);
        movement = 1.0;
        missiles = new ArrayList<ImageView>();
    }

    public void setPosition(double x, double y) {
        this.x = x;
        imageView.setX(x);
        this.y = y;
        imageView.setY(y);
    }

    public double getX() {
        return imageView.getX();
    }

    public double getY() {
        return imageView.getY();
    }

    public ImageView getImageView() { return imageView; }

    public double getWidthSpacing() { return (image.getWidth()
            - getImageWidth()) / 2; }

    public double getHeightSpacing() { return (image.getHeight()
            - getImageHeight()) / 2; }

    public double getImageWidth() {
        return image.getWidth() * ALIEN_SCALE;
    }

    public double getImageHeight() {
        return image.getHeight() * ALIEN_SCALE;
    }

    public double getMissileImageWidth() {
        return missileImage.getWidth() * ALIEN_SCALE;
    }

    public double getMissileImageHeight() {
        return missileImage.getHeight() * ALIEN_SCALE;
    }

    public double getMissileWidthSpacing() { return (missileImage.getWidth()
            - getMissileImageWidth()) / 2; }

    public double getMissileHeightSpacing() { return (missileImage.getHeight()
            - getMissileImageHeight()) / 2; }

    double movement;
    static double speed;

    public void invert() {
        movement *= -1;
        if (y + getImageHeight() < SpaceInvaders.WINDOW_HEIGHT) {
            y += getImageHeight();
            imageView.setY(y);
        }
    }

    public int move(double startX, double startY,
                        double endX, double endY,
                    Group root, AudioClip clip, Text scoreText) {
        x += movement * speed;
        imageView.setX(x);
        for (ImageView missileImageView : missiles) {
            missileImageView.setY(missileImageView.getY() + MISSILE_SPEED);
            if (missileImageView.getY() > SpaceInvaders.WINDOW_HEIGHT) {
                missiles.remove(missileImageView);
                root.getChildren().remove(missileImageView);
                return 1; // missile traversed the window
            }
            if (missileImageView.getX() + getMissileWidthSpacing()
                    + getMissileImageWidth() >= startX
                    && missileImageView.getX() + getMissileWidthSpacing()
                    <= endX && missileImageView.getY()
                    + getMissileHeightSpacing()
                    + getMissileImageHeight() >= startY) {
                missiles.remove(missileImageView);
                root.getChildren().remove(missileImageView);
                return 2; // missile collided with ship
            }
        }
        if (isAlive() && y + getHeightSpacing()
        + getImageHeight() >= startY) {
            return 3; // alien collided with ship
        }
        return 4; // nothing happened
    }

    public void prepareMissile(Group root) {
        ImageView missileImageView = new ImageView(missileImage);
        missileImageView.setScaleX(ALIEN_SCALE);
        missileImageView.setScaleY(ALIEN_SCALE);
        missileImageView.setX(x + getWidthSpacing() + getImageWidth() / 2
                                - getMissileImageWidth() / 2);
        missileImageView.setY(y + getHeightSpacing() + getImageHeight());
        missiles.add(missileImageView);
        root.getChildren().add(missileImageView);
    }

    public void kill(Group root, AudioClip clip, Text scoreText) {
        clip.play();
        alive = false;
        imageView.setVisible(false);
        root.getChildren().remove(this);
        --numAliensAlive;
        int factor = 0;
        switch (alienType) {
            case GREEN:
                factor = 3;
                break;
            case BLUE:
                factor = 2;
                break;
            case PINK:
                factor = 1;
                break;
        }
        SpaceInvaders.currentScore += 10 * factor;
        scoreText.setText(SpaceInvaders.SCORE_STRING + Integer.toString(
                SpaceInvaders.currentScore
        ));
        speed += 0.02 * 1;//SpaceInvaders.currentLevel;
    }

    public boolean isAlive() {
        return alive;
    }
}
