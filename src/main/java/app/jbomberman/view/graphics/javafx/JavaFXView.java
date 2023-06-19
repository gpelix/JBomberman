package app.jbomberman.view.graphics.javafx;

import app.jbomberman.drawing.Drawing;
import app.jbomberman.drawing.LayeredDrawObject;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.images.javafx.JavaFXImage;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;
import app.jbomberman.view.View;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * View class plays the role of the View in the MVC pattern we used to build
 * the application
 *
 * @author gp
 */
public class JavaFXView extends View implements Observer
{
    /**
     * initialize() must be called to initialize the view.
     * It will spawn a javafx thread and call callback inside that thread
     *
     * @param callback the callback to be run in the javafx thread
     */
    public static void initialize(Callback callback)
    {
        JavaFXSetup.initialize(callback);
    }

    /**
     * root is the group to which every other Group will be attached
     */
    private final Group root;
    /**
     * scene is the only javafx scene in our application containing the root
     * group
     */
    private final Scene scene;
    /**
     * stage is the only stage in our application, and its scene will always be
     * the variable called scene
     */
    private final Stage stage;

    /**
     * a logger for this class
     */
    private final Logger logger;

    /**
     *
     */

    private final List<ImageView> freeImageViewsPool;

    /**
     * View is the public constructor of the View.
     * Once a javafx thread has been spawned calling the initialize static method
     * of this class from the callback passed to the initialize() method, a View
     * can be finally created.
     *
     * @implNote this startup method is not optional but mandatory. We don't
     * want the view to subclass javafx.Application as we want to be able to
     * build an object of type view from the controller as a normal class
     */
    public JavaFXView()
    {
        super(new JavaFXGraphicFactory(), new JavaFXKeyBoardController());
        logger = LogManager.getLogger(getClass());
        freeImageViewsPool = new ArrayList<>();

        root = new Group();
        scene = new Scene(root);
        stage = new Stage();

        ((JavaFXKeyBoardController) getKeyBoardController()).attachToScene(scene);

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        scene.setCursor(Cursor.NONE);

        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    /**
     * layers is the list of the available froups
     */
    private final List<Group> layers = new ArrayList<>();

    /**
     * drawObjects is a map from the layeredObject to the corresponding imageview
     */
    private final Map<LayeredDrawObject, ImageView> drawObjects = new HashMap<>();

    /**
     * Given a drawing, draw() centers it in the middle of the screen and displays
     * it.
     *
     * @param drawing the drawing to be displayed
     * @implNote This approach is kind of slow as every time we have to redraw everything.
     * However, with some benchmarks, we saw that the total amount of time needed
     * to draw the screen is less than the time that the model needs to update
     * himself. So using this approach is not such a bad choice
     */
    public void draw(Drawing drawing)
    {
        Vector2D sceneDimensions = getDrawingDimensions(drawing);

        Vector2D sceneCenter = sceneDimensions.div(2);
        Vector2D screenCenter =
            Vector2D.of((int) stage.getWidth(), (int) stage.getHeight()).div(2);

        Vector2D shamt = screenCenter.sub(sceneCenter);

        drawing.getDrawObjects().forEach(layeredDrawObject -> assureExistingGroup(layeredDrawObject.getLayer()));

        layers.forEach(l -> l.getChildren().clear());

        Map<LayeredDrawObject, ImageView> toRemoveDrawObjectsMap = new HashMap<>();

        //System.out.println(drawing.numberOfItems() + " " + drawing.hashCode());

        // Draw the new objects
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
        for (Map.Entry<LayeredDrawObject, ImageView> d : drawObjects.entrySet())
            if (!newDrawObjects.contains(d.getKey()))
                toRemoveDrawObjectsMap.put(d.getKey(), d.getValue());

        for (Map.Entry<LayeredDrawObject, ImageView> d : toRemoveDrawObjectsMap.entrySet()) {
            ImageView imageView = d.getValue();
            if (!layers.get(d.getKey().getLayer()).getChildren().remove(imageView)) {
            }//logger.error("Removing an Image from a layer that didn't contain it. Weird");
            drawObjects.remove(d.getKey());
            freeImageViewsPool.add(imageView);
        }
    }

    private void assureExistingGroup(int layerNumber)
    {
        while (root.getChildren().size() <= layerNumber) {
            Group group = new Group();
            layers.add(group);
            root.getChildren().add(group);
        }
    }

    /**
     * drawDrawObject draws the drawObject in the appropriate place shifting it
     * depending on the value of the shift Vector2D
     *
     * @param drawObject the drawObject
     * @param shift      the vector by which the object must be shifted towards SE
     */
    private void drawDrawObject(LayeredDrawObject drawObject, Vector2D shift)
    {
        ImageView imageView =
            (freeImageViewsPool.size() == 0) ?
                new ImageView() :
                freeImageViewsPool.remove(freeImageViewsPool.size() - 1);
        imageView.setImage(toJavaFXImage(drawObject.getImage()));

        imageView.setX(drawObject.getX() + shift.getX());
        imageView.setY(drawObject.getY() + shift.getY());
        layers.get(drawObject.getLayer()).getChildren().add(imageView);
        drawObjects.put(drawObject, imageView);
    }

    /**
     * getScene() return the scene
     *
     * @return the scene
     */
    public Scene getScene()
    {
        return scene;
    }

    /**
     * setTitle() sets the title to the window
     *
     * @param title the title that we want to set to the window
     */
    public void setTitle(String title)
    {
        stage.setTitle(title);
    }

    /**
     * setIcon() sets the icon to be displayed for this application
     *
     * @param path the path of the icon to display
     */
    public void setIcon(String path)
    {
        stage.getIcons().add(toJavaFXImage(FlightWeightImagesFactory.getInstance()
            .getImage(path)));
    }

    /**
     * setCloseOperationCallBack() is needed if we want to add a callback to be
     * called when the application gets closed by the user
     *
     * @param callBack the callback to call on close of the window
     */
    public void setCloseOperationCallBack(Callback callBack)
    {
        stage.setOnHidden(ignoredEvent -> callBack.run());
    }

    /**
     * toJavaFXImage is a utility method used internally to unwrap AbstractImages
     * that for now we now to be instances of JavaFXImages
     *
     * @param abstractImage the image to unwrap
     * @return the unwrapped image
     */
    private static Image toJavaFXImage(AbstractImage abstractImage)
    {
        return ((JavaFXImage) abstractImage).unwrap();
    }

    /**
     * close() is needed to close the view. It might sometimes be needed to close
     * the view from the controller. In that case, we provide the close() function
     * that shuts down javafx
     */
    public void close()
    {
        logger.info("Closing view");
        Platform.exit();
    }
}