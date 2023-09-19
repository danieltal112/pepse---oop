package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The class responsible to everything related to the sun
 * object in the game.
 */
public class Sun {

    private static final String SUN_TAG = "sun";
    private static final float SUN_SIZE = 100;
    private static final float INIT_ANGLE = 0f;
    private static final float MAX_ANGLE = (float) Math.PI * 2;


    /**
     * Creates a new Sun object in the sky.
     *
     * @param gameObjects      - The Object-collection to which
     *                         the sun is added.
     * @param layer            - The game-layer to which the sun
     *                         will be rendered in.
     * @param windowDimensions - The dimensions of the game-window.
     * @param cycleLength      - The length of the sun's cycle in
     *                         the sky (in seconds).
     * @return
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength) {

        GameObject sun = new GameObject(
                Vector2.ZERO,
                new Vector2(SUN_SIZE, SUN_SIZE),
                new OvalRenderable(Color.YELLOW)
        );

        sun.setCenter(new Vector2(windowDimensions.x() / 2,
                SUN_SIZE));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        new Transition<>(
                sun,
                angle -> sun.setCenter(
                        new Vector2(
                                windowDimensions.mult(0.5f)).
                                add(new Vector2((float)
                                        Math.sin(angle) *
                                        windowDimensions.x()
                                        * (0.35f),
                                        (float) Math.cos(angle) *
                                                (-1) *
                                                windowDimensions.y()
                                                * (0.7f)+200))),
                0f,
                (float) Math.PI * 2,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        gameObjects.addGameObject(sun, layer);
        return sun;
    }
}
