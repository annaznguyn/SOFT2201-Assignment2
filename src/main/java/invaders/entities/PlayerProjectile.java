package invaders.entities;

import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;

public class PlayerProjectile implements Projectile, Renderable {

    private Vector2D position;
    private double width;
    private double height;
    private Image image;
    private Collider boxCollider;

    public PlayerProjectile(Vector2D position, Collider boxCollider, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.boxCollider = boxCollider;
        this.image = new Image(new File("src/main/resources/projectile.png").toURI().toString(), width, height, false, true);
    }

    @Override
    public Collider getBoxCollider() {
        return boxCollider;
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

    @Override
    public void setProjectileStrategy(ProjectileStrategy projectileStrategy) {}

    @Override
    public void applyStrategy() {}

    @Override
    public void shoot() {
        position.setY(position.getY() - 1);
    }
}
