package invaders.entities;

import invaders.physics.Vector2D;

public interface ProjectileCreator {

    public Projectile create(Vector2D position, double width, double height);
}
