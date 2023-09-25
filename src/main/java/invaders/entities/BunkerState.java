package invaders.entities;

import javafx.scene.image.Image;

public interface BunkerState {

    public Image getImage();

    public void changeState(Bunker bunker);
}