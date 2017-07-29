package ashcanary.aachpm.units;

import ashcanary.aachpm.AACHPM;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import ashcanary.aachpm.players.Player;

/**
 * 
 * Representation of a Base
 * @author Changyuan Lin & Sarah Gao
 * 
 */
public class Base implements Targetable {
	
	private float health;
	public static final float MAX_HEALTH = 20000;
	private AACHPM game;
	private Player owner;
	private long lastUnitMadeTime;
	private long unitProductionTime = 1000;
	private int x;
	private int y;
	private String gameMode;
	
	/**
	 * Construct a base without an owner (neutral base)
	 * @param gameMode game mode
	 * @param x x coord
	 * @param y y coord
	 */
	public Base(String gameMode, int x, int y) {
		lastUnitMadeTime = TimeUtils.millis();
		health = MAX_HEALTH;
		this.game = AACHPM.getInstance();
		this.gameMode = gameMode;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Construct a base belonging to a Player
	 * @param gameMode game mode
	 * @param x x coord
	 * @param y y coord
	 * @param owner player owning base
	 */
	public Base(String gameMode, int x, int y, ashcanary.aachpm.players.Player owner) {
		this.owner = owner;
		lastUnitMadeTime = TimeUtils.millis();
		health = MAX_HEALTH;
		this.game = AACHPM.getInstance();
		this.gameMode = gameMode;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Heal the base
	 * @param delta time since last called
	 */
	public void heal(float delta) {
		health += MAX_HEALTH * 0.01 * delta; // PROBABLY EXTREMEMLY SLOW
		if(health > MAX_HEALTH) {
			health = MAX_HEALTH;
		}
	}
	
	/**
	 * Get health
	 * @return current health
	 */
	public float getHealth() {
		return health;
	}
	
	/**
	 * Take damage, switching teams if killed
	 * @param delta time since last called
	 * @param dmg damage taken
	 * @param attacker team that attacked base
	 */
	public void takeDamage(float delta, int dmg, ashcanary.aachpm.players.Player attacker) {
		
		health -= dmg * delta;
		game.totalDamageDealt += dmg * delta;
		if(health <= 0) {
			health = MAX_HEALTH / 2;
			owner = attacker;
			game.basesSwitched++;
		}
		
	}
	
	/**
	 * Create a new unit dependent on game mode & and add to list of the base's owner
	 */
	public void makeUnit() {

		if(gameMode.equals("Assorted Units")) {
			int random = (int) (Math.random() * 10);
			switch(random) {
				case 0:
				case 1:
					owner.getUnits().add(new AntiUnit(owner, x, y));
					break;
				case 2:
					owner.getUnits().add(new Tank(owner, x, y));
					break;
				case 3:
					owner.getUnits().add(new Artillery(owner, x, y));
					break;
				case 4:
				case 5:
					owner.getUnits().add(new Speeder(owner, x, y));
					owner.getUnits().add(new Speeder(owner, x, y));
					owner.getUnits().add(new Speeder(owner, x, y));
					owner.getUnits().add(new Speeder(owner, x, y));
					break;
				default:
					owner.getUnits().add(new Standard(owner, x, y));
					break;
			}
		} else {
			owner.getUnits().add(new Standard(owner, x, y));
		}
		
		lastUnitMadeTime = TimeUtils.millis();
		
	}
	
	/**
	 * Check to see if this base overlaps other bases
	 * @param otherBases the array of bases to check
	 * @return true if another base is too close, false otherwise
	 */
	public static boolean tooClose(Array<Base> otherBases) {
		for(int i = 0; i < otherBases.size; i++) {
			Base first = otherBases.get(i);
			for(int j = 0; j < otherBases.size; j++) { // Do not look at itself
				if(i == j)
					continue;
				Base other = otherBases.get(j);
				if(Math.sqrt((other.getX() - first.getX()) * (other.getX() - first.getX()) + (other.getY() - first.getY()) * (other.getY() - first.getY())) <= 60)
					return true;
			}
		}
		return false;
		
	}
	
	public long getUnitProductionTime() {
		return unitProductionTime;
	}
	
	public long getLastUnitMadeTime() {
		return lastUnitMadeTime;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
