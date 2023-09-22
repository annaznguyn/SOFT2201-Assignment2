package invaders.entities;

import invaders.physics.Collider;

public interface Projectile {

    public void setProjectileStrategy(ProjectileStrategy projectileStrategy);

    public void applyStrategy();

    public void shoot();

    public Collider getBoxCollider();
}
