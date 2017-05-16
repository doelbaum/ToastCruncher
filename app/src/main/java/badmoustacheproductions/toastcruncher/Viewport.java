package badmoustacheproductions.toastcruncher;

import android.graphics.Rect;

/**
 * Created by LJBFe on 2017-04-24.
 */

public class Viewport {
    private Vector2Point currentViewportWorldCenter;
    private Rect convertedRect;
    private int pixelsPerMeterX;
    private int pixelsPerMeterY;
    private int screenXResolution;
    private int screenYResolution;
    private int screenCenterX;
    private int screenCenterY;
    private int metersToShowX;
    private int metersToShowY;
    private int numClipped;

    Viewport(int x, int y){
        screenXResolution = x;
        screenYResolution = y;

        screenCenterX = screenXResolution / 2;
        screenCenterY = screenYResolution / 2;

        pixelsPerMeterX = screenXResolution / 32; // TODO: Deal with the Magic Numbers - 2017-04-24
        pixelsPerMeterY = screenYResolution / 18;

        metersToShowX = 34;
        metersToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCenter = new Vector2Point();
    }

    void setWorldCenter(float x, float y) {
        currentViewportWorldCenter.x = x;
        currentViewportWorldCenter.y = y;
    }

    public int getScreenWidth() {
        return screenXResolution;
    }

    public int getScreenHeight() {
        return screenYResolution;
    }

    public int getPixelsPerMeterX() { return pixelsPerMeterX; }

    public int getPixelsPerMeterY() { return pixelsPerMeterY; }

    public int getyCentre() { return screenCenterY; }

    public float getViewportWorldCentreY() { return currentViewportWorldCenter.y; }


    public Rect worldToScreen(float objX, float objY, float objWidth, float objHeight) {
        int left = (int)(screenCenterX - ((currentViewportWorldCenter.x - objX) * pixelsPerMeterX));
        int top = (int)(screenCenterY - ((currentViewportWorldCenter.y - objY) * pixelsPerMeterY));

        int right = (int)(left + (objWidth * pixelsPerMeterX));
        int bottom = (int)(top + (objHeight * pixelsPerMeterY));

        convertedRect.set(left, top, right, bottom);

        return convertedRect;
    }

    public boolean clipObjects(float objX, float objY, float objWidth, float objHeight) {
        boolean clipped = true;

        if(objX - objWidth < currentViewportWorldCenter.x + (metersToShowX / 2)) {
            if(objX + objWidth > currentViewportWorldCenter.x - (metersToShowX / 2)) {
                if(objY - objHeight < currentViewportWorldCenter.y + (metersToShowY / 2)) {
                    if(objY + objHeight > currentViewportWorldCenter.y - (metersToShowY / 2)) {
                        clipped = false;
                    }
                }
            }
        }
        // For Debugging
        if(clipped) {
            numClipped++;
        }
        return clipped;
    }

    public void moveViewportRight(int maxWidth) {
        if(currentViewportWorldCenter.x < maxWidth - (metersToShowX/2) + 3) {
            currentViewportWorldCenter.x += 1;
        }
    }

    public void moveViewportLeft() {
        if(currentViewportWorldCenter.x > (metersToShowX/2) - 3) {
            currentViewportWorldCenter.x -= 1;
        }
    }

    public void moveViewportUp() {
        if(currentViewportWorldCenter.y > (metersToShowY/2) - 3) {
            currentViewportWorldCenter.y -= 1;
        }
    }

    public void moveViewportDown(int maxHeight) {
        if(currentViewportWorldCenter.y < maxHeight - (metersToShowY/2) + 3) {
            currentViewportWorldCenter.y += 1;
        }
    }

    public int getNumClipped() {
        return  numClipped;
    }

    public void resetNumClipped() { numClipped = 0; }

}
