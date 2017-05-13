package badmoustacheproductions.toastcruncher;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;

public class MainActivity extends Activity {
    // an object to handle the view
    private PlatformView platformView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //get a display object
        Display display = getWindowManager().getDefaultDisplay();

        //load the resolution to a point object
        Point resolution  = new Point();
        display.getSize(resolution);

        //finally we'll set the view for our game
        platformView = new PlatformView(this, resolution.x, resolution.y);

        //make the platformView the main view for the activity
        setContentView(platformView);
    }

    @Override
    protected void onPause(){
        super.onPause();
        platformView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        platformView.resume();
    }


}