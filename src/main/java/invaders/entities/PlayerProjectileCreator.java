package invaders.entities;

import invaders.physics.Vector2D;

public class PlayerProjectileCreator implements ProjectileCreator {

    @Override
    public Projectile create(Vector2D position, double width, double height) {
        return new PlayerProjectile(position, width, height);
    }
}
