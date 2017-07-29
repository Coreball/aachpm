package ashcanary.aachpm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import ashcanary.aachpm.players.Player;
import ashcanary.aachpm.units.Base;
import ashcanary.aachpm.screens.MenuScreen;

/**
 * 
 * Game Control for AACHPM
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class AACHPM extends Game {

	// THIS GAME!
	private static AACHPM currentGame;
	
	// IMPORTANT
	public BitmapFont font;
	public SpriteBatch batch;
	public SpriteBatch hudBatch;
	public ShapeRenderer shapes;
	public OrthographicCamera hudCam;
	public OrthographicCamera gameCam;
	
	// AUDIO
	public Sound[] clickSounds;
	public Sound clickRegular;
	
	// GAME DATA
	public Array<Base> bases;
	public Array<Player> players;
	public int totalUnitsKilled;
	public int basesSwitched;
	public long lasersShot;
	public long totalDamageDealt;
	public int timesMovedUnits;

	// SCREEN INFORMATION
	public float windowX;
	public float windowY;
	public float scaleFactor;

	/**
	 * Create the game for the first time!
	 */
	@Override
	public void create() {

		// Begin
		Gdx.app.log("Game", "Launching AACHPM...");
		long createStart = TimeUtils.millis();

		// Set static game
		currentGame = this;

		// Find the scale factor and window sizes
		windowX = Gdx.graphics.getWidth();
		windowY = Gdx.graphics.getHeight();
		scaleFactor = findScaleFactor(windowX, windowY);

		// Prepare cameras
		hudCam = new OrthographicCamera();
		gameCam = new OrthographicCamera();
		// Update 30 May 2017: Maybe this is actually not necessary, gameCam is made in GameScreen

		// Make font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("menuscreen/Helvetica.dfont"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) (20 * scaleFactor);
		font = generator.generateFont(parameter); // font size 20 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		// Make drawing materials
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		shapes = new ShapeRenderer();

		// Make click sounds
		clickSounds = new Sound[]{
				Gdx.audio.newSound(Gdx.files.internal("audio/clickC1.wav")),
				Gdx.audio.newSound(Gdx.files.internal("audio/clickC2.wav")),
				Gdx.audio.newSound(Gdx.files.internal("audio/clickC3.wav")),
				Gdx.audio.newSound(Gdx.files.internal("audio/clickS1.wav")),
				Gdx.audio.newSound(Gdx.files.internal("audio/clickS2.wav")),
				Gdx.audio.newSound(Gdx.files.internal("audio/clickS3.wav")),
				Gdx.audio.newSound(Gdx.files.internal("audio/clickS4.wav")),
				Gdx.audio.newSound(Gdx.files.internal("audio/clickS5.wav")),
			};
		clickRegular = Gdx.audio.newSound(Gdx.files.internal("audio/clickRegular.wav"));

		// Set input processor
		Gdx.input.setInputProcessor(new Input());

		// Set menu screen
		this.setScreen(new MenuScreen());

		Gdx.app.log("Game", "Done launching game in " + TimeUtils.timeSinceMillis(createStart) + " millis");
		
	}

	/**
	 * Find scale factor based on smallest difference from 1920x1080
	 * @param x x width
	 * @param y y width
	 * @return scale factor
	 */
	private float findScaleFactor(float x, float y) {
		float scaleX = x / 1920;
		float scaleY = y / 1080;
		return Math.min(scaleX, scaleY);
	}

	/**
	 * Get the current game object from the class
	 * @return current AACHPM
	 */
	public static AACHPM getInstance() {
		return currentGame;
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		
	}

}
