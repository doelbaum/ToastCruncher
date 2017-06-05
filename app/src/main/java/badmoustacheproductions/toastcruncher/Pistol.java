package badmoustacheproductions.toastcruncher;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by LJBFe on 2017-05-25.
 */

public class Pistol extends Weapon {
    private int maxBullets = 12;
    private int numBullets;
    private int nextBullet;
    private int rateOfFire = 1; // Bullets per second
    private long lastShotTime;
    private int damage = 50;

    private CopyOnWriteArrayList<Bullet> bullets;

    int speed = 25;

    Pistol() {
        bullets = new CopyOnWriteArrayList<Bullet>();
        lastShotTime = -1;
        nextBullet = -1;
    }

    public void update(long fps, float gravity) {
        // update all the bullets
        for(Bullet bullet: bullets) {
            bullet.update(fps, gravity);
        }
    }

    public void hideBullet(int index) {
        // TODO: Watch for the bullets that are out of range - 2017-04-27
        bullets.get(index).hideBullet();
    }

    public boolean attack(float ownerX, float ownerY, int ownerFacing, float ownerHeight) {
        boolean shotFired = false;
        if(System.currentTimeMillis() - lastShotTime > 1000/rateOfFire) {
            // Spawn another bullet
            nextBullet++;

            if (numBullets >= maxBullets) {
                numBullets = maxBullets;
            }
            if (nextBullet == maxBullets) {
                nextBullet = 0;
            }

            lastShotTime = System.currentTimeMillis();
            bullets.add(nextBullet, new Bullet(ownerX, (ownerY + ownerHeight/3), speed, ownerFacing));
            shotFired = true;
            numBullets++;
        }
        return shotFired;
    }

//    public void upgradeRateOfFire() {
//        rateOfFire += 2;
//    }

    // Getters and Setters
    public int getRateOfFire() { return rateOfFire; }
    public void setRateOfFire(int rate) { rateOfFire = rate; }
    public int getNumBullets() { return numBullets; }
    public float getBulletX(int bulletIndex) {
        if(bullets != null && bulletIndex < numBullets) {
            return bullets.get(bulletIndex).getX();
        }
        return -1f;
    }
    public float getBulletY(int bulletIndex) {
        if(bullets != null) {
            return bullets.get(bulletIndex).getY();
        }
        return -1f;
    }
    public int getDirection(int index) {
        // TODO: Watch for the bullets that are out of range - 2017-04-27
        return bullets.get(index).getDirection();
    }
}
