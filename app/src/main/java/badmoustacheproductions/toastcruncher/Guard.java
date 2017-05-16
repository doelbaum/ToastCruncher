package badmoustacheproductions.toastcruncher;

import android.content.Context;

/**
 * Created by LJBFe on 2017-05-03.
 */

public class Guard extends GameObject {

    // guards will only move on the x-axis between two waypoints
    private float waypointXStart; // always on the left
    private float waypointXEnd; // always on the right
    private int currentWaypoint;
    final float MAX_X_VELOCITY = 3;

    Guard(Context context, float worldStartX, float worldStartY, char type, int pixelfPerMetre) {
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 5;
        final String BITMAP_NAME = "guard";
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);

        setBitmapName("guard");
        // Now for all the other stuoid shit that the game needs
        setMoves(true);
        setActive(true);
        setVisible(true);

        // Now to set shit up to allow animation
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelfPerMetre, true);

        // Where the tile starts
        setWorldLocation(worldStartX, worldStartY, 0);
        setxVelocity(-MAX_X_VELOCITY);
        currentWaypoint = 1;
    }

    public void setWaypoints(float xOne, float xTwo) {
        waypointXStart = xOne;
        waypointXEnd = xTwo;
    }

    public void update(long fps, float gravity) {
        if(currentWaypoint == 1) { // We're heading left Jim
            if(getWorldLocation().x <= waypointXStart) { // We're at the starting waypoint Jim
                currentWaypoint = 2;
                setxVelocity(MAX_X_VELOCITY);
                setFacing(RIGHT);
            }
        }

        if(currentWaypoint == 2) { // We're heading right Jim
            if(getWorldLocation().x >= waypointXEnd) { // We're at the end waypoint Jim
                currentWaypoint = 1;
                setxVelocity(-MAX_X_VELOCITY);
                setFacing(LEFT);
            }
        }

        move(fps);
        setRectHitbox();
    }
}
