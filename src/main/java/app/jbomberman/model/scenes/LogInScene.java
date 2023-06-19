package app.jbomberman.model.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.HorizontalMenu;
import app.jbomberman.drawing.MenuEntry;
import app.jbomberman.drawing.Text;
import app.jbomberman.drawing.TypingBox;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.model.Account;
import app.jbomberman.model.AccountBuilder;
import app.jbomberman.model.Model;
import app.jbomberman.model.PlayerBuilderException;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;

import java.util.stream.Stream;

public class LogInScene extends BomberManScene
{
	private final HorizontalMenu menu;
	private final TypingBox loginBox;
	private final Text insertLoginName;
	private final Text maybeUnableToLogin;
	private final Timer errorMessageTimer;

	private static final int MARGIN = 10;

	public LogInScene()
	{
		AudioPlayer.getInstance().pausePlaying();

		errorMessageTimer = new Timer(10_000);

		Model model = Model.getInstance();

		Vector2D dimensions = model.getDimensions();

		insertLoginName = new Text("Insert login name...", Text.TextColor.Black,
			Vector2D.of(dimensions.getX() / 2, MARGIN));

		loginBox = new TypingBox(Model.getController(), Text.TextColor.Black, Vector2D.of(
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
					AccountBuilder.getInstance().buildPlayer(playerName);
					maybeUnableToLogin.setText("Successful sign up. You can now sign in");
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

		Model.getController().attachKeyBoardObserver(menu);

	}

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

	@Override
	public void cleanup()
	{
		AudioPlayer.getInstance().resumePlaying();
		loginBox.destruct(); // Detach from controller
		Model.getController().detachKeyBoardObserver(menu);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
