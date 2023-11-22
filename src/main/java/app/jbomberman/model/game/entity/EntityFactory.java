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
 
package app.jbomberman.model.game.entity;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.model.game.FullMovementMovingImageManager;
import app.jbomberman.model.game.GameGrid;
import app.jbomberman.model.game.ImageManager;
import app.jbomberman.model.game.LeftRightMovingImageManager;
import app.jbomberman.util.Resources;
import app.jbomberman.util.Vector2D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityFactory is the factory for the Entities.
 * It employs the simple factory pattern as we want to limit the use of JSON
 * in the Model to this class only.
 * It also uses the Singleton pattern as it doesn't make much sense having more
 * EntityFactories
 *
 * @author gp
 */
public class EntityFactory
{
	/**
	 * factory keeps a reference to the unique instance of the EntityFactory
	 */
	private static EntityFactory factory;

	/**
	 * CHARACTER_SPECIFICATION_FILE maintains a reference to a String containing
	 * the path of the json file in which there are the descriptions of the
	 * characteristics of the characters
	 */
	private static final String CHARACTER_SPECIFICATION_FILE =
		Properties.getInstance().getCharacterSpecificationFile();

	/**
	 * Constant String left
	 */
	private static final String LEFT = "left";

	/**
	 * Constant String right
	 */
	private static final String RIGHT = "right";

	/**
	 * Constant String up
	 */
	private static final String UP = "up";

	/**
	 * Constant String down
	 */
	private static final String DOWN = "down";

	/**
	 * Constant String alive
	 */
	private static final String ALIVE = "alive";

	/**
	 * Constant String death
	 */
	private static final String DEATH = "death";

	/**
	 * Constant String speed
	 */
	private static final String SPEED = "speed";

	/**
	 * Constant String goesThroughWalls
	 */
	private static final String GOES_THROUGH_WALLS_KEY = "goesThroughWalls";

	/**
	 * getInstance() must be used to get the unique instance of the EntityFactory
	 *
	 * @return the unique instance of the EntityFactory
	 */
	public static EntityFactory getInstance()
	{
		if (factory == null)
			factory = new EntityFactory();

		return factory;
	}

	/**
	 * entitySpecifications is the JSONObject containing the specifications of the
	 * enemies
	 */
	private final JSONObject entitySpecifications;

	/**
	 * private constructor meant to be called be the getInstance() method to
	 * initialize the unique instance of the EntityFactory
	 */
	private EntityFactory()
	{
		try {
			entitySpecifications =
				new JSONObject(Resources.readResource(CHARACTER_SPECIFICATION_FILE));
		} catch (JSONException e) {
			throw new RuntimeException(
				"JSONException was thrown. Format error in file " + CHARACTER_SPECIFICATION_FILE
			);
		}
	}

	/**
	 * getEnemySpecification() returns the specification of an enemy from its
	 * number
	 *
	 * @param enemyNumber the number specifying the type of the enemy
	 * @return the JSONObject that contains the specification of the Enemy
	 */
	private JSONObject getEnemySpecification(int enemyNumber)
	{
		return entitySpecifications.getJSONObject("Enemy" + enemyNumber);
	}

	/**
	 * buildEnemy() builds an Enemy from a model-coordinate and a reference to the
	 * GameGrid to which it must be added and a number describing the type of
	 * Enemy to build
	 *
	 * @param enemyNumber     the number specifying the type of the Enemy
	 * @param modelCoordinate the model-coordinate
	 * @param gameGrid        a reference to the GameGrid to which the Enemy must be
	 *                        added
	 * @return the newly built Enemy
	 */
	public Enemy buildEnemy(int enemyNumber, Vector2D modelCoordinate,
													GameGrid gameGrid)
	{
		try {
			JSONObject enemySpecificationObject =
				getEnemySpecification(enemyNumber);

			double speed = enemySpecificationObject.getDouble(SPEED);

			boolean goesThroughWalls = enemySpecificationObject.getBoolean(
				GOES_THROUGH_WALLS_KEY);

			List<AbstractImage> deathImages =
				getImagesList(enemySpecificationObject, DEATH);

			int deathAwardingPoints = enemySpecificationObject.getInt("points");

			deathImages.add(FlightWeightImagesFactory.getInstance()
				.getImage(deathAwardingPoints + ".png"));

			return new Enemy(modelCoordinate, speed, goesThroughWalls,
				gameGrid,
				new LeftRightMovingImageManager(
					getImagesList(enemySpecificationObject, LEFT),
					getImagesList(enemySpecificationObject, RIGHT)),
				new ImageManager(deathImages, 0),
				deathAwardingPoints);
		} catch (JSONException e) {
			throw new JBomberManException(
				"Json format error in specificationFile " + e.getMessage());
		}
	}

