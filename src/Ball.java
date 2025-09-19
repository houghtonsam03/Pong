public class Ball {
    float x;
    float y;
    float xvel; // Normalised (abs(xvel,yvel)) = 1
    float yvel; // Normalised (abs(xvel,yvel)) = 1
    int speed;
    Ball(float xPos,float yPos,float xv,float yv,int sp) {
        x = xPos;
        y = yPos;
        xvel = xv;
        yvel = yv;
        speed = sp;
    }
    public void SpeedUp(int increase) {
        speed += increase;
    }
}
