package ashcanary.aachpm.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.players.Player;

/**
 * 
 * Tank Unit that has a lot of health but moves reallly slow
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class Tank extends Unit {

	public static final Texture UNIT_TEXTURE = new Texture(Gdx.files.internal("units/Tank.png"));
	
	/**
	 * Constructor
	 * @param owner player owning unit
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Tank(Player owner, int x, int y) {
		super(UNIT_TEXTURE, owner, x, y, 10000, 250, 20, 2); // who knows how good these numbers are
	}

	/**
	 * Attack enemies and bases but only real close ones
	 */
	@Override
	public void attack(float delta) {
		// Reference not needed
		Array<Unit> enemies = getNearbyEnemies(12);
		Array<Base> enemyBases = getNearbyEnemyBases(24);
		if(enemies.size != 0) {
			hitEnemy(delta);
		} else if(enemyBases.size != 0) {
			hitBase(delta);
		}
	}

}
