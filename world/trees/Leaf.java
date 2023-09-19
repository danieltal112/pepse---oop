package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

/**
 * The class is responsible for instances of single leaves.
 */
public class Leaf extends GameObject {
    private static final float SPEED_LEAF_FALL = 120;
    private static final float TRANSITION_TIME = 5;
    private static final float LEFT = -1;
    private static final float RIGHT = 1;
    private static final int LIFE_TIME = 60;
    private static final Float INIT_MOVE_VALUE = 0f;
    private static final Float FINAL_MOVE_VALUE = 12f;
    private static final float LEAF_TRANS_TIME = 5;
    private static final int LEAF_FADE_IN_TIME = 2;
    private static final int RANDOM_WAIT_TIME = 150;
    private static final float WAIT_TIME_EXTENTION = 20f;
    private static final float WAIT_TIME_FACTOR = 100f;
    private static final Float INIT_LEAF_SIZE = 25f;
    private static final Float FINAL_LEAF_SIZE = 35f;
    private static final float MAX_HORIZONTAL_VEL = 40f;
    private static final int HORIZONTAL_RANDOM_BOUND = 4;
    private static final float WAIT_FALL_TIME = 1;
    private static final Float LEAF_TRANS_VAL = 1f;
    private final int DEATH_TIME = 10;

    private final Random random = new Random();


    /**
     * Construct a new Leaf GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        leafTransition(this);
    }

    /**
     * Assigns a chain of initial attributes to a leaf (falling, size, etc.)
     *
     * @param oakLeaf - Leaf object to be transitioned.
     */
    private void leafTransition(Leaf oakLeaf) {
//        Leaf movement:
        new ScheduledTask(oakLeaf,
                (random.nextInt(RANDOM_WAIT_TIME) + WAIT_TIME_EXTENTION) / WAIT_TIME_FACTOR, false,
                () -> leafWindMovement(oakLeaf));
//        change leaf size
        new ScheduledTask(oakLeaf,
                (random.nextInt(RANDOM_WAIT_TIME) + WAIT_TIME_EXTENTION) / WAIT_TIME_FACTOR, false,
                () -> changeLeafSize(oakLeaf));
    }

    /**
     * Determines the horizontal movement of the leaf while it falls.
     *
     * @param oakLeaf - game object representing a leaf.
     */
    private void leafWindMovement(Leaf oakLeaf) {
        new Transition<>(oakLeaf, oakLeaf.renderer()::setRenderableAngle, INIT_MOVE_VALUE, FINAL_MOVE_VALUE
                , Transition.LINEAR_INTERPOLATOR_FLOAT, LEAF_TRANS_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Expands and contracts the given object.
     *
     * @param oakLeaf - the given leaf to be stretched.
     */
    private void changeLeafSize(Leaf oakLeaf) {
        Transition<Float> transition = new Transition<>(oakLeaf,
                (Float width) -> oakLeaf.setDimensions(new Vector2(width, Block.SIZE)), INIT_LEAF_SIZE,
                FINAL_LEAF_SIZE, Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new ScheduledTask(oakLeaf, random.nextInt(LIFE_TIME), false, () -> leafFall(oakLeaf, transition));
    }

    /**
     * Supplementary function for the "HorizontalMovement" method, which randomizing the movement of the leaf.
     *
     * @return A float indicating the direction of the leaf.
     */
    private float leafDirection() {
        if (random.nextBoolean()) {
            return LEFT;
        }
        return RIGHT;
    }

    /**
     * Determines the horizontal movement of the leaf during its fall.
     *
     * @param oakLeaf - the gameObject to be manipulated.
     * @return Transition values.
     */
    private Transition<Float> HorizontalMovement(Leaf oakLeaf) {
        float direction = leafDirection();
        return new Transition<>(oakLeaf, (Float speed) -> oakLeaf.setVelocity(new Vector2(speed,
                SPEED_LEAF_FALL)), direction * MAX_HORIZONTAL_VEL, -direction * MAX_HORIZONTAL_VEL,
                Transition.CUBIC_INTERPOLATOR_FLOAT, (float) random.nextInt(HORIZONTAL_RANDOM_BOUND),
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Determines the changes which should hold while the leaf is falling.
     *
     * @param oakLeaf    - a leaf gameObject.
     * @param transition - the transition which should take place while the leaf is falling.
     */
    private void leafFall(Leaf oakLeaf, Transition<Float> transition) {
        Vector2 leafLocation = new Vector2(oakLeaf.getTopLeftCorner());
        Transition<Float> moveSide = HorizontalMovement(oakLeaf);
        new ScheduledTask(oakLeaf, WAIT_FALL_TIME, false, () -> oakLeaf.removeComponent(transition));

        new Transition<>(oakLeaf, oakLeaf.renderer()::fadeOut, LEAF_TRANS_VAL, LEAF_TRANS_VAL,
                Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE, () -> new ScheduledTask(oakLeaf,
                random.nextInt(DEATH_TIME), false, () -> reBorn(oakLeaf, leafLocation, moveSide)));
    }

    /**
     * The method is responsible for regenerating a given leaf back on the tree.
     *
     * @param leaf - the said leaf.
     * @param leafLocation - the location which the leaf should reappear on.
     * @param moveSide - Transition value for the leaf.
     */
    private void reBorn(Leaf leaf, Vector2 leafLocation, Transition<Float> moveSide) {
        leaf.removeComponent(moveSide);
        leaf.setVelocity(Vector2.ZERO);
        leaf.setTopLeftCorner(leafLocation);
        leaf.renderer().fadeIn(LEAF_FADE_IN_TIME);
        leafTransition(leaf);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.setVelocity(Vector2.ZERO);
    }


}
