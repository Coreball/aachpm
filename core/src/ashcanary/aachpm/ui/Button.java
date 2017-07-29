package ashcanary.aachpm.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ashcanary.aachpm.AACHPM;

/**
 * A button that could move around and stuff
 * Created by Changyuan Lin on 30 May 2017.
 */
public abstract class Button extends Sprite {

	/**
	 * Make the button thing
	 * Write action() when button is created
	 * @param texture image of button
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param center if the x and y should be the center of the button, else the bottom left corner
	 */
	public Button(Texture texture, float x, float y, boolean center) {
		super(texture);
		setScale(AACHPM.getInstance().scaleFactor);
		if(center) {
			setCenter(x, y);
		} else {
			setPosition(x, y);
		}
	}

	/**
	 * Check to see if the button was pressed according to x and y coordinates of mouse click
	 * @param x x coordinate of mouse
	 * @param y y coordinate of mouse
	 * @return true if x and y fall within button bounds
	 */
	public boolean isPressed(float x, float y) {
		return getBoundingRectangle().contains(x, y);
	}

	/**
	 * Perform an action
	 */
	public abstract void action();

}
