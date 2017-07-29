package ashcanary.aachpm.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * End Screen for benchmarks
 * NOT UPDATED UNLESS NECESSARY!!
 * Created by Changyuan Lin on 22 May 2017.
 * @author Changyuan Lin & Sarah Gao
 */
public class EndBenchmarkScreen extends EndScreen {

	private double fpsAvg;
	private double fpsMax;
	private double fpsMin;

	public EndBenchmarkScreen(boolean victory, long timeTaken, double fpsAvg, double fpsMax, double fpsMin) {
		super(victory, timeTaken);
		this.fpsAvg = fpsAvg;
		this.fpsMax = fpsMax;
		this.fpsMin = fpsMin;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.hudCam.update();

		// Draw important statistics
		game.hudBatch.begin();
		game.font.draw(game.hudBatch, "Average-ish FPS: " + fpsAvg / timeTaken * 1000, 385, 780);
		game.font.draw(game.hudBatch, "Max FPS: " + fpsMax, 385, 750);
		game.font.draw(game.hudBatch, "Min FPS: " + fpsMin, 385, 720);
		game.font.draw(game.hudBatch, String.format("Time taken: %1$02d:%2$02d", timeTaken / 60000, timeTaken / 1000 % 60), 385, 660);
		game.font.draw(game.hudBatch, "Total Units Killed: " + game.totalUnitsKilled, 385, 630);
		game.font.draw(game.hudBatch, "Bases Switched: " + game.basesSwitched, 385, 600);
		game.font.draw(game.hudBatch, "Lasers Shot: " + game.lasersShot, 385, 570);
		game.font.draw(game.hudBatch, "Total Damage Dealt: " + game.totalDamageDealt, 385, 540);
		game.font.draw(game.hudBatch, "Times Units Redirected: " + game.timesMovedUnits, 385, 510);
		game.hudBatch.end();

	}
}
