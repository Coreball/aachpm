package ashcanary.aachpm.screens;

import ashcanary.aachpm.AACHPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.ui.Button;

/**
 * 
 * Main menu
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class MenuScreen implements Screen {
	
	private final AACHPM game;
	private Array<Button> buttonArray;
	private Sprite mainMenuLogo;
	private Sprite mainMenuNames;
	private Sprite teamAmountHead;
	private Sprite playerColorHead;
	private Sprite gameModeHead;
	private Sprite teamBorder;
	private Sprite colorBorder;
	private Sprite gameModeBorder;

	private int teamAmount;
	private Color playerColor;
	private String gameMode;
	
	/**
	 * Constructor
	 */
	public MenuScreen() {

		Gdx.app.log("Game", "Creating MenuScreen");

		// Stuff
		this.game = AACHPM.getInstance();
		game.hudCam.setToOrtho(false, game.windowX, game.windowY);

		// Not clickable Sprites
		mainMenuLogo = new Sprite(new Texture(Gdx.files.internal("menuscreen/hands.png")));
		mainMenuLogo.setScale(game.scaleFactor);
		mainMenuLogo.setCenter(game.windowX / 2, game.windowY - game.scaleFactor * (80 + mainMenuLogo.getHeight() / 2));
		mainMenuNames = new Sprite(new Texture(Gdx.files.internal("menuscreen/names.png")));
		mainMenuNames.setScale(game.scaleFactor);
		mainMenuNames.setCenter(game.windowX / 2, game.windowY - game.scaleFactor * 360);
		teamAmountHead = new Sprite(new Texture(Gdx.files.internal("menuscreen/numberTeamsSmall.png")));
		teamAmountHead.setScale(game.scaleFactor);
		teamAmountHead.setCenter(game.windowX / 2 - game.scaleFactor * 205, game.windowY - 500 * game.scaleFactor);
		playerColorHead = new Sprite(new Texture(Gdx.files.internal("menuscreen/playerColor.png")));
		playerColorHead.setScale(game.scaleFactor);
		playerColorHead.setCenter(game.windowX / 2 + game.scaleFactor * 205, game.windowY - 500 * game.scaleFactor);
		gameModeHead = new Sprite(new Texture(Gdx.files.internal("menuscreen/gameMode.png")));
		gameModeHead.setScale(game.scaleFactor);
		gameModeHead.setCenter(game.windowX / 2, game.windowY - 750 * game.scaleFactor);

		// Borders
		teamBorder = new Sprite(new Texture(Gdx.files.internal("menuscreen/chooseBorder.png")));
		teamBorder.setScale(game.scaleFactor);
		// Don't have to manually set position OR set defaults, just tell the correct button to act!
		colorBorder = new Sprite(new Texture(Gdx.files.internal("menuscreen/chooseBorder.png")));
		colorBorder.setScale(game.scaleFactor);
		gameModeBorder = new Sprite(new Texture(Gdx.files.internal("menuscreen/wideChooseBorder.png")));
		gameModeBorder.setScale(game.scaleFactor);

		// Buttons and shet
		buttonArray = new Array<Button>();
		makeButtons();

		// Default options
		// HAHA no actual math here for me, just tell the button to press itself
		buttonArray.get(2).action();
		buttonArray.get(5).action();
		buttonArray.get(8).action();

		Gdx.app.log("Game", "Done creating MenuScreen");
		
	}

	/**
	 * Render the menu
	 */
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.hudCam.update();
		
		// REGULAR BATCH
		game.hudBatch.begin();

		// Textures
		// Draw buttons
		for(Button button : buttonArray) {
			button.draw(game.hudBatch);
		}
		// Borders
		mainMenuLogo.draw(game.hudBatch);
		mainMenuNames.draw(game.hudBatch);
		teamAmountHead.draw(game.hudBatch);
		playerColorHead.draw(game.hudBatch);
		gameModeHead.draw(game.hudBatch);
		teamBorder.draw(game.hudBatch);
		colorBorder.draw(game.hudBatch);
		gameModeBorder.draw(game.hudBatch);
		
		game.hudBatch.end();
		
	}

	/**
	 * Return buttons
	 * @return array of buttons
	 */
	public Array<Button> getButtonArray() {
		return buttonArray;
	}

	/**
	 * Make buttons used in the menu
	 */
	private void makeButtons() {

		// Play Button
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/playButton.png")), game.windowX / 2, 120 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Game", "Starting Game");
				if(gameMode.equals("Benchmark")) {
					game.setScreen(new GameBenchmarkScreen());
				} else {
					game.setScreen(new GameScreen(gameMode, playerColor, teamAmount));
				}
			}
		});

		// Team Amount Buttons
		// Constants are predetermined based on size of buttons
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/three.png")),
				game.windowX / 2 - game.scaleFactor * 325, game.windowY - 600 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "3 Teams Pressed");
				teamBorder.setPosition(this.getX(), this.getY()); // TELEPORT THE BORDER TO WHERE THIS BUTTON LIVES
				teamAmount = 3;
			}
		});
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/four.png")),
				game.windowX / 2 - game.scaleFactor * 205, game.windowY - 600 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "4 Teams Pressed");
				teamBorder.setPosition(this.getX(), this.getY());
				teamAmount = 4;
			}
		});
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/five.png")),
				game.windowX / 2 - game.scaleFactor * 85, game.windowY - 600 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "5 Teams Pressed");
				teamBorder.setPosition(this.getX(), this.getY());
				teamAmount = 5;
			}
		});

		// Color Buttons
		// Constants are predetermined based on size of buttons
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/cyan.png")),
				game.windowX / 2 + game.scaleFactor * 85, game.windowY - 600 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "Cyan Color Pressed");
				colorBorder.setPosition(this.getX(), this.getY());
				playerColor = Color.CYAN;
			}
		});
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/yellow.png")),
				game.windowX / 2 + game.scaleFactor * 205, game.windowY - 600 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "Yellow Color Pressed");
				colorBorder.setPosition(this.getX(), this.getY());
				playerColor = Color.YELLOW;
			}
		});
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/magenta.png")),
				game.windowX / 2 + game.scaleFactor * 325, game.windowY - 600 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "Magenta Color Pressed");
				colorBorder.setPosition(this.getX(), this.getY());
				playerColor = Color.MAGENTA;
			}
		});

		// Game Mode Buttons
		// Constants are predetermined based on size of buttons
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/laserBeams.png")),
				game.windowX / 2 - game.scaleFactor * 240, game.windowY - 830 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "Laser Beams Mode Pressed");
				gameModeBorder.setPosition(this.getX(), this.getY());
				gameMode = "Laser Beams";
			}
		});
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/swarm.png")),
				game.windowX / 2, game.windowY - 830 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "Swarm Mode Pressed");
				gameModeBorder.setPosition(this.getX(), this.getY());
				gameMode = "Swarm";
			}
		});
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/assortedUnits.png")),
				game.windowX / 2 + game.scaleFactor * 240, game.windowY - 830 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Input", "Assorted Units Mode Pressed");
				gameModeBorder.setPosition(this.getX(), this.getY());
				gameMode = "Assorted Units";
			}
		});

		// Exit Button
		buttonArray.add(new Button(new Texture(Gdx.files.internal("menuscreen/close.png")),
				game.windowX - 64 * game.scaleFactor, game.windowY - 64 * game.scaleFactor, false) {
			@Override
			public void action() {
				Gdx.app.log("Game", "Exiting...");
				Gdx.app.exit();
			}
		});

	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
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
