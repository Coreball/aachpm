package ashcanary.aachpm.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.players.Player;

/**
 * 
 * Standard Unit
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class Standard extends Unit {

	public static final Texture UNIT_TEXTURE = new Texture(Gdx.files.internal("units/Standard.png"));

	/**
	 * Constructor
	 * @param owner player owning unit
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Standard(Player owner, int x, int y) {
		super(UNIT_TEXTURE, owner, x, y, 1000, 250, 50, 5); // who knows how good these numbers are
	}

	/**
	 * Attack enemies first and if there are none attack bases
	 */
	@Override
	public void attack(float delta) {
		// Reference not needed
		Array<Unit> enemies = getNearbyEnemies(24);
		Array<Base> enemyBases = getNearbyEnemyBases(24);
		if(enemies.size != 0) {
			hitEnemy(delta);
		} else if(enemyBases.size != 0) {
			hitBase(delta);
		}
	}

}
