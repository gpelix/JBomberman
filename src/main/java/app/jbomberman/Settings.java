
package app.jbomberman;

import app.jbomberman.util.Resources;
import org.json.JSONObject;

/**
 * General settings for the program
 *
 * @author gp
 */
public class Settings
{
    /**
     * SCALE_FACTOR is the scaling factor we used to create the images
     */
    public static final int SCALE_FACTOR;

    public static final int MAX_FPS;
    public static final String APPLICATION_NAME;
    public static final String JSON_DIRECTORY = "json/";
    public static final String IMAGES_DIRECTORY;
    public static final String JBOMBERMAN_DIRECTORY_NAME;
    public static final String PLAYERS_FILE = "players.json";
    
    private static final String SETTINGS_FILE =
        JSON_DIRECTORY + "settings.json";

    // load the json file and set SCALE_FACTOR
    static {
        JSONObject settingsJsonRead =
            new JSONObject(Resources.readResource(SETTINGS_FILE));
        SCALE_FACTOR = settingsJsonRead.getInt("SCALE_FACTOR");
        MAX_FPS = settingsJsonRead.getInt("MAX_FPS");
        APPLICATION_NAME = settingsJsonRead.getString("APPLICATION_NAME");
        IMAGES_DIRECTORY = settingsJsonRead.getString("IMAGES_DIRECTORY");
        JBOMBERMAN_DIRECTORY_NAME = settingsJsonRead.getString("JBOMBERMAN_DIRECTORY_NAME");
    }
}
