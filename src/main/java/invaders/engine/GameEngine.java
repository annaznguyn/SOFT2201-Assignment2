package invaders.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import invaders.entities.*;
import invaders.physics.BoxCollider;
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

	private final int gameX;
	private final int gameY;

	private List<Renderable> renderables;
	private Player player;

	private int playerX;
	private int playerY;

	private boolean left;
	private boolean right;

	private ArrayList<Alien> aliens;
	private ArrayList<Bunker> bunkers;
	private ArrayList<Projectile> alienProjectiles;
	private ArrayList<Projectile> playerProjectiles;

	private boolean flyLeft;
	private int interval;

	public GameEngine(String config, int gameX, int gameY){
		this.gameX = gameX;
		this.gameY = gameY;

		this.renderables = new ArrayList<Renderable>();
		this.aliens = new ArrayList<Alien>();
		this.bunkers = new ArrayList<Bunker>();
		this.alienProjectiles = new ArrayList<Projectile>();
		this.playerProjectiles = new ArrayList<Projectile>();
		// read the config here
		JSONParser parser = new JSONParser();
		try {
			// create a JSONObject of the json file
			JSONObject jo = (JSONObject) parser.parse(new FileReader(config));
			// read player
			JSONObject playerObj = (JSONObject) jo.get("Player");
			playerX = (int) ((long) ((JSONObject) playerObj.get("position")).get("x"));
			playerY = (int) ((long) ((JSONObject) playerObj.get("position")).get("y"));
			String colour = (String) playerObj.get("colour");
			int lives = (int) ((long) playerObj.get("lives"));
			player = new Player(new Vector2D(playerX, playerY), lives, colour);
			renderables.add(player);

			// read enemies
			JSONArray jsonAliens = (JSONArray) jo.get("Enemies");
			for (Object obj : jsonAliens) {
				// cast Object type to JSONObject type
				JSONObject jsonAlien = (JSONObject) obj;
				int positionX = (int) ((long) ((JSONObject) jsonAlien.get("position")).get("x"));
				int positionY = (int) ((long) ((JSONObject) jsonAlien.get("position")).get("y"));
				String projectileType = (String) jsonAlien.get("projectile");
				// build aliens
				Vector2D alienPosition = new Vector2D(positionX, positionY);
				AlienBuilder alienBuilder = new ConcreteAlienBuilder(alienPosition);
				alienBuilder.setSpeed(1);
				alienBuilder.setAlienType(projectileType);
				alienBuilder.setBoxCollider(new BoxCollider(25, 30, alienPosition));
				Alien newAlien = alienBuilder.getAlien();
				aliens.add(newAlien);
				renderables.add(newAlien);
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
				Vector2D bunkerPosition = new Vector2D(positionX, positionY);
				BunkerBuilder bunkerBuilder = new ConcreteBunkerBuilder(bunkerPosition, width, height);
				bunkerBuilder.setDisappear(false);
				// initially the state is green
				bunkerBuilder.setState(new GreenBunker(width, height));
				bunkerBuilder.setBoxCollider(new BoxCollider(width, height, bunkerPosition));
				Bunker newBunker = bunkerBuilder.getBunker();
				bunkers.add(newBunker);
				renderables.add(newBunker);
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
		// initially, the aliens fly to the left side
		flyLeft = true;
		// random interval length
		interval = 0;
	}

	/**
	 * Updates the game/simulation
	 */
	public void update() {
		move();
		collide();

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

	public boolean shootPressed() {
		if (playerProjectiles.size() < 1) {
			playerProjectiles.add(player.shoot(this));
		}
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

	private void move() {
		movePlayer();
		moveAlien();
		moveAlienProjectile();
		movePlayerProjectile();
	}

	private void collide() {
		checkBunkerAlive();
		bunkerColProj(alienProjectiles);
		bunkerColProj(playerProjectiles);
		projCollidesProj();
		alienCollidesBunker();
		alienCollidesPlayer();
		alienGotShot();
		playerGotShot();
	}

	private void endGame() {
		aliens.clear();
		bunkers.clear();
		alienProjectiles.clear();
		playerProjectiles.clear();
		renderables.clear();
	}

	private void checkBunkerAlive() {
		ArrayList<Bunker> toBeRemovedBunker = new ArrayList<Bunker>();
		for (Bunker b : bunkers) {
			if (b.hasDisappeared()) {
				toBeRemovedBunker.add(b);
			}
		}
		removeBunker(toBeRemovedBunker);
	}

	private void removeBunker(ArrayList<Bunker> toBeRemovedBunker) {
		for (Bunker b : toBeRemovedBunker) {
			bunkers.remove(b);
			renderables.remove(b);
		}
	}

	private void removeProjectile(ArrayList<Projectile> toBeRemovedProj, ArrayList<Projectile> projectiles) {
		for (Projectile p : toBeRemovedProj) {
			projectiles.remove(p);
			renderables.remove((Renderable) p);
		}
	}

	private void removeAlien(ArrayList<Alien> toBeRemovedAlien) {
		for (Alien alien : toBeRemovedAlien) {
			aliens.remove(alien);
			renderables.remove(alien);
		}
	}

	private void movePlayerProjectile() {
		ArrayList<Projectile> toBeRemovedProj = new ArrayList<Projectile>();
		for (Projectile p : playerProjectiles) {
			// remove the projectile if it goes out of the screen
			if (((Renderable) p).getPosition().getY() - 2 <= 0) {
				toBeRemovedProj.add(p);
			}
			else {
				p.shoot();
			}
		}
		removeProjectile(toBeRemovedProj, playerProjectiles);
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
		}
		alienShoot();
		alienReachesFloor();
	}

	private void alienShoot() {
		// after the waiting interval ends
		if (interval <= 0 && alienProjectiles.size() == 0) {
			Random r = new Random();
			// get a random number from 1-3 inclusive, random number of projectile
			int randNumOfProj = r.nextInt(2) + 2;
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

	private void moveAlienProjectile() {
		ArrayList<Projectile> toBeRemovedProj = new ArrayList<Projectile>();
		for (Projectile p : alienProjectiles) {
			// remove the projectile if it goes out of the screen
			if (((Renderable) p).getPosition().getY() + ((Renderable) p).getHeight() + 1 >= this.gameY) {
				toBeRemovedProj.add(p);
			}
			else {
				p.applyStrategy();
			}
		}
		removeProjectile(toBeRemovedProj, alienProjectiles);
	}

	private void alienGotShot() {
		ArrayList<Alien> toBeRemovedAlien = new ArrayList<Alien>();
		ArrayList<Projectile> toBeRemovedProj = new ArrayList<Projectile>();
		for (Alien alien : aliens) {
			for (Projectile p : playerProjectiles) {
				if (alien.getBoxCollider().isColliding(p.getBoxCollider())) {
					toBeRemovedAlien.add(alien);
					toBeRemovedProj.add(p);
					break;
				}
			}
		}
		removeAlien(toBeRemovedAlien);
		removeProjectile(toBeRemovedProj, playerProjectiles);
		// increase speed when aliens are shot
		if (toBeRemovedAlien.size() > 0) {
			for (Alien alien : aliens) {
				alien.increaseSpeed();
			}
		}
	}

	private void alienCollidesBunker() {
		ArrayList<Bunker> toBeRemovedBunker = new ArrayList<Bunker>();
		for (Bunker b : bunkers) {
			for (Alien alien : aliens) {
				if (b.getBoxCollider().isColliding(alien.getBoxCollider())) {
					toBeRemovedBunker.add(b);
					break;
				}
			}
		}
		removeBunker(toBeRemovedBunker);
	}

	private void alienCollidesPlayer() {
		for (Alien a : aliens) {
			if (a.getBoxCollider().isColliding(player.getBoxCollider())) {
				endGame();
				break;
			}
		}
	}

	private void playerGotShot() {
		ArrayList<Projectile> toBeRemovedProj = new ArrayList<Projectile>();
		for (Projectile p : alienProjectiles) {
			if (p.getBoxCollider().isColliding(player.getBoxCollider())) {
				player.takeDamage(1);
				toBeRemovedProj.add(p);
				// reset player's position
				player.getPosition().setX(playerX);
				player.getPosition().setY(playerY);
				break;
			}
		}
		removeProjectile(toBeRemovedProj, alienProjectiles);
		if (!player.isAlive()) {
			endGame();
		}
	}

	private void bunkerColProj(ArrayList<Projectile> projectiles) {
		ArrayList<Projectile> toBeRemovedProj = new ArrayList<Projectile>();
		for (Bunker b : bunkers) {
			for (Projectile p : projectiles) {
				if (b.getBoxCollider().isColliding(p.getBoxCollider())) {
					toBeRemovedProj.add(p);
					b.updateState();
					break;
				}
			}
		}
		removeProjectile(toBeRemovedProj, projectiles);
	}

	private void projCollidesProj() {
		ArrayList<Projectile> toBeRemovedAlienProj = new ArrayList<Projectile>();
		ArrayList<Projectile> toBeRemovedPlayerProj = new ArrayList<Projectile>();
		for (Projectile ap : alienProjectiles) {
			for (Projectile pp : playerProjectiles) {
				if (ap.getBoxCollider().isColliding(pp.getBoxCollider())) {
					toBeRemovedAlienProj.add(ap);
					toBeRemovedPlayerProj.add(pp);
					break;
				}
			}
		}
		removeProjectile(toBeRemovedAlienProj, alienProjectiles);
		removeProjectile(toBeRemovedPlayerProj, playerProjectiles);
	}

	private void alienReachesFloor() {
		for (Alien alien : aliens) {
			if (alien.getPosition().getY() + alien.getHeight() + alien.getSpeed() >= this.gameY) {
				endGame();
				break;
			}
		}
	}
}