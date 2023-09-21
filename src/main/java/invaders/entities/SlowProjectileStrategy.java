package invaders.entities;

import invaders.physics.Vector2D;

public class SlowProjectileStrategy implements ProjectileStrategy {

    @Override
    public void designBehaviour(Vector2D position) {
        // go down
        position.setY(position.getY() + 1);
    }
}
