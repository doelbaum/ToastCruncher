package badmoustacheproductions.toastcruncher;


import android.graphics.Rect;

/**
 * Created by doelb on 2017-04-24.
 */

public class Viewport {

    private Vector2Point currentViewportWorldCenter;
    private Rect convertedRect;
    private int pixelsPerMeterX;
    private int pixelsPerMeterY;
    private int screenResolutionX;
    private int screenResolutionY;
    private int screenCenterX;
    private int screenCenterY;
    private int metersToShowX;
    private int metersToShowY;
    private int numClipped;

    Viewport(int x, int y){
        screenResolutionX = x;
        screenResolutionY = y;

        screenCenterX = screenResolutionX / 2;
        screenCenterY = screenResolutionY / 2;

        pixelsPerMeterX = screenResolutionX / 32;  //TODO: magic numbers
        pixelsPerMeterY = screenResolutionY / 18;

        metersToShowX = 34;
        metersToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCenter = new Vector2Point();
    }

    void setWorldCenter(float x, float y){
        currentViewportWorldCenter.x = x;
        currentViewportWorldCenter.y = y;
    }

    public int getScreenWidth(){
        return screenResolutionX;
    }

    public int getScreenHeight(){
        return screenResolutionY;
    }

    public int getPixelsPerMeterX(){
        return pixelsPerMeterX;
    }

    public int getPixelsPerMeterY(){
        return pixelsPerMeterY;
    }

    public Rect worldToScreen(float objX, float objY, float objWidth, float objHeight){
        int left = (int)(screenCenterX - (
                (currentViewportWorldCenter.x - objX) * pixelsPerMeterX));
        int top = (int)(screenCenterY - (
                (currentViewportWorldCenter.y - objY) * pixelsPerMeterY));

        int right = (int)(left + (objWidth * pixelsPerMeterX));
        int bottom = (int)(top + (objHeight * pixelsPerMeterY));

        convertedRect.set(left, top, right, bottom);

        return convertedRect;
    }

    public boolean clipObjects(float objX, float objY, float objWidth, float objHeight){
        boolean clipped = true;

        if(objX - objWidth < currentViewportWorldCenter.x + (metersToShowX/2)){
            if(objX + objWidth > currentViewportWorldCenter.x - (metersToShowX/2)){
                if(objY - objHeight < currentViewportWorldCenter.y + (metersToShowY /2)){
                    if(objY + objHeight > currentViewportWorldCenter.y - (metersToShowY /2)){
                        clipped = false; //dont clip, its on the screen
                    }
                }
            }
        }

        //for debugging
        if(clipped){
            numClipped++;
        }
        return clipped;
    }
    public int getNumClipped(){return numClipped;}

    public void resetNumClipped(){numClipped = 0;}
}