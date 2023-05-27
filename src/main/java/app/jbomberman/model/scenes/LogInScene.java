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

public class LogInScene extends BomberManScene
{
    private Maybe<HorizontalMenu> menu;
    private Maybe<TypingBox> loginBox;
    private Maybe<Text> insertLoginName;
    private Maybe<Text> maybeUnableToLogin;
    private final Timer errorMessageTimer;

    private static final int MARGIN = 10;

    public LogInScene()
    {
        AudioPlayer.getInstance().pausePlaying();

        errorMessageTimer = new Timer(10_000);
    }

    @Override
    public void setup()
    {

        Model model = Model.getInstance();

        Vector2D dimensions = model.getDimensions();

        insertLoginName = Maybe.just(
            new Text("Insert login name...", true,
                Vector2D.of(dimensions.getX() / 2, MARGIN))
        );


        loginBox = Maybe.just(
            new TypingBox(Model.getController(), true, Vector2D.of(
                dimensions.getX() / 2,
                dimensions.getY() / 2
            ))
        );

        maybeUnableToLogin = Maybe.just(
            new Text(true, Vector2D.of(dimensions.getX() / 2, dimensions.getY() * 2 / 3))
        );

        menu = Maybe.just(new HorizontalMenu(
            new MenuEntry("go back", true, () ->
                doTransition(new StartDisplay())),
            new MenuEntry("sign up", true, () -> {
                String playerName = loginBox.fromJust().getText();
                try {
                    AccountBuilder.getInstance().buildPlayer(playerName);
                    maybeUnableToLogin.fromJust().setText("Successful sign up. You can now sign in");
                } catch (PlayerBuilderException e) {
                    maybeUnableToLogin.fromJust().setText(e.getMessage());
                } finally {
                    errorMessageTimer.start();
                }
            }),
            new MenuEntry("sign in", true, () -> {
                String playerName = loginBox.fromJust().getText();
                Maybe<Account> player = AccountBuilder.getInstance().retrievePlayer(playerName);
                if (player.isNothing()) {
                    maybeUnableToLogin.fromJust().setText("Username: " + playerName + " Unable to login.");
                    errorMessageTimer.start();
                } else {
                    model.setAccount(player.fromJust());
                    doTransition(new StartDisplay());
                }
            })
        ));

        menu.fromJust().setWidth(dimensions.getX());
        menu.fromJust().setTopLeft(
            Vector2D.of(0, dimensions.getY() - FlightWeightImagesFactory.CHARACTER_SIZE_IN_PIXEL * 2)
        );

        Model.getController().attachKeyBoardObserver(menu.fromJust());

    }

    @Override
    public Drawing update()
    {
        if (maybeUnableToLogin.fromJust().length() != 0 && errorMessageTimer.hasStarted() && errorMessageTimer.timeIsUp()) {
            maybeUnableToLogin.fromJust().setText("");
        }
        Drawing drawing = menu.fromJust().getDrawing();
        drawing.merge(loginBox.fromJust().getDrawing());
        drawing.merge(insertLoginName.fromJust().getDrawing());
        drawing.merge(maybeUnableToLogin.fromJust().getDrawing());

        drawing.setDimensions(Model.getInstance().getDimensions());

        return drawing;
    }

    @Override
    public void cleanup()
    {
        AudioPlayer.getInstance().resumePlaying();
        loginBox.fromJust().destruct(); // Detach from controller
        Model.getController().detachKeyBoardObserver(menu.fromJust());
    }
}
