package invaders.entities;

import invaders.physics.Vector2D;

public class ConcreteAlienBuilder extends AlienBuilder {

    public ConcreteAlienBuilder(Vector2D position) {
        super(position);
    }

    @Override
    public Alien getAlien() {
        return new Alien(this);
    }
}
