package invaders.entities;

import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;

public class AlienProjectile implements Renderable {

    private final Image image;

    private ProjectileStrategy projectileStrategy;
    private Vector2D position;
    private double width;
    private double height;

    public AlienProjectile(ProjectileStrategy projectileStrategy, Vector2D position, double width, double height) {
        this.projectileStrategy = projectileStrategy;
        this.position = position;
        this.width = width;
        this.height = height;
        this.image = new Image(new File("src/main/resources/projectile.png").toURI().toString(), width, height, false, true);
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

    public void applyStrategy() {
        projectileStrategy.designBehaviour(position);
    }
}
