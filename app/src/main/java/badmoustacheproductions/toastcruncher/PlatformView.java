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

import java.security.cert.PolicyNode;
import java.util.ArrayList;

/**
 * Created by doelb on 2017-04-24.
 */

public class PlatformView extends SurfaceView implements Runnable {
    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;

    //for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    //new engine class
    private LevelManager lm;
    private Viewport vp;
    InputController ic;
    SoundManager sm;
    private PlayerState ps;

    PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        //init the drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        vp = new Viewport(screenWidth, screenHeight);

        sm = new SoundManager();
        sm.loadSound(context);

        ps = new PlayerState();
        loadLevel("LevelCave", 15, 2);


    }

    public void loadLevel(String level, float px, float py){

        lm = null;
        lm = new LevelManager(context, vp.getPixelsPerMeterX(), vp.getScreenWidth(), ic, level, px, py);

        ic = new InputController(vp.getScreenWidth(), vp.getScreenHeight());

        vp.setWorldCenter(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);

        PointF location = new PointF(px,py);
        ps.saveLocation(location);
    }

    @Override
    public void run(){
        while(running){
            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            //calculate the fps this frame
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >=1){
                fps= 1000/timeThisFrame;
            }

        }
    }

    private void update(){
        for(GameObject go : lm.gameObjects){
            if (go.isActive()){
                //Clip anything off screen
                if(!vp.clipObjects(go.getWorldLocation().x, go.getWorldLocation().y, go.getWidth(), go.getHeight())){
                    //set visable flasg to true
                    go.setVisable(true);

                    //check for collisions with the player
                    int hit = lm.player.checkCollisions(go.getRectHitbox());
                    if (hit>0){
                        switch(go.getType()){
                            case 'c':
                                sm.playSound("coin_pickup");
                                go.setActive(false);
                                go.setVisable(false);
                                ps.gotCredit();
                                if(hit != 2){
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'u':
                                sm.playSound("gun_upgrade");
                                go.setActive(false);
                                go.setVisable(false);
                                lm.player.bfg.upgradeRateOfFire();
                                ps.increaseFireRate();
                                if(hit != 2){
                                    lm.player.restorePreviousVelocity();
                                }
                                break;
                            case 'e':
                                sm.playSound("extra_life");
                                go.setActive(false);
                                go.setVisable(false);
                                ps.addLife();
                                if(hit != 2){
                                    lm.player.restorePreviousVelocity();
                                }
                                break;
                            case 'd':
                                PointF location;
                                sm.playSound("player+burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);
                                break;
                            default:
                                if(hit==1){
                                    lm.player.setxVelocity(0);
                                    lm.player.setPressingRight(false);
                                }
                                if(hit ==2){
                                    lm.player.isFalling = false;
                                }
                                break;
                        }
                    }

                    if(!lm.isPlaying()){
                        go.update(fps,lm.gravity);
                        if(go.getType() == 'd'){
                            Drone d = (Drone) go;
                            d.setWaypoint(lm.player.getWorldLocation());
                        }
                    }
                }else{
                    //set visable flag to false
                    go.setVisable(false);
                }
            }
        }

        if(lm.isPlaying()){
            //Reset the players location as the center of the viewport
            vp.setWorldCenter(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x,
                    lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
        }
    }

    private void draw(){
        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();

            //clear the last frame
            paint.setColor(Color.argb(255,0,0,255));
            canvas.drawColor(Color.argb(255,0,0,255));

            //draw the gameobjects
            Rect toScreen2d = new Rect();

            //draw a layer at a time
            for(int layer = -1; layer <=1; layer++){
                for(GameObject go : lm.gameObjects){
                    //only draw if visible and this layer
                    if(go.isVisable() && go.getWorldLocation().z == layer){
                        toScreen2d.set(vp.worldToScreen(go.getWorldLocation().x, go.getWorldLocation().y,
                                go.getWidth(),go.getHeight()));

                        if(go.isAnimated()){
                            if(go.getFacing() == 1){
                                //rotate
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1,1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(lm.bitMapsArray[lm.getBitmapIndex(go.getType())],
                                        r.left, r.top,r.width(), r.height(), flipper, true);
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            } else{
                                canvas.drawBitmap(lm.bitMapsArray[lm.getBitmapIndex(go.getType())],
                                        go.getRectToDraw(System.currentTimeMillis()), toScreen2d, paint);
                            }
                        }else{
                            //draw the appropriate bitmap
                            canvas.drawBitmap(lm.bitMapsArray[lm.getBitmapIndex(go.getType())], toScreen2d.left, toScreen2d.top, paint);

                        }
                    }
                }
            }

            //Text for debugging
            if(debugging){
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor((Color.argb(255,255,255,255)));
                canvas.drawText("fps: " + fps, 10,60,paint);

                canvas.drawText("num objects: " + lm.gameObjects.size(), 10,80,paint);

                canvas.drawText("num clipped: " + vp.getNumClipped(), 10,100,paint);

                canvas.drawText("playerX: " + lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, 10,120,paint);

                canvas.drawText("playerY: " + lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, 10,140,paint);

                canvas.drawText("Gravity: " + lm.gravity, 10,160,paint);
                canvas.drawText("X Velocity: " + lm.gameObjects.get(lm.playerIndex).getxVelocity(), 10,180,paint);
                canvas.drawText("Y Velocity: " + lm.gameObjects.get(lm.playerIndex).getxVelocity(), 10,200,paint);
                vp.resetNumClipped();
            } //End if(debugging)


            paint.setColor(Color.argb(80,255,255,255));

            ArrayList<Rect> buttonsToDraw;
            buttonsToDraw = ic.getButtons();

            for(Rect rect : buttonsToDraw){
                RectF rf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
                canvas.drawRoundRect(rf, 15f, 15f, paint);
            }

            //draw the Pause Screen
            if(!this.lm.isPlaying()){
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));

                paint.setTextSize(120);
                canvas.drawText("Paused", vp.getScreenWidth() / 2, vp.getScreenHeight()/2, paint);
            }
            //unlock the canvas
            ourHolder.unlockCanvasAndPost(canvas);


        }
    }

    public void pause(){
        running = false;

        try{
            gameThread.join();
        } catch(InterruptedException e){
            Log.e("error", "failed to pause thread");
        }
    }

    public void resume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if(lm != null){
            ic.handleInput(motionEvent,lm,sm,vp);
        }
        //invalidate();

        // switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
        //    case MotionEvent.ACTION_DOWN:
        //        lm.switchPlayingStatus();
        //        break;
        //  }
        return true;
    }

}
