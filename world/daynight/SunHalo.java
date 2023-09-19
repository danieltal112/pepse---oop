package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 *
 */
public class SunHalo {

    /**
     * Creates a new sunHalo.
     *
     * @param gameObjects - the gameObject collection of the game.
     * @param layer - The layer to add the sunHalo.
     * @param sun - the sun object.
     * @param color - the color of the halo.
     * @return a pointer to the new object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun,
                                    Color color) {

        GameObject sunHalo = new GameObject(Vector2.ZERO, new Vector2(200, 200),
                new OvalRenderable(color));
        sunHalo.setCenter(sun.getCenter());
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("sunHalo");
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.addComponent(num -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;

    }
}
