package badmoustacheproductions.toastcruncher;

import android.content.Context;

/**
 * Created by LJBFe on 2017-05-07.
 */

public class Fire extends GameObject{
    Fire(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre) {

        final int ANIMATION_FPS = 3;
        final int ANIMATION_FRAME_COUNT = 3;
        final String BITMAP_NAME = "fire";

        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);
        // The Game Engine stuff
        setMoves(false);
        setActive(true);
        setVisible(true);

        // Set the bitmap
        setBitmapName(BITMAP_NAME);
        // Set the object to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        //setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);

        // Where does the tile start
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {}
}
