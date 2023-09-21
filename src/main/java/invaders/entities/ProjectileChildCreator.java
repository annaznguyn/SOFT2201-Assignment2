package invaders.entities;

import invaders.physics.Vector2D;

public class ProjectileChildCreator implements ProjectileParentCreator {

    @Override
    public AlienProjectile create(ProjectileStrategy projectileStrategy, Vector2D position, double width, double height) {
        return new AlienProjectile(projectileStrategy, position, width, height);
    }
}
