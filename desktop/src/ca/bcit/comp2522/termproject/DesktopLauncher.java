package ca.bcit.comp2522.termproject;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Represents the DesktopLauncher class, which runs the Crowd Survivor game.
 * MacOS users need to start the application with the -XstartOnFirstThread JVM argument.
 *
 * @author Jonathan Liu
 * @author A01375621
 * @author jwl0724
 * @author Jason Chow
 * @author A00942129
 * @author Beijzum
 * @version 2024
 */
public class DesktopLauncher {

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;

    /**
     * Drives the program.
     *
     * @param arg is the command-line argument
     */
    public static void main(final String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        final int foregroundFPS = 60;
        config.setForegroundFPS(foregroundFPS);
        config.setTitle("Crowd Survivor");
        config.setWindowedMode(WIDTH, HEIGHT);
        new Lwjgl3Application(new CrowdSurvivor(), config);
    }
}
