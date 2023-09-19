package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;

import static pepse.PepseGameManager.*;
import static pepse.world.trees.OakTree.*;

/**
 * This class is responsible for generating a random tree in a random location in a given range.
 */
public class Tree extends GameObjectCollection {

    private static final float DOUBLE_BLOCK_SIZE = Block.SIZE * 2;
    public static final int TREE_LAYER = Layer.STATIC_OBJECTS - 50;
    public static final int LEAF_LAYER = Layer.BACKGROUND + 60;
    public static final String TREE_TAG = "tree";
    private static final float LEAF_HEIGHT_FACTOR = 1.3f;
    private static final int FOREST_DENSITY_FACTOR = 15;

    private static GameObjectCollection gameObjects;
    private static Terrain terrain;
    private final int seed = PepseGameManager.getSeed();

    /**
     * Constructor for the Tree class.
     *
     * @param gameObjects - a collection of game objects.
     * @param terrain     - a game object which represents the ground in the game.
     */
    public Tree(GameObjectCollection gameObjects, Terrain terrain) {
        Tree.gameObjects = gameObjects;
        Tree.terrain = terrain;
    }

    /**
     * Calculates The dimensions of the new tree.
     *
     * @param minHeight - the minimum height for which the tree can be created.
     * @param maxHeight - the maximum extra-height that can bee added to the minHeight (i.e., a minimal
     *                  height tree will be of size minHeight, while the maximal height can be
     *                  minHeight+maxHeight).
     * @param location  - the location of the tree inside the world.
     * @return a vector with tree's dimensions.
     */
    protected Vector2 trunkDimensions(int minHeight, int maxHeight, int location) {
        int height = minHeight + ForestManager.nextIntByX(location, maxHeight, seed);
        return new Vector2(Block.SIZE, height);
    }

    /**
     * Calculates the tree coordinates inside the game.
     *
     * @param x           - the x-coordinate of the tree inside the world.
     * @param trunkHeight - the height of the tree.
     * @return vector of the top-left corner of the tree.
     */
    protected Vector2 trunkPosition(int x, float trunkHeight) {
        float y = terrain.groundHeightAt(x) - trunkHeight;
        return new Vector2(x, y);
    }

    /**
     * This method is responsible for creating a different trees in its range.
     *
     * @param minX - the left-most point of the tree-range.
     * @param maxX - the right-most point of the tree-range.
     */
    public static void createInRange(int minX, int maxX) {
        TreeFactory treeFactory = new TreeFactory(gameObjects, terrain, PepseGameManager.getSeed());
        int[] fixMinMax = Block.fixRange(minX, maxX);
        minX = fixMinMax[FIRST_INDEX];
        maxX = fixMinMax[SECOND_INDEX];
        for (int x = minX; x < maxX; x += Block.SIZE) {
            if (ForestManager.nextBoolByX(x, PepseGameManager.getSeed(), FOREST_DENSITY_FACTOR)) {
                treeFactory.placeTree(x);
                x += DOUBLE_BLOCK_SIZE;
            }
        }
    }

    /**
     * Generates a single tree.
     *
     * @param treePosition - The vector position of the tree's destination.
     * @param heightParam  - Minimum and maximum expansion values for the tree's height.
     * @param trunkColor   - The desired color for the tree's trunk.
     */
    protected void generateTree(Vector2 treePosition, Vector2 heightParam, Color trunkColor) {
        Block treeBlock = new Block(Vector2.ZERO, new RectangleRenderable(trunkColor));
        treeBlock.setDimensions(trunkDimensions((int) heightParam.x(), (int) heightParam.y(),
                (int) treePosition.x()));
        treeBlock.setTag(TREE_TAG);
        treeBlock.setTopLeftCorner(treePosition);
        gameObjects.addGameObject(treeBlock, Tree.TREE_LAYER);
    }

    /**
     * Generates all the leaves of the given tree.
     *
     * @param treePosition    - The position of the said tree.
     * @param rowNum          - The number of leaves to appear in each column on the tree.
     * @param colNum          - The number of leaves to appear in each row on the tree.
     * @param leafColor       - The desired color of the tree's leaves.
     * @param yPlacementBound - Bound parameter which insures a proper placement for the leaves.
     * @param xPlacementBound - Bound parameter which insures a proper placement for the leaves.
     */
    protected void generateLeaves(Vector2 treePosition, int rowNum, int colNum, Color leafColor,
                                  int yPlacementBound, int xPlacementBound) {
        for (int yPlacement = 1; yPlacement <= rowNum; yPlacement++) {
            for (int xPlacement = 1; xPlacement <= colNum; xPlacement++) {
                initSingleLeaf(treePosition, xPlacement, yPlacement, xPlacementBound, yPlacementBound,
                        leafColor);
            }
        }
    }

    /**
     * Generates a single leaf.
     *
     * @param treePosition    - The position of the said tree.
     * @param xPlacement      - The horizontal Placement of the leaf relatively to the other leaves.
     * @param yPlacement      - The vertical Placement of the leaf relatively to the other leaves.
     * @param yPlacementBound - Bound parameter which insures a proper placement for the leaves.
     * @param xPlacementBound - Bound parameter which insures a proper placement for the leaves.
     */
    private void initSingleLeaf(Vector2 treePosition, int xPlacement, int yPlacement, int xPlacementBound,
                                int yPlacementBound, Color color) {
        float leavesX = treePosition.x() - alignToCenter();
        float leavesY = treePosition.y() / LEAF_HEIGHT_FACTOR;
        boolean alignment = yPlacement < yPlacementBound || xPlacement != xPlacementBound;
        if (alignment && ForestManager.nextBoolByX((int) treePosition.x(), seed, FOREST_DENSITY_FACTOR)) {
            leafAttributes(leavesX, leavesY, xPlacement, yPlacement, color);
        }
    }

    /**
     * Accumulates the attributes needed for creating a single leaf: creating the leaf, adding it to game,
     * and physics issues.
     *
     * @param leavesX    - The horizontal location of the leaf.
     * @param leavesY    - The vertical location of the leaf.
     * @param xPlacement - The horizontal location of the tree.
     * @param yPlacement - The vertical location of the tree.
     */
    private void leafAttributes(float leavesX, float leavesY, float xPlacement, float yPlacement,
                                Color color) {
        Leaf oakLeaf = new Leaf(Vector2.ZERO, Vector2.ONES.mult(Block.SIZE),
                new RectangleRenderable(color));
        oakLeaf.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        oakLeaf.setTopLeftCorner(new Vector2(leavesX + (Block.SIZE * xPlacement),
                leavesY + (Block.SIZE * yPlacement)));
        oakLeaf.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(oakLeaf, Tree.LEAF_LAYER);
    }

    /**
     * Aligns the entire set of leafs relatively to the tree's trunk.
     *
     * @return integer noting the correction needed to the horizontal placement of the leaves.
     */
    private int alignToCenter() {
        return (int) Math.ceil((float) LEAVES_COL_NUM / 2f) * Block.SIZE;
    }


}

