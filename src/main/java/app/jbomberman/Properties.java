package app.jbomberman;

import app.jbomberman.util.Resources;
import org.json.JSONObject;

/**
 * General settings for the program
 *
 * @author gp
 */
public class Properties
{
    /**
     * SCALE_FACTOR is the scaling factor we used to create the images
     */
    private final int scaleFactor;
    private final int maxFps;
    private final String applicationName;
    private static final String JSON_DIRECTORY = "json/";
    private final String imagesDirectory;
    private final String soundsDirectory;
    private final String jbombermanDirectoryDataName;
    private static final String playersFile = "players.json";
    private final String icon;

    private static final String SETTINGS_FILE =
        JSON_DIRECTORY + "settings.json";

    // load the json file and set SCALE_FACTOR
    private Properties()
    {
        JSONObject settingsJsonRead =
            new JSONObject(Resources.readResource(SETTINGS_FILE));
        scaleFactor = settingsJsonRead.getInt("SCALE_FACTOR");
        maxFps = settingsJsonRead.getInt("MAX_FPS");
        applicationName = settingsJsonRead.getString("APPLICATION_NAME");
        imagesDirectory = settingsJsonRead.getString("IMAGES_DIRECTORY");
        jbombermanDirectoryDataName = settingsJsonRead.getString("JBOMBERMAN_DATA_DIRECTORY_NAME");
        soundsDirectory = settingsJsonRead.getString("SOUNDS_DIRECTORY");
        icon = settingsJsonRead.getString("ICON");
    }

    private static Properties instance;

    public static Properties getInstance()
    {
        if (instance == null) {
            instance = new Properties();
        }
        return instance;
    }

    public int getMaxFps()
    {
        return maxFps;
    }

    public int getScaleFactor()
    {
        return scaleFactor;
    }

    public String getApplicationName()
    {
        return applicationName;
    }

    public String getImagesDirectory()
    {
        return imagesDirectory;
    }

    public String getJbombermanDirectoryDataName()
    {
        return jbombermanDirectoryDataName;
    }

    public static String getJsonDirectory()
    {
        return JSON_DIRECTORY;
    }

    public String getPlayersFile()
    {
        return playersFile;
    }

    public String getIconFileName()
    {
        return icon;
    }

    public String getSoundsDirectory()
    {
        return soundsDirectory;
    }

    public String getSoundPath(String filename)
    {
        return getSoundsDirectory() + filename;
    }
}