package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by LJBFe on 2017-04-24.
 */


// This means it must be inherited
public abstract class GameObject {
    Vector2Point worldLocation;
    private float width;
    private float height;

    private boolean active = true;
    private boolean visible = true;
    private int animFrameCount = 1;
    private char type;

    private String bitmapName;

    private float xVelocity;
    private float yVelocity;
    final int LEFT = -1;
    final int RIGHT = 1;
    private int facing;
    private boolean moves = false;

    private boolean transversable = false;

    // For Animation
    private Animation anim = null;
    private boolean animated;
    private int animFps = 1;

    private RectHitbox rectHitbox = new RectHitbox();

    public abstract void update(long fps, float gravity);

    public String getBitmapName() {
        return bitmapName;
    }

    public Bitmap prepareBitmap(Context context, String bitmapName, int pixelsPerMeter) {
        // Make a resource ID from the bitmap Name
        int resID = context.getResources().getIdentifier(bitmapName, "drawable", context.getPackageName());

        // Create the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        // Scale the bitmap based off of the number of pixels per meter
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(width * animFrameCount * pixelsPerMeter), (int)(height * pixelsPerMeter), false);

        return bitmap;
    }


    public Vector2Point getWorldLocation() {
        return worldLocation;
    }

    public void setWorldLocation(float x, float y, int z) {
        this.worldLocation = new Vector2Point();
        this.worldLocation.x = x;
        this.worldLocation.y = y;
        this.worldLocation.z = z;
    }

    void move(long fps) {
        if(xVelocity != 0) {
            this.worldLocation.x += xVelocity / fps;
        }
        if(yVelocity != 0) {
            this.worldLocation.y += yVelocity / fps;
        }
    }

    public void setRectHitbox() {
        rectHitbox.setTop(worldLocation.y);
        rectHitbox.setLeft(worldLocation.x);
        rectHitbox.setBottom(worldLocation.y + height);
        rectHitbox.setRight(worldLocation.x + width);
    }

    RectHitbox getRectHitbox() {
        return rectHitbox;
    }

    // Getters and Setters
    public void setBitmapName(String bitmapName) {
        this.bitmapName = bitmapName;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public boolean isActive() { return active; }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public char getType() {
        return type;
    }
    public void setType(char type) { this.type = type; }

    public int getFacing() { return facing; }
    public void setFacing(int facing) { this.facing = facing; }
    public float getxVelocity() { return xVelocity; }
    public void setxVelocity(float xVelocity) { if(moves) { this.xVelocity = xVelocity; } }
    public float getyVelocity() { return yVelocity; }
    public void setyVelocity(float yVelocity) { if(moves) { this.yVelocity = yVelocity; } }
    public boolean isMoves() { return moves; }
    public void setMoves(boolean moves) { this.moves = moves; }
    public void setActive(boolean active) {this.active = active; }

    public void setWorldLocationX(float x) { this.worldLocation.x = x; }
    public void setWorldLocationY(float y) { this.worldLocation.y = y; }

    public void setAnimFps(int animFps) { this.animFps = animFps; }
    public void setAnimFrameCount(int animFrameCount) { this.animFrameCount = animFrameCount; }
    public boolean isAnimated() { return animated; }
    public void setAnimated(Context context, int pixelsPerMetre, boolean animated) {
        this.animated = animated;
        this.anim = new Animation(context, bitmapName, height, width, animFps, animFrameCount, pixelsPerMetre);
    }
    public Rect getRectToDraw(long deltaTime) { return anim.getCurrentFrame(deltaTime, xVelocity, isMoves()); }

    public void setTransversable() { transversable = true; }
    public boolean isTransversable() { return transversable; }
}
