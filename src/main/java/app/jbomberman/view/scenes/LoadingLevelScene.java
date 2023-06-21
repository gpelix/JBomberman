package app.jbomberman.view.scenes;

import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.Text;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;

/**
 * LoadingLevelScene is the scene loaded before starting the gameplay
 * Its only duty is the one of displaying a little tiny text telling which
 * level the player is going to play and playing a sound
 *
 * @author gp
 */
public class LoadingLevelScene extends SoundyBomberManScene
{
	/**
	 * loadingLevelSceneSound is the sound to be played during this scene
	 */
	private static final String LOADING_LEVEL_SCENE_SOUND =
		Properties.getInstance().getSoundPath("02 Stage Start.wav");

	private final Drawing drawing;

	public LoadingLevelScene(View view)
	{
		super(view);

		AudioPlayer.getInstance().stopAll();

		Model model = Model.getInstance();

		Vector2D dimensions = model.getDimensions();

		Text stageText = new Text("Stage " + model.getAccount().getLevel(), Text.TextColor.Black, dimensions.div(2));
		Text loadingText = new Text("Loading...", Text.TextColor.Black,
			dimensions.sub(
				Vector2D.of(dimensions.getX() / 2,
					FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL * 2)));


		drawing = new Drawing(loadingText.getDrawing(), stageText.getDrawing());
		drawing.setDimensions(dimensions);

		playIfNeeded(LOADING_LEVEL_SCENE_SOUND);
	}


	@Override
	public Drawing update()
	{
		if (!isPlayingSomething())
			doTransition(GameScene::new);

		return drawing;
	}

	@Override
	public void cleanup()
	{
		// This should not be necessary
		AudioPlayer.getInstance().stopAll();
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
