package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by doelb on 2017-05-19.
 */

public class Button {
    Rect rect;
    Character buttontype;
    public Bitmap bitmap;

    public void setBitmap(Context context, String bitmapName) {
        // Make a resource ID from the bitmap Name
        int resID = context.getResources().getIdentifier(bitmapName, "drawable", context.getPackageName());

        // Create the bitmap
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

    }
}
