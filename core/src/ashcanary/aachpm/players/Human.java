package ashcanary.aachpm.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import ashcanary.aachpm.units.Base;

/**
 * 
 * Human Player
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class Human extends Player {

	private boolean benchmark;
	
	/**
	 * Constructor
	 * @param color team color
	 */
	public Human(Color color, boolean benchmark) {
		super(color);
		this.benchmark = benchmark;
	}

	/**
	 * Set initial target to where the player's base is
	 */
	@Override
	public void firstChoose() {
		if(benchmark) {
			this.x = 480;
			this.y = 0;
		} else {
			this.x = getGame().bases.get(0).getX();
			this.y = getGame().bases.get(0).getY();
		}
	}
	
	/**
	 * Tell units to move towards where mouse was pressed
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void choose(int x, int y) {
		for(Base base : getGame().bases) {
			if(x > base.getX() - 24 && x < base.getX() + 24 && y > base.getY() - 24 && y < base.getY() + 24) {
				x = base.getX();
				y = base.getY();
				Gdx.app.log("Human", "Snapped to base");
				break;
			}
		}
		this.x = x;
		this.y = y;
		getGame().timesMovedUnits++;
	}

}
