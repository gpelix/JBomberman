package app.jbomberman.model.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalMenu;
import app.jbomberman.drawing.MenuEntry;
import app.jbomberman.drawing.Text;
import app.jbomberman.model.Model;
import app.jbomberman.util.Vector2D;

public class PauseScene extends BomberManScene
{
	/**
	 * gameNode is the group that contains the game that has been interrupted
	 */
	private final GameScene gameScene;

	private final HorizontalMenu menu;

	public PauseScene(GameScene gameScene)
	{
		this.gameScene = gameScene;

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

	@Override
	public void cleanup()
	{
		AudioPlayer.getInstance().resumePlaying();
		Model.getController().detachKeyBoardObserver(menu);
	}

	@Override
	public Drawing update()
	{
		return menu.getDrawing();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
