package badmoustacheproductions.toastcruncher;

/**
 * Created by LJBFe on 2017-05-01.
 */

public class ExtraLife extends GameObject {

    ExtraLife(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 0.8f;
        final float WIDTH = 0.65f;

        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);

        // Set the bitmap
        setBitmapName("bread");

        // Where does the tile start
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {}
}
