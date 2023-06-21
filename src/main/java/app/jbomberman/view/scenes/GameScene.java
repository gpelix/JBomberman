package app.jbomberman.view.scenes;

import app.jbomberman.JBomberManException;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.model.game.Game;
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
	private final HorizontalPanel<Text> topPanel;

	private final Text timeEntry;

	private final Text scoreEntry;

	public GameScene(View view)
	{
		super(view);

		Model model = Model.getInstance();
		model.resetGame();


		Text lives = new Text("left  " + model.getAccount().getCapabilities().getNumberOfLives(), Text.TextColor.Black);
		timeEntry = new Text("", Text.TextColor.Black);
		scoreEntry = new Text("", Text.TextColor.Black);

		topPanel = new HorizontalPanel<>(
			timeEntry,
			scoreEntry,
			lives);

		initialize();
	}

	@Override
	public Drawing update()
	{
		Drawing drawing = Model.getInstance().update();

		scoreEntry.setText(String.valueOf(Model.getInstance().getAccount().getCapabilities().getPointsEarned()));

		Model model = Model.getInstance();

		if (model.getGame().exited()) {
			if (Model.getInstance().getAccount().getCapabilities().getNumberOfLives() >= 0) {
				if (model.getAccount().getLevel() == Game.getLastLevel() + 1) {
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
		Model.getController().attachKeyBoardObserver(this); // Listen for ESC

		topPanel.setTopLeft(Vector2D.of(0, 0));
		topPanel.setWidth(Model.getInstance().getGame().getDimensions().multiply(Entity.SIZE_IN_MODEL).getX());
	}

	@Override
	public void cleanup()
	{
		Controller.controller.detachKeyBoardObserver(this);
	}

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
