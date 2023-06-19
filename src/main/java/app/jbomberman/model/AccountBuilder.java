package app.jbomberman.model;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.util.DirUtils;
import app.jbomberman.util.Maybe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AccountBuilder
{
	private static AccountBuilder instance;

	private static final String LEVEL = "level",
		POINTS_EARNED = "pointEarned",
		SPEED = "speed",
		BOMB_POWER = "bomberPower",
		AVAILABLE_BOMS = "availableBombs",
		REMOTE_CONTROL = "removeControl",
		GOES_THROUGH_WALLS = "goesThroughWalls",
		GOES_THROUGH_BOMBS = "goesThroughBombs",
		GOES_THROUGH_FIRE = "goesThroughfire",
		IS_INVINCIBLE = "isInvincible",
		NO_OF_LIVES = "numberOfLives";

	private static final int START_LEVEL = 1;

	private Logger logger;

	public static AccountBuilder getInstance()
	{
		if (instance == null)
			instance = new AccountBuilder();
		return instance;
	}

	private AccountBuilder()
	{
		logger = LogManager.getLogger(getClass());
	}

	private Path getFilePath()
	{
		return Path.of(DirUtils.getAppData(), Properties.getInstance().getJbombermanDirectoryDataName(), Properties.getInstance().getPlayersFile());
	}

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

	public Maybe<Account> retrievePlayer(String playerName)
	{
		JSONObject jsonPlayers;

		jsonPlayers = getPlayersJson();

		if (!jsonPlayers.has(playerName))
			return Maybe.nothing();

		JSONObject playerStats = jsonPlayers.getJSONObject(playerName);

		return Maybe.just(new Account(
			playerName,
			playerStats.getInt(LEVEL),
			new BomberManCapabilities(
				playerStats.getDouble(SPEED),
				playerStats.getInt(BOMB_POWER),
				playerStats.getInt(AVAILABLE_BOMS),
				playerStats.getBoolean(REMOTE_CONTROL),
				playerStats.getBoolean(GOES_THROUGH_WALLS),
				playerStats.getBoolean(GOES_THROUGH_BOMBS),
				playerStats.getBoolean(GOES_THROUGH_FIRE),
				false, // Invincibility cannot be inherited
				playerStats.getInt(POINTS_EARNED),
				playerStats.getInt(NO_OF_LIVES)
			)
		));
	}

	public void buildPlayer(String playerName)
	{
		JSONObject playersJSON = getPlayersJson();

		if (playersJSON.has(playerName))
			throw new PlayerBuilderException(playerName + " already exists. Unable to create again");

		//savePlayer(getDefaultAccount(playerName)); WHAT WAS THIS FOR?
	}

	public void savePlayer(Account account)
	{
		// If the player is the default one
		if (account.getPlayerName().equals(""))
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
		player.put(AVAILABLE_BOMS, cap.getAvailableBombs());
		player.put(REMOTE_CONTROL, cap.hasRemoveControl());
		player.put(GOES_THROUGH_WALLS, cap.goesThroughWalls());
		player.put(GOES_THROUGH_BOMBS, cap.goesThroughBombs());
		player.put(GOES_THROUGH_FIRE, cap.goesThroughFire());
		player.put(IS_INVINCIBLE, cap.isInvincible());

		saveFile(playerJSON);
	}

	private void saveFile(JSONObject obj)
	{
		try {
			Files.writeString(getFilePath(), obj.toString(2));
		} catch (IOException e) {
			e.printStackTrace();
			throw new JBomberManException("Unable to write settings file " + getFilePath() + " " + e.getMessage());
		}
	}

	public Account getDefaultAccount(String playerName)
	{
		return new Account(
			playerName,
			START_LEVEL,
			BomberManCapabilities.defaultCapabilities);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
