package ashcanary.aachpm.desktop;

import ashcanary.aachpm.AACHPM;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Launch the game on desktop
 */
public class DesktopLauncher {

	public static void main (String[] args) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		config.resizable = false;
		config.foregroundFPS = 0;
		config.backgroundFPS = 0;
		config.samples = 8;
		config.fullscreen = true;
		config.addIcon("menuscreen/Logo128.png", FileType.Internal);
		config.addIcon("menuscreen/Logo32.png", FileType.Internal);
		config.addIcon("menuscreen/Logo16.png", FileType.Internal);
		int random = (int)(Math.random() * 3);
		switch(random) {
			case 0:
				config.title = "AACHPM: The best game ever";
				break;
			case 1:
				config.title = "AACHPM: Made by Changyuan Lin and Sarah Gao!!!";
				break;
			case 2:
				config.title = "AACHPM";
				break;
		}
		new LwjglApplication(new AACHPM(), config);

	}

}
