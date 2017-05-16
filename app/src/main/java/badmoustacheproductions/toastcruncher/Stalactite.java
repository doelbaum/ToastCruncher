package badmoustacheproductions.toastcruncher;

import java.util.Random;

/**
 * Created by LJBFe on 2017-05-07.
 */

public class Stalactite extends GameObject {

    Stalactite(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 3;
        final float WIDTH = 2;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        setBitmapName("stalactite");
        setActive(false);

        // Randomly set the Stalactite either infront or behind the player
        Random rand = new Random();
        if(rand.nextInt(2) == 0) {
            setWorldLocation(worldStartX, worldStartY, -1);
        } else {
            setWorldLocation(worldStartX, worldStartY, 1);
        }
    }

    public void update(long fps, float gravity) {}
}
