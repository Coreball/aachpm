package ashcanary.aachpm.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.players.Player;

/**
 * 
 * Artillery Unit that only attacks bases but high damage and from distance
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class Artillery extends Unit {

	public static final Texture UNIT_TEXTURE = new Texture(Gdx.files.internal("units/Artillery.png"));
	
	/**
	 * Constructor
	 * @param owner player owning unit
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public Artillery(Player owner, int x, int y) {
		super(UNIT_TEXTURE, owner, x, y, 750, 4000, 40, 80);
	}

	/**
	 * Attack bases from long range
	 */
	@Override
	public void attack(float delta) {
		// Reference not needed
		Array<Base> enemyBases = getNearbyEnemyBases(120);
		if(enemyBases.size != 0) {
			hitBase(delta);
		}
	}

}
