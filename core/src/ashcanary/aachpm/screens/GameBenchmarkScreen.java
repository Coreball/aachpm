package ashcanary.aachpm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import ashcanary.aachpm.players.*;
import ashcanary.aachpm.ui.Button;
import ashcanary.aachpm.units.*;

/**
 * Game Screen for benchmarks; does not allow for user input
 * NOT UPDATED UNLESS NECESSARY!!
 * Created by Changyuan Lin on 22 May 2017.
 * @author Changyuan Lin & Sarah Gao
 */
public class GameBenchmarkScreen extends GameScreen {

	private double fpsAvg;
	private double fpsMax;
	private double fpsMin;

	private long lastAvg;

	public GameBenchmarkScreen() {
		super("Benchmark", Color.YELLOW, 4);
		lastAvg = TimeUtils.millis();
		fpsMin = Integer.MAX_VALUE;
	}

	/**
	 * Render game WITHOUT user input
	 * @param delta time since last frame
	 */
	@Override
	public void render(float delta) {

		// FPS STUFF
		int currentFps = Gdx.graphics.getFramesPerSecond();
		if(currentFps > fpsMax) {
			fpsMax = currentFps;
		}
		if(currentFps < fpsMin) {
			fpsMin = currentFps;
		}
		if(TimeUtils.timeSinceMillis(lastAvg) >= 1000) {
			fpsAvg += currentFps;
			lastAvg = TimeUtils.millis();
		}

		// SOME STUFF
		// FIXME THIS STARTS INCLUDING PLAYER, GAMESCREEN DOES *NOT*
		int totalUnits = 0;
		for(int i = 0; i < game.players.size; i++) {
			totalUnits += game.players.get(i).getUnits().size;
		}
		Gdx.app.log("Total Units", "" + totalUnits);

		// IS GAME OVER?
		if(game.players.get(0).getUnits().size == 0) {
			game.setScreen(new EndBenchmarkScreen(false, TimeUtils.millis() - startTime, fpsAvg, fpsMax, fpsMin));
		} else if(totalUnits - game.players.get(0).getUnits().size == 0) {
			game.setScreen(new EndBenchmarkScreen(true, TimeUtils.millis() - startTime, fpsAvg, fpsMax, fpsMin));
		}

		// CLEAR
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// UPDATE
		game.gameCam.update();
		game.batch.setProjectionMatrix(game.gameCam.combined);
		game.shapes.setProjectionMatrix(game.gameCam.combined);

		// GAME LOGIC
		for(Base base : game.bases) {
			base.heal(delta);
			if(base.getOwner() != null && totalUnits < 3000) {
				base.makeUnit();
			}
		}
		for(Player plr : game.players) {
			if(plr instanceof AI) {
				((AI) plr).choose();
			}
			plr.moveUnits(delta);
		}

		// Draw bases
		game.shapes.begin(ShapeRenderer.ShapeType.Filled);
		drawBases();
		// Units attack and draw a line shape
		for(int i = 0; i < game.players.size; i++) {
			Player plr = game.players.get(i);
			for(int j = 0; j < plr.getUnits().size; j++) {
				Unit unit = plr.getUnits().get(j);
				unit.attack(delta);
			}
		}
		game.shapes.end();

		// Draw Units
		game.batch.begin();
		for(int i = 0; i < game.players.size; i++) {
			Player plr = game.players.get(i);
			for(int j = 0; j < plr.getUnits().size; j++) {
				Unit unit = plr.getUnits().get(j);
				unit.draw(game.batch);
			}
		}
		game.batch.end();

		// HUD THINGS
		game.shapes.begin(ShapeRenderer.ShapeType.Filled);
		// Draw PIE CHART PIE CHART PIE CHART!!! AIIEHUEHSIOFSDJ
		pieChart();
		game.shapes.end();
		game.hudBatch.begin();
		if(paused) {
			pauseBkg.draw(game.hudBatch);
			// pauseText.draw(game.hudBatch);
			for(Button button : buttonArray) {
				button.draw(game.hudBatch);
			}
		}
		border.draw(game.hudBatch);
		game.hudBatch.end();

	}

	/**
	 * Make bases in a SPECIFIC PATTERN
	 * @param numTeams number of teams * 3 = total bases
	 */
	@Override
	public void makeBases(int numTeams) {
		game.bases = new Array<Base>(); // Clear out bases
		int x = 192;
		int y = 192;
		// Owned Bases
		game.bases.add(new Base(gameMode, 24,24,game.players.get(0)));
		game.bases.add(new Base(gameMode, WORLD_WIDTH - 24,24,game.players.get(1)));
		game.bases.add(new Base(gameMode, 24,WORLD_HEIGHT - 24,game.players.get(2)));
		game.bases.add(new Base(gameMode, WORLD_WIDTH - 24,WORLD_HEIGHT - 24,game.players.get(3)));
		// Unowned Bases
		for(int i = 0; i < numTeams * 2; i++) {
			game.bases.add(new Base(gameMode, x, y));
			x += 192;
			if(x > WORLD_WIDTH - 192) {
				x = 192;
				y += 192;
			}
		}
	}

}
