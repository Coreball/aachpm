package ashcanary.aachpm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import ashcanary.aachpm.*;
import ashcanary.aachpm.players.*;
import ashcanary.aachpm.ui.Button;
import ashcanary.aachpm.units.*;

/**
 * 
 * Actual game screen with all the things doing things
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class GameScreen implements Screen {

	// Sizes
	public static final int WORLD_WIDTH = 960;
	public static final int WORLD_HEIGHT = 540;

	// Variables used by this one screen
	protected final AACHPM game;
	protected Sprite border;
	protected Sprite pauseBkg;
	protected Sprite pauseText;
	protected Array<Button> buttonArray;
	protected String gameMode;
	public boolean paused;
	protected long startTime;
	protected long pauseTime;
	
	/**
	 * Constructor
	 * @param gameMode gamemode type
	 * @param playerColor color of the human player
	 * @param numTeams number of teams in the game
	 */
	public GameScreen(String gameMode, Color playerColor, int numTeams) {

		Gdx.app.log("Game", "Creating GameScreen");
		long createStart = TimeUtils.millis();
		
		this.game = AACHPM.getInstance();
		this.gameMode = gameMode;
		
		// Colors available, remove the one the player chose
		Array<Color> colors = new Array<Color>();
		colors.add(Color.CYAN);
		colors.add(Color.YELLOW);
		colors.add(Color.MAGENTA);
		colors.add(Color.CHARTREUSE);
		colors.add(Color.RED);
		colors.removeValue(playerColor, true);
		
		// Create Players
		game.players = new Array<Player>();
		if(gameMode.equals("Benchmark")) {
			game.players.add(new Human(playerColor, true));
		} else {
			game.players.add(new Human(playerColor, false)); // Index of 0 is the Human
		}
		for(int i = 0; i < numTeams - 1; i++) {
			game.players.add(new AI(colors.get(i)));
		}
		
		// Make bases
		makeBases(numTeams);
		
		// Camera stuff
		// Set the game's camera to have a view on the world of the width of the world and the height according to aspect ratio
		// FIXME Maybe don't remake the whole camera here?
		game.gameCam = new OrthographicCamera(WORLD_WIDTH, WORLD_WIDTH * (game.windowY / game.windowX));
		// Set the camera to the middle of the game world
		game.gameCam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
		game.gameCam.zoom = 1.1f;
		game.gameCam.update();
		game.hudCam.setToOrtho(false, game.windowX, game.windowY);
		
		// Make some starter units
		for(int i = 0; i < numTeams; i++) {
			for(int j = 0; j < 5; j++) {
				game.bases.get(i).makeUnit();
			}
		}
		
		// AI (and Human now) choose first base to attack
		for(int i = 0; i < numTeams; i++) {
			game.players.get(i).firstChoose();
		}
		
		// Border thing to tell which team the human controls
		border = new Sprite(new Texture(Gdx.files.internal("gamescreen/gameBorder.png")));
		border.setColor(playerColor);
		
		// Pausation
		paused = false;
		pauseBkg = new Sprite(new Texture(Gdx.files.internal("gamescreen/pauseBackground.png")));
		pauseBkg.setScale(game.scaleFactor);
		pauseBkg.setCenter(game.windowX / 2, game.windowY / 2);
		pauseText = new Sprite(new Texture(Gdx.files.internal("gamescreen/pauseHead.png")));
		pauseText.setScale(game.scaleFactor);
		pauseText.setCenter(game.windowX / 2, game.windowY / 2 + 80 * game.scaleFactor);
		buttonArray = new Array<Button>();
		buttonArray.add(new Button(new Texture(Gdx.files.internal("gamescreen/returnToMenu.png")),
				game.windowX / 2, game.windowY / 2 - 40 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Game", "Returning to MenuScreen via pause menu");
				game.setScreen(new MenuScreen());
			}
		});
		buttonArray.add(new Button(new Texture(Gdx.files.internal("gamescreen/resume.png")),
				game.windowX / 2, game.windowY / 2 - 120 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "Unpausing via pause menu");
				paused = false;
				fixTimer(paused);
			}
		});
		
		// Reset Stats
		startTime = TimeUtils.millis();
		game.totalUnitsKilled = 0;
		game.basesSwitched = 0;
		game.lasersShot = 0;
		game.totalDamageDealt = 0;
		game.timesMovedUnits = 0;

		Gdx.app.log("Game", "Done creating GameScreen in " + TimeUtils.timeSinceMillis(createStart) + " millis");
		
	}

	/**
	 * Render game
	 * @param delta time since last frame
	 */
	@Override
	public void render(float delta) {
		
		// TEST GAME END
		if(game.players.get(0).getUnits().size == 0) {
			// LOSS
			Gdx.app.log("Game", "Game results in Loss");
			game.setScreen(new EndScreen(false, TimeUtils.millis() - startTime));
		} else {
			// WIN
			int totalUnits = 0;
			for(int i = 1; i < game.players.size; i++) {
				totalUnits += game.players.get(i).getUnits().size;
			}
			if(totalUnits == 0) {
				Gdx.app.log("Game", "Game results in Win");
				game.setScreen(new EndScreen(true, TimeUtils.millis() - startTime));
			}
		}
		
		// CLEAR
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// UPDATE
		if(!paused) {
			cameraInput();
		}
		game.gameCam.update();
		game.batch.setProjectionMatrix(game.gameCam.combined);
		game.shapes.setProjectionMatrix(game.gameCam.combined);
		
		// GAME LOGIC
		if(!paused) {
			for(Base base : game.bases) {
				base.heal(delta);
				if(base.getOwner() != null && (TimeUtils.millis() >= base.getLastUnitMadeTime() + base.getUnitProductionTime()
						|| gameMode.equals("Laser Beams")))
					base.makeUnit();
			}
			for(Player plr : game.players) {
				if(plr instanceof AI) {
					((AI) plr).choose();
				}
				plr.moveUnits(delta);
			}
		}
		
		// Draw bases
		game.shapes.begin(ShapeRenderer.ShapeType.Filled);
		drawBases();
		// Units attack and draw a line shape
		if(!paused) {
			for(Player plr : game.players) {
				for(Unit unit : plr.getUnits()) {
					unit.attack(delta);
				}
			}
		}
		game.shapes.end();
		
		// Draw Units
		game.batch.begin();
		drawUnits();
		game.batch.end();
		
		// HUD THINGS
		game.shapes.begin(ShapeRenderer.ShapeType.Filled);
		// Draw PIE CHART PIE CHART PIE CHART!!! AIIEHUEHSIOFSDJ
		game.shapes.setProjectionMatrix(game.hudCam.combined); // I want to draw on the HUD
		pieChart();
		drawBorder();
		game.shapes.end();
		game.hudBatch.begin();
		if(paused) {
			pauseBkg.draw(game.hudBatch);
			pauseText.draw(game.hudBatch);
			for(Button button : buttonArray) {
				button.draw(game.hudBatch);
			}
		}
		// border.draw(game.hudBatch);
		game.hudBatch.end();
		
	}
	
	/**
	 * Poll for keys to pan/zoom the game camera
	 */
	private void cameraInput() {
		
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.MINUS)) {
			game.gameCam.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.EQUALS)) {
			game.gameCam.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
			game.gameCam.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
			game.gameCam.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
			game.gameCam.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
			game.gameCam.translate(0, 3, 0);
		}
		game.gameCam.zoom = MathUtils.clamp(game.gameCam.zoom, 0.5f, 4);
		// Haha no limits on where the game.gameCam is x and y I mean zoom is now clamped
		
	}

	/**
	 * Make the bases
	 * Method will be overridden in GameBenchmarkScreen.java
	 * @param numTeams number of teams * 3 = total bases
	 */
	public void makeBases(int numTeams) {
		// Make Bases, redoing if bases are too close
		do {
			game.bases = new Array<Base>(); // Clear out bases
			// Owned Bases
			for(int i = 0; i < numTeams; i++) {
				int x = (int)(Math.random() * (WORLD_WIDTH - 48)) + 24;
				int y = (int)(Math.random() * (WORLD_HEIGHT - 48)) + 24;
				game.bases.add(new Base(gameMode, x, y, game.players.get(i)));
			}
			// Unowned Bases
			for(int i = 0; i < numTeams * 2; i++) {
				int x = (int)(Math.random() * (WORLD_WIDTH - 48)) + 24;
				int y = (int)(Math.random() * (WORLD_HEIGHT - 48)) + 24;
				game.bases.add(new Base(gameMode, x, y));
			}
		} while(Base.tooClose(game.bases));
	}
	
	/**
	 * Account for pausation by marking a start time of pause
	 * and adding it to the game start time when unpaused
	 * @param start true if starting pause, false if resuming
	 */
	public void fixTimer(boolean start) {
		if(start) {
			pauseTime = TimeUtils.millis();
		} else {
			startTime += TimeUtils.timeSinceMillis(pauseTime);
		}
	}

	/**
	 * Draw bases
	 */
	public void drawBases() {
		for(Base base : game.bases) {
			if(base.getOwner() != null) {
				game.shapes.setColor(base.getOwner().getColor());
			} else {
				game.shapes.setColor(Color.WHITE);
			}
			game.shapes.arc(base.getX(), base.getY(), 24, 90, base.getHealth() * 360 / Base.MAX_HEALTH, 36);
			game.shapes.setColor(0.2f, 0.2f, 0.2f, 0);
			game.shapes.arc(base.getX(), base.getY(), 21, 90, base.getHealth() * 360 / Base.MAX_HEALTH, 36);
		}
	}

	/**
	 * Draw Units
	 */
	public void drawUnits() {
		for(Player plr : game.players) {
			for(Unit unit : plr.getUnits()) {
				unit.draw(game.batch);
			}
		}
	}

	/**
	 * Draw a pie chart comparing player strengths
	 */
	public void pieChart() {
		float total = 0;
		for(Player plr : game.players) {
			total += plr.getUnits().size;
		}
		float lastDegree = 90;
		for(Player plr : game.players) {
			float degrees = plr.getUnits().size / total * 360;
			game.shapes.setColor(plr.getColor());
			game.shapes.arc(150 * game.scaleFactor, game.windowY - 150 * game.scaleFactor, 120 * game.scaleFactor, lastDegree, degrees, 36);
			lastDegree += degrees;
		}
	}

	/**
	 * Draw the border indicating player color
	 * No longer a texture!
	 */
	public void drawBorder() {
		game.shapes.setColor(game.players.get(0).getColor());
		game.shapes.rect(0, 0, 4 * game.scaleFactor, game.windowY);
		game.shapes.rect(game.windowX, 0, -4 * game.scaleFactor, game.windowY);
		game.shapes.rect(0, game.windowY, game.windowX, -4 * game.scaleFactor); // lol negative heights
		game.shapes.rect(0, 0, game.windowX, 4 * game.scaleFactor);
	}

	/**
	 * Return button array
	 * @return array of buttons
	 */
	public Array<Button> getButtonArray() {
		return buttonArray;
	}

	@Override
	public void show() {
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		paused = true; // Pause on focus loss
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}
