package pepse.world.trees;

import java.util.Random;

/**
 * Determines where and how the trees should appear.
 */
public class ForestManager {

    /**
     * Determines the seed to be used in the creation of the trees.
     *
     * @param x          - vertical location of the potential tree.
     * @param randomSeed - integer.
     * @return Random-type object.
     */
    private static Random randomByDirection(int x, int randomSeed) {
        Random rand;
        if (x >= 0) rand = new Random(randomSeed);
        else {
            rand = new Random(-1 * randomSeed);
        }
        return rand;
    }

    /**
     * Returns a random boolean variable depended on a location.
     *
     * @param x          - the vertical coordinate.
     * @param randomSeed - seed to be used in the random number generator.
     * @param bound      - the range of number.
     * @return boolean.
     */
    public static boolean nextBoolByX(int x, int randomSeed, int bound) {
        Random rand = randomByDirection(x, randomSeed);
        x = Math.abs(x);
        for (int i = 0; i < x; i++) {
            rand.nextInt(bound);
        }
        return rand.nextInt(bound) % bound == 0;
    }

    /**
     * Returns a random integer variable depended on a location.
     *
     * @param xLocation  - the vertical coordinate.
     * @param randomSeed - seed to be used in the random number generator.
     * @param bound      - the range of number.
     * @return boolean.
     */
    public static int nextIntByX(int xLocation, int bound, int randomSeed) {
        Random rand = new Random(randomSeed);
        xLocation = Math.abs(xLocation);
        for (int i = 0; i < xLocation; i++) {
            rand.nextInt(bound);
        }
        return rand.nextInt(bound);
    }

}
