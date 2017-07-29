package ashcanary.aachpm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ashcanary.aachpm.AACHPM;
import ashcanary.aachpm.ui.Button;

/**
 * 
 * Screen displayed after a game ends
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class EndScreen implements Screen {
	
	// MESSAGES
	public static final String[] WIN_MESSAGES = { "YOU'RE SO AMAZING!", "YOU ARE THE GREATEST PERSON ALIVE!",
			"HAPPY BIRTHDAY!", "YOU'RE MY IDOL", "CONGRATS", "TELL ME HOW TO WIN LIKE YOU" };
	public static final String[] LOSS_MESSAGES = { "TRY HARDER NEXT TIME", "BE BETTER", "YOU SUCK LOL :D",
			"GET GOOD", "THESE AI CHOOSE RANDOMLY YET YOU LOSE", "THUMBS DOWN", "I HOPE YOU LEARN FROM MISTAKES",
			"NEVER ENTER A BATTLE OF WITS UNARMED", "BAD" };
	
	// THIS ENDSCREEN
	protected final AACHPM game;
	protected long timeTaken;
	private String message;
	private Sprite winLoss;
	private Button returnButton;
	private float commonX;
	private float commonY;
	
	/**
	 * Constructor
	 * @param victory whether or not the player won
	 * @param timeTaken time taken to finish game in milliseconds
	 */
	public EndScreen(boolean victory, long timeTaken) {

		Gdx.app.log("Game", "Creating EndScreen");
		this.game = AACHPM.getInstance();
		game.hudCam.setToOrtho(false, game.windowX, game.windowY);
		this.timeTaken = timeTaken;
		
		// Set win messages
		if(victory) {
			message = WIN_MESSAGES[(int)(Math.random() * WIN_MESSAGES.length)];
			winLoss = new Sprite(new Texture(Gdx.files.internal("endscreen/victory.png")));
		} else {
			message = LOSS_MESSAGES[(int)(Math.random() * LOSS_MESSAGES.length)];
			winLoss = new Sprite(new Texture(Gdx.files.internal("endscreen/gameOver.png")));
		}
		winLoss.setScale(game.scaleFactor);
		winLoss.setCenter(game.windowX / 2, game.windowY / 2 + 64 * game.scaleFactor);

		// Return button
		returnButton = new Button(new Texture(Gdx.files.internal("gamescreen/returnToMenu.png")),
				game.windowX / 2, game.windowY / 2 - 260 * game.scaleFactor, true) {
			@Override
			public void action() {
				Gdx.app.log("Game", "Returning to MenuScreen via end screen");
				game.setScreen(new MenuScreen());
			}
		};

		commonX = game.windowX / 2 - 575 * game.scaleFactor;
		commonY = game.windowY / 2 + 150 * game.scaleFactor;

		Gdx.app.log("Game", "Done creating EndScreen");
		
	}

	/**
	 * Show how bad or how great the user is at playing games
	 */
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.hudCam.update();
		
		// Draw the win/loss main screen
		game.hudBatch.begin();
		winLoss.draw(game.hudBatch);
		returnButton.draw(game.hudBatch);
		// Draw the cool stats
		game.font.draw(game.hudBatch, message, commonX, commonY);
		game.font.draw(game.hudBatch, String.format("Time taken: %1$02d:%2$02d", timeTaken / 60000, timeTaken / 1000 % 60), commonX, commonY - 30 * game.scaleFactor);
		game.font.draw(game.hudBatch, "Total Units Killed: " + game.totalUnitsKilled, commonX, commonY - 60 * game.scaleFactor);
		game.font.draw(game.hudBatch, "Bases Switched: " + game.basesSwitched, commonX, commonY - 90 * game.scaleFactor);
		game.font.draw(game.hudBatch, "Lasers Shot: " + game.lasersShot, commonX, commonY - 120 * game.scaleFactor);
		game.font.draw(game.hudBatch, "Total Damage Dealt: " + game.totalDamageDealt, commonX, commonY - 150 * game.scaleFactor);
		game.font.draw(game.hudBatch, "Times Units Redirected: " + game.timesMovedUnits, commonX, commonY - 180 * game.scaleFactor);
		game.hudBatch.end();
		
	}

	/**
	 * Return return button
	 * @return return button
	 */
	public Button getReturnButton() {
		return returnButton;
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
