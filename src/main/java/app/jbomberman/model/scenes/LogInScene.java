
package app.jbomberman.model.scenes;

import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
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

public class LogInScene extends BomberManScene
{
	private Maybe<HorizontalMenu> menu;
	private Maybe<TypingBox> loginBox;
	private Maybe<Text> insertLoginName;
	private Maybe<Text> maybeUnableToLogin;
	private final Timer errorMessageTimer;

	private static final int MARGIN = 10;


	private final Model model;

	private final Controller controller;


	public LogInScene(Controller controller, Model model)
	{
		super(model);

		AudioPlayer.getInstance().pausePlaying();

		this.model = model;
		this.controller = controller;
		errorMessageTimer = new Timer(10_000);
	}

	@Override
	public void setup()
	{

		Vector2D dimensions = model.getDimensions().unwrap();

		insertLoginName = Maybe.just(
			new Text("Insert login name...", true,
				Vector2D.of(dimensions.getX() / 2, MARGIN))
		);


		loginBox = Maybe.just(
			new TypingBox(controller, true, Vector2D.of(
				dimensions.getX() / 2,
				dimensions.getY() / 2
			))
		);

		maybeUnableToLogin = Maybe.just(
			new Text(true, Vector2D.of(dimensions.getX() / 2, dimensions.getY() * 2 / 3))
		);

		menu = Maybe.just(new HorizontalMenu(
			new MenuEntry("go back", true, () ->
				doTransition(new StartDisplay(controller, model))),
			new MenuEntry("sign up", true, () -> {
				String playerName = loginBox.unwrap().getText();
				try {
					AccountBuilder.getInstance().buildPlayer(playerName);
					maybeUnableToLogin.unwrap().setText("Successful sign up. You can now sign in");
				} catch (PlayerBuilderException e) {
					maybeUnableToLogin.unwrap().setText(e.getMessage());
				} finally {
					errorMessageTimer.start();
				}
			}),
			new MenuEntry("sign in", true, () -> {
				String playerName = loginBox.unwrap().getText();
				Maybe<Account> player = AccountBuilder.getInstance().retrievePlayer(playerName);
				if (player.isNothing()) {
					maybeUnableToLogin.unwrap().setText("Username: " + playerName + " Unable to login.");
					errorMessageTimer.start();
				} else {
					model.setAccount(player.unwrap());
					doTransition(new StartDisplay(controller, model));
				}
			})
		));

		menu.unwrap().setWidth(dimensions.getX());
		menu.unwrap().setTopLeft(
			Vector2D.of(0, dimensions.getY() - FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL * 2)
		);

		controller.attachKeyBoardObserver(menu.unwrap());

	}

	@Override
	public Drawing update()
	{
		if (maybeUnableToLogin.unwrap().length() != 0 && errorMessageTimer.hasStarted() && errorMessageTimer.timeIsUp()) {
			maybeUnableToLogin.unwrap().setText("");
		}
		Drawing drawing = menu.unwrap().getDrawing();
		drawing.merge(loginBox.unwrap().getDrawing());
		drawing.merge(insertLoginName.unwrap().getDrawing());
		drawing.merge(maybeUnableToLogin.unwrap().getDrawing());

		drawing.setDimensions(model.getDimensions().unwrap());

		return drawing;
	}

	@Override
	public void cleanup()
	{
		AudioPlayer.getInstance().resumePlaying();
		controller.detachKeyBoardObserver(menu.unwrap());
	}
}
