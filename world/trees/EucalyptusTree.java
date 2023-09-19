package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 * Represents one instance of a tree of type Eucalyptus.
 */
public class EucalyptusTree extends Tree {
    protected static final Color EUC_COLOR = new Color(173, 94, 3);
    protected static final Color EUC_LEAF_COLOR = new Color(43, 90, 30);
    protected static final int MIN_EUC_HEIGHT = 240;
    protected static final int MAX_EUC_HEIGHT = 90;
    protected final static int LEAVES_ROW_NUM = 3;
    protected final static int LEAVES_COL_NUM = 4;
    protected static final int Y_EUC_PLACEMENT_BOUND = 3;
    protected static final int X_EUC_PLACEMENT_BOUND = 3;
    protected final Vector2 treeDimensions;

    /**
     * Constructor for the Tree class.
     *
     * @param gameObjects - a collection of game objects.
     * @param terrain     - a game object which represents the ground in the game.
     */
    public EucalyptusTree(GameObjectCollection gameObjects, int xCoordinate, Terrain terrain) {
        super(gameObjects, terrain);
        this.treeDimensions = trunkDimensions(MIN_EUC_HEIGHT, MAX_EUC_HEIGHT, xCoordinate);
        Vector2 treePosition = trunkPosition(xCoordinate, treeDimensions.y());
        generateTree(treePosition, new Vector2(MIN_EUC_HEIGHT, MAX_EUC_HEIGHT), EUC_COLOR);
        generateLeaves(treePosition, LEAVES_ROW_NUM, LEAVES_COL_NUM, EUC_LEAF_COLOR, Y_EUC_PLACEMENT_BOUND,
                X_EUC_PLACEMENT_BOUND);
    }

}
