package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

/**
 * Responsible for informative text objects in the game.
 */
public class UIText extends GameObject {

    public static final float TEXT_SIZE = 25;
    protected static final Vector2 TEXT_DIMENSIONS = new Vector2(TEXT_SIZE, TEXT_SIZE);

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     */
    public UIText(Vector2 topLeftCorner, String text, int info) {
        super(topLeftCorner, TEXT_DIMENSIONS, textTORenderer(text, info));
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

    }

    /**
     * A dds the object to game.
     * @param text - The text which needs to appear before the info.
     * @param info - Integer. The info itself.
     * @return a pointer to the new object.
     */
    protected static Renderable textTORenderer(String text, int info) {
        return new TextRenderable(String.format(text + "%d", info));
    }
}
