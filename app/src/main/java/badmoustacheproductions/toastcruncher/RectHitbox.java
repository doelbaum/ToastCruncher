package badmoustacheproductions.toastcruncher;

/**
 * Created by doelb on 2017-04-26.
 */

public class RectHitbox {

    float top;
    float left;
    float bottom;
    float right;
    float height;

    boolean intersects(RectHitbox rectHitbox){
        boolean hit = false;

        if(this.right > rectHitbox.left && this.left < rectHitbox.right){
            //intersecting on the x
            if(this.top < rectHitbox.bottom && this.bottom > rectHitbox.top){
                hit= true;
            }
        }

        return hit;
    }

    //gettars and settars
    public void setTop(float top){
        this.top = top;
    }

    public void setLeft(float left){
        this.left = left;
    }

    public void setBottom(float bottom){
        this.bottom = bottom;
    }

    public void setRight(float right){
        this.right = right;
    }
    public void setHeight(float height){
        this.height = height;
    }

    public float getLeft(){
        return left;
    }

    public float getRight(){
        return right;
    }

    public float getHeight(){
        return height;
    }



}