package invaders;

import javafx.application.Application;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

public class App extends Application {

    private String configPath;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.configPath = "src/main/resources/config.json";
        Map<String, String> params = getParameters().getNamed();

        // parse json file to get width and height
        JSONParser parser = new JSONParser();
        try {
            // create a JSONObject of the json file
            JSONObject jo = (JSONObject) parser.parse(new FileReader(configPath));

            // create a JSONObject of "Game"
            JSONObject gameObj = (JSONObject) jo.get("Game");
            long gameX = (Long) ((JSONObject) gameObj.get("size")).get("x");
            long gameY = (Long) ((JSONObject) gameObj.get("size")).get("y");

            GameEngine model = new GameEngine(configPath, (int) gameX, (int) gameY);
            GameWindow window = new GameWindow(model, (int) gameX, (int) gameY);
            window.run();

            primaryStage.setTitle("Space Invaders");
            primaryStage.setScene(window.getScene());
            primaryStage.show();

            window.run();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
