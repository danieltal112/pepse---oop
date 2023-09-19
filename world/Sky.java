package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Background object for the game.
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static final String SKY_TAG = "sky";

    /**
     * Creates and adds a sky to the game.
     *
     * @param gameObjects      - The collection to which we add the sky object.
     * @param windowDimensions - The dimensions of the game window.
     * @param skyLayer         - The layer to we which we add the sky object.
     * @return - a pointer to the object (sky) that was created.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions,
                                    int skyLayer) {

        GameObject sky = new GameObject(Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR)
        );
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        gameObjects.addGameObject(sky, skyLayer);
        return sky;
    }
}
