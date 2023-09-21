package invaders.entities;

import invaders.GameObject;
import invaders.engine.GameEngine;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

public class Alien implements Renderable, GameObject {

    private double width;
    private double height;
    private Vector2D position;
    private int speed;
    private boolean isHit;
    private String alienType;
    private final Image image;

    public Alien(AlienBuilder ab) {
        this.position = ab.position;
        this.speed = ab.speed;
        this.isHit = ab.isHit;
        this.alienType = ab.alienType;
        this.image = ab.image;
        this.width = ab.width;
        this.height = ab.height;
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

    public int getSpeed() {
        return speed;
    }

    public boolean getIsHit() {
        return isHit;
    }

    public void increaseSpeed() {
        speed++;
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
        Projectile alienProjectile = creator.create(new Vector2D(position.getX(), position.getY()), 2, 15);
        alienProjectile.setProjectileStrategy(projectileStrategy);
        gameEngine.getRenderables().add((Renderable) alienProjectile);
        return alienProjectile;
    }

    @Override
    public void start() {}

    @Override
    public void update() {

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
