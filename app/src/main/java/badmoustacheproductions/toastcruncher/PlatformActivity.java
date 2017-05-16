package badmoustacheproductions.toastcruncher;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class PlatformActivity extends Activity {

    // Object to handle the view
    private PlatformView platformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display Object
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a point object
        Point resolution = new Point();
        display.getSize(resolution);

        // Set the view for our Game
        platformView = new PlatformView(this, resolution.x, resolution.y);

        // Make the platform view the main view for the Activity
        setContentView(platformView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        platformView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        platformView.resume();
    }


}