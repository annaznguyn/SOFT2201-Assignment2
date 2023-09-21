package invaders.entities;

import invaders.rendering.Renderable;

public interface Projectile {

    public void setProjectileStrategy(ProjectileStrategy projectileStrategy);

    public void applyStrategy();
}
