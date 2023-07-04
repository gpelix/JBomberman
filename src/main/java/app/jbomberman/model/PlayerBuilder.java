package app.jbomberman.model;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.util.DirUtils;
import app.jbomberman.util.Maybe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * PlayerBuilder is a class that is in charge of building the Player with some
 * defaults.
 * <p>
 * We use the singleton pattern as we have no interest in creating more
 * instances of an PlayerBuilder
 *
 * @author gp
 */
public class PlayerBuilder
{
	/**
	 * instance is the only unique instance of the PlayerBuilder
	 */
	private static PlayerBuilder instance;

	/**
	 * LEVEL is the string that will be mapped to the level number in the json
	 * file
	 */
	private static final String LEVEL = "level",
	/**
	 * POINTS_EARNED is the string that will be mapped to the points earned in the
	 * json file
	 */
	POINTS_EARNED = "pointEarned",
	/**
	 * SPEED is the string that will be mapped to the speed in the json file
	 */
	SPEED = "speed",
	/**
	 * BOMB_POWER is the string that will be mapped to the bomb power of the
	 * account in the json file
	 */
	BOMB_POWER = "bomberPower",
	/**
	 * AVAILABLE_BOMBS is the string that will be mapped to the available bombs of
	 * the account in the json file
	 */
	AVAILABLE_BOMBS = "availableBombs",
	/**
	 * REMOTE_CONTROL is the string that will be mapped to the boolean
	 * representing whether the bomberman will be able to use remote control in
	 * the account in the json file
	 */
	REMOTE_CONTROL = "removeControl",
	/**
	 * GOES_THROUGH_WALLS is the string that will be mapped to the boolean
	 * representing whether the bomberman will be able to pass through walls in
	 * the account in the json file
	 */
	GOES_THROUGH_WALLS = "goesThroughWalls",
	/**
	 * GOES_THROUGH_BOMBS is the string that will be mapped to a boolean that
	 * represents whether the bomberman can go through bombs in the json file
	 */
	GOES_THROUGH_BOMBS = "goesThroughBombs",
	/**
	 * GOES_THROUGH_FIRE is the string that will be mapped to a boolean that
	 * represents whether the bomberman can go through fire in the json file
	 */
	GOES_THROUGH_FIRE = "goesThroughfire",
	/**
	 * IS_INVINCIBLE is the string that will be mapped to a boolean representing
	 * whether the account is invincible in the json file
	 */
	IS_INVINCIBLE = "isInvincible",
	/**
	 * NO_OF_LIVES is the string that will be mapped to the number of lives of
	 * the account in the json file
	 */
	NO_OF_LIVES = "numberOfLives",
	/**
	 * NO_OF_LIVES is the string that will be mapped to the path of the avatar image
	 */
	AVATAR_PATH = "avatarPath";

	/**
	 * START_LEVEL is the start level = 1
	 */
	private static final int START_LEVEL = 1;

	/**
	 * getInstance() gets the single instance of the PlayerBuilder
	 *
	 * @return the single instance of the PlayerBuilder
	 */
	public static PlayerBuilder getInstance()
	{
		if (instance == null)
			instance = new PlayerBuilder();
		return instance;
	}

	/**
	 * private empty constructor to avoid the creation of other PlayerBuilder
	 * instances
	 */
	private PlayerBuilder()
	{
	}

	/**
	 * getFilePath() gets the path of the settings file
	 *
	 * @return the path of the settings file
	 */
	private Path getFilePath()
	{
		return Path.of(DirUtils.getAppData(), Properties.getInstance().getJbombermanDirectoryDataName(), Properties.getInstance().getPlayersFile());
	}

	/**
	 * getPlayersJson() gets the JSONObject that has been created from the read
	 * settings file
	 *
	 * @return the JSONObject that has been created from the read settings
	 * file
	 */
	private JSONObject getPlayersJson()
	{
		try {
			String appdata = DirUtils.getAppData();

			Path jbombermanInAppdata = Path.of(appdata, Properties.getInstance().getJbombermanDirectoryDataName());

			if (!Files.exists(jbombermanInAppdata))
				Files.createDirectory(jbombermanInAppdata);

			Path jsonFilePath = Path.of(String.valueOf(jbombermanInAppdata), Properties.getInstance().getPlayersFile());

			if (!Files.exists(jsonFilePath))
				try (OutputStream o = Files.newOutputStream(jsonFilePath)) {
					o.write("{}".getBytes());
				}

			return new JSONObject(Files.readString(jsonFilePath));
		} catch (IOException e) {
			throw new JBomberManException(e.getMessage());
		}
	}

