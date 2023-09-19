package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

/**
 * The class handles all the ground and blocks, as well as providing information regarding the terrain height.
 */
public class Terrain {

    private static final float GROUND_HEIGHT_PARAM = 4.5f / 6;
    private static final String GROUND_TAG = "ground";
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private static final Color BASE_GROUND_COLOR = new Color(140, 76, 9);
    private final NoiseGenerator noise;
    private static final int TERRAIN_DEPTH = 20;

    /**
     * Constructor for a Terrain-class object.
     *
     * @param gameObjects      - The collection to which we add the terrain.
     * @param groundLayer      - The layer to which the terrain is added.
     * @param windowDimensions - The dimensions of the game window.
     * @param seed             - Integer Parameter which is used to generate the terrain height.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.noise = new NoiseGenerator(seed);
    }

    /**
     * Determines what is the desired ground height at a given point.
     *
     * @param x - The horizontal value (X-axis) of the given point.
     * @return - The desired ground height (as float).
     */
    private float calcGroundHeightAt(float x) {
        return windowDimensions.y() * GROUND_HEIGHT_PARAM + noise.noise(x);
    }

    /**
     * Determines what is the desired ground height at a given point, using additional logic to prevent
     * spikes in the terrain.
     *
     * @param x - The horizontal value (X-axis) of the given point.
     * @return - The desired ground height (as float).
     */
    public float groundHeightAt(float x) {
        float result = calcGroundHeightAt(x);
        if (x - Block.SIZE > Block.SIZE && x + Block.SIZE < windowDimensions.x()) {
            if (calcGroundHeightAt(x - Block.SIZE) < result +
                    Block.SIZE && calcGroundHeightAt(x + Block.SIZE) < result + Block.SIZE)
                return calcGroundHeightAt(x - Block.SIZE);
        }
        if (x - Block.SIZE > Block.SIZE && x + Block.SIZE < windowDimensions.x()) {
            if (calcGroundHeightAt(x - Block.SIZE) > result + Block.SIZE && calcGroundHeightAt(x +
                    Block.SIZE) > result + Block.SIZE)
                return calcGroundHeightAt(x - Block.SIZE);
        }
        return result;
    }

    /**
     * Generates all the ground blocks in a given horizontal array.
     *
     * @param minX - The left corner of the array.
     * @param maxX - The right corner of the array.
     */
    public void createInRange(int minX, int maxX) {
        int[] fixMinMax = Block.fixRange(minX, maxX);
        minX = fixMinMax[FIRST_INDEX];
        maxX = fixMinMax[SECOND_INDEX];

        Renderable renderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        for (int i = minX; i <= maxX; i += Block.SIZE) {
            GameObject ground = new Block(Vector2.ZERO, renderable);
            ground.setDimensions(new Vector2(Block.SIZE, Block.SIZE * TERRAIN_DEPTH));
            ground.setTopLeftCorner(new Vector2(i, groundHeightAt(i)));
            ground.setTag(GROUND_TAG);
            gameObjects.addGameObject(ground, groundLayer);
        }
    }
}
