package app.jbomberman.view.graphics.swing;

import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardControllerKeyCode;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

public class SwingKeyBoardController extends KeyBoardController
{
    private final KeyListener keyListener;


    public SwingKeyBoardController()
    {
        keyListener = new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {/*do nothing*/}

            @Override
            public void keyPressed(KeyEvent e)
            {
                onPressed(fromSwingKeyCode(e.getKeyCode()));
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                onReleased(fromSwingKeyCode(e.getKeyCode()));
            }
        };
    }

    public void addToComponent(Component component)
    {
        component.addKeyListener(keyListener);
    }

    public void removeFromComponent(Component component)
    {
        component.removeKeyListener(keyListener);
    }

    private static KeyBoardControllerKeyCode fromSwingKeyCode(int keyCode)
    {
        return switch (keyCode) {
            case VK_DOWN -> DOWN;
            case VK_UP -> UP;
            case VK_LEFT -> LEFT;
            case VK_RIGHT -> RIGHT;
            case VK_ESCAPE -> ESC;
            case VK_ENTER -> ENTER;
            case VK_SPACE -> SPACE;
            case VK_BACK_SPACE -> BACK_SPACE;
            case VK_DELETE -> DEL;
            case VK_A -> A;
            case VK_B -> B;
            case VK_C -> C;
            case VK_D -> D;
            case VK_E -> E;
            case VK_F -> F;
            case VK_G -> G;
            case VK_H -> H;
            case VK_I -> I;
            case VK_J -> J;
            case VK_K -> K;
            case VK_L -> L;
            case VK_M -> M;
            case VK_N -> N;
            case VK_O -> O;
            case VK_P -> P;
            case VK_Q -> Q;
            case VK_R -> R;
            case VK_S -> S;
            case VK_T -> T;
            case VK_U -> U;
            case VK_V -> V;
            case VK_W -> W;
            case VK_X -> X;
            case VK_Y -> Y;
            case VK_Z -> Z;
            default -> OTHER;

        };
    }
}
