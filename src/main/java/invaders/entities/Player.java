package invaders.entities;

import invaders.engine.GameEngine;
import invaders.logic.Damagable;
import invaders.physics.BoxCollider;
import invaders.physics.Collider;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Animator;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;

import java.io.File;

public class Player implements Moveable, Damagable, Renderable {

    private Vector2D position;
    private double health;
    private final double width = 25;
    private final double height = 30;
    private final Image image;
    private Collider boxCollider;
    private String colour;

    public Player(Vector2D position, int lives, String colour){
        this.position = position;
        this.health = lives;
        this.colour = colour;
        this.boxCollider = new BoxCollider(width, height, this.position);

        // default colour of player is blue
        String path = "src/main/resources/player.png";
        if (colour.equals("green")) {
            path = "src/main/resources/green-player.png";
        }
        this.image = new Image(new File(path).toURI().toString(), width, height, true, true);
    }

    public String getColour() {
        return colour;
    }

    @Override
    public void takeDamage(double amount) {
        this.health -= amount;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public void up() {
        return;
    }

    @Override
    public void down() {
        return;
    }

    @Override
    public void left() {
        this.position.setX(this.position.getX() - 1);
    }

    @Override
    public void right() {
        this.position.setX(this.position.getX() + 1);
    }

    public Projectile shoot(GameEngine gameEngine) {
        ProjectileCreator creator = new PlayerProjectileCreator();
        Vector2D projectilePosition = new Vector2D(position.getX() + width/2, position.getY());
        Projectile playerProjectile = creator.create(projectilePosition, new BoxCollider(2, 10, projectilePosition), 2, 10);
        gameEngine.getRenderables().add((Renderable) playerProjectile);
        return playerProjectile;
    }

    public Collider getBoxCollider() {
        return boxCollider;
    }

    @Override
    public Image getImage() {
        return this.image;
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
}
