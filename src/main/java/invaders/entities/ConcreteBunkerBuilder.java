package invaders.entities;

import invaders.physics.Vector2D;

public class ConcreteBunkerBuilder extends BunkerBuilder {

    public ConcreteBunkerBuilder(Vector2D position, double width, double height) {
        super(position, width, height);
    }

    @Override
    public Bunker getBunker() {
        return new Bunker(this);
    }
}