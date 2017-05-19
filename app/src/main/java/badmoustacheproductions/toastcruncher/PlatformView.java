package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by LJBFe on 2017-04-24.
 */

public class PlatformView extends SurfaceView implements Runnable{
    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;

    // For Drawing
    private Paint paint;

    private Canvas canvas;
    private SurfaceHolder ourHolder;

    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    // New Engine Class
    private LevelManager lm;
    private Viewport vp;
    InputController ic;
    SoundManager sm;
    private PlayerState ps;

    PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        // Initialize the drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        vp = new Viewport(screenWidth, screenHeight);

        sm = new SoundManager();
        sm.loadSound(context);
        ps = new PlayerState();

        loadLevel("LevelCave", 15, 2);
    }

    public void loadLevel(String level, float px, float py) {
        lm = null;
        lm = new LevelManager(context, vp.getPixelsPerMeterX(), vp.getScreenWidth(), ic, level, px, py);

        ic = new InputController(vp.getScreenWidth(), vp.getScreenHeight());

        vp.setWorldCenter(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);

        PointF location = new PointF(px, py);
        ps.saveLocation(location);
    }

    @Override // TODO: This is temp code - 2017-04-26
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if(lm != null) {
            ic.handleInput(motionEvent, lm, sm, vp);
        }

        // Invalidate();
        return true;

