package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 *  Represents one instance of a tree of type Eucalyptus.
 */
public class OakTree extends Tree {
    protected static final Color OAK_COLOR = new Color(81, 52, 27);
    protected static final Color OAK_LEAF_COLOR = new Color(33, 194, 79);
    protected static final int MIN_OAK_HEIGHT = 150;
    protected static final int MAX_OAK_HEIGHT = 120;
    protected final static int LEAVES_COL_NUM = 3;
    protected final static int LEAVES_ROW_NUM = 6;
    protected static final int Y_PLACEMENT_BOUND = 5;
    protected static final int X_PLACEMENT_BOUND = 2;
    protected final Vector2 treeDimensions;

    /**
     * Constructor for the Tree class.
     *
     * @param gameObjects - a collection of game objects.
     * @param terrain     - a game object which represents the ground in the game.
     */
    public OakTree(GameObjectCollection gameObjects, int xCoordinate, Terrain terrain) {
        super(gameObjects, terrain);
        this.treeDimensions = trunkDimensions(MIN_OAK_HEIGHT, MAX_OAK_HEIGHT, xCoordinate);
        Vector2 treePosition = trunkPosition(xCoordinate, treeDimensions.y());
        generateTree(treePosition, new Vector2(MIN_OAK_HEIGHT, MAX_OAK_HEIGHT), OAK_COLOR);
        generateLeaves(treePosition, LEAVES_ROW_NUM, LEAVES_COL_NUM, OAK_LEAF_COLOR, Y_PLACEMENT_BOUND,
                X_PLACEMENT_BOUND);
    }

}
