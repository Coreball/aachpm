package ashcanary.aachpm.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.players.Player;

/**
 * 
 * Speeder Unit that goes really fast like really fast yeah
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class Speeder extends Unit {

	public static final Texture UNIT_TEXTURE = new Texture(Gdx.files.internal("units/Speeder.png"));
	
	/**
	 * Constructor
	 * @param owner player owning unit
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Speeder(Player owner, int x, int y) {
		super(UNIT_TEXTURE, owner, x, y, 200, 25, 100, 2);
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
