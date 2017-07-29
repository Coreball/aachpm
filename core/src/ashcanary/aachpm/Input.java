package ashcanary.aachpm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import ashcanary.aachpm.players.Human;
import ashcanary.aachpm.screens.*;
import ashcanary.aachpm.ui.Button;

/**
 * 
 * Input handler for AACHPM
 * @author Changyuan Lin & Sarah Gao
 *
 */
public class Input implements InputProcessor {
	
	private final AACHPM game;
	public Vector3 touchPos;
	
	// What click sound today?
	private boolean clickMode;
	
	/**
	 * Constructor
	 */
	public Input() {

		Gdx.app.log("Game", "Creating Input");

		this.game = AACHPM.getInstance();
		touchPos  = new Vector3();
		clickMode = false;
		
	}
	
	/**
	 * Plays "Click"
	 */
	public void playClick() {
		if(clickMode) { // If special
			if(Math.random() < 0.99) {
				game.clickSounds[(int)(Math.random() * (game.clickSounds.length - 2))].play();
			} else {
				Gdx.app.log("Input", "Special Click!");
				game.clickSounds[(int)(Math.random() * 2) + 6].play();
			}
		} else {
			game.clickRegular.play();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		
		// Toggle pause menu if in GameScreen
		if(keycode == com.badlogic.gdx.Input.Keys.ESCAPE && game.getScreen() instanceof GameScreen) {
			Gdx.app.log("Game", "ESC Pressed");
			GameScreen gameScreen = (GameScreen) game.getScreen();
			playClick();
			gameScreen.paused = !gameScreen.paused;
			gameScreen.fixTimer(gameScreen.paused);
		}
		
		return false;

	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		playClick();
		
		// Set input to touchPos
		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		
		// If MAIN MENU
		if(game.getScreen() instanceof MenuScreen) {
			MenuScreen menu = (MenuScreen) game.getScreen();
			game.hudCam.unproject(touchPos);
			for(Button aButton : menu.getButtonArray()) {
				if(aButton.isPressed(touchPos.x, touchPos.y)) {
					aButton.action();
				}
			}
		}
		
		// IF GAME SCREEN
		else if(game.getScreen() instanceof GameScreen) {
			
			GameScreen gameScreen = (GameScreen) game.getScreen();
			if(gameScreen.paused) {
				game.hudCam.unproject(touchPos);
				for(Button aButton : gameScreen.getButtonArray()) {
					if(aButton.isPressed(touchPos.x, touchPos.y)) {
						aButton.action();
					}
				}
			} else {
				game.gameCam.unproject(touchPos);
				Gdx.app.log("Input", "Directing Units towards: (" + touchPos.x + ", " + touchPos.y + ")");
				((Human) game.players.get(0)).choose((int) touchPos.x, (int) touchPos.y); // Send touch to Human for processing
			}
			
		}
		
		// IF END SCREEN
		else if(game.getScreen() instanceof EndScreen) {
			EndScreen endScreen = (EndScreen) game.getScreen();
			game.hudCam.unproject(touchPos);
			// Return to Menu button
			if(endScreen.getReturnButton().isPressed(touchPos.x, touchPos.y)) {
				endScreen.getReturnButton().action();
			}
			
		}
		
		return false;
		
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		
		// Zoom in. Zoom out.
		game.gameCam.zoom += 0.2 * amount;
		
		return false;
	}
	
}
