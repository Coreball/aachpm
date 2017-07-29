package ashcanary.aachpm.players;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.units.Base;

// BEEP BOOP I AM A ROBOT
/**
 * 
 * Computer-controlled Player
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class AI extends Player {
	
	private Base target;
	private Array<Base> enemyBases;

	/**
	 * Constructor
	 * @param color team color
	 */
	public AI(Color color) {
		super(color);
		enemyBases = new Array<Base>();
	}
	
	/**
	 * Choose where to attack for the first time
	 */
	@Override
	public void firstChoose() {
		findEnemyBases();
		attackNewEnemyBase();
	}

	/**
	 * Choose where to attack once the game has already begun
	 */
	public void choose() {
		findEnemyBases();
		if(enemyBases.size == 0) {
			followPlayer();
		} else {
			if(target.getOwner() == this) {
				attackNewEnemyBase();
			}
		}
	}

	/**
	 * Finds bases owned by enemies
	 */
	private void findEnemyBases() {
		Array<Base> bases = getGame().bases;
		enemyBases.clear();
		for(Base base : bases) {
			if(base.getOwner() != this) {
				enemyBases.add(base);
			}
		}
	}

	/**
	 * Attack a random base from the list of enemy Bases
	 */
	private void attackNewEnemyBase() {
		Base chosenBase = enemyBases.get((int) (Math.random() * enemyBases.size));
		x = chosenBase.getX();
		y = chosenBase.getY();
		target = chosenBase;
	}

	/**
	 * Set the target coordinates to where the Human's units are heading
	 */
	private void followPlayer() {
		x = getGame().players.get(0).x;
		y = getGame().players.get(0).y;
	}

}
