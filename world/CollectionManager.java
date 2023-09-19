package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;
import pepse.world.trees.Tree;

import static pepse.PepseGameManager.FIRST_INDEX;
import static pepse.PepseGameManager.SECOND_INDEX;
import static pepse.world.trees.Tree.LEAF_LAYER;
import static pepse.world.trees.Tree.TREE_LAYER;

/**
 * This class is responsible to keep generating the landscape as the avatar moves forwards or backwards.
 */
public class CollectionManager {
    private static final int REMOVE_RANGE_FACTOR = 2;
    private final int chunkSize;
    private final Terrain terrain;
    private final Avatar avatar;
    private final GameObjectCollection gameObjects;
    private final Counter chunkCounter = new Counter(1);

    /**
     * The constructor to the collection manager.
     *
     * @param windowDimensionsX - a vector which holds the game's window dimensions.
     * @param terrain           - the terrain of the game.
     * @param avatar            - the avatar of the game.
     * @param gameObjects       - the collection of objects in the game.
     */
    public CollectionManager(float windowDimensionsX, Terrain terrain, Avatar avatar,
                             GameObjectCollection gameObjects) {
        this.chunkSize = (int) (windowDimensionsX / 2);
        this.terrain = terrain;
        this.avatar = avatar;
        this.gameObjects = gameObjects;
    }

    /**
     * Updates the landscape on each update.
     */
    public void updateLandscape() {
        addLandscape();
        removeObj();
    }

    /**
     * Adds terrain and trees to the game if needed.
     */
    private void addLandscape() {
        int[] range = findRange();
        terrain.createInRange(range[FIRST_INDEX], range[SECOND_INDEX]);
        Tree.createInRange(range[FIRST_INDEX], range[SECOND_INDEX]);
    }

    /**
     * Determines the range which needed to be handled.
     *
     * @return - Integer range indicating the most right and most left edges of the range.
     */
    private int[] findRange() {
        int[] range = new int[2];
        int avatarLocX = (int) Math.floor(avatar.getCenter().x());
        int currentChunk = (int) Math.floor((float) avatarLocX / chunkSize);
        if (currentChunk > chunkCounter.value()) {
            chunkCounter.increment();
            range[FIRST_INDEX] = avatarLocX + chunkSize;
            range[SECOND_INDEX] = avatarLocX + (2 * chunkSize);
        }
        if (currentChunk < chunkCounter.value()) {
            chunkCounter.decrement();
            range[0] = avatarLocX - (2 * chunkSize);
            range[1] = avatarLocX - (chunkSize);
        }
        return range;
    }

    /**
     * Removes an unnecessary object from all the relevant layers.
     *
     * @param obj - the object which needed to be removed.
     */
    private void removeByLayer(GameObject obj) {
        gameObjects.removeGameObject(obj, Layer.STATIC_OBJECTS);
        gameObjects.removeGameObject(obj, TREE_LAYER);
        gameObjects.removeGameObject(obj, LEAF_LAYER);
    }

    /**
     * Removes all the objects outside the camera's range of sight.
     */
    private void removeObj() {
        float avatarX = avatar.getCenter().x();
        int currentChunk = (int) ((avatarX / chunkSize) * chunkSize);
        for (var obj : gameObjects) {
            if (obj.getCenter().x() < currentChunk - (REMOVE_RANGE_FACTOR * chunkSize)) {
                removeByLayer(obj);
            }
        }
        for (var obj : gameObjects) {
            if (obj.getCenter().x() > currentChunk + (REMOVE_RANGE_FACTOR * chunkSize)) {
                removeByLayer(obj);
            }
        }
    }
}



