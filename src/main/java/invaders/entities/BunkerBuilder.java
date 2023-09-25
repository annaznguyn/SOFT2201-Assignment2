package invaders.entities;

import invaders.physics.Vector2D;
import invaders.physics.Collider;
import javafx.scene.image.Image;

import java.io.File;

public abstract class BunkerBuilder {

    protected final double width;
    protected final double height;

    protected final Image image;
    protected Vector2D position;
    protected boolean disappear;
    protected BunkerState state;
    protected Collider boxCollider;

    public BunkerBuilder(Vector2D position, double width, double height) {
        this.width = width;
        this.height = height;
        this.position = position;
        this.image = new Image(new File("src/main/resources/green-bunker.png").toURI().toString(), width, height, false, true);
    }


    public void setDisappear(boolean disappear) {
        this.disappear = disappear;
    }

    public void setState(BunkerState state) {
        this.state = state;
    }

    public void setBoxCollider(Collider col) {
        this.boxCollider = col;
    }

    public abstract Bunker getBunker();
}
