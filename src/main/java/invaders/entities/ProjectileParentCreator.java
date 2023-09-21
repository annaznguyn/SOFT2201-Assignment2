package invaders.entities;

import invaders.physics.Vector2D;

public interface ProjectileParentCreator {

    public AlienProjectile create(ProjectileStrategy projectileStrategy, Vector2D position, double width, double height);
}
