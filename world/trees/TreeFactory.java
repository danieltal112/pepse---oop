package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import pepse.world.Terrain;

/**
 * Responsible for generating and adding new trees to the simulation.
 */
public class TreeFactory {

    private static final int TYPE_OPTIONS_NUM = 2;
    private final GameObjectCollection gameObjects;
    private final Terrain terrain;
    private final int seed;

    /**
     * Constructor for the TreeFactory.
     *
     * @param collection  - The collection of all the objects currently in the game.
     * @param gameTerrain - The game's terrain.
     * @param randomSeed  - The seed (generated randomly) which used to create all the pseudo-random
     *                    generations in the game.
     */
    public TreeFactory(GameObjectCollection collection, Terrain gameTerrain, int randomSeed) {
        gameObjects = collection;
        terrain = gameTerrain;
        seed = randomSeed;
    }

    /**
     * The method randomly selects between predetermined number of tree types and returns one Tree object
     * which is a Tree decorated by one of the decorator strategies, or decorated by two randomly
     * selected strategies, or decorated by one of the decorator strategies and a pair of additional two
     * decorator strategies.
     */
    public void placeTree(float locationX) {
        int coordinate = (int) Math.floor(locationX);
        int option = ForestManager.nextIntByX(coordinate, TYPE_OPTIONS_NUM, seed);
        switch (option) {
            case 0:
                new OakTree(gameObjects, coordinate, terrain);
                return;
            case 1:
                new EucalyptusTree(gameObjects, coordinate, terrain);
                return;
            default:
        }
    }
}

