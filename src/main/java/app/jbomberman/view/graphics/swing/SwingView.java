package app.jbomberman.view.graphics.swing;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.LayeredDrawObject;
import app.jbomberman.image.AbstractImage;
import app.jbomberman.image.FlightWeightImagesFactory;
import app.jbomberman.image.swing.SwingImage;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Maybe;
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

/**
 * SwingView is the concrete implementation of the View class for java swing
 *
 * @author gp
 */
public class SwingView extends View
{
	/**
	 * frame is the JFrame to which JPanels must be added to
	 */
	private final JFrame frame;

	/**
	 * callback to call on close of the View
	 */
	private Maybe<Callback> callback = Maybe.nothing();

	/**
	 * labelPool is a pool of freeLabels
	 * <p>
	 * A lot of time was getting wasted on creating new JLabels. So, instead of
	 * letting the GC deallocate the JLabels, we put the unused JLabels in this
	 * labelPool
	 */
	private final List<JLabel> labelPool = new ArrayList<>();

	/**
	 * SwingView is the public constructor of the SwingView.
	 * <p>
	 * It creates a ready-to-use window
	 */
	public SwingView()
	{
		super(new SwingGraphicFactory());

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
		frame.setCursor(blankCursor);

		frame.setBackground(Color.BLACK);
		frame.setResizable(false);

		frame.setVisible(true);
		frame.setLayout(null);
	}

	/**
	 * layers is the List of layers. Layer number n is in position n in the list
	 */
	private final List<JPanel> layers = new ArrayList<>();

	/**
	 * drawObjects maps a drawObject to the corresponding displayed label
	 */
	private final Map<LayeredDrawObject, JLabel> drawObjects = new HashMap<>();

	/**
	 * Given a drawing, draw() centers it in the middle of the screen and displays
	 * it.
	 *
	 * @param drawing the drawing to be displayed
	 */
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

	/**
	 * assureExistingLayers adds as many empty layers as needed to be able to accomodate
	 * layer numbered layerNumber
	 *
	 * @param layerNumber the layer whose number we want to be available
	 */
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

	/**
	 * drawDrawObject() adds a LayeredDrawObject to the View
	 *
	 * @param drawObject the drawObject to draw
	 * @param shift      vector to sum to coordinates due to the centering of drawObjects
	 */
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
	}

	/**
	 * toSwingImage is a simple method to be called internally by SwingView to unwrap
	 * an AbstractImage
	 *
	 * @param abstractImage the AbstractImage to unwrap
	 * @return the Image wrapped in abstractImage
	 */
	private static Image toSwingImage(AbstractImage abstractImage)
	{
		return ((SwingImage) abstractImage).unwrap();
	}

	/**
	 * setTitle() sets the title to the window
	 *
	 * @param title the title that we want to set to the window
	 */
	@Override
	public void setTitle(String title)
	{
		frame.setTitle(title);
	}

	/**
	 * setIcon() sets the icon to be displayed for this application
	 *
	 * @param path the path of the icon to display
	 */
	@Override
	public void setIcon(String path)
	{
		BufferedImage iconBuffered = ((SwingImage) FlightWeightImagesFactory.getInstance().getImage(path)).unwrap();
		ImageIcon icon = new ImageIcon(iconBuffered);
		frame.setIconImage(icon.getImage());
	}

	/**
	 * setCloseOperationCallBack() is needed if we want to add a callback to be
	 * called when the application gets closed by the user
	 *
	 * @param callBack the callback to call when the window gets closed
	 */
	@Override
	public void setCloseOperationCallBack(Callback callBack)
	{
		this.callback = Maybe.just(callBack);
	}

	/**
	 * close() function just exits the program returning success
	 */
	@Override
	public void close()
	{
		System.exit(0);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
