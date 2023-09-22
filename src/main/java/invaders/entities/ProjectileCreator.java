package invaders.entities;

import invaders.physics.Collider;
import invaders.physics.Vector2D;

public interface ProjectileCreator {

    public Projectile create(Vector2D position, Collider boxCollider, double width, double height);
}
