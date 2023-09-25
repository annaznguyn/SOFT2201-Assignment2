package invaders.entities;

import javafx.scene.image.Image;

import java.io.File;

public class RedBunker implements BunkerState {

    private final double width;
    private final double height;
    private Image image;

    public RedBunker(double width, double height) {
        this.width = width;
        this.height = height;
        this.image = new Image(new File("src/main/resources/red-bunker.png").toURI().toString(), width, height, false, true);
    }

    @Override
    public Image getImage() {
        return image;
    }

    // change color to Yellow
    @Override
    public void changeState(Bunker bunker) {
        bunker.setDisappear(true);
    }
}
