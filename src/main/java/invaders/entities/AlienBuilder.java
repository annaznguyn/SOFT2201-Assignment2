package invaders.entities;

import invaders.physics.Collider;
import invaders.physics.Vector2D;
import javafx.scene.image.Image;

import java.io.File;

public abstract class AlienBuilder {

    protected final double width = 25;
    protected final double height = 30;

    protected final Image image;
    protected Vector2D position;
    protected double speed;
    protected String alienType;
    protected Collider boxCollider;

    public AlienBuilder(Vector2D position) {
        this.position = position;
        this.image = new Image(new File("src/main/resources/enemy.png").toURI().toString(), width, height, false, true);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAlienType(String alienType) {
        this.alienType = alienType;
    }

    public void setBoxCollider(Collider col) {
        this.boxCollider = col;
    }

    public abstract Alien getAlien();
}
