package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by LJBFe on 2017-05-07.
 */

public class Background {

    Bitmap bitmap;
    Bitmap bitmapReversed;

    int width;
    int height;

    boolean reversedFirst;
    int xClip; // Controls where the bitmaps are clipped each frame
    float y;
    float endY;
    int z;

    float speed;
    boolean isParallax; // Not currently in use

    Background(Context context, int yPixelsPerMetre, int screenWidth, BackgroundData data) {
        int resID = context.getResources().getIdentifier(data.bitmapName, "drawable", context.getPackageName());
        bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        // Which version of background is currently drawn first
        reversedFirst = false;

        // Init the anim variables
        xClip = 0; // start at 0 obviously
        y = data.startY;
        endY = data.endY;
        z = data.layer;
        isParallax = data.isParallax;
        speed = data.speed; // The scrolling background speed

        // Scale the background to fit the screen
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, data.height * yPixelsPerMetre, true);

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        // Create a mirror image of the background
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1); // Horizontal mirror effect
        bitmapReversed = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
