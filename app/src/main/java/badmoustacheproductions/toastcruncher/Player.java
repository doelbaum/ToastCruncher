package badmoustacheproductions.toastcruncher;


import android.content.Context;

/**
 * Created by doelb on 2017-04-24.
 */

public class Player extends GameObject {
    final float MAX_X_VELOCITY = 10;
    boolean isPressingRight = false;
    boolean isPressingLeft = false;
    public boolean isFalling;
    private boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700; //TODO: magic number (this is 7 tenths of a second

    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMeter){
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        //standing still to start
        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);
        isFalling = false;

        //now the other attributes
        setMoves(true);
        setActive(true);
        setVisable(true);
        //isPressingRight = true;
        rectHitboxHead = new RectHitbox();
        rectHitboxFeet = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();

        setType('p');

        //chose a bitmap
        setBitmapName("player");
        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 5;

        setAnimFPS(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMeter, true);

        setWorldLocation(worldStartX, worldStartY, 0);
    }

    public void update(long fps, float gravity){
        if(isPressingRight){
            this.setxVelocity(MAX_X_VELOCITY);
        }else if(isPressingLeft){
            this.setxVelocity(-MAX_X_VELOCITY);
        }else{
            this.setxVelocity(0);
        }

        //which way is the player facing
        if(this.getxVelocity() > 0){
            setFacing(RIGHT);
        } else if(this.getxVelocity() < 0){
            setFacing(LEFT);
        } //if 0 then unchanged

        //jumping and gravity
        if(isJumping){
            long timejumping = System.currentTimeMillis() - jumpTime;
            if(timejumping < maxJumpTime){
                if(timejumping < timejumping / 2){
                    this.setyVelocity(-gravity); //on the way up
                } else if(timejumping > maxJumpTime / 2){
                    this.setyVelocity(gravity); // going down
                }
            } else{
                isJumping = false;
            }
        }else{
            this.setyVelocity(gravity);
            //README!!! REMOVE THE NEXT LINE TO MAKE THE GAME EASIER
            isFalling = true;
        }

        this.move(fps);

        //TODO: Sooooooo inefficient
        //update all the hitboxes
        Vector2Point location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        //update the players feet
        rectHitboxFeet.top = ly + getHeight() * 0.95f;
        rectHitboxFeet.left = lx + getWidth() * 0.2f;
        rectHitboxFeet.bottom = ly + getHeight() * 0.98f;
        rectHitboxFeet.right = ly + getHeight() * 0.95f;

        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + getWidth() * 0.4f;
        rectHitboxHead.bottom = ly + getHeight() * 0.2f;
        rectHitboxHead.right = ly + getHeight() * 0.6f;

        rectHitboxLeft.top = ly + getHeight() * 0.2f;
        rectHitboxLeft.left = lx + getWidth() * 0.2f;
        rectHitboxLeft.bottom = ly + getHeight() * 0.8f;
        rectHitboxLeft.right = ly + getHeight() * 0.3f;

        rectHitboxRight.top = ly + getHeight() * 0.2f;
        rectHitboxRight.left = lx + getWidth() * 0.8f;
        rectHitboxRight.bottom = ly + getHeight() * 0.8f;
        rectHitboxRight.right = ly + getHeight() * 0.7f;

    }

    public int checkCollisions(RectHitbox rectHitbox){
        int collided = 0;

        //the left
        if(this.rectHitboxLeft.intersects(rectHitbox)){
            this.setWorldLocationX(rectHitbox.right - getWidth() * 0.2f);
            collided = 1;
        }

        //the right
        if(this.rectHitboxRight.intersects(rectHitbox)){
            this.setWorldLocationX(rectHitbox.left - getWidth() * 0.8f);
            collided = 1;
        }

        //the feet
        if(this.rectHitboxFeet.intersects(rectHitbox)){
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
        }

        //the head
        if(this.rectHitboxHead.intersects(rectHitbox)){
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }

        return collided;
    }

    public void restorePreviousVelocity(){
        if(!isJumping && !isFalling){
            if(getFacing() == LEFT){
                isPressingLeft = true;
                setxVelocity(-MAX_X_VELOCITY);
            } else{
                isPressingRight = true;
                setxVelocity((MAX_X_VELOCITY));
            }
        }
    }

    public void setPressingRight(boolean isPressingRight){
        this.isPressingRight = isPressingRight;
    }

    public void setPressingLeft(boolean isPressingLeft){
        this.isPressingLeft = isPressingLeft;
    }

    public void startJump(SoundManager sm){
        if(!isFalling){
            if(!isJumping){
                isJumping = true;
                jumpTime = System.currentTimeMillis();
                sm.playSound("jump");
            }
        }
    }
}
