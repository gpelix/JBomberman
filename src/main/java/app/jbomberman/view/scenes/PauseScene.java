package app.jbomberman.view.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.model.Model;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.HorizontalMenu;
import app.jbomberman.view.widgets.MenuEntry;
import app.jbomberman.view.widgets.Text;

/**
 * PauseScene is the scene to display when a game gets suspended
 *
 * @author gp
 */
public class PauseScene extends BomberManScene
{
	/**
	 * menu keeps the HorizontalMenu that has two entries, exit and resume
	 */
	private final HorizontalMenu menu;

	/**
	 * public constructor builds a PauseScene from a View
	 *
	 * @param view the View
	 */
	public PauseScene(View view, GameScene gameScene)
	{
		super(view);

		AudioPlayer.getInstance().pausePlaying();

		menu = new HorizontalMenu(
			new MenuEntry("resume", Text.TextColor.Black, () -> {
				doTransition(() -> gameScene);
				gameScene.resume();
			}),
			new MenuEntry("exit", Text.TextColor.Black, () -> {
				AudioPlayer.getInstance().stopAll();
				doTransition(StartDisplay::new);
			})
		);

		Vector2D modelDims = Model.getInstance().getDimensions();

		menu.setWidth(modelDims.getX());
		menu.setTopLeft(Vector2D.of(0, 0));

		Model.getController().attachKeyBoardObserver(menu);
	}

	/**
	 * cleanup() must be called before transition to a new scene
	 *
	 * @implNote resume playing and detach menu from keyboardcontroller
	 */
	@Override
	public void cleanup()
	{
		AudioPlayer.getInstance().resumePlaying();
		Model.getController().detachKeyBoardObserver(menu);
	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 *
	 * @implNote simply return the drawing of the menu
	 */
	@Override
	public Drawing update()
	{
		return menu.getDrawing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
