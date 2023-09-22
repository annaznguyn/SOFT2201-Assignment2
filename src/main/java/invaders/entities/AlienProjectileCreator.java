package invaders.entities;

import invaders.physics.Collider;
import invaders.physics.Vector2D;

public class AlienProjectileCreator implements ProjectileCreator {

    @Override
    public Projectile create(Vector2D position, Collider boxCollider, double width, double height) {
        return new AlienProjectile(position, boxCollider, width, height);
    }
}
