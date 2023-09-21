package invaders.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import invaders.GameObject;
import invaders.entities.*;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {

	private String configPath = "src/main/resources/config.json";

	private final int gameX;
	private final int gameY;

	private int playerX;
	private int playerY;

	private List<GameObject> gameobjects;
	private List<Renderable> renderables;
	private Player player;

	private boolean left;
	private boolean right;

	private ArrayList<Alien> aliens;
	private ArrayList<Bunker> bunkers;
	private ArrayList<AlienProjectile> alienProjectiles;

	private boolean flyLeft;
	private int interval;

	public GameEngine(String config, int gameX, int gameY){
		this.gameX = gameX;
		this.gameY = gameY;

		this.gameobjects = new ArrayList<GameObject>();
		this.renderables = new ArrayList<Renderable>();
		this.aliens = new ArrayList<Alien>();
		this.bunkers = new ArrayList<Bunker>();
		this.alienProjectiles = new ArrayList<AlienProjectile>();
		// read the config here
		JSONParser parser = new JSONParser();
		try {
			// create a JSONObject of the json file
			JSONObject jo = (JSONObject) parser.parse(new FileReader(configPath));
			// read player
			JSONObject playerObj = (JSONObject) jo.get("Player");
			this.playerX = (int) ((long) ((JSONObject) playerObj.get("position")).get("x"));
			this.playerY = (int) ((long) ((JSONObject) playerObj.get("position")).get("y"));
			// read enemies
			JSONArray jsonAliens = (JSONArray) jo.get("Enemies");

			for (Object obj : jsonAliens) {
				// cast Object type to JSONObject type
				JSONObject jsonAlien = (JSONObject) obj;
				int positionX = (int) ((long) ((JSONObject) jsonAlien.get("position")).get("x"));
				int positionY = (int) ((long) ((JSONObject) jsonAlien.get("position")).get("y"));
				String projectileType = (String) jsonAlien.get("projectile");
				// build aliens
				AlienBuilder alienBuilder = new ConcreteAlienBuilder(new Vector2D(positionX, positionY));
				alienBuilder.setIsHit(false);
				alienBuilder.setSpeed(1);
				alienBuilder.setAlienType(projectileType);
				Alien newAlien = alienBuilder.getAlien();
				aliens.add(newAlien);
				renderables.add(newAlien);
//				gameobjects.add(newAlien);
			}

			// read bunkers
			JSONArray jsonBunkers = (JSONArray) jo.get("Bunkers");
			for (Object obj : jsonBunkers) {
				JSONObject jsonBunker = (JSONObject) obj;
				int positionX = (int) ((long) ((JSONObject) jsonBunker.get("position")).get("x"));
				int positionY = (int) ((long) ((JSONObject) jsonBunker.get("position")).get("y"));
				int width = (int) ((long) ((JSONObject) jsonBunker.get("size")).get("x"));
				int height = (int) ((long) ((JSONObject) jsonBunker.get("size")).get("y"));
				// build bunkers
				BunkerBuilder bunkerBuilder = new ConcreteBunkerBuilder(new Vector2D(positionX, positionY), width, height);
				bunkerBuilder.setTimesHit(0);
				bunkerBuilder.setDisappear(false);
				// initially the state is green
				bunkerBuilder.setState(new GreenBunker(width, height));
				Bunker newBunker = bunkerBuilder.getBunker();
				bunkers.add(newBunker);
				renderables.add(newBunker);
				gameobjects.add(newBunker);
			}
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
		player = new Player(new Vector2D(this.playerX, this.playerY));
		renderables.add(player);
		// initially, the aliens fly to the left side
		this.flyLeft = true;
		// random interval length
		interval = 0;
	}

	/**
	 * Updates the game/simulation
	 */
	public void update(){
		this.movePlayer();
		this.moveAlien();
		this.removeBunker();
		this.moveProjectile();
		for (GameObject go: gameobjects){
			go.update();
		}

		// ensure that renderable foreground objects don't go off-screen
		for (Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			// not going out of the right side
			if(ro.getPosition().getX() + ro.getWidth() >= this.gameX) {
				ro.getPosition().setX(this.gameX - 1 - ro.getWidth());
			}
			// not going out of the left side
			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}
			// not going out of the floor
			if(ro.getPosition().getY() + ro.getHeight() >= this.gameY) {
				ro.getPosition().setY(this.gameY - 1 - ro.getHeight());
			}
			// not going out of the ceiling
			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
		}
	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		player.shoot();
		return true;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	private void moveAlien() {
		// move horizontally left first until the alien with the x position closest to the left wall reaches the left wall
		// then switch the boolean
		if (flyLeft && aliens.size() > 0) {
			// get the closest alien to the left wall
			Alien alienClosestToLeftWall = aliens.get(0);
			for (Alien alien : aliens) {
				if (((Renderable) alien).getPosition().getX() < ((Renderable) alienClosestToLeftWall).getPosition().getX()) {
					alienClosestToLeftWall = alien;
				}
			}
			// if the x position of the alienClosestToLeftWall after moving to the left once more is out of the left side,
			// all aliens descend and flyLeft is set to false to start going right
			if (((Renderable) alienClosestToLeftWall).getPosition().getX() - alienClosestToLeftWall.getSpeed() <= 0) {
				for (Alien alien : aliens) {
					alien.goDown();
				}
				flyLeft = false;
			}
			// else, all aliens keep moving left
			else {
				for (Alien alien : aliens) {
					alien.goLeft();
				}
			}
		}
		else if (!flyLeft && aliens.size() > 0) {
			// get the closest alien to the right wall
			Alien alienClosestToRightWall = aliens.get(0);
			for (Alien alien : aliens) {
				if (((Renderable) alien).getPosition().getX() > ((Renderable) alienClosestToRightWall).getPosition().getX()) {
					alienClosestToRightWall = alien;
				}
			}
			if (((Renderable) alienClosestToRightWall).getPosition().getX() + alienClosestToRightWall.getSpeed()
				+ ((Renderable) alienClosestToRightWall).getWidth()	>= this.gameX) {
				for (Alien alien : aliens) {
					alien.goDown();
				}
				flyLeft = true;
			}
			// else, all aliens keep moving right
			else {
				for (Alien alien : aliens) {
					alien.goRight();
				}
			}
			this.alienShoot();
//			this.moveProjectile();
		}

		// increase speed if an alien is hit
	}

	public void alienShoot() {
		// after the waiting interval ends
		if (interval <= 0) {
			Random r = new Random();
			// get a random number from 1-3 inclusive, random number of projectile
			int randNumOfProj = r.nextInt(3) + 1;
			// pick a random number of random aliens
			for (int i = 0; i < randNumOfProj; i++) {
				if (aliens.size() >= randNumOfProj) {
					// choose a random index in the random range
					int randAlien = r.nextInt(aliens.size());
					alienProjectiles.add(aliens.get(randAlien).fireProjectile(this));
				}
			}
			interval = (r.nextInt(2) + 3) * 60;
		}
		else {
			interval--;
		}
	}

	public void moveProjectile() {
		if (alienProjectiles.size() > 0) {
//			System.out.println("Height: " + alienProjectiles.get(0).getHeight() + "\nY: " + alienProjectiles.get(0).getPosition().getY() + "\nGame Y: " + gameY);
		}
		ArrayList<AlienProjectile> toBeRemovedProj = new ArrayList<AlienProjectile>();
		for (AlienProjectile p : alienProjectiles) {
			if (p.getPosition().getY() + p.getHeight() + 1 >= this.gameY) {
				toBeRemovedProj.add(p);
			}
			else {
				p.applyStrategy();
			}
		}
		for (AlienProjectile p : toBeRemovedProj) {
			alienProjectiles.remove(p);
			renderables.remove(p);
		}
	}

	public void removeBunker() {
		ArrayList<Bunker> toBeRemovedBunkers = new ArrayList<Bunker>();
		for (Bunker bunker : bunkers) {
			if (bunker.hasDisappeared()) {
				toBeRemovedBunkers.add(bunker);
			}
		}
		for (Bunker bunker : toBeRemovedBunkers) {
			bunkers.remove(bunker);
		}
	}
}