	/**
	 * buildPowerUp() builds a PowerUp from a model-coordinate and a reference to the
	 * GameGrid to which it must be added and a number describing the type of
	 * PowerUp
	 *
	 * @param powerupNumber   the number specifying the type of the powerup
	 * @param modelCoordinate the model-coordinate
	 * @param gameGrid        a reference to the GameGrid to which the Door must be
	 *                        added
	 * @return the newly built Powerup
	 */
	public Powerup buildPowerUp(int powerupNumber, Vector2D modelCoordinate,
															GameGrid gameGrid)
	{
		try {
			JSONObject powerupSpecification =
				entitySpecifications.getJSONObject("powerup" + powerupNumber);

			int enemyNumber = powerupSpecification.getInt(
				"enemyAfterExplosion");

			AbstractImage image = FlightWeightImagesFactory.getInstance().getImage(
				powerupSpecification.getString("img")
			);

			JSONObject capabilitiesJson = powerupSpecification.getJSONObject(
				"addedCapabilities");

			BomberManCapabilities addedCapabilities = new BomberManCapabilities(
				capabilitiesJson.getDouble("speed"),
				capabilitiesJson.getInt("bombRange"),
				capabilitiesJson.getInt("availableBombs"),
				capabilitiesJson.getBoolean("remoteControl"),
				capabilitiesJson.getBoolean("goesThroughWalls"),
				capabilitiesJson.getBoolean("goesThroughBombs"),
				capabilitiesJson.getBoolean("goesThroughFire"),
				capabilitiesJson.getBoolean("isInvincible"),
				0, // For now no powerup causes an increase of score or an addition of lives
				0
			);

			return new Powerup(modelCoordinate, gameGrid,
				image,
				buildEnemy(enemyNumber, modelCoordinate, gameGrid),
				addedCapabilities);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new JBomberManException(
				"Json format error in specificationFile " + e.getMessage());
		}
	}

	/**
	 * buildDoor() builds a Door from a model-coordinate and a reference to the
	 * GameGrid to which it must be added
	 *
	 * @param modelCoordinate the model-coordinate
	 * @param gameGrid        a reference to the GameGrid to which the Door must be
	 *                        added
	 * @return the newly built Door
	 */
	public Door buildDoor(Vector2D modelCoordinate, GameGrid gameGrid)
	{
		try {
			JSONObject doorSpec = entitySpecifications.getJSONObject("door");

			AbstractImage image = FlightWeightImagesFactory.getInstance().getImage(
				doorSpec.getString("img")
			);

			return new Door(modelCoordinate,
				image,
				gameGrid,
				() -> buildEnemy(2, modelCoordinate, gameGrid));
		} catch (JSONException e) {
			e.printStackTrace();
			throw new JBomberManException(
				"Json format error in specificationFile " + e.getMessage());
		}
	}

	/**
	 * buildWall() builds a Wall from a model-coordinate and a reference to the
	 * GameGrid to which it must be added
	 *
	 * @param modelCoordinate the model-coordinate
	 * @param gameGrid        a reference to the GameGrid to which the Wall must be
	 *                        added
	 * @return the newly built Wall
	 */
	public Wall buildWall(Vector2D modelCoordinate, GameGrid gameGrid)
	{
		try {
			JSONObject wallSpecificationObject =
				entitySpecifications.getJSONObject("wall");

			ImageManager deathImages = new ImageManager(getImagesList(wallSpecificationObject,
				DEATH), 0);

			deathImages.setUpdateInterval(5); // This should be fine

			return new Wall(
				modelCoordinate,
				gameGrid,
				new ImageManager(getImagesList(wallSpecificationObject,
					ALIVE), 0),
				deathImages
			);
		} catch (JSONException e) {
			throw new JBomberManException(
				"Json format error in specificationFile " + e.getMessage());
		}
	}

