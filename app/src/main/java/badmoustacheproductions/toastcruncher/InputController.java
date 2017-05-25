package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.sql.Struct;
import java.util.ArrayList;

/**
 * Created by LJBFe on 2017-04-24.
 */

public class InputController {

    Button left;
    Button right;
    Button jump;
    Button shoot;
    Button pause;

    InputController(int screenWidth, int screenHeight, Context context) {
        // Configure the buttons
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        left = new Button();
        right = new Button();
        jump = new Button();
        shoot = new Button();
        pause = new Button();


        left.rect = new Rect(buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth,
                screenHeight - buttonPadding);
        left.buttontype = 'l';
        left.setBitmap(context, "lbutton");

        right.rect = new Rect(buttonWidth + buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth + buttonPadding + buttonWidth,
                screenHeight - buttonPadding);
        right.buttontype = 'r';
        right.setBitmap(context, "rbutton");

        jump.rect = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding - buttonHeight - buttonPadding);
        jump.buttontype = 'j';
        jump.setBitmap(context, "upbutton");

        shoot.rect = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding);
        shoot.buttontype = 's';
        shoot.setBitmap(context, "shoot");

        pause.rect = new Rect(screenWidth - buttonPadding - buttonWidth,
                buttonPadding,
                screenWidth - buttonPadding,
                buttonPadding + buttonHeight);
        pause.buttontype = 'p';
        pause.setBitmap(context, "pause");


    }

    public ArrayList getButtons() {
        ArrayList<Button> currentButtonList = new ArrayList<>();

        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(jump);
        currentButtonList.add(shoot);
        currentButtonList.add(pause);

        return currentButtonList;
    }

    public void handleInput(MotionEvent motionEvent, LevelManager lm, SoundManager sm, Viewport vp) {
        int pointerCount = motionEvent.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            if(lm.isPlaying()) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN:
                        if(right.rect.contains(x, y)) {
                            lm.player.setPressingRight(true);
                            lm.player.setPressingLeft(false);
                            //vp.moveViewportRight(lm.mapWidth);
                        } else if (left.rect.contains(x, y)) {
                            lm.player.setPressingRight(false);
                            lm.player.setPressingLeft(true);
                            //vp.moveViewportLeft();
                        } else if (jump.rect.contains(x, y)) {
                            lm.player.startJump(sm);
                        } else if (shoot.rect.contains(x, y)) {
                            if(lm.player.pullTrigger()) {
                                sm.playSound("shoot");
                            }
                        } else if (pause.rect.contains(x, y)) {
                            lm.switchPlayingStatus();
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP:
                        if(right.rect.contains(x, y)) {
                            lm.player.setPressingRight(false);
                        } else if (left.rect.contains(x, y)) {
                            lm.player.setPressingLeft(false);
                        }
                        break;
                }
            } else {
                switch (motionEvent.getAction() & motionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if(pause.rect.contains(x, y)) {
                            lm.switchPlayingStatus();
                        }
                        break;
                }
            }
        }
    }
}
