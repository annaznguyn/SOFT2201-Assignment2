package invaders.entities;

import invaders.GameObject;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Bunker implements Renderable, GameObject {

    private double width;
    private double height;
    private Vector2D position;
    private Image image;
    private int timesHit;
    private BunkerState state;
    protected boolean disappear;
    protected Collider boxCollider;

    public Bunker(BunkerBuilder bb) {
        this.width = bb.width;
        this.height = bb.height;
        this.position = bb.position;
        this.image = bb.image;
        this.timesHit = bb.timesHit;
        this.state = bb.state;
        this.disappear = bb.disappear;
        this.boxCollider = bb.boxCollider;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public int getTimesHit() {
        return timesHit;
    }

    public void increaseTimesHit() {
        timesHit++;
    }

    public Collider getBoxCollider() {
        return boxCollider;
    }

    public boolean hasDisappeared() {
        return disappear;
    }

    public void setDisappear(boolean disappear) {
        this.disappear = disappear;
    }

    @Override
    public void start() {}

    @Override
    public void update() {}

    public void setState(BunkerState state) {
        this.state = state;
        image = state.getImage();
    }

    public void updateState() {
        state.changeState(this);
    }
}
