package invaders.entities;

import invaders.physics.Vector2D;

public class AlienProjectileCreator implements ProjectileCreator {

    @Override
    public Projectile create(Vector2D position, double width, double height) {
        return new AlienProjectile(position, width, height);
    }
}
