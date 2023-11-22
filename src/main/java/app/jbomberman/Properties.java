/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
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
	 * GraphicLibrary is an enum representing a GraphicLibrary
	 */
	public enum GraphicLibrary
	{
		/**
		 * JAVAFX's constant
		 */
		JAVAFX,
		/**
		 * SWING's constant
		 */
		SWING
	}

	/**
	 * SCALE_FACTOR is the scaling factor we used to create the images
	 */
	private final int scaleFactor;
	/**
	 * the name of the application
	 */
	private final String applicationName;
	/**
	 * the directory that will contain json files
	 */
	private static final String JSON_DIRECTORY = "json/";
	/**
	 * the name of the directory that will contain the images
	 */
	private final String imagesDirectory;
	/**
	 * the name of the directory that will contain the sounds
	 */
	private final String soundsDirectory;
	/**
	 * the basename of the data directory. The dirname must be determined using
	 * DirUtils.getAppData()
	 */
	private final String jbombermanDirectoryDataName;
	/**
	 * the json file that will contain the specifications of the players in the
	 * jbombermanDirectoryDataName
	 */
	private static final String playersFile = "players.json";
	/**
	 * the file that will contain the specification of the entities
	 */
	private static final String characterSpecificationFile = "entitiesSpecs.json";
	/**
	 * the graphic library that will be used
	 */
	private final GraphicLibrary graphicLibrary;
	/**
	 * name of the icon of the application
	 */
	private final String icon;
	/**
	 * the number of powerups available
	 */
	private final int numberOfPowerUps;
	/**
	 * lastLevel is the number of the last level
	 */
	private final int lastLevel;

	/**
	 * SETTINGS_FILE is the file that will contain the settings to be read and
	 * decoded from this class
	 */
	private static final String SETTINGS_FILE =
		JSON_DIRECTORY + "settings.json";

	/**
	 * private constructor that reads and decodes the SETTINGS_FILE
	 */
	private Properties()
	{
		JSONObject settingsJsonRead =
			new JSONObject(Resources.readResource(SETTINGS_FILE));
		scaleFactor = settingsJsonRead.getInt("SCALE_FACTOR");
		lastLevel = settingsJsonRead.getInt("LAST_LEVEL");
		applicationName = settingsJsonRead.getString("APPLICATION_NAME");
		imagesDirectory = settingsJsonRead.getString("IMAGES_DIRECTORY");
		jbombermanDirectoryDataName = settingsJsonRead.getString("JBOMBERMAN_DATA_DIRECTORY_NAME");
		soundsDirectory = settingsJsonRead.getString("SOUNDS_DIRECTORY");
		icon = settingsJsonRead.getString("ICON");
		numberOfPowerUps = settingsJsonRead.getInt("NUMBER_OF_POWERUPS");
		graphicLibrary = settingsJsonRead.getString("GRAPHIC_LIBRARY").equalsIgnoreCase("javafx") ? GraphicLibrary.JAVAFX : GraphicLibrary.SWING;
	}

	/**
	 * instance if the unique instance of the class
	 */
	private static Properties instance;

	/**
	 * getInstance() gets the unique instance of this class
	 *
	 * @return the unique instance of this class
	 */
	public static Properties getInstance()
	{
		if (instance == null) {
			instance = new Properties();
		}
		return instance;
	}

	/**
	 * getGraphicLibrary() gets an enum that specifies which graphic library to
	 * use
	 *
	 * @return an enum that specifies which graphic library to use
	 */
	public GraphicLibrary getGraphicLibrary()
	{
		return graphicLibrary;
	}

	/**
	 * getScaleFactor() gets the scale factor
	 *
	 * @return the scale factor
	 */
	public int getScaleFactor()
	{
		return scaleFactor;
	}

	/**
	 * getApplicationName() gets the name of the application
	 *
	 * @return the name of the application
	 */
	public String getApplicationName()
	{
		return applicationName;
	}

	/**
	 * getImagesDirectory() gets the filepath of the images' directory
	 *
	 * @return the filepath of the images' directory
	 */
	public String getImagesDirectory()
	{
		return imagesDirectory;
	}

	/**
	 * getJbombermanDirectoryDataName() gets the filepath for the players'
	 * specification
	 *
	 * @return the filepath for the players' specification
	 */
	public String getJbombermanDirectoryDataName()
	{
		return jbombermanDirectoryDataName;
	}

	/**
	 * getJsonDirectory() gets the filepath containing the json files
	 *
	 * @return the filepath containing the json files
	 */
	public static String getJsonDirectory()
	{
		return JSON_DIRECTORY;
	}

	/**
	 * getPlayersFile() gets the file containing the players' specifications
	 *
	 * @return the file containing the players' specifications
	 */
	public String getPlayersFile()
	{
		return playersFile;
	}

	/**
	 * getIconFileName() gets the file name of the icon of the program
	 *
	 * @return the file name of the icon of the program
	 */
	public String getIconFileName()
	{
		return icon;
	}

	/**
	 * getSoundsDirectory() gets the directory containing sounds
	 *
	 * @return the directory containing sounds
	 */
	public String getSoundsDirectory()
	{
		return soundsDirectory;
	}

	/**
	 * getNumberOfPowerUps() gets the number of powerups in the game
	 *
	 * @return the number of powerups in the game
	 */
	public int getNumberOfPowerUps()
	{
		return numberOfPowerUps;
	}

	/**
	 * getLastLevel() gets the last level of the game
	 *
	 * @return the last level of the game
	 */
	public int getLastLevel()
	{
		return lastLevel;
	}

	/**
	 * getSoundPath() gets the soundPath
	 *
	 * @param filename the audiofile of which we want the resource path
	 * @return the soundPath of the file
	 */
	public String getSoundPath(String filename)
	{
		return getSoundsDirectory() + filename;
	}

	/**
	 * getCharacterSpecificationFile() gets the getCharacterSpecificationFile
	 *
	 * @return the getCharacterSpecificationFile
	 */
	public String getCharacterSpecificationFile()
	{
		return getJsonDirectory() + characterSpecificationFile;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
