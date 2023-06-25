package app.jbomberman.view.scenes;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.entity.BomberManCapabilities;
import app.jbomberman.model.game.entity.Entity;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardControllerKeyCode;
import app.jbomberman.view.KeyBoardUpdateInfo;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.HorizontalPanel;
import app.jbomberman.view.widgets.Text;

/**
 * GameScene represents a game
 *
 * @author gp
 */
public class GameScene extends SoundyBomberManScene implements Observer
{
	/**
	 * topPanel represents the top panel of the game that will display the time
	 * remaining, the number of lives and the points
	 */
	private final HorizontalPanel<Text> topPanel;

	/**
	 * entry in the topPanel that will keep the time remaining
	 */
	private final Text timeEntry;

	/**
	 * scoreEntry is the entry in the topPanel that will show the score
	 */
	private final Text scoreEntry;

	/**
	 * livesEntry is the entry in the topPanel that will show the available lives
	 */
	private final Text livesEntry;

	/**
	 * LAST_LEVEL is the number of the last level
	 */
	private static final int LAST_LEVEL = Properties.getInstance().getLastLevel();

	/**
	 * public constructor builds a GameScene from a View
	 *
	 * @param view the View
	 */
	public GameScene(View view)
	{
		super(view);

		Model model = Model.getInstance();
		model.resetGame();


		livesEntry = new Text("left  " + model.getAccount().getCapabilities().getNumberOfLives(), Text.TextColor.Black);
		timeEntry = new Text("", Text.TextColor.Black);
		scoreEntry = new Text("", Text.TextColor.Black);

		topPanel = new HorizontalPanel<>(
			timeEntry,
			scoreEntry,
			livesEntry);

		initialize();
	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 *
	 * @implNote return the drawing of the model and add to it the topPanel. It
	 * also updates the topPanel and does transitions to other scenes
	 */
	@Override
	public Drawing update()
	{
		Drawing drawing = Model.getInstance().update();

		BomberManCapabilities capabilities = Model.getInstance().getAccount().getCapabilities();

		scoreEntry.setText(String.valueOf(capabilities.getPointsEarned()));
		livesEntry.setText(String.valueOf(capabilities.getNumberOfLives()));

		Model model = Model.getInstance();

		if (model.getGame().exited()) {
			if (Model.getInstance().getAccount().getCapabilities().getNumberOfLives() >= 0) {
				if (model.getAccount().getLevel() == LAST_LEVEL + 1) {
					doTransition(WinScene::new);
					model.getAccount().setLevel(1);
				} else
					doTransition(LoadingLevelScene::new);
			} else
				doTransition(GameOverScene::new);
		}

		timeEntry.setText("time  " + Math.max(0, model.getGame().secsToEnd()));
		drawing.merge(topPanel.getDrawing());
		return drawing;
	}

	/**
	 * resume() is a function called by the PauseScene to resume the game
	 */
	public void resume()
	{
		initialize();
	}

	/**
	 * initialize() is a utility function that gets called from both resume and setup.
	 * We use this function to avoid code duplication
	 */
	private void initialize()
	{
		Controller.controller.attachKeyBoardObserver(this); // Listen for ESC

		topPanel.setTopLeft(Vector2D.of(0, 0));
		topPanel.setWidth(Model.getInstance().getGame().getDimensions().multiply(Entity.SIZE_IN_MODEL).getX());
	}

	/**
	 * cleanup() must be called before transition to a new scene
	 *
	 * @implNote detaches this from the controller
	 */
	@Override
	public void cleanup()
	{
		Controller.controller.detachKeyBoardObserver(this);
	}

	/**
	 * update(Observable) checks for ESC pressed events. If one is received, it
	 * does a transition to a PauseScene. Otherwise, it forwards the event to the
	 * Model
	 *
	 * @param observable the observable that called the method update.
	 */
	@Override
	public void update(Observable observable)
	{
		if (observable instanceof KeyBoardController keyBoardController) {
			KeyBoardUpdateInfo keyBoardUpdateInfo =
				keyBoardController.getUpdateInfo();

			if (keyBoardUpdateInfo.getKeyBoardEventType() == KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED
				&& keyBoardUpdateInfo.getKeyCode() == KeyBoardControllerKeyCode.ESC)
			{
				doTransition(() -> new PauseScene(getView(), this));
			} else {
				if (!Model.getInstance().getGame().exiting())
					// Forward it to the Model that will forward it to bomberman
					Model.getInstance().keyBoardEvent(keyBoardUpdateInfo);
			}
		} else
			throw new JBomberManException("Unsupported " + observable);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/