	/**
	 * retrievePlayer() gets the player from the JSON Object loaded from the
	 * settings file
	 *
	 * @param playerName the name of the player
	 * @return the account
	 */
	public Maybe<Player> retrievePlayer(String playerName)
	{
		JSONObject jsonPlayers;

		jsonPlayers = getPlayersJson();

		if (!jsonPlayers.has(playerName))
			return Maybe.nothing();

		JSONObject playerStats = jsonPlayers.getJSONObject(playerName);

		Maybe<Path> avatarPath = Maybe.nothing();
		try {
			avatarPath = Maybe.just(Path.of(playerStats.getString(AVATAR_PATH)));
		} catch (JSONException ignored) {
			// It was null
		}

		return Maybe.just(new Player(
			playerName,
			playerStats.getInt(LEVEL),
			new BomberManCapabilities(
				playerStats.getDouble(SPEED),
				playerStats.getInt(BOMB_POWER),
				playerStats.getInt(AVAILABLE_BOMBS),
				playerStats.getBoolean(REMOTE_CONTROL),
				playerStats.getBoolean(GOES_THROUGH_WALLS),
				playerStats.getBoolean(GOES_THROUGH_BOMBS),
				playerStats.getBoolean(GOES_THROUGH_FIRE),
				false, // Invincibility cannot be inherited
				playerStats.getInt(POINTS_EARNED),
				playerStats.getInt(NO_OF_LIVES),
				avatarPath
			)
		));
	}

	/**
	 * buildPlayer() creates a player with the default settings and writes it
	 * to the file
	 *
	 * @param playerName the name of the player to be built
	 */
	public void buildPlayer(String playerName)
	{
		JSONObject playersJSON = getPlayersJson();

		if (playersJSON.has(playerName))
			throw new PlayerBuilderException(playerName + " already exists. Unable to create again");

		savePlayer(getDefaultAccount(playerName));
	}

	/**
	 * savePlayer() encodes the account to a JSONObject structure and writes it
	 * to the settings-file
	 *
	 * @param account the account to be saved
	 */
	public void savePlayer(Player account)
	{
		// If the player is the default one
		if (account.getPlayerName().matches("^\\W*"))
			return;

		JSONObject playerJSON = getPlayersJson();

		String playerName = account.getPlayerName();

		JSONObject player;

		if (playerJSON.has(playerName))
			player = playerJSON.getJSONObject(playerName);
		else {
			player = new JSONObject();
			playerJSON.put(playerName, player);
		}

		BomberManCapabilities cap = account.getCapabilities();

		player.put(LEVEL, account.getLevel());
		player.put(POINTS_EARNED, cap.getPointsEarned());
		player.put(NO_OF_LIVES, cap.getNumberOfLives());
		player.put(SPEED, cap.getSpeed());
		player.put(BOMB_POWER, cap.getBombPower());
		player.put(AVAILABLE_BOMBS, cap.getAvailableBombs());
		player.put(REMOTE_CONTROL, cap.hasRemoteControl());
		player.put(GOES_THROUGH_WALLS, cap.goesThroughWalls());
		player.put(GOES_THROUGH_BOMBS, cap.goesThroughBombs());
		player.put(GOES_THROUGH_FIRE, cap.goesThroughFire());
		player.put(IS_INVINCIBLE, cap.isInvincible());
		cap.getAvatar().doOrDo(
			ava -> player.put(AVATAR_PATH, ava.toString()),
			() -> player.put(AVATAR_PATH, JSONObject.NULL)
		);

		saveFile(playerJSON);
	}

	/**
	 * saveFile() must be called internally to write the JSONObject provided in
	 * input to the settings file
	 *
	 * @param obj the JSONObject to write to the settings-file
	 */
	private void saveFile(JSONObject obj)
	{
		try {
			Files.writeString(getFilePath(), obj.toString(2));
		} catch (IOException e) {
			e.printStackTrace();
			throw new JBomberManException("Unable to write settings file " + getFilePath() + " " + e.getMessage());
		}
	}

	/**
	 * getDefaultAccount() creates a default empty account
	 *
	 * @param playerName the name of the Player
	 * @return the built account
	 */
	public Player getDefaultAccount(String playerName)
	{
		return new Player(
			playerName,
			START_LEVEL,
			BomberManCapabilities.defaultCapabilities);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