//        switch (motionEvent.getAction() & motionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                lm.switchPlayingStatus();
//                break;
//        }
//        return true;
    }

    @Override
    public void run() {
        while(running) {
            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            // Calculate the FPS this frame to time out the animations
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1) {
                fps = 1000/timeThisFrame;
            }
        }
    }

    private void update() {
        for(GameObject go : lm.gameObjects) {
            if(go.isActive()) {
                // Clip everything that is off-screen
                if(!vp.clipObjects(go.getWorldLocation().x,
                        go.getWorldLocation().y, go.getWidth(), go.getHeight())) {
                    // Set the visible flag to true
                    go.setVisible(true);

                    // Check collisions
                    int hit = lm.player.checkCollisions(go.getRectHitbox());
                    if(hit > 0) {
                        switch (go.getType()) {
                            case 'c':
                                sm.playSound("coin_pickup");
                                go.setActive(false);
                                go.setVisible(false);
                                ps.gotCredits();
                                // restore the state that was removed by collision detection
                                if(hit != 2) {
                                    lm.player.restorePreviousVelocity();;
                                }
                                break;

                            case 'u':
                                sm.playSound("gun_upgrade");
                                go.setActive(false);
                                go.setVisible(false);
                                lm.player.bfg.upgradeRateOfFire();
                                ps.increaseFireRate();
                                if(hit != 2) {
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'e':
                                sm.playSound("extra_life");
                                go.setActive(false);
                                go.setVisible(false);
                                ps.addLife();
                                if(hit != 2) {
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'd':
                                PointF location;
                                // hit the drone
                                sm.playSound("player_burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);
                                break;

                            case 'g':
                                // Hit by a guard
                                sm.playSound("player_burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);
                                break;

                            case 'f':
                                // Hit the fire
                                sm.playSound("player_burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);

                            default:
                                if( hit == 1 ) {
                                    lm.player.setxVelocity(0);
                                    lm.player.setPressingRight(false);
                                }
                                if ( hit == 2 ) {
                                    lm.player.isFalling = false;
                                }
                                break;
                        }
                    }

                    // Check for bullet collisions
                    for(int i = 0; i < lm.player.bfg.getNumBullets(); i++) {
                        // Make a hitbox out of the current bullet
                        RectHitbox r = new RectHitbox();
                        r.setLeft(lm.player.bfg.getBulletX(i));
                        r.setTop(lm.player.bfg.getBulletY(i));
                        r.setRight(lm.player.bfg.getBulletX(i) + 0.1f);
                        r.setBottom(lm.player.bfg.getBulletY(i) + 0.1f);

                        if(go.getRectHitbox().intersects(r)) {
                            // Collision Detected so the bullet should disappear
                            lm.player.bfg.hideBullet(i);

                            // So what did you hit?
                            if(go.getType() != 'g' && go.getType() != 'd') {
                                sm.playSound("ricochet");
                            } else if (go.getType() == 'g') {
                                // Knock the guard back
                                go.setWorldLocationX(go.getWorldLocation().x + 2 * (lm.player.bfg.getDirection(i)));
                                sm.playSound("hit_guard");
                            } else if (go.getType() == 'd') {
                                // Destroy the drone
                                sm.playSound("explode");

                                // Permanently clip this drone
                                go.setWorldLocation(-100, -100, 0);
                            }
                        }
                    }

                    if(lm.isPlaying()) {
                        // Run any unclipped updates
                        go.update(fps, lm.gravity);

                        if(go.getType() == 'd') {
                            // Let any near by drones know where the player is
                            Drone d = (Drone) go;
                            d.setWaypoint(lm.player.getWorldLocation());
                        }
                    }
                } else {
                    // Set the visible flag to false
                    go.setVisible(false);
                    // Now draw(); gan ignore them
                }
            }
        }
        if(lm.isPlaying()) {
            // Reset the player's location as the centre of the viewport
            vp.setWorldCenter(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
        }
    }

    private void draw() {
        if(ourHolder.getSurface().isValid()) {
            // Lock the canvas
            canvas = ourHolder.lockCanvas();

            // Clear the last frame
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Draw parallax backgrounds from -1 to -3
            drawBackground(0, -3);

            // Drawing code will go here
            Rect toScreen = new Rect();

            // Draw a layer at a time
            for(int layer = -1; layer <= 1; layer++) {
                for(GameObject go : lm.gameObjects) {
                    // Only draw if visible and in this layer
                    if(go.isVisible() && go.getWorldLocation().z == layer) {
                        toScreen.set(vp.worldToScreen(go.getWorldLocation().x, go.getWorldLocation().y, go.getWidth(), go.getHeight()));

                        if(go.isAnimated()) {
                            // Get the next frame and rotate if necessary
                            if(go.getFacing() == 1) {
                                // Rotate
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], r.left, r.top, r.width(), r.height(), flipper, true);
                                canvas.drawBitmap(b, toScreen.left, toScreen.top, paint);
                            } else {
                                // Draw the normal way
                                canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], go.getRectToDraw(System.currentTimeMillis()), toScreen, paint);
                            }
                        } else {
                            // Draw the whole thing
                            canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())], toScreen.left, toScreen.top, paint);
                        }
                    }
                }
            }

            // Draw the Bullets
            paint.setColor(Color.argb(255, 255, 255, 255));
            for(int i = 0; i < lm.player.bfg.getNumBullets(); i++) {
                // Pass in the coords then set the bullet width and height
                toScreen.set(vp.worldToScreen(lm.player.bfg.getBulletX(i), lm.player.bfg.getBulletY(i), 0.25f, 0.05f));
                canvas.drawRect(toScreen, paint);
            }

            // Draw parallax backgrounds from layer 1 to 3
            drawBackground(4, 0);

            // Text for debugging
            if(debugging) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawText("fps:" + fps, 10, 60, paint);

                canvas.drawText("num objects:" + lm.gameObjects.size(), 10, 80, paint);
                canvas.drawText("num clipped:" + vp.getNumClipped(), 10, 100, paint);
                canvas.drawText("playerX:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, 10, 120, paint);
                canvas.drawText("playerY:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, 10, 140, paint);

                canvas.drawText("Gravity:" + lm.gravity, 10, 160, paint);
                canvas.drawText("X Velocity:" + lm.gameObjects.get(lm.playerIndex).getxVelocity(), 10, 180, paint);
                canvas.drawText("Y Velocity:" + lm.gameObjects.get(lm.playerIndex).getyVelocity(), 10, 200, paint);


                // For resetting the number of clipped object each frame
                vp.resetNumClipped();
            }

            // Draw Buttons
            paint.setColor(Color.argb(80, 255, 255, 255));
            ArrayList<Button> buttonsToDraw;
            buttonsToDraw = ic.getButtons();

            for(Button button : buttonsToDraw) {
                RectF rf = new RectF(button.rect.left, button.rect.top, button.rect.right, button.rect.bottom);
                canvas.drawRoundRect(rf, 15f, 15f, paint);
            }

            // Draw paused text
            if (!this.lm.isPlaying()) {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(120);
                canvas.drawText("Paused", vp.getScreenWidth() / 2, vp.getScreenHeight() / 2, paint);
            }

            // Unlock the canvas
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "failed the pause thread");
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void drawBackground(int start, int stop) {
        Rect fromRect1 = new Rect();
        Rect toRect1 = new Rect();
        Rect fromRect2 = new Rect();
        Rect toRect2 = new Rect();

        for(Background bg : lm.backgrounds) {
            if(bg.z < start && bg.z > stop) {
                // Is the layer in the viewport?
                if(!vp.clipObjects(-1, bg.y, 1000, bg.height)) {
                    float floatstartY = ((vp.getyCentre() - ((vp.getViewportWorldCentreY() - bg.y) * vp.getPixelsPerMeterY())));
                    int startY = (int) floatstartY;
                    float floatendY = ((vp.getyCentre() - ((vp.getViewportWorldCentreY() - bg.endY) * vp.getPixelsPerMeterY())));
                    int endY = (int) floatendY;

                    // Define what portion of the bitmaps to capture
                    fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
                    toRect1 = new Rect(bg.xClip, startY, bg.width, endY);

                    fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
                    toRect2 = new Rect(0, startY, bg.xClip, endY);
                }
                if(!bg.reversedFirst) {
                    canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
                    canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
                } else {
                    canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
                    canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
                }

                // Calculate the next position for the backgrounds
                bg.xClip -= lm.player.getxVelocity() / (20 / bg.speed);
                if(bg.xClip >= bg.width) {
                    bg.xClip = 0;
                    bg.reversedFirst = !bg.reversedFirst;
                } else if(bg.xClip <= 0) {
                    bg.xClip = bg.width;
                    bg.reversedFirst = !bg.reversedFirst;
                }
            }
        }
    }
}

