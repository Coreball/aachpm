package ashcanary.aachpm.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.players.Player;

/**
 * 
 * Anti-Unit specializing in murdering other units and not bases
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class AntiUnit extends Unit {

	public static final Texture UNIT_TEXTURE = new Texture(Gdx.files.internal("units/AntiUnit.png"));
	
	/**
	 * Constructor
	 * @param owner player owning unit
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public AntiUnit(Player owner, int x, int y) {
		super(UNIT_TEXTURE, owner, x, y, 750, 1000, 80, 20); // who knows how good these numbers are
	}

	/**
	 * Attack only enemies but in a larger range
	 */
	@Override
	public void attack(float delta) {
		// Reference not needed
		Array<Unit> enemies = getNearbyEnemies(72);
		if(enemies.size != 0) {
			hitEnemy(delta);
		}
	}

}
