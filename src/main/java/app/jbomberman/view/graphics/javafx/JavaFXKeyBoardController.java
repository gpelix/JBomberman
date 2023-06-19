package app.jbomberman.view.graphics.javafx;

import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardControllerKeyCode;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static app.jbomberman.view.KeyBoardControllerKeyCode.A;
import static app.jbomberman.view.KeyBoardControllerKeyCode.B;
import static app.jbomberman.view.KeyBoardControllerKeyCode.BACK_SPACE;
import static app.jbomberman.view.KeyBoardControllerKeyCode.C;
import static app.jbomberman.view.KeyBoardControllerKeyCode.D;
import static app.jbomberman.view.KeyBoardControllerKeyCode.DEL;
import static app.jbomberman.view.KeyBoardControllerKeyCode.DOWN;
import static app.jbomberman.view.KeyBoardControllerKeyCode.E;
import static app.jbomberman.view.KeyBoardControllerKeyCode.ENTER;
import static app.jbomberman.view.KeyBoardControllerKeyCode.ESC;
import static app.jbomberman.view.KeyBoardControllerKeyCode.F;
import static app.jbomberman.view.KeyBoardControllerKeyCode.G;
import static app.jbomberman.view.KeyBoardControllerKeyCode.H;
import static app.jbomberman.view.KeyBoardControllerKeyCode.I;
import static app.jbomberman.view.KeyBoardControllerKeyCode.J;
import static app.jbomberman.view.KeyBoardControllerKeyCode.K;
import static app.jbomberman.view.KeyBoardControllerKeyCode.L;
import static app.jbomberman.view.KeyBoardControllerKeyCode.LEFT;
import static app.jbomberman.view.KeyBoardControllerKeyCode.M;
import static app.jbomberman.view.KeyBoardControllerKeyCode.N;
import static app.jbomberman.view.KeyBoardControllerKeyCode.O;
import static app.jbomberman.view.KeyBoardControllerKeyCode.OTHER;
import static app.jbomberman.view.KeyBoardControllerKeyCode.P;
import static app.jbomberman.view.KeyBoardControllerKeyCode.Q;
import static app.jbomberman.view.KeyBoardControllerKeyCode.R;
import static app.jbomberman.view.KeyBoardControllerKeyCode.RIGHT;
import static app.jbomberman.view.KeyBoardControllerKeyCode.S;
import static app.jbomberman.view.KeyBoardControllerKeyCode.SPACE;
import static app.jbomberman.view.KeyBoardControllerKeyCode.T;
import static app.jbomberman.view.KeyBoardControllerKeyCode.U;
import static app.jbomberman.view.KeyBoardControllerKeyCode.UP;
import static app.jbomberman.view.KeyBoardControllerKeyCode.V;
import static app.jbomberman.view.KeyBoardControllerKeyCode.W;
import static app.jbomberman.view.KeyBoardControllerKeyCode.X;
import static app.jbomberman.view.KeyBoardControllerKeyCode.Y;
import static app.jbomberman.view.KeyBoardControllerKeyCode.Z;

/**
 * KeyBoardController is the class responsible for the handling the keyboard
 * input that comes from the user.
 * <p>
 * KeyBoardController communicates with other class the keystrokes using the
 * observer/Observable pattern.
 *
 * @author gp
 */
public class JavaFXKeyBoardController extends KeyBoardController
{
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
	 * KeyBoardController
	 */
	public JavaFXKeyBoardController()
	{
		pressedHandler = event -> onPressed(fromJavaFXKeyCode(event.getCode()));
		releasedHandler = event -> onReleased(fromJavaFXKeyCode(event.getCode()));
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
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
