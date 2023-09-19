package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Base-object for the entire range of rectangles which will be
 * used in the game.
 */
public class Block extends GameObject {
    public static final int SIZE = 30;
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;

    /**
     * A constructor for a Block class object.
     *
     * @param topLeftCorner - The top-left coordinates of the object at the screen.
     * @param renderable    - Renderable-type object, which provides a visual representation for the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * The method rounds the values of minX and maxX (representing the edges of a range on the X-axis).
     *
     * @param minX - The left-edge corner of the range.
     * @param maxX - The right-edge corner of the range.
     * @return - New (rounded) values for the edges.
     */
    public static int[] fixRange(int minX, int maxX) {
        int[] fixMinMax = new int[2];

        if (minX >= 0) {
            minX -= (minX % Block.SIZE);
        } else {
            minX -= (Block.SIZE + minX % Block.SIZE) % Block.SIZE;
        }
        fixMinMax[FIRST_INDEX] = minX;
        int counter = minX;
        while (counter <= maxX) {
            counter += Block.SIZE;
        }
        fixMinMax[SECOND_INDEX] = counter;
        return fixMinMax;
    }
}



