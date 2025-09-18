import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
    //Static values
    static int FPS = 60;
    static float elasticity = 0.9f;
    static float drag = 0.01f;

    // Constant attributes
    private int gameSpeed = 1;
    private int ballAmount; // Allow multiple balls in play.
    private int ID;

    // Changing attributes
    // All position coordinates are the bottom left corner of an object.
    private float ballSpeed;
    private float[] BlockerMovement; // {LeftBlockerPos,RightBlockerPos,LeftBlockerVel,RightBlockerVel,LeftBlockerAcc,RightBlockerAcc}
    private int[] ballScore; // Amount of balls scored for L & R. {LScore,RScore}. 
    private boolean gameOver;
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

        ballSpeed = 5;
        BlockerMovement = new float[6];
        ballScore = new int[2];
        gameOver = false;
    }
    public void Setup() {
        // Initialise ballPositions.
        float ballX = (PongPanel.gameWidth-PongPanel.ballSize)/2;
        float ballY = (PongPanel.gameHeight-PongPanel.ballSize)/2;
        
        // Initialise ballVelocities.
        // Find a random avarage direction angle for all balls.
        double randAng;
        if (Math.random() < 0.5) { // Randomly chooses direction to the left.
            randAng = 140*Math.random()+110; // ang:(110,250)
        }
        else {
            randAng = 140*(Math.random()-0.5); // ang:(-70,70)
        }
        // Make random directions based on the avarage angle.
        double ang;
        for (int i=0;i<ballAmount;i++) {
            ang = randAng + 10*(Math.random()-0.5);
            float velX = (float)Math.cos(ang)*ballSpeed;
            float velY = (float)Math.sin(ang)*ballSpeed;
            balls.add(new Ball(ballX, ballY, velX, velY));
        }

        // Initialise blockers.
        BlockerMovement[0] = (PongPanel.gameHeight-PongPanel.blockerHeight)/2;
        BlockerMovement[2] = 0;
        BlockerMovement[4] = 0;
        BlockerMovement[1] = (PongPanel.gameHeight-PongPanel.blockerHeight)/2;
        BlockerMovement[3] = 0;
        BlockerMovement[5] = 0;

        Debug();

        // Send initial state.
        sendUpdate();
    }
    public void Run() throws InterruptedException{

        while (!gameOver) {
            sendUpdate();
            RunPhysics();
            TimeUnit.MILLISECONDS.sleep(1000/(FPS*gameSpeed));
        }
        sendUpdate();
        manager.GameOver(ID,GameOverUpdate());
    }
    public void RunPhysics() {
        for (Ball ball : balls) {
            moveBall(ball);
        }
        removeBalls();
        moveBlockers();
    }
    private void moveBall(Ball ball) {
        float newX = ball.x + ball.xvel;
        float newY = ball.y + ball.yvel;
        // Collision with the top of the screen
        if (newY < 0) {
            ball.y = -newY;
            ball.yvel = -ball.yvel;
        }
        // Collision with the bottom of the screen
        else if (newY + PongPanel.ballSize > PongPanel.gameHeight) {
            ball.y = Math.min(PongPanel.gameHeight - PongPanel.ballSize,2*PongPanel.gameHeight - newY - PongPanel.ballSize);
            ball.yvel = -ball.yvel;
        }
        // Collision with the blocker
        float middleX = ball.x+PongPanel.ballSize/2;
        float middleY = ball.y+PongPanel.ballSize/2;
        float pX = Float.MAX_VALUE;
        float pY = Float.MAX_VALUE;
        // Right side of pong
        if (middleX > PongPanel.gameWidth/2) {
            // X
            if (middleX < PongPanel.RBlockerX) pX = PongPanel.RBlockerX;
            else if (PongPanel.RBlockerX < middleX && middleX < PongPanel.RBlockerX+PongPanel.blockerWidth) pX = middleX;
            else if (PongPanel.RBlockerX+PongPanel.blockerWidth < middleX) pX = PongPanel.RBlockerX+PongPanel.blockerWidth;
            // Y
            if (middleY < BlockerMovement[1]) pY = BlockerMovement[1];
            else if (BlockerMovement[1] < middleY && middleY < BlockerMovement[1]+PongPanel.blockerHeight) pY = middleY;
            else if (BlockerMovement[1]+PongPanel.blockerHeight < middleY) pY = BlockerMovement[1]+PongPanel.blockerHeight;
        }
        // Left side of pong
        else {
            // X
            if (middleX < PongPanel.LBlockerX) pX = PongPanel.LBlockerX;
            else if (PongPanel.LBlockerX < middleX && middleX < PongPanel.LBlockerX+PongPanel.blockerWidth) pX = middleX;
            else if (PongPanel.LBlockerX+PongPanel.blockerWidth < middleX) pX = PongPanel.LBlockerX+PongPanel.blockerWidth;
            // Y
            if (middleY < BlockerMovement[0]) pY = BlockerMovement[0];
            else if (BlockerMovement[0] < middleY && middleY < BlockerMovement[0]+PongPanel.blockerHeight) pY = middleY;
            else if (BlockerMovement[0]+PongPanel.blockerHeight < middleY) pY = BlockerMovement[0]+PongPanel.blockerHeight;
        }
        // Check for collision
        if (Math.pow(pX-middleX, 2)+Math.pow(pY-middleY, 2) < Math.pow(PongPanel.ballSize/2,2)) {
            float dirX = middleX-pX;
            float dirY = middleY-pY;
            dirX = dirX / Math.abs(dirX);
            dirY = dirY / Math.abs(dirY);
            ball.xvel = dirX*ballSpeed;
            ball.yvel = dirY*ballSpeed;
        }
        // No blocker collision
        else {
            ball.y = newY;
            ball.x = newX;
        }
        // Check winning balls
        if (ball.x > PongPanel.gameWidth) {
            ballScore[0]++;
            toRemove.add(ball);
            ballAmount--;
            if (ballAmount == 0) {
                gameOver = true;
            }
        }
        else if (ball.x + PongPanel.ballSize < 0) {
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
            else if (newPos + PongPanel.blockerHeight > PongPanel.gameHeight) {
                BlockerMovement[i] = Math.min(PongPanel.gameHeight - PongPanel.blockerHeight,2*PongPanel.gameHeight - newPos - PongPanel.blockerHeight);
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
    public void sendUpdate() {
        // {LeftBlockerY,RightBlockerY,Ball1X,Ball1Y,Ball2X,ball2Y,...}
        float[] update = new float[2*balls.size()+2];
        update[0] = BlockerMovement[0]; update[1] = BlockerMovement[1];
        int i = 2;
        for(Ball ball : balls) {
            update[i] = ball.x;
            update[i+1] = ball.y;
            i=i+2;
        }
        manager.Update(ID,update);
    }
    public String GameOverUpdate() {
        Debug();
        if (ballScore[0] > ballScore[1]) {
            return "L:" + Integer.toString(ballScore[0]) + "-" + Integer.toString(ballScore[1]);
        }
        else if (ballScore[1] > ballScore[0])
            return "R:" + Integer.toString(ballScore[1]) + "-" + Integer.toString(ballScore[0]);
        else
            return "T:" + Integer.toString(ballScore[1]) + "-" + Integer.toString(ballScore[0]);
    }
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
