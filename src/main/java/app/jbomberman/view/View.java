package app.jbomberman.view;

import app.jbomberman.drawing.DrawObject;
import app.jbomberman.drawing.Drawing;
import app.jbomberman.images.AbstractImage;
import app.jbomberman.images.FlightWeightImagesFactory;
import app.jbomberman.images.javafx.JavaFXImage;
import app.jbomberman.model.Model;
import app.jbomberman.util.Callback;
import app.jbomberman.util.Observable;
import app.jbomberman.util.Observer;
import app.jbomberman.util.Vector2D;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class View implements Observer
{
    public static void initialize(Callback callback)
    {
        JavaFXSetup.initialize(callback);
    }

    private final Group root;
    private final Scene scene;
    private final Stage stage;

    private final Logger logger;

    public View()
    {
        logger = LogManager.getLogger();

        root = new Group();
        scene = new Scene(root);
        stage = new Stage();

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public void draw(Drawing drawing)
    {
        root.getChildren().clear();

        Vector2D sceneDimensions = drawing.getDimensions().orElse(
            drawing.getDrawObjects().values().stream().flatMap(List::stream)
                .reduce(
                    Vector2D.getIdentity(),
                    (acc, drawObj) -> acc.elementWiseApply(drawObj.sum(drawObj.getDimensions()),
                        Math::max),
                    (product1, product2) ->
                        product1.elementWiseApply(product2, Math::max)));

        Vector2D sceneCenter = sceneDimensions.div(2);
        Vector2D screenCenter =
            Vector2D.of((int) stage.getWidth(), (int) stage.getHeight()).div(2);

        Vector2D shamt = screenCenter.sub(sceneCenter);

        drawing.getDrawObjects().entrySet()
            .stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .flatMap(entry -> entry.getValue().stream())
            .forEachOrdered(drawObj -> this.drawDrawObject(drawObj, shamt));
    }

    private void drawDrawObject(DrawObject drawObject, Vector2D shift)
    {
        ImageView imageView =
            new ImageView(toJavaFXImage(drawObject.getImage()));

        imageView.setX(drawObject.getX() + shift.getX());
        imageView.setY(drawObject.getY() + shift.getY());
        root.getChildren().add(imageView);
    }

    public Scene getScene()
    {
        return scene;
    }

    public void setTitle(String title)
    {
        stage.setTitle(title);
    }

    public void setIcon(String path)
    {
        stage.getIcons().add(toJavaFXImage(FlightWeightImagesFactory.getInstance()
            .getImage(path)));
    }

    public void setCloseOperationCallBack(Callback callBack)
    {
        stage.setOnHidden(ignoredEvent -> callBack.run());
    }

    private static Image toJavaFXImage(AbstractImage abstractImage)
    {
        return ((JavaFXImage) abstractImage).unwrap();
    }

    @Override
    public void update(Observable observable)
    {
        if (observable instanceof Model model) {
            draw(model.getDraw());
        }
    }

    public void close()
    {
        logger.info("Closing view");
        Platform.exit();
    }
}