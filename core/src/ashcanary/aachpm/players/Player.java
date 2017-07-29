package ashcanary.aachpm.players;

import ashcanary.aachpm.AACHPM;
import com.badlogic.gdx.graphics.Color; // I think this is the right one
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.units.Unit;


/**
 * 
 * Represents a Player in AACHPM
 * @author Changyuan Lin & Sarah Gao
 *
 */
public abstract class Player {
	
	// Where units should head towards
	protected int x;
	protected int y;
	
	private Array<Unit> units;
	private Color color;
	private final AACHPM game;
	
	/**
	 * Make the Player
	 * @param color the team color
	 */
	public Player(Color color) {
		this.color = color;
		this.game = AACHPM.getInstance();
		units = new Array<Unit>();
	}

	/**
	 * Moves each unit that the player owns
	 */
	public void moveUnits(float delta) {
		for(Unit unit : units) {
			unit.move(delta, x, y);
		}
	}
	
	/**
	 * Determine the first location to attack
	 */
	public abstract void firstChoose();
	
	/**
	 * Get Units
	 * @return Array<Unit> representing player's units
	 */
	public Array<Unit> getUnits() {
		return units;
	}
	
	/**
	 * Get Color
	 * @return color representing team
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Get Game
	 * @return game
	 */
	public AACHPM getGame() {
		return game;
	}

}
