package app.jbomberman.view.graphics.swing;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.LayeredDrawObject;
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
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: TODO javadoc
public class SwingView extends View implements Observer
{
    private final JFrame frame;

    private Maybe<Callback> callback = Maybe.nothing();

    private final List<JLabel> labelPool = new ArrayList<>();

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

        //frame.setSize(700, 700);
        frame.setVisible(true);
        frame.setLayout(null);
    }

    private final List<JPanel> layers = new ArrayList<>();

    private final Map<LayeredDrawObject, JLabel> drawObjects = new HashMap<>();

    @Override
    public void draw(Drawing drawing)
    {
        Vector2D sceneDimensions = getDrawingDimensions(drawing);
        Vector2D sceneCenter = sceneDimensions.div(2);
        Vector2D screenCenter =
            Vector2D.of(frame.getWidth(), frame.getHeight()).div(2);

        Vector2D shamt = screenCenter.sub(sceneCenter);

        //layers.forEach(Container::removeAll);

        drawing.getDrawObjects().forEach(layeredDrawObject -> assureExistingLayer(layeredDrawObject.getLayer()));

        Map<LayeredDrawObject, JLabel> toRemoveDrawObjectsMap = new HashMap<>();

        for (LayeredDrawObject d : drawing.getDrawObjects()) {
            if (drawObjects.containsKey(d))
                continue;
            drawDrawObject(d, shamt);
        }

        Set<LayeredDrawObject> newDrawObjects = new HashSet<>()
        {
            {
                addAll(drawing.getDrawObjects());
            }
        };

        // Select objects for removal
        for (Map.Entry<LayeredDrawObject, JLabel> d : drawObjects.entrySet())
            if (!newDrawObjects.contains(d.getKey()))
                toRemoveDrawObjectsMap.put(d.getKey(), d.getValue());

        for (Map.Entry<LayeredDrawObject, JLabel> d : toRemoveDrawObjectsMap.entrySet()) {
            JLabel label = d.getValue();
            layers.get(d.getKey().getLayer()).remove(label);
            drawObjects.remove(d.getKey());
            labelPool.add(label);
        }


        frame.repaint();
    }

    private void assureExistingLayer(int layerNumber)
    {
        while (layers.size() <= layerNumber) {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(0, 0, 0, 0));
            frame.add(panel, 0);
            panel.setSize(frame.getWidth(), frame.getHeight());
            panel.setVisible(true);
            panel.setLayout(null);
            layers.add(panel);
        }
    }

    // Note that Swing draws in reverse
    private void drawDrawObject(LayeredDrawObject drawObject, Vector2D shift)
    {
        JLabel label = (labelPool.size() == 0) ?
            new JLabel() :
            labelPool.remove(labelPool.size() - 1);

        label.setIcon(new ImageIcon(toSwingImage(drawObject.getImage())));

        Dimension size = label.getPreferredSize();
        label.setBounds(drawObject.getX() + shift.getX(),
            drawObject.getY() + shift.getY(), size.width, size.height);

        layers.get(drawObject.getLayer()).add(label);
        drawObjects.put(drawObject, label);
//        panel.add(label);
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
