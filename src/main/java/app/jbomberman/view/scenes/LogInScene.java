package app.jbomberman.view.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.model.Account;
import app.jbomberman.model.AccountBuilder;
import app.jbomberman.model.Model;
import app.jbomberman.model.PlayerBuilderException;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.HorizontalMenu;
import app.jbomberman.view.widgets.MenuEntry;
import app.jbomberman.view.widgets.Text;
import app.jbomberman.view.widgets.TypingBox;

import java.util.stream.Stream;

/**
 * LogInScene is the scene to display when a user wants to log in to an account
 *
 * @author gp
 */
public class LogInScene extends BomberManScene
{
	/**
	 * menu is the bottom menu where there are the buttons to login or get back
	 * to the StartScene
	 */
	private final HorizontalMenu menu;

	/**
	 * loginBox is the TypingBox where there will be displayed the typing
	 */
	private final TypingBox loginBox;

	/**
	 * insertLoginName is the text at the top of the window
	 */
	private final Text insertLoginName;

	/**
	 * maybeUnableToLogin is a text possibly empty that will contain an error
	 * message if there was an error while logging in with an account or a
	 * problem while creating one
	 */
	private final Text maybeUnableToLogin;

	/**
	 * the errorMessageTimer as a timer used to clear the maybeUnableToLogin text
	 * after a certain amount of time
	 */
	private final Timer errorMessageTimer;

	/**
	 * MARGIN is the amount of margin that will be used when placing the text
	 * of insertLoginName
	 */
	private static final int MARGIN = 10;

	/**
	 * public constructor builds a LoginScene from a View
	 *
	 * @param view the View
	 */
	public LogInScene(View view)
	{
		super(view);

		AudioPlayer.getInstance().pausePlaying();

		errorMessageTimer = new Timer(10_000);

		Model model = Model.getInstance();

		Vector2D dimensions = model.getDimensions();

		insertLoginName = new Text("Insert login name...", Text.TextColor.Black,
			Vector2D.of(dimensions.getX() / 2, MARGIN));

		loginBox = new TypingBox(Controller.controller, Text.TextColor.Black, Vector2D.of(
			dimensions.getX() / 2,
			dimensions.getY() / 2
		));

		maybeUnableToLogin = new Text(Text.TextColor.Black, Vector2D.of(dimensions.getX() / 2, dimensions.getY() * 2 / 3));

		menu = new HorizontalMenu(
			new MenuEntry("go back", Text.TextColor.Black, () ->
				doTransition(StartDisplay::new)),
			new MenuEntry("sign up", Text.TextColor.Black, () -> {
				String playerName = loginBox.getText();
				try {
					if (playerName.matches("[\\W]*")) {
						maybeUnableToLogin.setText("Specify a valid username");
					} else {
						AccountBuilder.getInstance().buildPlayer(playerName);
						maybeUnableToLogin.setText("Successful sign up. You can now sign in");
					}
				} catch (PlayerBuilderException e) {
					maybeUnableToLogin.setText(e.getMessage());
				} finally {
					errorMessageTimer.start();
				}
			}),
			new MenuEntry("sign in", Text.TextColor.Black, () -> {
				String playerName = loginBox.getText();
				Maybe<Account> player = AccountBuilder.getInstance().retrievePlayer(playerName);
				if (player.isNothing()) {
					maybeUnableToLogin.setText("Username: " + playerName + " Unable to login.");
					errorMessageTimer.start();
				} else {
					model.setAccount(player.fromJust());
					doTransition(StartDisplay::new);
				}
			}));

		menu.setWidth(dimensions.getX());
		menu.setTopLeft(
			Vector2D.of(0, dimensions.getY() - FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL * 2)
		);

		Controller.controller.attachKeyBoardObserver(menu);

	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 */
	@Override
	public Drawing update()
	{
		if (maybeUnableToLogin.length() != 0 && errorMessageTimer.hasStarted() && errorMessageTimer.timeIsUp())
			maybeUnableToLogin.setText("");

		Drawing drawing = new Drawing();
		Stream.of(menu, loginBox, insertLoginName, maybeUnableToLogin)
			.forEach(d -> drawing.merge(d.getDrawing()));

		drawing.setDimensions(Model.getInstance().getDimensions());

		return drawing;
	}

	/**
	 * cleanup() must be called before transition to a new scene
	 * <p>
	 * resume playing and destruct the loginBoard and the menu
	 */
	@Override
	public void cleanup()
	{
		AudioPlayer.getInstance().resumePlaying();
		loginBox.destruct(); // Detach from controller
		Controller.controller.detachKeyBoardObserver(menu);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
