package app.jbomberman.view.scenes;

import app.jbomberman.JBomberManException;
import app.jbomberman.Properties;
import app.jbomberman.audio.AudioPlayer;
import app.jbomberman.controller.Controller;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.model.Model;
import app.jbomberman.model.Player;
import app.jbomberman.model.PlayerBuilder;
import app.jbomberman.model.PlayerBuilderException;
import app.jbomberman.util.DirUtils;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Timer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;
import app.jbomberman.view.widgets.HorizontalMenu;
import app.jbomberman.view.widgets.MenuEntry;
import app.jbomberman.view.widgets.Text;
import app.jbomberman.view.widgets.TypingBox;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
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
	 * messagesConsole is a text possibly empty that will contain an error
	 * message if there was an error while logging in with an account or a
	 * problem while creating one
	 */
	private final Text messagesConsole;

	/**
	 * the messageTimer as a timer used to clear the messagesConsole text
	 * after a certain amount of time
	 */
	private final Timer messageTimer;

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

		messageTimer = new Timer(10_000);

		Model model = Model.getInstance();

		Vector2D dimensions = model.getDimensions();

		insertLoginName = new Text("Insert login name...", Text.TextColor.Black,
			Vector2D.of(dimensions.getX() / 2, MARGIN));

		loginBox = new TypingBox(Controller.controller, Text.TextColor.Black, Vector2D.of(
			dimensions.getX() / 2,
			dimensions.getY() / 2
		));

		messagesConsole = new Text(Text.TextColor.Black, Vector2D.of(dimensions.getX() / 2, dimensions.getY() * 2 / 3));

		menu = new HorizontalMenu(
			new MenuEntry("go back", Text.TextColor.Black, () ->
				doTransition(StartDisplay::new)),
			new MenuEntry("sign up", Text.TextColor.Black, () -> {
				String playerName = loginBox.getText();
				try {
					if (playerName.matches("[\\W]*")) {
						setMessage("Specify a valid username");
					} else {
						PlayerBuilder.getInstance().buildPlayer(playerName);
						setMessage("Successful sign up. You can now sign in");
					}
				} catch (PlayerBuilderException e) {
					messagesConsole.setText(e.getMessage());
				} finally {
					messageTimer.start();
				}
			}),
			new MenuEntry("sign in", Text.TextColor.Black, () -> {
				String playerName = loginBox.getText();
				Maybe<Player> player = PlayerBuilder.getInstance().retrievePlayer(playerName);
				if (player.isNothing()) {
					setMessage("Username: " + playerName + " Unable to login.");
				} else {
					setMessage("Successfully signed in");
					model.setAccount(player.fromJust());
				}
			}),
			new MenuEntry("set avatar", Text.TextColor.Black, () -> {
				if (model.getAccount().getPlayerName().isBlank()) {
					setMessage("You have to log in first");
					return;
				}
				Maybe<File> file = view.getFileChooser().getFile();
				if (file.isNothing())
					return;

				File f = file.fromJust();

				if (!checkIfImage(f.getName())) {
					setMessage("The file you specified is not an image");
					return;
				}

				Path avatarDirectory = Path.of(
					DirUtils.getAppData(),
					Properties.getInstance().getJbombermanDirectoryDataName());

				Path finalAvatarPath = Path.of(avatarDirectory.toString(), f.getName());
				try {
					Files.createDirectory(avatarDirectory);
				} catch (FileAlreadyExistsException ignored) {
				} catch (IOException e) {
					throw new JBomberManException("Error while creating avatar directory");
				}

				try {
					Files.copy(Path.of(f.getPath()), finalAvatarPath);
				} catch (FileAlreadyExistsException ignored) {
					LogManager.getLogger(getClass()).info("File already there");
					setMessage("Error while loading avatar");
				} catch (IOException e) {
					throw new JBomberManException("Error while writing configuration files");
				}

				setMessage("Avatar set correctly");
				model.getAccount().setAvatar(finalAvatarPath.getFileName());
			})
		);

		menu.setWidth(dimensions.getX() * 2);
		menu.setTopLeft(
			Vector2D.of(-dimensions.getX() / 2, dimensions.getY() - FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL * 2)
		);

		Controller.controller.attachKeyBoardObserver(menu);

	}

	/**
	 * checkIfImage() checks if the filename named name is a valid image.
	 * It uses the extension to determine if the filename is an image
	 *
	 * @param name the image
	 * @return true if the filename is an image
	 */
	private boolean checkIfImage(String name)
	{
		String nameLowerCase = name.toLowerCase();

		return nameLowerCase.endsWith(".png") ||
			nameLowerCase.endsWith(".jpg") ||
			nameLowerCase.endsWith(".jpeg");
	}

	/**
	 * setMessage copies the msg to the maybeFileNotFound Text container
	 *
	 * @param msg the message to copy
	 */
	private void setMessage(String msg)
	{
		messagesConsole.setText(msg);
		messageTimer.start();
	}

	/**
	 * update() should be called regularly by a timer like AnimationTimer in javafx
	 * package. It should be used to update what gets displayed on the screen
	 */
	@Override
	public Drawing update()
	{
		if (messagesConsole.length() != 0 && messageTimer.hasStarted() && messageTimer.timeIsUp())
			messagesConsole.setText("");

		Drawing drawing = new Drawing();
		Stream.of(menu, loginBox, insertLoginName, messagesConsole)
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
