package invaders.entities;

import invaders.physics.Vector2D;

public class FastProjectileStrategy implements ProjectileStrategy {

    @Override
    public void designBehaviour(Vector2D position) {
        // go down
        position.setY(position.getY() + 2);
    }
}