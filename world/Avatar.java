package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.gui.*;

import java.awt.event.KeyEvent;

import static pepse.world.trees.Tree.TREE_TAG;


/**
 * The class is responsible for everything related to the user's avatar object.
 */
public class Avatar extends GameObject {

    private static final float VELOCITY_X = 250;
    private static final float VELOCITY_Y = -400;
    private static final float GRAVITY = 300;
    public static final float AVATAR_SIZE = 30;
    private static final double STANDING_TIME_PACE = 1;
    private static final double WALKING_TIME_PACE = 0.14;
    private static final double FLYING_TIME_PACE = 0.1;

    private static UserInputListener inputListener;
    private static boolean avatarFlies = false;
    private static boolean avatarFalls = false;
    private static AnimationRenderable standAnimation;
    private static AnimationRenderable walkAnimation;
    private static AnimationRenderable flyAnimation;
    private static final Renderable[] standRenderable = new Renderable[1];
    private static final Renderable[] walkRenderable = new Renderable[3];
    private static final Renderable[] flyRenderable = new Renderable[3];

    /**
     * Constructor for an Avatar type object.
     *
     * @param topLeftCorner - The top-left corner to which the object will be rendered.
     * @param dimensions    - The object's dimensions (size).
     * @param renderable    - Renderable object which will be used to render the Avatar's image.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        transform().setAccelerationY(GRAVITY);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }

    /**
     * Creates new Avatar object.
     *
     * @param gameObjects   - The objects-collection to which the avatar will be added.
     * @param layer         - The layer on the game screen to which the object will present.
     * @param topLeftCorner - The top-left corner of the object on the screen.
     * @param inputListener - UI object used to control the object.
     * @param imageReader   - ImageReader object used to render the object on the screen.
     * @return - a pointer to the created object.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer,
                                Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        initRenderables(imageReader);
        standAnimation = new AnimationRenderable(standRenderable, STANDING_TIME_PACE);
        walkAnimation = new AnimationRenderable(walkRenderable, WALKING_TIME_PACE);
        flyAnimation = new AnimationRenderable(flyRenderable, FLYING_TIME_PACE);
        Avatar avatar = new Avatar(topLeftCorner, new Vector2(AVATAR_SIZE, AVATAR_SIZE), standAnimation);
        gameObjects.addGameObject(avatar, layer);
        Avatar.inputListener = inputListener;

        return avatar;
    }

    /**
     * Initiates all the Renderable-arrays necessary to render the avatar.
     *
     * @param imageReader - ImageReader instance used to read the avatar's images.
     */
    private static void initRenderables(ImageReader imageReader) {
        standRenderable[0] = imageReader.readImage("pepse/Girl-Melee_Static.png", true);

        walkRenderable[0] = imageReader.readImage("pepse/Girl-Melee_LeftFoot.png", true);
        walkRenderable[1] = imageReader.readImage("pepse/Girl-Melee_Static.png", true);
        walkRenderable[2] = imageReader.readImage("pepse/Girl-Melee_RightFoot.png", true);

        flyRenderable[0] = imageReader.readImage("pepse/Girl-Melee_Flying_UmbrellaLeft.png", true);
        flyRenderable[1] = imageReader.readImage("pepse/Girl-Melee_Flying_HandUp.png", true);
        flyRenderable[2] = imageReader.readImage("pepse/Girl-Melee_Flying_UmbrellaRight.png", true);
    }

    /**
     * Boolean flag which indicates if the avatar is currently flying.
     *
     * @return True if it flies; Otherwise returns false.
     */
    public static boolean getAvatarFliesFlag() {
        return avatarFlies;
    }

    /**
     * Boolean flag which sets the avatar to a falling state.
     * @param flag - boolean.
     */
    public static void setAvatarFalls(boolean flag) {
        avatarFalls = flag;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (getVelocity().approximatelyEquals(Vector2.ZERO)) {
            if (other.getTag().equals(TREE_TAG))
                setCenter(getCenter().add(new Vector2(Block.SIZE, Vector2.ZERO.y())));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            renderer().setIsFlippedHorizontally(true);
            renderer().setRenderable(walkAnimation);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            renderer().setIsFlippedHorizontally(false);
            renderer().setRenderable(walkAnimation);

        }
        transform().setVelocityX(xVel);

        if (!inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && !inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            renderer().setRenderable(standAnimation);

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
            avatarFlies = true;
            if (!avatarFalls) {
                renderer().setRenderable(flyAnimation);
                transform().setVelocityY(0.45f * VELOCITY_Y);
                new ScheduledTask(this, .5f, false,
                        () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
                return;
            }
        }
        if (getVelocity().y() == 0) {
            avatarFlies = false;
            avatarFalls = false;
            if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
                renderer().setRenderable(flyAnimation);
                transform().setVelocityY(VELOCITY_Y);
            }
        }
    }


}
