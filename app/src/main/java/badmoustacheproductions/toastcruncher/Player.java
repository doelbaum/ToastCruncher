package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by LJBFe on 2017-04-24.
 */

public class Player extends GameObject {

    final float MAX_X_VELOCITY = 10;
    boolean isPressingRight = false;
    boolean isPressingLeft = false;

    public boolean isFalling;
    private boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700; // // TODO: Magic Number (this is 7/10ths of a second) - 2017-04-25

    public MachineGun bfg;

    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    Player(Context context, float worldStartX, float worldStartY, int pixelsPerMeter) {
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        // Standing still to start
        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);
        isFalling = false;

        // Now the other attributes
        setMoves(true);
        setActive(true);
        setVisible(true);

        bfg = new MachineGun();

        isPressingRight = true;
        rectHitboxHead = new RectHitbox();
        rectHitboxFeet = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();


        setType('p');

        // Choose a bitmap
        setBitmapName("bandron");

        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 5;

        // Set objects to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMeter, true);

        // X and y lovations for construction
        setWorldLocation(worldStartX, worldStartY, 0);
    }

    public void update(long fps, float gravity) {
        if(isPressingRight) {
            this.setxVelocity(MAX_X_VELOCITY);
        } else if (isPressingLeft) {
            this.setxVelocity(-MAX_X_VELOCITY);
        } else {
            this.setxVelocity(0);
        }

        // Which way is the player facing
        if (this.getxVelocity() > 0) {
            // Clearly facing right
            setFacing(RIGHT);
        } else if (this.getxVelocity() < 0) {
            setFacing(LEFT);
        }

        // Jumping and Gravity (What an uppity bitch)
        if(isJumping) {
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if(timeJumping < maxJumpTime) {
                if(timeJumping < maxJumpTime / 2) {
                    this.setyVelocity(-gravity); // Heading upwards asshole
                } else if (timeJumping > maxJumpTime / 2) {
                    this.setyVelocity(gravity); // Heading back down
                }
            } else {
                isJumping = false;
            }
        } else {
            this.setyVelocity(gravity);
            // Read Me: Remove the next line to make the game easier (ir. allows double jumps)
            isFalling = true;
        }

        bfg.update(fps, gravity);
        this.move(fps);

        // Update all the Hitboxes
        // // TODO: Inefficient AF - 2017-04-26
        Vector2Point location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        // Update the player feet hitbox
        rectHitboxFeet.top = ly + getHeight() * 0.95f;
        rectHitboxFeet.left = lx + getWidth() * 0.2f;
        rectHitboxFeet.bottom = ly + getHeight() * 0.98f;
        rectHitboxFeet.right = lx + getWidth() * 0.8f;

        // Update the player head hitbox
        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + getWidth() * 0.4f;
        rectHitboxHead.bottom = ly + getHeight() * 0.2f;
        rectHitboxHead.right = lx + getWidth() * 0.6f;

        // Update the player left hitbox
        rectHitboxLeft.top = ly + getHeight() * 0.2f;
        rectHitboxLeft.left = lx + getWidth() * 0.2f;
        rectHitboxLeft.bottom = ly + getHeight() * 0.8f;
        rectHitboxLeft.right = lx + getWidth() * 0.3f;

        // Update the player right hitbox
        rectHitboxRight.top = ly + getHeight() * 0.2f;
        rectHitboxRight.left = lx + getWidth() * 0.8f;
        rectHitboxRight.bottom = ly + getHeight() * 0.8f;
        rectHitboxRight.right = lx + getWidth() * 0.7f;
    }

    public boolean pullTrigger() {
        return bfg.shoot(this.getWorldLocation().x, this.getWorldLocation().y, getFacing(), getHeight());
    }

    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;

        // the left
        if(this.rectHitboxLeft.intersects(rectHitbox)) {
            // Left collided
            this.setWorldLocationX(rectHitbox.right - getWidth() * 0.2f);
            collided = 1;
        }

        // the right
        if(this.rectHitboxRight.intersects(rectHitbox)) {
            // Right collided
            this.setWorldLocationX(rectHitbox.left - getWidth() * 0.8f);
            collided = 1;
        }

        // the feet
        if(this.rectHitboxFeet.intersects(rectHitbox)) {
            // Feet collided
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
        }

        // the head
        if(this.rectHitboxHead.intersects(rectHitbox)) {
            // Feet collided
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }

        return collided;
    }

    public void restorePreviousVelocity() {
        if(!isJumping && !isFalling) {
            if(getFacing() == LEFT) {
                isPressingLeft = true;
                setxVelocity(-MAX_X_VELOCITY);
            } else {
                isPressingRight = true;
                setxVelocity(MAX_X_VELOCITY);
            }
        }
    }

    public void setPressingRight(boolean isPressingRight) { this.isPressingRight = isPressingRight; }
    public void setPressingLeft(boolean isPressingLeft) { this.isPressingLeft = isPressingLeft; }
    public void startJump(SoundManager sm) {
        if (!isFalling) {
            if(!isJumping) {
                isJumping = true;
                jumpTime = System.currentTimeMillis();
                sm.playSound("jump");
            }
        }
    }
}
