package app.jbomberman.view.graphics.swing;

import app.jbomberman.util.Maybe;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardUpdateInfo;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class SwingKeyBoardController extends KeyBoardController
{
    private final Set<Integer> pressedKeys;

    private final KeyListener keyListener;

    private Maybe<KeyBoardUpdateInfo> updateInfo;


    public SwingKeyBoardController()
    {
        pressedKeys = new HashSet<>();

        keyListener = new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {/*do nothing*/}

            @Override
            public void keyPressed(KeyEvent e)
            {
                int keyCode = e.getKeyCode();
                pressedKeys.add(keyCode);

//                setUpdateInfo(new KeyBoardUpdateInfo(
//                    KeyBoardUpdateInfo.KeyBoardEventType.KEY_PRESSED,
//                    fromSwingKeyCode(keyCode),
//                    pressedKeys.stream()
//                        .map(SwingKeyBoardController::fromSwingKeyCode)
//                        .collect(Collectors.toCollection(HashSet::new));
//                ));
                updateAllObservers();
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                pressedKeys.remove(e.getKeyCode());

                updateAllObservers();
            }
        };
    }

    public void addToComponent(Component component)
    {
        component.addKeyListener(keyListener);
    }
}
