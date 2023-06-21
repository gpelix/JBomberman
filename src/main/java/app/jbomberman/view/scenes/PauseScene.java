package app.jbomberman.view.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalMenu;
import app.jbomberman.drawing.MenuEntry;
import app.jbomberman.drawing.Text;
import app.jbomberman.model.Model;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;

public class PauseScene extends BomberManScene
{
	private final HorizontalMenu menu;

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
