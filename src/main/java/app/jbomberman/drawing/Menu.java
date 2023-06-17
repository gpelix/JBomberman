package app.jbomberman.drawing;

import app.jbomberman.JBomberManException;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardControllerKeyCode;
import app.jbomberman.view.KeyBoardUpdateInfo;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Menu represents a menu. It extends a Panel of MenuEntry's and implements the
 * Observable interface as it must listen for KeyBoard events to chenge the
 * currently selected menu entry
 *
 * @author gp
 * @see Panel
 * @see MenuEntry
 */
public abstract class Menu extends Panel<MenuEntry> implements Observer
{
    /**
     * selectedEntry is the selected entry. It's in a Maybe as there might be
     * no menu entries and no selectedEntry actually
     */
    private Maybe<Integer> selectedEntry;

    /**
     * The policy is, at the moment, a function that looks at the underlying panel
     * and returns the Drawing
     */
    private Supplier<Drawing> arrangementPolicy;

    /**
     * Constructor from Stream of MenuEntry's
     *
     * @param stream the stream containing the MenuEntry's
     */
    public Menu(Stream<MenuEntry> stream)
    {
        super(stream);

        if (!getEntries().isEmpty())
            selectedEntry = Maybe.just(0);
    }

    /**
     * Constructor from varargs
     *
     * @param menuEntries the list of the MenuEntry's
     */
    public Menu(MenuEntry... menuEntries)
    {
        this(Arrays.stream(menuEntries));
    }

    /**
     * setArrangementPolicy() sets the function used to draw the Menu
     *
     * @param policy the policy to set up
     */
    protected final void setArrangementPolicy(Supplier<Drawing> policy)
    {
        arrangementPolicy = policy;
    }

    /**
     * getDrawing() is a function that gets the current drawing
     *
     * @return the current drawing depicting the menu
     */
    @Override
    public final Drawing getDrawing()
    {
        getEntries().forEach(e -> e.setSelected(false));
        getSelectedEntry().maybeDo(eIdx -> getEntries().get(eIdx).setSelected(true));
        return arrangementPolicy.get();
    }

    /**
     * update() must listen from keyboard events like the pressing of the arrows
     *
     * @param observable the observable
     * @throws app.jbomberman.JBomberManException if the observable is not a KeyBoardController
     */
    @Override
    public void update(Observable observable)
    {
        if (observable instanceof KeyBoardController keyBoardController) {
            KeyBoardUpdateInfo updateInfo =
                keyBoardController.getUpdateInfo();

            if (updateInfo.getKeyBoardEventType() == KeyBoardUpdateInfo.KeyBoardEventType.KEY_RELEASED)
                return;

            if (updateInfo.getKeyCode() == KeyBoardControllerKeyCode.ENTER)
                selectedEntry.maybeDo(currentEntryIdx -> getEntries().get(currentEntryIdx).callCallback());
            else {
                KeyBoardControllerKeyCode keyCode = updateInfo.getKeyCode();

                if (keyCode == getNextKey()) {
                    selectedEntry = selectedEntry.maybeCompute(entryOffset ->
                        entryOffset < getEntries().size() - 1 ? entryOffset + 1 : entryOffset);
                } else if (keyCode == getPrevKey()) {
                    selectedEntry = selectedEntry.maybeCompute(entryOffset ->
                        entryOffset > 0 ? entryOffset - 1 : entryOffset);
                }
            }
        } else
            throw new JBomberManException("Receiving an update from an unknown observable `" + observable + "'");
    }

    /**
     * getNextKey() is an abstract function that returns the KeyBoardControllerKeyCode
     * that must be sent to the menu to select the next entry in the menu
     *
     * @return the key that must be sent to the menu to select the next entry
     */
    protected abstract KeyBoardControllerKeyCode getNextKey();

    /**
     * getPrevKey() is an abstract function that returns the KeyBoardControllerKeyCode
     * that must be sent to the menu to select the previous entry in the menu
     *
     * @return the key that must be sent to the menu to select the previou entry
     */
    protected abstract KeyBoardControllerKeyCode getPrevKey();

    /**
     * getSelectedEntry() returns the currently selected entry
     *
     * @return the currently selected entry
     */
    protected Maybe<Integer> getSelectedEntry()
    {
        return selectedEntry;
    }
}
