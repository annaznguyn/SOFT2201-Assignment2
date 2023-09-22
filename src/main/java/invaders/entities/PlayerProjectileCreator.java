package invaders.entities;

import invaders.physics.Collider;
import invaders.physics.Vector2D;

public class PlayerProjectileCreator implements ProjectileCreator {

    @Override
    public Projectile create(Vector2D position, Collider boxCollider, double width, double height) {
        return new PlayerProjectile(position, boxCollider, width, height);
    }
}
