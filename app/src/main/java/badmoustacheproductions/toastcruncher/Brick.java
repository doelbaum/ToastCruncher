package badmoustacheproductions.toastcruncher;

/**
 * Created by LJBFe on 2017-05-07.
 */

public class Brick extends GameObject{

    Brick(float worldStartX, float worldStartY, char type) {
        setTransversable();
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("brick");
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity){}
}
