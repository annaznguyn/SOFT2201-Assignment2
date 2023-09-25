package invaders.entities;

import javafx.scene.image.Image;

import java.io.File;

public class YellowBunker implements BunkerState {

    private final double width;
    private final double height;
    private Image image;

    public YellowBunker(double width, double height) {
        this.width = width;
        this.height = height;
        this.image = new Image(new File("src/main/resources/yellow-bunker.jpg").toURI().toString(), width, height, false, true);
    }

    @Override
    public Image getImage() {
        return image;
    }

    // change color to Red
    @Override
    public void changeState(Bunker bunker) {
        bunker.setState(new RedBunker(width, height));
    }
}
