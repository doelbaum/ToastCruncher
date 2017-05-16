package badmoustacheproductions.toastcruncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by LJBFe on 2017-04-24.
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
    ArrayList<Background> backgrounds;

    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapsArray;

    public LevelManager(Context context,
                        int pixelsPerMeter,
                        int screenWidth,
                        InputController ic,
                        String level,
                        float px,
                        float py) {
        this.level = level;

        switch (level) {
            case "LevelCave":
                levelData = new LevelCave();
                break;

            // TODO: Make more levels you dumb fuck - 2017-04-24
        }

        // Setup Game Objects
        gameObjects = new ArrayList<>();
        bitmapsArray = new Bitmap[25]; // Holds one of each type

        loadMapData(context, pixelsPerMeter, px, py);
        loadBackgrounds(context, pixelsPerMeter, screenWidth);

        // set Guards waypoints
        setWaypoints();

        //playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public Bitmap getBitmap(char blockType) {
        int index;
        switch (blockType) {
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
            case 'g':
                index = 7;
                break;
            case 'f':
                index = 8;
                break;
            case '2':
                index = 9;
                 break;
            case '3':
                index = 10;
                break;
            case '4':
                index = 11;
                break;
            case '5':
                index = 12;
                break;
            case '6':
                index = 13;
                break;
            case '7':
                index = 14;
                break;
            case 'w':
                index = 15;
                break;
            case 'x':
                index = 16;
                break;
            case 'l':
                index = 17;
                break;
            case 'r':
                index = 18;
                break;
            case 's':
                index = 19;
                break;
            case 'm':
                index = 20;
                break;
            case 'z':
                index = 21;
                break;
            default:
                index = 0;
                break;
        }
        return bitmapsArray[index];
    }

    public int getBitmapIndex(char blockType) {
        int index;
        switch (blockType) {
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
            case 'g':
                index = 7;
                break;
            case 'f':
                index = 8;
                break;
            case '2':
                index = 9;
                break;
            case '3':
                index = 10;
                break;
            case '4':
                index = 11;
                break;
            case '5':
                index = 12;
                break;
            case '6':
                index = 13;
                break;
            case '7':
                index = 14;
                break;
            case 'w':
                index = 15;
                break;
            case 'x':
                index = 16;
                break;
            case 'l':
                index = 17;
                break;
            case 'r':
                index = 18;
                break;
            case 's':
                index = 19;
                break;
            case 'm':
                index = 20;
                break;
            case 'z':
                index = 21;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }

    private void loadMapData(Context context, int pixelsPerMeter, float px, float py) {

        char c;
        int currentIndex = -1;

        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for(int i = 0; i < mapHeight; i++) {
            for(int j = 0; j < mapWidth; j++) {
                c = levelData.tiles.get(i).charAt(j);

                if(c != '.') {
                    currentIndex++;
                    switch (c) {
                        case '1':
                            // Add the Grass
                            gameObjects.add(new Grass(j, i, c));
                            break;
                        case 'p':
                            // Add teh player
                            gameObjects.add(new Player(context, px, py, pixelsPerMeter));
                            playerIndex = currentIndex;
                            player = (Player)gameObjects.get(playerIndex);
                            break;
                        case 'c':
                            // Add a coing to the gameObjects
                            gameObjects.add(new Coin(j, i, c));
                            break;
                        case 'u':
                            // Add the Gun Upgrade
                            gameObjects.add(new MachineGunUpgrade(j, i, c));
                            break;
                        case 'e':
                            // Add an Extra Life
                            gameObjects.add(new ExtraLife(j, i, c));
                            break;
                        case 'd':
                            // Add a Drone
                            gameObjects.add(new Drone(j, i, c));
                            break;
                        case 'g':
                            // Add a guard
                            gameObjects.add(new Guard(context, j, i, c, pixelsPerMeter));
                            break;
                        case 'f':
                            // Add the fire
                            gameObjects.add(new Fire(context, j, i, c, pixelsPerMeter));
                            break;
                        case '2':
                            // Add the Snow Tile
                            gameObjects.add(new Snow(j, i, c));
                            break;
                        case '3':
                            // Add the Brick Tile
                            gameObjects.add(new Brick(j, i, c));
                            break;
                        case '4':
                            // Add the Coal Tile
                            gameObjects.add(new Coal(j, i, c));
                            break;
                        case '5':
                            // Add the Concrete Tile
                            gameObjects.add(new Concrete(j, i, c));
                            break;
                        case '6':
                            // Add the Scorched Tile
                            gameObjects.add(new Scorched(j, i, c));
                            break;
                        case '7':
                            // Add the Stone Tile
                            gameObjects.add(new Stone(j, i, c));
                            break;
                        case 'w':
                            // Add the Tree to the gameObjects
                            gameObjects.add(new Tree(j, i, c));
                            break;
                        case 'x':
                            // Add the alternate tree to the gameObjects
                            gameObjects.add(new TreeTwo(j, i, c));
                            break;
                        case 'l':
                            // Add the lampost to the gameObjects
                            gameObjects.add(new Lampost(j, i, c));
                            break;
                        case 'r':
                            // Add the stalactite to the gameobjects
                            gameObjects.add(new Stalactite(j, i, c));
                            break;
                        case 's':
                            // Add the stalagmite to the gameObjects
                            gameObjects.add(new Stalagmite(j, i, c));
                            break;
                        case 'm':
                            // Add the cart to the gameobjects
                            gameObjects.add(new Cart(j, i, c));
                            break;
                        case 'z':
                            // add the boulders to the gameobject
                            gameObjects.add(new Boulder(j, i, c));
                            break;
                    }
                    // Look to ensure that the indexed bitmap has already been loaded
                    if(bitmapsArray[getBitmapIndex(c)] == null) {
                        bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(), pixelsPerMeter);
                    }
                }
            }
        }
    }

    public void switchPlayingStatus() {
        playing = !playing;
        if(playing) {
            gravity = 6;
        } else {
            gravity = 0;
        }
    }

    public void setWaypoints() {
        // Loop through all game objects looking for guards
        for(GameObject guard : this.gameObjects) {
            if(guard.getType() == 'g') {
                // Set waypoints for the guard, as well as find the tiles beneath the guard (to the level designer, remember that the guard needs someplace to walk)
                int startTileIndex = -1;
                int startGuardIndex = 0;
                float waypointXOne = -1;
                float waypointXTwo = -1;

                for (GameObject tile : this.gameObjects) {
                    startTileIndex++;
                    if(tile.getWorldLocation().y == guard.getWorldLocation().y + 2) {
                        if(tile.getWorldLocation().x == guard.getWorldLocation().x) {
                            // We've now found the tile the guard is standing on.
                            // Now go left as far as possible until you're told you no longer can
                            for (int i = 0; i < 5; i++) { // left for loop
                                if(!gameObjects.get(startTileIndex - i).isTransversable()) {
                                    // Set the left way point
                                    waypointXOne = gameObjects.get(startTileIndex - (i + 1)).getWorldLocation().x;
                                    break; // Leave left for loop
                                } else {
                                    // Set to max 5 tiles if non transversable tile is found
                                    waypointXOne = gameObjects.get(startTileIndex - 5).getWorldLocation().x;
                                }
                            } // end of the left way point
                            for(int i = 0; i < 5; i++) { // right for loop
                                if(!gameObjects.get(startTileIndex + i).isTransversable()) {
                                    // Set the right way point
                                    waypointXTwo = gameObjects.get(startTileIndex + (i - 1)).getWorldLocation().x;
                                    break; // leave left for loop
                                } else {
                                    waypointXTwo = gameObjects.get(startTileIndex + 5).getWorldLocation().x;
                                }
                            } // end of right waypoint

                            Guard g = (Guard) guard;
                            g.setWaypoints(waypointXOne, waypointXTwo);
                        }
                    }
                }
            }
        }
    }

    private void loadBackgrounds(Context context, int pixelsPerMetre, int screenWidth) {
        backgrounds = new ArrayList<Background>();
        // Load the background data into the background objects and put them in the gameobject arraylist
        for(BackgroundData bgData : levelData.backgroundDataList) {
            backgrounds.add(new Background(context, pixelsPerMetre, screenWidth, bgData));
        }
    }
}
