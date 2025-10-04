import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
    //Static values
    static int FPS = 60;
    static float elasticity = 0.9f;
    static float drag = 0.01f;
    static int startSpeed = 5;

    // Constant attributes
    private int gameSpeed = 1;
    private int ballAmount; // Allow multiple balls in play.
    private int ID;

    // Changing attributes
    // All position coordinates are the bottom left corner of an object.
    private float[] BlockerMovement; // {LeftBlockerPos,RightBlockerPos,LeftBlockerVel,RightBlockerVel,LeftBlockerAcc,RightBlockerAcc}
    private int[] ballScore; // Amount of balls scored for L & R. {LScore,RScore}. 
    private boolean gameOver;
    private boolean paused;
    private ArrayList<Ball> toRemove = new ArrayList<>(); // Balls to remove in the future. 

    private boolean leftUpHeld;
    private boolean leftDownHeld;
    private boolean rightUpHeld;
    private boolean rightDownHeld;
    // Classes
    GameManager manager;
    private ArrayList<Ball> balls = new ArrayList<>();

    public Game(int id,int speed,int bAmount,GameManager man) {
        ID = id;
        gameSpeed = speed;
        ballAmount = bAmount;
        manager = man;

        BlockerMovement = new float[6];
        ballScore = new int[2];
        gameOver = false;
        paused = false;
    }
    public void Setup() {
        // Initialise ballPositions.
        float ballX = (PongPanel.GAME_WIDTH-PongPanel.BALL_SIZE)/2;
        float ballY = (PongPanel.GAME_HEIGHT-PongPanel.BALL_SIZE)/2;
        
        // Initialise ballVelocities.
        // Find a random direction for each ball that excludes shooting down and up by a minimum angle difference.
        double minAng = 20d; // angle is the minimum from shooting straight up and down.
        double angRange = 180-2*minAng;
        double ang;
        for (int i=0;i<ballAmount;i++) {
            if (Math.random() < 0.5) { // Randomly chooses direction to the left.
            ang = angRange*Math.random()+(90+minAng); // ang:(90+minAng,270-minAng)
            }
            else {
                ang = angRange*(Math.random()-0.5); // ang:(-90+minAng,90-minAng)
            }
            double rad = Math.toRadians(ang);
            float velX = (float)Math.cos(rad);
            float velY = (float)Math.sin(rad);
            balls.add(new Ball(ballX, ballY, velX, velY,startSpeed));
        }

        // Initialise blockers.
        BlockerMovement[0] = (PongPanel.GAME_HEIGHT-PongPanel.BLOCKER_HEIGHT)/2;
        BlockerMovement[2] = 0;
        BlockerMovement[4] = 0;
        BlockerMovement[1] = BlockerMovement[0];
        BlockerMovement[3] = 0;
        BlockerMovement[5] = 0;
    }
    public void Run() throws Exception{

        while (!gameOver) {
            if (!paused) {
                sendUpdate();
                RunPhysics();
            }
            TimeUnit.MILLISECONDS.sleep(1000/(FPS*gameSpeed));
        }
        sendUpdate();
        manager.GameOver(ID,GameOverUpdate());
    }
    public void RunPhysics() throws Exception {
        for (Ball ball : balls) {
            moveBall(ball);
        }
        removeBalls();
        moveBlockers();
    }
    private void moveBall(Ball ball) throws Exception {
        float newX = ball.x + ball.xvel*ball.speed;
        float newY = ball.y + ball.yvel*ball.speed;
        // Collision with the top of the screen
        if (newY < 0) {
            ball.y = (ball.y-newY); // Bounce the ball based on speed
            ball.yvel = -ball.yvel; // Flip the speed
        }
        // Collision with the bottom of the screen
        else if (newY + PongPanel.BALL_SIZE > PongPanel.GAME_HEIGHT) {
            ball.y = Math.min(PongPanel.GAME_HEIGHT - PongPanel.BALL_SIZE,2*PongPanel.GAME_HEIGHT - newY - PongPanel.BALL_SIZE); // Bounce the ball based on speed
            ball.yvel = -ball.yvel; // Flip the speed
        }
        // Collision with the blocker
        float r = PongPanel.BALL_SIZE/2f;
        float middleX = newX+r;
        float middleY = newY+r;
        float rectX, rectY, rectW = PongPanel.BLOCKER_WIDTH, rectH = PongPanel.BLOCKER_HEIGHT;
        // Right side of pong
        if (middleX > PongPanel.GAME_WIDTH/2f) {
            rectX = PongPanel.R_BLOCKER_X;
            rectY = BlockerMovement[1];
        }
        // Left side of pong
        else {
            rectX = PongPanel.L_BLOCKER_X;
            rectY = BlockerMovement[0];
        }

        float closestX = Math.max(rectX,Math.min(rectX+rectW,middleX));
        float closestY = Math.max(rectY,Math.min(rectY+rectH,middleY));

        float dx = middleX - closestX;
        float dy = middleY - closestY;

        // Check for collision
        if (dx*dx + dy*dy < r*r) {
            float dist = (float)Math.hypot(dx, dy);
            float nx = 0, ny = 0;
            // Corner case
            if ((closestX == rectX || closestX == rectX + rectW) && (closestY == rectY || closestY == rectY + rectH)) {
                nx = dx / dist;
                ny = dy / dist;
            }
            // Inside case
            else if (dist == 0f) {
                float rectHor, rectVer;
                if (ball.xvel > 0) rectHor = rectX; // Left side
                else rectHor = rectX + rectW; // Right side
                if (ball.yvel > 0) rectVer = rectY; // Top side
                else rectVer = rectY + rectH; // Bottom side

                // Find the closest time to exit the blocker in x and y direction.
                float alphaX = (middleX - rectHor) / ball.xvel;
                float alphaY = (middleY - rectVer) / ball.yvel;
                float alpha = Math.min(Math.abs(alphaX), Math.abs(alphaY));
                float e = 0.001f; // Small value to ensure the ball is out of the blocker.

                // Move the ball out of the blocker then run physics again.
                ball.x -= ball.xvel * (alpha+e);
                ball.y -= ball.yvel * (alpha+e);
                moveBall(ball);
                return;
            }
            // Flat case
            else {
                // Left or Right side
                if (closestX == rectX || closestX == rectX + rectW) {
                    nx = -Math.signum(ball.xvel);
                    ny = 0;
                }
                // Top or Bottom side
                else {
                    nx = 0;
                    ny = -Math.signum(ball.yvel);
                }
                
            }
            // Reflect velocity
            float vdotn = ball.xvel * nx + ball.yvel * ny;
            ball.xvel -= 2f * vdotn * nx;
            ball.yvel -= 2f * vdotn * ny;
            // Move ball out of collision
            middleX = closestX + nx * r;
            middleY = closestY + ny * r;
            ball.x = middleX - r;
            ball.y = middleY - r;

            // Increase the ballspeed;
            ball.SpeedUp(2);
        }
        // No blocker collision
        else {
            ball.y = newY;
            ball.x = newX;
        }
        // Check winning balls
        if (ball.x > PongPanel.GAME_WIDTH) {
            ballScore[0]++;
            toRemove.add(ball);
            ballAmount--;
            if (ballAmount == 0) {
                gameOver = true;
            }
        }
        else if (ball.x + PongPanel.BALL_SIZE < 0) {
            ballScore[1]++;
            toRemove.add(ball);
            ballAmount--;
            if (ballAmount == 0) {
                gameOver = true;
            }
        }
    }
    private void removeBalls() {
        for (Ball ball : toRemove) {
            balls.remove(ball);
        }
    }
    private void moveBlockers() {
        // For both blockers
        for (int i=0;i<2;i++) { 
            float acc = 0;
            if (i == 0) { // Left blocker
                if (leftUpHeld) acc -= 1f;
                if (leftDownHeld) acc += 1f;
            } else { // Right blocker
                if (rightUpHeld) acc -= 1f;
                if (rightDownHeld) acc += 1f;
            }

            // Apply acceleration
            BlockerMovement[i+2] += acc;
            BlockerMovement[i+2] *= (1 - drag*4);
            
            float newPos = BlockerMovement[i] + BlockerMovement[i+2];
            // Collision with the top of the screen
            if (newPos < 0) {
                BlockerMovement[i] = -newPos;
                BlockerMovement[i+2] = -BlockerMovement[i+2]*elasticity;
            }
            // Collision with the bottom of the screen
            else if (newPos + PongPanel.BLOCKER_HEIGHT > PongPanel.GAME_HEIGHT) {
                BlockerMovement[i] = Math.min(PongPanel.GAME_HEIGHT - PongPanel.BLOCKER_HEIGHT,2*PongPanel.GAME_HEIGHT - newPos - PongPanel.BLOCKER_HEIGHT);
                BlockerMovement[i+2] = -BlockerMovement[i+2]*elasticity;
            // No collision
            } 
            else {
                BlockerMovement[i] = newPos;
            }
        }

    }
    public void ListenHuman(boolean left, boolean actionUp, boolean pressed) {
        if (left && actionUp) leftUpHeld = pressed;
        if (left && !actionUp) leftDownHeld = pressed;
        if (!left && actionUp) rightUpHeld = pressed;
        if (!left && !actionUp) rightDownHeld = pressed;
    }
    public void ListenAI(boolean left,boolean actionUp) {
        if (left && actionUp) {
            BlockerMovement[2] -= 1f;
            BlockerMovement[2] *= (1-drag);
        }
        if (left && !actionUp) {
            BlockerMovement[2] += 1f;
            BlockerMovement[2] *= (1-drag);
        }
        if (!left && actionUp) {
            BlockerMovement[3] -= 1f;
            BlockerMovement[3] *= (1-drag);
        }
        if (!left && !actionUp) {
            BlockerMovement[3] += 1f;
            BlockerMovement[3] *= (1-drag);
        }
    }
    public void TogglePause() {
        paused = !paused;
    }
    public float[] getState() {
        float[] update = new float[2*balls.size()+3];
        update[0] = ID;
        update[1] = BlockerMovement[0]; update[2] = BlockerMovement[1];
        int i = 3;
        for(Ball ball : balls) {
            update[i] = ball.x;
            update[i+1] = ball.y;
            i=i+2;
        }
        return update;
    }
    private void sendUpdate() {
        // {LeftBlockerY,RightBlockerY,Ball1X,Ball1Y,Ball2X,ball2Y,...}
        float[] update = getState();
        manager.Update(ID,update);
    }
    private String GameOverUpdate() {
        if (ballScore[0] > ballScore[1]) {
            return "L:" + Integer.toString(ballScore[0]) + "-" + Integer.toString(ballScore[1]);
        }
        else if (ballScore[1] > ballScore[0])
            return "R:" + Integer.toString(ballScore[1]) + "-" + Integer.toString(ballScore[0]);
        else
            return "T:" + Integer.toString(ballScore[1]) + "-" + Integer.toString(ballScore[0]);
    }
    @SuppressWarnings("unused")
    private void Debug() {
        for (int i=0;i<2;i++) {
            System.out.println("Blocker: " + Integer.toString(i+1));
            System.out.println("BlockerPos: " + Float.toString(BlockerMovement[i]));
            System.out.println("BlockerVel: " + Float.toString(BlockerMovement[i+2]));
            System.out.println("BlockerAcc: " + Float.toString(BlockerMovement[i+4]));
        }
        for (int i=0;i<ballAmount;i++) {
            System.out.println("Ball: " + Integer.toString(i+1));
            System.out.println("BallPos: " + "(" + Float.toString(balls.get(i).x) + "-" + Float.toString(balls.get(i).y) + ")");
            System.out.println("BallVel: " + "(" + Float.toString(balls.get(i).xvel) + "-" + Float.toString(balls.get(i).yvel) + ")");
        }
    }
}
