package app.jbomberman.view.graphics.swing;

import app.jbomberman.drawing.DrawObject;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.images.swing.SwingImage;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Maybe;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

// TODO: TODO
public class SwingView extends View implements Observer
{
    private final JFrame frame;

    private Maybe<Callback> callback = Maybe.nothing();

    public SwingView()
    {
        super(new SwingGraphicFactory(), new SwingKeyBoardController());

        frame = new JFrame();

        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                callback.maybeDo(Callback::run);
                System.exit(0);
            }
        });

        ((SwingKeyBoardController) getKeyBoardController()).addToComponent(frame);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor"
        );
        frame.getContentPane().setCursor(blankCursor);

        frame.getContentPane().setBackground(Color.BLACK);
        frame.setResizable(false);

        frame.setSize(700, 700);
        frame.setVisible(true);
        frame.setLayout(null);
    }

    @Override
    public void draw(Drawing drawing)
    {
        frame.getContentPane().removeAll();
        Vector2D sceneDimensions = getDrawingDimensions(drawing);
        Vector2D sceneCenter = sceneDimensions.div(2);
        Vector2D screenCenter =
            Vector2D.of(frame.getWidth(), frame.getHeight()).div(2);

        Vector2D shamt = screenCenter.sub(sceneCenter);

        drawing.getDrawObjects().entrySet()
            .stream()
            // Note here that we reverse the order! swing wants us to draw first the top and then the bottom
            .sorted((a, b) -> b.getKey() - a.getKey())
            .forEachOrdered(drawEntry -> drawEntry.getValue().forEach(_draw -> drawDrawObject(_draw, shamt)));

        frame.getContentPane().repaint();
    }

    // Note that Swing draws in reverse
    private void drawDrawObject(DrawObject drawObject, Vector2D shift)
    {
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(toSwingImage(drawObject.getImage())));

        Dimension size = label.getPreferredSize();
        label.setBounds(drawObject.getX() + shift.getX(),
            drawObject.getY() + shift.getY(), size.width, size.height);

        frame.add(label);
    }

    private static Image toSwingImage(AbstractImage abstractImage)
    {
        return ((SwingImage) abstractImage).unwrap();
    }

    @Override
    public void setTitle(String title)
    {
        frame.setTitle(title);
    }

    @Override
    public void setIcon(String path)
    {
        BufferedImage iconBuffered = ((SwingImage) FlightWeightImagesFactory.getInstance().getImage(path)).unwrap();
        ImageIcon icon = new ImageIcon(iconBuffered);
        frame.setIconImage(icon.getImage());
    }

    @Override
    public void setCloseOperationCallBack(Callback callBack)
    {
        this.callback = Maybe.just(callBack);
    }

    @Override
    public void close()
    {
        System.exit(0);
    }
}
