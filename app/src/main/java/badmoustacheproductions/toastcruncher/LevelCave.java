package badmoustacheproductions.toastcruncher;

import java.util.ArrayList;

/**
 * Created by LJBFe on 2017-04-24.
 */

public class LevelCave extends LevelData{
    LevelCave(){
        tiles = new ArrayList<String>();
        this.tiles.add("..............................................");
        this.tiles.add(".............................c................");
        this.tiles.add("...................d.........1................");
        this.tiles.add("..........c...................................");
        this.tiles.add("..........1..........c.................d......");
        this.tiles.add("..p.u................1...............u........");
        this.tiles.add("..111111............................u1........");
        this.tiles.add("...................................u11......x.");
        this.tiles.add("...................................111........");
        this.tiles.add(".......g.......................g.u1111........");
        this.tiles.add("...............fffff........e....111111...e...");
        this.tiles.add("12345671234567123456712345671234567123456712345");

        backgroundDataList = new ArrayList<BackgroundData>();
        // speeds less than 2 cause issues
        this.backgroundDataList.add(new BackgroundData("skyline", true, -1, 3, 18, 10, 15));
        this.backgroundDataList.add(new BackgroundData("grass", true, 1, 20, 24, 24, 4));
    }
}
