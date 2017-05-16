package badmoustacheproductions.toastcruncher;

/**
 * Created by LJBFe on 2017-05-01.
 */

public class MachineGunUpgrade extends GameObject {

    MachineGunUpgrade(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 0.5f;
        final float WIDTH = 0.5f;

        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);

        // Set the bitmap
        setBitmapName("clip");

        // Where does the tile start
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {}
}
