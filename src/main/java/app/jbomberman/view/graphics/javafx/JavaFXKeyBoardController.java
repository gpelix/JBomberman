package app.jbomberman.view;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JavaFXKeyBoardController is the class responsible for the handling the keyboard
 * input that comes from the user.
 * <p>
 * JavaFXKeyBoardController communicates with other class the keystrokes using the
 * observer/Observable pattern.
 *
 * @author gp
 */
public class JavaFXKeyBoardController implements Observable
{
    /**
     * Set of KeyCodes currently pressed
     */
    private final HashSet<KeyCode> pressedKeyCodes;
    /**
     * pressedHandler is an EventHandler that will get attached to the javafx scene
     * we'll need to listen to
     */
    private final EventHandler<KeyEvent> pressedHandler;
    /**
     * pressedHandler is an EventHandler that will get attached to the javafx scene
     * we'll need to listen to
     */
    private final EventHandler<KeyEvent> releasedHandler;

    /**
     * JavaFXKeyBoardController has a Set of observers that will be notified in case
     * of the detection of a keyboard related event
     */
    private final Set<Observer> observers;

    /**
     * updateInfo contains a reference to the most recent keyboard event
     */
    private Maybe<KeyBoardUpdateInfo> updateInfo;

    /**
     * JavaFXKeyBoardController
     */
    public JavaFXKeyBoardController()
    {
        pressedKeyCodes = new HashSet<>();

        pressedHandler = event -> {
            pressedKeyCodes.add(event.getCode());

            updateInfo = Maybe.just(new KeyBoardUpdateInfo(
                KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED,
                KeyBoardControllerKeyCode.fromJavaFXKeyCode(event.getCode()),
                pressedKeyCodes.stream()
                    .map(KeyBoardControllerKeyCode::fromJavaFXKeyCode)
                    .collect(Collectors.toCollection(HashSet::new))));

            updateAllObservers();
        };

        releasedHandler = event -> {
            pressedKeyCodes.remove(event.getCode());

            updateInfo = Maybe.just(new KeyBoardUpdateInfo(
                KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED,
                KeyBoardControllerKeyCode.fromJavaFXKeyCode(event.getCode()),
                pressedKeyCodes.stream()
                    .map(KeyBoardControllerKeyCode::fromJavaFXKeyCode)
                    .collect(Collectors.toCollection(HashSet::new))
            ));

            updateAllObservers();
        };

        observers = new HashSet<>();
    }

    /**
     * attachToScene() must be called to set up the pressed and released handler
     * and bind them to the provided scene
     *
     * @param scene the scene to which the handlers must be bound
     */
    public void attachToScene(Scene scene)
    {
        scene.setOnKeyPressed(pressedHandler);
        scene.setOnKeyReleased(releasedHandler);
    }

    /**
     * detachFromScene() must be called to detach the handlers from the scene
     * provided in input
     *
     * @param scene the scene to which the handlers must be released
     */
    public void detachFromScene(Scene scene)
    {
        scene.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        scene.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
    }

    @Override
    public Collection<Observer> getObservers()
    {
        return observers;
    }

    /**
     * getUpdateInfo() must be called by the observer upon notification to get
     * information about the event that triggered the notification
     *
     * @return the KeyBoardUpdateInfo represent the event
     */
    public KeyBoardUpdateInfo getUpdateInfo()
    {
        return updateInfo.unwrapOrThrow(() -> new JBomberManException(
            "Called update but there are no update info"));
    }
}
