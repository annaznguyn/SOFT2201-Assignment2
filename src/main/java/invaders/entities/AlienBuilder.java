package invaders.entities;

import invaders.physics.Vector2D;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

import java.io.File;

public abstract class AlienBuilder {

    protected final double width = 25;
    protected final double height = 30;

    protected final Image image;
    protected Vector2D position;
    protected int speed;
    protected boolean isHit;
    protected String alienType;

    public AlienBuilder(Vector2D position) {
        this.position = position;
        this.image = new Image(new File("src/main/resources/enemy.png").toURI().toString(), width, height, false, true);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    public void setAlienType(String alienType) {
        this.alienType = alienType;
    }

    public abstract Alien getAlien();
}
