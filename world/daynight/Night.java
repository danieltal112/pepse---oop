package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class is responsible for simulating the nighttime of the game.
 */
public class Night {

    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String NIGHT_TAG = "night";

    /**
     * Generates rectangle according to the window size, and
     * integrates it within the game so that its
     * appearance will change according to the time factor
     * inside the game.
     *
     * @param gameObjects      - The objects-collection to which
     *                         the night object will be added.
     * @param layer            - The rendering layer to which the
     *                        rectangle will be added.
     * @param windowDimensions - the dimension of the game window.
     * @param cycleLength      - The length of a single day-night
     *                         cycle, in seconds.
     * @return - A pointer to the object that was created.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength) {

        GameObject night = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(Color.black));

        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        gameObjects.addGameObject(night, layer);

        new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return night;
    }
}
