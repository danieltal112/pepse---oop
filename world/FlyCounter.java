package pepse.world;

import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Counter for the flight power remained for the avatar.
 */
public class FlyCounter extends UIText {

    private static final String FLIGHT_TEXT = "Flight power: ";

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     */
    public FlyCounter(Counter flyDurationCounter, Vector2 topLeftCorner) {
        super(topLeftCorner, FLIGHT_TEXT, flyDurationCounter.value());
     }

}
