package badmoustacheproductions.toastcruncher;

import android.graphics.PointF;
import android.provider.Settings;

/**
 * Created by LJBFe on 2017-05-03.
 */

public class Drone extends GameObject{

    long lastWaypointSetTime;
    PointF currentWaypoint;

    final float MAX_X_VELOCITY = 3;
    final float MAX_Y_VELOCITY = 3;

    Drone(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        setBitmapName("drone");
        setMoves(true);
        setActive(true);
        setVisible(true);

        currentWaypoint = new PointF();

        // Where the drone starts
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
        setFacing(RIGHT);
    }

    public void update(long fps, float gravity) {
        if(currentWaypoint.x > getWorldLocation().x) {
            setxVelocity(MAX_X_VELOCITY);
        } else if (currentWaypoint.x < getWorldLocation().x) {
            setxVelocity(-MAX_X_VELOCITY);
        } else {
            setxVelocity(0);
        }

        if(currentWaypoint.y >= getWorldLocation().y) {
            setyVelocity(MAX_Y_VELOCITY);
        } else if (currentWaypoint.y < getWorldLocation().y) {
            setyVelocity(-MAX_Y_VELOCITY);
        } else {
            setyVelocity(0);
        }

        move(fps);

        setRectHitbox();
    }

    public void setWaypoint(Vector2Point playerLocation) {
        if(System.currentTimeMillis() > lastWaypointSetTime + 2000) {
            lastWaypointSetTime = System.currentTimeMillis();
            currentWaypoint.x = playerLocation.x;
            currentWaypoint.y = playerLocation.y;
        }
    }
}
