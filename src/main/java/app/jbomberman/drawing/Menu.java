package app.jbomberman.drawing;

import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.view.KeyBoardController;
import app.jbomberman.view.KeyBoardControllerKeyCode;
import app.jbomberman.view.KeyBoardUpdateInfo;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class Menu extends Panel<MenuEntry> implements Observer
{
    private Maybe<Integer> selectedEntry;

    private Supplier<Drawing> arrangementPolicy;

    public Menu(Stream<MenuEntry> stream)
    {
        super(stream);

        if (!getEntries().isEmpty())
            selectedEntry = Maybe.just(0);
    }

    public Menu(MenuEntry... menuEntries)
    {
        this(Arrays.stream(menuEntries));
    }

    // Call immediately after the call to super in every subclass
    protected final void setArrangementPolicy(Supplier<Drawing> policy)
    {
        arrangementPolicy = policy;
    }

    @Override
    public final Drawing getDrawing()
    {
        getEntries().forEach(e -> e.setSelected(false));
        getSelectedEntry().maybeDo(eIdx -> getEntries().get(eIdx).setSelected(true));
        return arrangementPolicy.get();
    }

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
        }
    }

    protected abstract KeyBoardControllerKeyCode getNextKey();

    protected abstract KeyBoardControllerKeyCode getPrevKey();

    protected Maybe<Integer> getSelectedEntry()
    {
        return selectedEntry;
    }
}
