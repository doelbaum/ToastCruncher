package badmoustacheproductions.toastcruncher;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by doelb on 2017-04-24.
 */

public class LevelManager {
    private String level;

    int mapWidth;
    int mapHeight;

    Player player;
    int playerIndex;

    private boolean playing;
    float gravity;

    LevelData levelData;
    ArrayList<GameObject> gameObjects;

    ArrayList<Rect> currentButtons;
    Bitmap[] bitMapsArray;

    LevelManager(Context context, int pixelsPerMeter, int screenWidth, InputController ic, String level, float px, float py){
        this.level = level;

        switch(level){
            case "LevelCave":
                levelData = new LevelCave();
                break;

            //TODO: add more levels here
        }

        //setup game objects
        gameObjects = new ArrayList<>();
        bitMapsArray = new Bitmap[25]; //holds one of each type

        loadMapData(context, pixelsPerMeter, px, py);

        playing = true;
    }

    public boolean isPlaying(){
        return playing;
    }

    public void switchPlayingStatus(){
        playing = !playing;
        if(playing){
            gravity = 6;
        } else{
            gravity = 0;
        }
    }

    public Bitmap getBitMap(char blockType){
        int index;
        switch(blockType){
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            case 'c':
                index = 3;
                break;
            case 'u':
                index = 4;
                break;
            case 'e':
                index = 5;
                break;
            case 'd':
                index = 6;
                break;
            default:
                index = 0;
                break;
        }
        return bitMapsArray[index];
    }

    public int getBitmapIndex(char blockType){
        int index;

        switch(blockType){
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            case 'c':
                index = 3;
                break;
            case 'u':
                index = 4;
                break;
            case 'e':
                index = 5;
                break;
            case 'd':
                index = 6;
                break;
            default:
                index = 0;
                break;
        }

        return index;
    }

    private void loadMapData(Context context, int pixelsPerMeter, float px, float py){
        char c;
        int currentIndex = -1;

        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for(int i=0; i<mapHeight; i++){
            for(int j = 0; j < mapWidth; j++){
                c =levelData.tiles.get(i).charAt(j);

                if(c != '.'){
                    currentIndex++;

                    switch (c){
                        case '1':
                            gameObjects.add(new Grass(j,i,c));
                            break;
                        case 'p':
                            gameObjects.add(new Player(context, px, py, pixelsPerMeter));
                            playerIndex = currentIndex;
                            player = (Player)gameObjects.get(playerIndex);
                            break;
                        case 'c':
                            gameObjects.add(new Coin(j,i,c));
                            break;
                        case 'u':
                            gameObjects.add(new MachineGunUpgrade(j,i,c));
                            break;
                        case 'e':
                            gameObjects.add(new ExtraLife(j,i,c));
                            break;
                        case 'd':
                            gameObjects.add(new Drone(j,i,c));
                            break;
                    }

                    //look to ensure that the indexed bitmap has already been loaded
                    if (bitMapsArray[getBitmapIndex(c)] == null){
                        bitMapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context,
                                gameObjects.get(currentIndex).getBitmapName(), pixelsPerMeter);
                    }
                }
            }
        }
    }
}
