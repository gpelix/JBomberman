package app.jbomberman.controller;

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
 * KeyBoardController is the class responsible for the handling the keyboard
 * input that comes from the user.
 * <p>
 * KeyBoardController communicates with other class the keystrokes using the
 * observer/Observable pattern.
 *
 * @author gp
 */
public class KeyBoardController implements Observable
{
    /**
     * KeyBoardControllerKeyCode is an enumeration of the currently available
     * keys that this class can handle. Since this class is a sort of abstraction
     * layer between the specific (now it's javafx) implementation and another
     * representation, these codes are the new representation.
     *
     * @author gp
     */
    public enum KeyBoardControllerKeyCode
    {
        LEFT(),
        RIGHT(),
        UP(),
        DOWN(),
        ESC(),
        ENTER(),
        DEL(),
        BACK_SPACE(),
        SPACE(' '),
        A('a'),
        B('b'),
        C('c'),
        D('d'),
        E('e'),
        F('f'),
        G('g'),
        H('h'),
        I('i'),
        J('j'),
        K('k'),
        L('l'),
        M('m'),
        N('n'),
        O('o'),
        P('p'),
        Q('q'),
        R('r'),
        S('s'),
        T('t'),
        U('u'),
        V('v'),
        W('w'),
        X('x'),
        Y('y'),
        Z('z'),

        /**
         * OTHER represents an unknown character (thus it's not one of the ones
         * listed above)
         */
        OTHER();

        /**
         * repr is nothing if the character is not printable
         * repr is just if it's printable, and the content of the just is the
         * character representation of the code
         */
        private final Maybe<Character> repr;

        /**
         * KeyBoardControllerKeyCode() initializes the enum given its character
         * representation
         *
         * @param repr the character representation of the enum
         */
        KeyBoardControllerKeyCode(char repr)
        {
            this.repr = Maybe.just(repr);
        }

        /**
         * KeyBoardControllerKeyCode() must be used to initialize the enum
         * constants that have no associated symbol
         */
        KeyBoardControllerKeyCode()
        {
            this.repr = Maybe.nothing();
        }

        /**
         * fromJavaFXKeyCode is a utility function that provides the mapping
         * between javafx KeyCode's and KeyBoardControllerKeyCode
         *
         * @param keyCode the javafx KeyCode
         * @return the KeyBoardControllerKeyCode associated with that
         */
        private static KeyBoardControllerKeyCode fromJavaFXKeyCode(KeyCode keyCode)
        {
            return switch (keyCode) {
                case DOWN -> DOWN;
                case UP -> UP;
                case LEFT -> LEFT;
                case RIGHT -> RIGHT;
                case ESCAPE -> ESC;
                case ENTER -> ENTER;
                case SPACE -> SPACE;
                case BACK_SPACE -> BACK_SPACE;
                case DELETE -> DEL;
                case A -> A;
                case B -> B;
                case C -> C;
                case D -> D;
                case E -> E;
                case F -> F;
                case G -> G;
                case H -> H;
                case I -> I;
                case J -> J;
                case K -> K;
                case L -> L;
                case M -> M;
                case N -> N;
                case O -> O;
                case P -> P;
                case Q -> Q;
                case R -> R;
                case S -> S;
                case T -> T;
                case U -> U;
                case V -> V;
                case W -> W;
                case X -> X;
                case Y -> Y;
                case Z -> Z;
                default -> OTHER;
            };
        }

        /**
         * isPrintable() checks whether a character is printable or not
         *
         * @return true if the character is printable otherwise false
         */
        public boolean isPrintable()
        {
            return repr.isJust();
        }

        /**
         * getRepr() returns the Maybe that might contain the representation
         * of the KeyBoardControllerKeyCode
         *
         * @return the Maybe that might contain the representation
         * of the KeyBoardControllerKeyCode
         */
        public Maybe<Character> getRepr()
        {
            return repr;
        }
    }

    /**
     * KeyBoardUpdateInfo is a class meant to be accessed by the clients/observers
     * to gather information about the keyboard state.
     *
     * @author gp
     */
    public static class KeyBoardUpdateInfo
    {
        /**
         * KeyBoardEventType is an enum that says whether the event was caused by
         * the release or by pressing a key
         */
        public enum KeyBoardEventType
        {
            KEY_PRESSED,
            KEY_RELEASED
        }

        /**
         * keyBoardEventType is the KeyBoardEventType that describes the event
         */
        private final KeyBoardEventType keyBoardEventType;
        /**
         * keyCode is the KeyBoardControllerKeyCode of the key that triggered the
         * event
         */
        private final KeyBoardControllerKeyCode keyCode;
        /**
         * currentlyPressed is a reference to the set of currently pressed keys
         */
        private final Set<KeyBoardControllerKeyCode> currentlyPressed;

        /**
         * KeyBoardUpdateInfo() is used to initialize a KeyBoardUpdateInfo from
         * the KeyBoardController
         *
         * @param keyBoardEventType the type of KeyBoardEvent
         * @param keyCode           the code of the key that triggered the event
         * @param pressedKeyCodes   the set of currently pressed codes
         */
        private KeyBoardUpdateInfo(KeyBoardEventType keyBoardEventType,
                                   KeyBoardControllerKeyCode keyCode,
                                   Set<KeyBoardControllerKeyCode> pressedKeyCodes)
        {
            this.keyBoardEventType = keyBoardEventType;
            this.keyCode = keyCode;
            currentlyPressed = pressedKeyCodes;
        }

        /**
         * getKeyBoardEventType() return the keyBoardEventType
         *
         * @return the keyBoardEventType
         */
        public KeyBoardEventType getKeyBoardEventType()
        {
            return keyBoardEventType;
        }

        /**
         * getKeyCode() returns the keyCode
         *
         * @return the keyCode
         */
        public KeyBoardControllerKeyCode getKeyCode()
        {
            return keyCode;
        }

        /**
         * getCurrentlyPressed() gets the currentlyPressed
         *
         * @return the currently pressed key codes
         */
        public Set<KeyBoardControllerKeyCode> getCurrentlyPressed()
        {
            return currentlyPressed;
        }
    }

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
     * KeyBoardController has a Set of observers that will be notified in case
     * of the detection of a keyboard related event
     */
    private final Set<Observer> observers;

    /**
     * updateInfo contains a reference to the most recent keyboard event
     */
    private Maybe<KeyBoardUpdateInfo> updateInfo;

    /**
     * KeyBoardController
     */
    public KeyBoardController()
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
