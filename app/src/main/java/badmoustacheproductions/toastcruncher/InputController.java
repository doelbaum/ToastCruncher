package badmoustacheproductions.toastcruncher;


import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by doelb on 2017-04-24.
 */

public class InputController {
    Rect left;
    Rect right;
    Rect jump;
    Rect shoot;
    Rect pause;

    InputController(int screenWidth, int screenHeight){
        //configure the buttons
        int buttonWidth = screenWidth /8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        left = new Rect(buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth,
                screenHeight - buttonPadding);

        right = new Rect(buttonWidth + buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth + buttonPadding + buttonWidth,
                screenHeight - buttonPadding);

        jump = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding - buttonHeight - buttonPadding);

        shoot = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding);

        pause = new Rect(screenWidth - buttonPadding,
                buttonWidth,
                buttonPadding,
                buttonPadding + buttonHeight);
    }

    public ArrayList getButtons(){
        ArrayList<Rect> currentButtonList = new ArrayList<>();

        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(jump);
        currentButtonList.add(shoot);
        currentButtonList.add(pause);

        return currentButtonList;
    }

    public void handleInput(MotionEvent motionEvent, LevelManager lm, SoundManager sm, Viewport vp){
        int pointerCount = motionEvent.getPointerCount();

        for(int i = 0; i < pointerCount; i++){
            int x = (int)motionEvent.getX(i);
            int y = (int)motionEvent.getY(i);

            if(lm.isPlaying()){
                switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN:
                        if(right.contains(x,y)){
                            lm.player.setPressingRight(true);
                            lm.player.setPressingLeft(false);
                        } else if(left.contains(x,y)){
                            lm.player.setPressingRight(false);
                            lm.player.setPressingLeft(true);
                        }else if(jump.contains(x,y)){
                            lm.player.startJump(sm);
                        } else if(shoot.contains(x,y)){

                        } else if(pause.contains(x,y)){
                            lm.switchPlayingStatus();
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP:
                        if(right.contains(x,y)){
                            lm.player.setPressingLeft(false);
                        }else if(left.contains(x,y)){
                            lm.player.setPressingLeft(false);
                        }else if(shoot.contains(x,y)){
                            //handle shooting here
                        }else if(jump.contains(x,y)){
                            //handle more jumping sutff here later
                        }
                        break;
                }
            }else{
                switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        if(pause.contains(x,y)){
                            lm.switchPlayingStatus();;
                            Log.w("pause hit:","DOWN");
                        }
                        break;
                }
            }
        }
    }
}
