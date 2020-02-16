import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class Ship {
    double x, y;
    Image image, missileImage;
    ImageView imageView;
    ArrayList<ImageView> missiles;
    AudioClip clip, explosionClip;
    public Ship() {
        image = new Image("images/player.png");
        missileImage = new Image("images/player_missile.png");
        imageView = new ImageView(image);
        missiles = new ArrayList<ImageView>();
        clip = new AudioClip(getClass().getClassLoader().getResource(
                "sounds/shoot.wav"
        ).toString());
        explosionClip = new AudioClip(getClass().getClassLoader().getResource(
                "sounds/explosion.wav"
        ).toString());
        x = SpaceInvaders.WINDOW_WIDTH / 2 - image.getWidth() / 2;
        y = SpaceInvaders.WINDOW_HEIGHT - image.getHeight();
        imageView.setScaleX(Alien.ALIEN_SCALE);
        imageView.setScaleY(Alien.ALIEN_SCALE);
        imageView.setX(x);
        imageView.setY(y);
    }

    public void shot() {
        explosionClip.play();
        x = SpaceInvaders.WINDOW_WIDTH / 2 - image.getWidth() / 2;
        y = SpaceInvaders.WINDOW_HEIGHT - image.getHeight();
        imageView.setX(x);
        imageView.setY(y);
    }

    public double getMissileImageWidth() {
        return missileImage.getWidth() * Alien.ALIEN_SCALE;
    }

    public double getMissileImageHeight() {
        return missileImage.getHeight() * Alien.ALIEN_SCALE;
    }

    public double getMissileWidthSpacing() { return (missileImage.getWidth()
            - getMissileImageWidth()) / 2; }

    public double getMissileHeightSpacing() { return (missileImage.getHeight()
            - getMissileImageHeight()) / 2; }

    public double getX() {
        return imageView.getX();
    }

    public double getY() {
        return imageView.getY();
    }

    public double getWidthSpacing() { return (image.getWidth()
            - getImageWidth()) / 2; }

    public double getHeightSpacing() { return (image.getHeight()
            - getImageHeight()) / 2; }

    public double getImageWidth() {
        return image.getWidth()
                * Alien.ALIEN_SCALE;
    }

    public double getImageHeight() {
        return image.getHeight() * Alien.ALIEN_SCALE;
    }

    public ImageView getImageView() {
        return imageView;
    }
    static final int SPEED = 3;
    public void moveRight() {
        if (x + SPEED <= SpaceInvaders.WINDOW_WIDTH - getImageWidth()
                            - getWidthSpacing()) {
            x += SPEED;
            imageView.setX(x);
        }
    }

    public void moveLeft() {
        if (x - SPEED >= -getWidthSpacing()) {
            x -= SPEED;
            imageView.setX(x);
        }
    }

    public void playExplosion() {
        explosionClip.play();
    }

    public int move(Group root, Alien[] aliens, AudioClip clip, Text scoreText) {
        for (int j = 0; j < missiles.size(); ++j) {
            ImageView missileImageView = missiles.get(j);
            missileImageView.setY(missileImageView.getY() - Alien.MISSILE_SPEED);
            if (missileImageView.getY() + getMissileHeightSpacing()
                    + getMissileImageHeight() < 0) {
                missiles.remove(missileImageView);
                root.getChildren().remove(missileImageView);
                return 1; // missile traversed the window
            }
            for (int i = 0; i < SpaceInvaders.NUM_ALIENS; ++i) {
                if (aliens[i].isAlive()) {
                    double startX = aliens[i].getX() + aliens[i].getWidthSpacing(),
                            startY = aliens[i].getY() + aliens[i].getHeightSpacing(),
                            endX = aliens[i].getX() + aliens[i].getWidthSpacing()
                                    + aliens[i].getImageWidth(),
                            endY = aliens[i].getY() + aliens[i].getHeightSpacing()
                                    + aliens[i].getImageHeight();
                    if (missileImageView.getX() + getMissileWidthSpacing()
                            + getMissileImageWidth() >= startX
                            && missileImageView.getX() + getMissileWidthSpacing()
                            <= endX && missileImageView.getY()
                            + getMissileHeightSpacing() <= endY) {
                        missiles.remove(missileImageView);
                        root.getChildren().remove(missileImageView);
                        aliens[i].kill(root, clip, scoreText);
                        return 2; // missile collided with alien
                    }
                }
            }
        }
        return 4;
    }

    public void prepareMissile(Group root) {
        clip.play();
        ImageView missileImageView = new ImageView(missileImage);
        missileImageView.setScaleX(Alien.ALIEN_SCALE);
        missileImageView.setScaleY(Alien.ALIEN_SCALE);
        missileImageView.setX(x + getWidthSpacing() + getImageWidth() / 2
                - getMissileImageWidth() / 2 - getMissileWidthSpacing());
        missileImageView.setY(y + getHeightSpacing() - getMissileImageHeight());
        missiles.add(missileImageView);
        root.getChildren().add(missileImageView);
    }
}
