package app.jbomberman.controller;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Direction;
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

import static app.jbomberman.util.Direction.EAST;
import static app.jbomberman.util.Direction.NORTH;
import static app.jbomberman.util.Direction.SOUTH;
import static app.jbomberman.util.Direction.WEST;

public class KeyBoardController implements Observable
{
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

        OTHER();

        private final Maybe<Character> repr;

        KeyBoardControllerKeyCode(char repr)
        {
            this.repr = Maybe.just(repr);
        }

        KeyBoardControllerKeyCode()
        {
            this.repr = Maybe.nothing();
        }

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

        public boolean isPrintable()
        {
            return repr.isJust();
        }

        public Maybe<Character> getRepr()
        {
            return repr;
        }

        public Maybe<Direction> toDirection()
        {
            return switch (this) {
                case UP -> Maybe.just(NORTH);
                case DOWN -> Maybe.just(SOUTH);
                case LEFT -> Maybe.just(WEST);
                case RIGHT -> Maybe.just(EAST);
                default -> Maybe.nothing();
            };
        }
    }

    public static class KeyBoardUpdateInfo
    {
        public enum KeyBoardEventType
        {
            KEY_PRESSED,
            KEY_RELEASED
        }

        private final KeyBoardEventType keyBoardEventType;
        private final KeyBoardControllerKeyCode keyCode;
        private final Set<KeyBoardControllerKeyCode> currentlyPressed;

        private KeyBoardUpdateInfo(KeyBoardEventType keyBoardEventType,
                                   KeyBoardControllerKeyCode keyCode,
                                   Set<KeyBoardControllerKeyCode> pressedKeyCodes)
        {
            this.keyBoardEventType = keyBoardEventType;
            this.keyCode = keyCode;
            currentlyPressed = pressedKeyCodes;
        }

        public KeyBoardEventType getKeyBoardEventType()
        {
            return keyBoardEventType;
        }

        public KeyBoardControllerKeyCode getKeyCode()
        {
            return keyCode;
        }

        public Set<KeyBoardControllerKeyCode> getCurrentlyPressed()
        {
            return currentlyPressed;
        }
    }

    private final HashSet<KeyCode> pressedKeyCodes;
    private final EventHandler<KeyEvent> pressedHandler;
    private final EventHandler<KeyEvent> releasedHandler;

    private final Set<Observer> observers;

    private KeyBoardUpdateInfo updateInfo;

    public KeyBoardController()
    {
        pressedKeyCodes = new HashSet<>();

        pressedHandler = event -> {
            pressedKeyCodes.add(event.getCode());

            updateInfo = new KeyBoardUpdateInfo(
                KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED,
                KeyBoardControllerKeyCode.fromJavaFXKeyCode(event.getCode()),
                pressedKeyCodes.stream()
                    .map(KeyBoardControllerKeyCode::fromJavaFXKeyCode)
                    .collect(Collectors.toCollection(HashSet::new)));

            updateAllObservers();
        };

        releasedHandler = event -> {
            pressedKeyCodes.remove(event.getCode());

            updateInfo = new KeyBoardUpdateInfo(
                KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED,
                KeyBoardControllerKeyCode.fromJavaFXKeyCode(event.getCode()),
                pressedKeyCodes.stream()
                    .map(KeyBoardControllerKeyCode::fromJavaFXKeyCode)
                    .collect(Collectors.toCollection(HashSet::new))
            );

            updateAllObservers();
        };

        observers = new HashSet<>();
    }

    public void attachToScene(Scene scene)
    {
        scene.setOnKeyPressed(pressedHandler);
        scene.setOnKeyReleased(releasedHandler);
    }

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

    public KeyBoardUpdateInfo getUpdateInfo()
    {
        if (updateInfo == null)
            throw new JBomberManException(
                "Called update but there are no update info");

        return updateInfo;
    }
}
