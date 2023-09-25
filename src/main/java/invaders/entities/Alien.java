package invaders.entities;

import invaders.engine.GameEngine;
import invaders.physics.BoxCollider;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Alien implements Renderable {

    private double width;
    private double height;
    private Vector2D position;
    private double speed;
    private String alienType;
    private final Image image;
    private Collider boxCollider;

    public Alien(AlienBuilder ab) {
        this.position = ab.position;
        this.speed = ab.speed;
        this.alienType = ab.alienType;
        this.image = ab.image;
        this.width = ab.width;
        this.height = ab.height;
        this.boxCollider = ab.boxCollider;
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

    public double getSpeed() {
        return speed;
    }

    public void increaseSpeed() {
        speed += 0.2;
    }

    public Projectile fireProjectile(GameEngine gameEngine) {
        ProjectileStrategy projectileStrategy = null;
        if (alienType.equals("slow_straight")) {
            projectileStrategy = new SlowProjectileStrategy();
        }
        else if (alienType.equals("fast_straight")) {
            projectileStrategy = new FastProjectileStrategy();
        }
        ProjectileCreator creator = new AlienProjectileCreator();
        Vector2D projectilePosition = new Vector2D(position.getX() + width/2, position.getY());
        Projectile alienProjectile = creator.create(projectilePosition, new BoxCollider(2, 10, projectilePosition), 2, 10);
        alienProjectile.setProjectileStrategy(projectileStrategy);
        gameEngine.getRenderables().add((Renderable) alienProjectile);
        return alienProjectile;
    }

    public Collider getBoxCollider() {
        return boxCollider;
    }

    public void goLeft() {
        position.setX(position.getX() - speed);
    }

    public void goRight() {
        position.setX(position.getX() + speed);
    }

    public void goDown() {
        position.setY(position.getY() + 10);
    }
}
