package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ca.bcit.comp2522.termproject.CrowdSurvivor;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	private static final int width = 1920;
	private static final int height = 1080;

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Crowd Survivor");
		config.setWindowedMode(width, height);
		new Lwjgl3Application(new CrowdSurvivor(), config);
	}
}
