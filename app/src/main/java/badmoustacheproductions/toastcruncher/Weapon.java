package badmoustacheproductions.toastcruncher;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by LJBFe on 2017-05-25.
 */

public abstract class Weapon extends GameObject {
    private int currentAmmo;

    public abstract boolean attack(float ownerX, float ownerY, int ownerFacing, float ownerHeight);

    public boolean hasAmmo() {
        return currentAmmo > 0;
    }

}
