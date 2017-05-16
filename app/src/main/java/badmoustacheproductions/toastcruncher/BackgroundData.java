package badmoustacheproductions.toastcruncher;

/**
 * Created by LJBFe on 2017-05-07.
 */

public class BackgroundData {
    String bitmapName;
    boolean isParallax;
    // layer 0 is the map
    int layer;
    float startY;
    float endY;
    float speed;
    int height;
    int width;

    BackgroundData(String bitmapName, boolean isParallax, int layer, float startY, float endY, float speed, int height) {
        this.bitmapName = bitmapName;
        this.isParallax = isParallax;
        this.layer = layer;
        this.startY = startY;
        this.endY = endY;
        this.speed = speed;
        this.height = height;
    }
}
