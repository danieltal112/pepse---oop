package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.Random;

public class PepseGameManager extends GameManager {
    private static final float CYCLE_LENGTH = 30;
    public static final int INIT_FLY_COUNTER_VAL = 100;
    private static final String AVATAR_TAG = "avatar";
    private static final String LOCATION_TXT = "Location: ";
    public static final int FIRST_INDEX = 0;
    public static final int SECOND_INDEX = 1;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    private static GameObjectCollection gameObjects;
    private static Avatar avatar;

    private Terrain terrain;
    private WindowController windowController;
    private int horizontalWindowSize;
    private Vector2 initAvatarPlacement;
    private FlyCounter flyCounter;
    private final Counter currentFlightDuration = new Counter(INIT_FLY_COUNTER_VAL);
    private CollectionManager collectionManager;
    private UIText locationPresenter;


    private static final Random rand = new Random();
    private static final int forestSeed = rand.nextInt();

    /**
     * Initializes the game, including the avatar and the landscape.
     *
     * @param imageReader      - ImageReader object, used to render the game objects.
     * @param soundReader      - SoundReader Object, used to supply the sounds in the game.
     * @param inputListener    - InputListener object, used to get input from the user.
     * @param windowController - WindowController object.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.horizontalWindowSize = (int) windowController.getWindowDimensions().x();
        gameObjects = gameObjects();
//        Initialize sky:
        Sky.create(gameObjects, windowController.getWindowDimensions(), Layer.BACKGROUND);
//        Initialize the ground:
        createGround();
//        Initialize night:
        Night.create(gameObjects, Layer.FOREGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);
//        Initialize the sun and the sun's halo:
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND, windowController.getWindowDimensions()
                , CYCLE_LENGTH);
        SunHalo.create(gameObjects, Layer.BACKGROUND, sun, HALO_COLOR);
//        Initialize trees:
        Tree tree = new Tree(gameObjects, terrain);
        Tree.createInRange((int) initAvatarPlacement.x() - horizontalWindowSize,
                (int) (initAvatarPlacement.x() + horizontalWindowSize));
//        Initialize the avatar
        avatar = Avatar.create(gameObjects, Layer.DEFAULT, initAvatarPlacement, inputListener, imageReader);
        turnCameraOn();
        avatar.setTag(AVATAR_TAG);
//        Initialize Fly duration counter:
        this.flyCounter = initFlyCounter();
        this.locationPresenter = initLocationPresenter();
//        Initialize LandscapeManager:
        this.collectionManager = new CollectionManager(horizontalWindowSize, terrain, avatar, gameObjects);
        collisionManagement();
    }

    /**
     * Initializes the flightCounter.
     *
     * @return a pointer to the new object.
     */
    private FlyCounter initFlyCounter() {
        Vector2 textPlacement = new Vector2(Block.SIZE, Block.SIZE);
        FlyCounter counter = new FlyCounter(currentFlightDuration, textPlacement);
        gameObjects.addGameObject(counter, Layer.FOREGROUND);
        return counter;
    }

    /**
     * Initializes the Location presenter.
     *
     * @return a pointer to the new object.
     */
    private UIText initLocationPresenter() {
        int location =
                (int) ((avatar.getCenter().x() - initAvatarPlacement.x() - 0.5 * Block.SIZE) / Block.SIZE);

        Vector2 textPlacement = new Vector2(Block.SIZE, Block.SIZE + 2 * UIText.TEXT_SIZE);
        UIText locationText = new UIText(textPlacement, LOCATION_TXT, location);
        gameObjects.addGameObject(locationText, Layer.FOREGROUND);
        return locationText;
    }

    /**
     * Initializes the ground.
     */
    private void createGround() {
        terrain = new Terrain(gameObjects, Layer.STATIC_OBJECTS, windowController.getWindowDimensions(),
                rand.nextInt());
        int AvatarXPlacement = fixAvatarAlignment();
        this.initAvatarPlacement = new Vector2(AvatarXPlacement,
                terrain.groundHeightAt(AvatarXPlacement) - Avatar.AVATAR_SIZE);
        terrain.createInRange(AvatarXPlacement - horizontalWindowSize / 2,
                (AvatarXPlacement + horizontalWindowSize));
    }

    /**
     * Fixes the placement of the avatar, so it would appear in the center of the screen.
     *
     * @return the new location.
     */
    private int fixAvatarAlignment() {
        int start = Block.SIZE;
        while (start < horizontalWindowSize / 2) {
            start += Block.SIZE;
        }
        return start;
    }

    /**
     * Sets the camera to follow the avatar.
     */
    private void turnCameraOn() {
        Vector2 placement = new Vector2(initAvatarPlacement.mult(-1));
        Vector2 start = new Vector2(windowController.getWindowDimensions().mult(0.5f).add(placement));
        setCamera(new Camera(avatar,            //object to follow
                start,    //follow the center of the object
                windowController.getWindowDimensions(),  //widen the frame a bit
                windowController.getWindowDimensions()   //share the window dimensions
        ));
    }

    /**
     * Checks that the avatar isn't under the surface of the terrain.
     *
     * @return true if the avater is underground; False otherwise.
     */
    private boolean isAvatarUnderground() {
        return avatar.getTopLeftCorner().y() > terrain.groundHeightAt(avatar.getTopLeftCorner().x() + (Block.SIZE));
    }

    /**
     * Relocates the avatar back to the surface of the terrain.
     */
    private void relocateAvatar() {
        if (isAvatarUnderground()) {
            float yValue = terrain.groundHeightAt(avatar.getTopLeftCorner().x()) - Avatar.AVATAR_SIZE;
            Vector2 reLocation = new Vector2(avatar.getTopLeftCorner().x(), yValue);
            avatar.setTopLeftCorner(reLocation);
        }
    }

    @Override
    public void update(float deltaTime) {
        gameObjects.removeGameObject(flyCounter, Layer.FOREGROUND);
        flyCounter = initFlyCounter();
        gameObjects.removeGameObject(locationPresenter, Layer.FOREGROUND);
        locationPresenter = initLocationPresenter();

        super.update(deltaTime);
        collectionManager.updateLandscape();

        relocateAvatar();
        if (Avatar.getAvatarFliesFlag()) {
            if (currentFlightDuration.value() == 0) Avatar.setAvatarFalls(true);
            else currentFlightDuration.decrement();
        } else {
            currentFlightDuration.reset();
            currentFlightDuration.increaseBy(INIT_FLY_COUNTER_VAL);
        }
    }

    /**
     * Handles the collision of the different layers used in the game.
     */
    private static void collisionManagement() {
//        collide leaves, tree-trucks and ground:
        gameObjects.layers().shouldLayersCollide(Tree.LEAF_LAYER, Layer.STATIC_OBJECTS, true);
//        collide The avatar, the ground and tree's trunks:
        gameObjects.layers().shouldLayersCollide(Layer.DEFAULT, Layer.STATIC_OBJECTS, true);
        gameObjects.layers().shouldLayersCollide(Layer.DEFAULT, Tree.TREE_LAYER, true);
    }

    /**
     * Getter for the pseudo-random seed.
     *
     * @return the seed as integer.
     */
    public static int getSeed() {
        return forestSeed;
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }


}
