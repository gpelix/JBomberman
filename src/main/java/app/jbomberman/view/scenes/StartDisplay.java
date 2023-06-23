package app.jbomberman.view.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.LayeredDrawObject;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.HorizontalMenu;
import app.jbomberman.view.widgets.MenuEntry;
import app.jbomberman.view.widgets.Text;

/**
 * StartDisplay is the first scene of the Application.
 *
 * @author gp
 */
public class StartDisplay extends SoundyBomberManScene
{
	/**
	 * startDisplaySound is the soundtrack to play during the scene
	 */
	private static final String startDisplaySound = Properties.getInstance().getSoundPath("01 Title Screen.wav");

	/**
	 * MENU_POSITION is the position of the menu relative to the background image
	 */
	private static final double MENU_POSITION = 21. / 32;

	/**
	 * menu will keep the Menu to start a game, log in or exit
	 */
	private final HorizontalMenu menu;

	/**
	 * backgroundLayeredDrawObject is the LayeredObject that will keep the underlying
	 * image
	 */
	private final LayeredDrawObject backgroundLayeredDrawObject;

	/**
	 * public constructor builds a StartDisplay from a View
	 *
	 * @param view the View
	 */
	public StartDisplay(View view)
	{
		super(view);

		AbstractImage startImage = FlightWeightImagesFactory.getInstance().getImage(
			"StartDisplay.png");

		Model model = Model.getInstance();
		Vector2D modelDimensions = model.getDimensions();

		int width = modelDimensions.getX();
		int height = modelDimensions.getY();

		menu = new HorizontalMenu(
			new MenuEntry("Start", Text.TextColor.Black,
				() -> doTransition(LoadingLevelScene::new)),
			new MenuEntry("Log In", Text.TextColor.Black,
				() -> doTransition(LogInScene::new)),
			new MenuEntry("Exit", Text.TextColor.Black, model::close)
		);
		menu.setTopLeft(Vector2D.of(0, (int) (height * MENU_POSITION)));
		menu.setWidth(width);

		Model.getController().attachKeyBoardObserver(menu);

		backgroundLayeredDrawObject = new LayeredDrawObject(Vector2D.of(0, 0), startImage, 0);
	}

	/**
	 * cleanup() must be called before transition to a new scene
	 *
	 * @implNote detach the menu from the controller and stop every sound
	 */
	@Override
	public void cleanup()
	{
		Controller.controller.detachKeyBoardObserver(menu);
		AudioPlayer.getInstance().stopAll();
	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 *
	 * @implNote It loops and, if the audioHandle is not playing something, it plays
	 * the startDisplaySound
	 */
	@Override
	public Drawing update()
	{
		getAudioHandle().playIfNeeded(startDisplaySound);

		Drawing drawing = new Drawing(backgroundLayeredDrawObject);
		drawing.merge(menu.getDrawing(), x -> x + 1);

		return drawing;
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
