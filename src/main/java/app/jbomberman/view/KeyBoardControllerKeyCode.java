package app.jbomberman.view;

import app.jbomberman.util.Maybe;
import javafx.scene.input.KeyCode;

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
