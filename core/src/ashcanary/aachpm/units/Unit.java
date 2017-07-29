package ashcanary.aachpm.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import ashcanary.aachpm.AACHPM;
import ashcanary.aachpm.players.Player;

/**
 * 
 * Basis for Unit types
 * @author Changyuan Lin & Sarah Gao
 *
 */
public abstract class Unit extends Sprite implements Targetable {
	
	private float health;
	// private int maxHealth; This was going to be used for healing.
	private int damage;
	private double speed;
	private int drift;
	protected Player owner;
	private final AACHPM game;
	private Targetable target;

	private Array<Unit> enemies;
	private Array<Base> enemyBases;
	
	/**
	 * Constructor
	 * @param tex texture representing unit
	 * @param owner player owning unit
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param health health value
	 * @param damage damage to do to others
	 * @param speed how much to move per second
	 */
	public Unit(Texture tex, Player owner, int x, int y, int health, int damage, double speed, int drift) {
		
		super(tex); // Does Sprite stuff

		setColor(owner.getColor());
		setCenter(x, y);
		this.game = AACHPM.getInstance();
		this.health = health;
		// maxHealth = health;
		this.damage = damage;
		this.speed = speed;
		this.drift = drift;
		this.owner = owner;

		enemies = new Array<Unit>();
		enemyBases = new Array<Base>();
		
	}
	
	/**
	 * Move the unit towards a point
	 * @param delta time since last moved
	 * @param newX x value of point to move towards
	 * @param newY y value of point to move towards
	 */
	public void move(float delta, float newX, float newY) {
		
		// If drifts close enough do not move
		if(Math.sqrt((newX - getX()) * (newX - getX()) + (newY - getY()) * (newY - getY())) < drift) {
			return;
		}

		// Math triangles and stuff
		double distanceToMove = speed * delta;
		double bigDistance = Math.sqrt((newX - getX()) * (newX - getX()) + (newY - getY()) * (newY - getY()));
		double a = (newX - getX()) * (distanceToMove) / (bigDistance);
		double b = (newY - getY()) * (distanceToMove) / (bigDistance);
		
		setPosition((float)(getX() + a), (float)(getY() + b));
		if(a < 0){
			setRotation((float)(Math.atan(b / a) * 180 / Math.PI + 180));
		} else {
			setRotation((float)(Math.atan(b / a) * 180 / Math.PI));
		}
		
	}
	
	/**
	 * Take damage and die if needed
	 * @param delta time since last called
	 * @param dmg damage taken
	 */
	public void takeDamage(float delta, int dmg) {
		health -= dmg * delta;
		game.totalDamageDealt += dmg * delta;
		if(health <= 0) {
			owner.getUnits().removeValue(this, true); // kill itself :D
			game.totalUnitsKilled++;
		}
	}
	
	/**
	 * Get damage
	 * @return damage that unit will do
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Get health
	 * @return health of unit
	 */
	public float getHealth() {
		return health;
	}
	
	/**
	 * Search for enemies in a nearby radius
	 * @param radius distance to search
	 * @return list of nearby enemies
	 */
	public Array<Unit> getNearbyEnemies(int radius) {

		enemies.clear();
		for(int i = 0; i < game.players.size; i++) {
			Player plr = game.players.get(i);
			if(plr != owner) {
				for(int j = 0; j < plr.getUnits().size; j++) {
					Unit unit = plr.getUnits().get(j);
					if(Math.sqrt((unit.getX() - getX()) * (unit.getX() - getX()) + (unit.getY() - getY()) * (unit.getY() - getY())) <= radius) {
						enemies.add(unit);
					}
				}
			}
		}
		return enemies;
		
	}
	
	/**
	 * Search for bases in a nearby radius
	 * @param radius distance to search
	 * @return list of nearby enemy bases
	 */
	public Array<Base> getNearbyEnemyBases(int radius) {

		enemyBases.clear();
		for(int i = 0; i < game.bases.size; i++) {
			Base base = game.bases.get(i);
			if(base.getOwner() != owner) {
				if(Math.sqrt((base.getX() - getX()) * (base.getX() - getX()) + (base.getY() - getY()) * (base.getY() - getY())) <= radius) {
					enemyBases.add(base);
				}
			}
		}
		return enemyBases;
		
	}
	
	/**
	 * Draw a laser towards an enemy
	 * @param enemy where to draw line
	 */
	public void drawLaser(Unit enemy) {
		game.shapes.setColor(getColor());
		game.shapes.line(getX() + getWidth() / 2, getY() + getHeight() / 2, enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
		game.lasersShot++;
	}
	
	/**
	 * Draw a laser towards an enemy base
	 * @param enemy where to draw line
	 */
	public void drawLaser(Base enemy) {
		game.shapes.setColor(getColor());
		game.shapes.line(getX() + getWidth() / 2, getY() + getHeight() / 2, enemy.getX(), enemy.getY());
		game.lasersShot++;
	}
	
	/**
	 * Get owner
	 * @return owner of unit
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Attack enemies
	 * @param delta time since last called
	 */
	public abstract void attack(float delta);

	/**
	 * Attack enemy UNIT
	 * @param delta time since last frame
	 */
	public void hitEnemy(float delta) {
		if(target instanceof Unit && (target.getHealth() <= 0 || !enemies.contains((Unit) target, true))) {
			target = enemies.get((int)(Math.random() * enemies.size));
		} else if(target instanceof Base) {
			target = enemies.get((int)(Math.random() * enemies.size));
		} else if(target == null) {
			target = enemies.get((int)(Math.random() * enemies.size));
		}
		((Unit) target).takeDamage(delta, getDamage());
		drawLaser((Unit) target);
	}

	/**
	 * Attack enemy BASE
	 * @param delta time since last frame
	 */
	public void hitBase(float delta) {
		if(target instanceof Base && !enemyBases.contains((Base) target, true)) {
			target = enemyBases.get((int)(Math.random() * enemyBases.size));
		} else if(target instanceof Unit) {
			target = enemyBases.get((int)(Math.random() * enemyBases.size));
		} else if(target == null) {
			target = enemyBases.get((int)(Math.random() * enemyBases.size));
		}
		((Base) target).takeDamage(delta, getDamage(), getOwner());
		drawLaser((Base) target);
	}
	
}