	/**
	 * buildBomberMan() builds Bomberman from a model-coordinate,
	 * his capabilities, and a reference to the GameGrid to which it must be added
	 *
	 * @param modelCoordinate the model-coordinate
	 * @param capabilities    the capabilities bomberman has
	 * @param gameGrid        a reference to the GameGrid to which the BomberMan must be
	 *                        added
	 * @return the newly built bomberman
	 */
	public BomberMan buildBomberMan(Vector2D modelCoordinate,
																	BomberManCapabilities capabilities,
																	GameGrid gameGrid)
	{
		try {
			JSONObject bomberManSpecification =
				entitySpecifications.getJSONObject("bomberMan");

			return new BomberMan(modelCoordinate, capabilities,
				gameGrid,
				new FullMovementMovingImageManager(
					getImagesList(bomberManSpecification, LEFT),
					getImagesList(bomberManSpecification, RIGHT),
					getImagesList(bomberManSpecification, UP),
					getImagesList(bomberManSpecification, DOWN)),
				new ImageManager(getImagesList(bomberManSpecification, DEATH),
					0)
			);
		} catch (JSONException e) {
			throw new JBomberManException(
				"Json format error in specificationFile" + e.getMessage());
		}
	}

	/**
	 * buildBomb() builds a bomb from the model-coordinate, its range, a reference
	 * to the BomberMan that spawned the Bomb, a reference to the GameGrid to
	 * which it must be added, and a constructor of the Bomb
	 *
	 * @param coordinate  the model-coordinate
	 * @param bombRange   the range of the bomb when it explodes
	 * @param bomberMan   the bomberman that spawned the bomb
	 * @param gameGrid    a reference to the GameGrid to which the Bomb must be
	 *                    added
	 * @param constructor the constructor of the Bomb
	 * @return the newly built bomb
	 */
	public Bomb buildBomb(Vector2D coordinate,
												int bombRange,
												BomberMan bomberMan,
												GameGrid gameGrid,
												Bomb.BombConstructor constructor)
	{
		try {
			JSONArray bombSpecification =
				entitySpecifications.getJSONArray("bomb");

			return constructor.construct(coordinate, bombRange, bomberMan,
				new ImageManager(getImageListFromJsonArray(bombSpecification)
					, 0), gameGrid);
		} catch (JSONException e) {
			throw new JBomberManException(
				"Json format error in specificationFile" + e.getMessage());
		}
	}

	/**
	 * buildFire() builds a fire from its type, model-coordinate and a reference
	 * to the GameGrid to which it must be added
	 *
	 * @param fireType   the type of fire to be built
	 * @param coordinate the model-coordinate
	 * @param gameGrid   a reference to the GameGrid to which the Fire must be
	 *                   added
	 * @return the newly built fire
	 */
	public Fire buildFire(Fire.FireType fireType, Vector2D coordinate,
												GameGrid gameGrid)
	{
		try {
			JSONArray fireSpecification =
				entitySpecifications.getJSONObject("fire").getJSONArray(fireType.toString());

			ImageManager imageManager = new ImageManager(
				getImageListFromJsonArray(fireSpecification), 0);
			imageManager.setUpdateInterval(5); // Half the default time should be ok

			return new Fire(coordinate, gameGrid, imageManager);

		} catch (JSONException e) {
			throw new JBomberManException(
				"Json format error in specificationFile" + e.getMessage());
		}
	}

	/**
	 * getEnemyCapabilities() gets the capabilities of the enemy whose number is
	 * the one specified in input
	 *
	 * @param enemyNumber the number of the enemy to build
	 * @return the capabilities of the enemy to build
	 */
	public GameCharacterCapabilities getEnemyCapabilities(int enemyNumber)
	{
		JSONObject enemySpecificationObject =
			getEnemySpecification(enemyNumber);

		double speed = enemySpecificationObject.getDouble(SPEED);

		boolean goesThroughWalls = enemySpecificationObject.getBoolean(
			GOES_THROUGH_WALLS_KEY);

		return new GameCharacterCapabilities(goesThroughWalls, speed);
	}

	/**
	 * getImagesList is a method to be called internally that gets the List of
	 * abstractImages loaded from the JSONArray in the specification with the key
	 * passed in input
	 *
	 * @param specification the specification of the Entity
	 * @param key           the key to which the list of image is associated
	 * @return the loaded list of AbstractImages
	 */
	private List<AbstractImage> getImagesList(JSONObject specification, String key)
	{
		return getImageListFromJsonArray(specification.getJSONArray(key));
	}

	/**
	 * getImageListFromJsonArray() transforms a JSONArray containing Strings to
	 * representing paths to Images, to a List of AbstractImages
	 *
	 * @param jsonArrayListOfImages the JSONArray containing the paths
	 * @return the List of loaded AbstractImages
	 */
	private List<AbstractImage> getImageListFromJsonArray(JSONArray jsonArrayListOfImages)
	{
		List<AbstractImage> images = new ArrayList<>();

		for (int i = 0; i < jsonArrayListOfImages.length(); i++)
			images.add(FlightWeightImagesFactory.getInstance().getImage(jsonArrayListOfImages.getString(i)));

		return images;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
