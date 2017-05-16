package badmoustacheproductions.toastcruncher;

/**
 * Created by doelb on 2017-05-15.
 */

public class SlopedHitbox {
    private Vector2Point leftPoint;
    private Vector2Point rightPoint;
    private float slope;
    private float yIntercept;

    SlopedHitbox(Vector2Point leftPoint, Vector2Point rightPoint){
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
        slope = (leftPoint.y - rightPoint.y)/ (leftPoint.x - rightPoint.x);
        yIntercept = leftPoint.y -slope*leftPoint.x;
    }

    public float SlopedY(float x){
        return(slope*x + yIntercept);
    }
    //y = mx + b
}
