package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by LJBFe on 2017-04-27.
 */

public class Animation {
    Bitmap bitmapSheet;
    String bitmapName;
    private Rect sourceRect;
    private int frameCount;
    private int currentFrame;
    private long frameTicker;
    private int framePeriod;
    private int frameWidth;
    private int frameHeight;
    int pixelsPerMetre;

    Animation(Context context, String bitmapName, float frameHeight, float frameWidth, int animFps, int frameCount, int pixelsPerMetre) {

        this.currentFrame = 0;
        this.frameCount = frameCount;
        this.frameWidth = (int) frameWidth * pixelsPerMetre;
        this.frameHeight = (int) frameHeight * pixelsPerMetre;
        sourceRect = new Rect(0, 0, this.frameWidth, this.frameHeight);

        framePeriod = 1000 / animFps;
        frameTicker = 01;
        this.bitmapName = "" + bitmapName;
        this.pixelsPerMetre = pixelsPerMetre;
    }

    public Rect getCurrentFrame(long time, float xVelocity, boolean moves) {
        if(xVelocity != 0 || moves == false) {
            // Only animate if the object is moving, or is an inanimate animated object
            if(time > frameTicker + framePeriod) {
                frameTicker = time;
                currentFrame++;
                if(currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
        }
        // Update the values for the next frame
        this.sourceRect.left = currentFrame * frameWidth;
        this.sourceRect.right = this.sourceRect.left + frameWidth;

        return sourceRect;
    }
}
