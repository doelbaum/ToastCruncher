package badmoustacheproductions.toastcruncher;

/**
 * Created by LJBFe on 2017-04-24.
 */

public class Grass extends GameObject {

    Grass(float worldStartX, float worldStartY, char type) {
        final float height = 1;
        final float width = 1;

        setHeight(height);
        setWidth(width);

        setType(type);

        // Choose a bitmap
        setBitmapName("turf");

        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();

        setTransversable();
    }

    public void update(long fps, float gravity) {
        // TODO: Something? - 2017-04-24
    }
}
