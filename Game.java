import java.util.concurrent.TimeUnit;

public class Game {
    //Static values
    static int FPS = 60;
    static float elasticity = 0.9f;
    static float drag = 0.5f;

    // Constant attributes
    private int gameSpeed = 1;
    private int ballAmount; // Allow multiple balls in play.
    private int ID;

    // Changing attributes
    // All position coordinates are the bottom left corner of an object.
    private float ballSpeed;
    private float[] ballPos; // {ball1X,ball1Y,ball2X,ball2Y,...}
    private float[] ballVel; // ...
    private float[] BlockerMovement; // {LeftBlockerPos,RightBlockerPos,LeftBlockerVel,RightBlockerVel,LeftBlockerAcc,RightBlockerAcc}
    private int[] ballScore; // Amount of balls scored for L & R. {LScore,RScore}. 
    private boolean gameOver;
    private char winner;

    // Classes
    GameManager manager;

    public Game(int id,int speed,int bAmount,GameManager man) {
        ID = id;
        gameSpeed = speed;
        ballAmount = bAmount;
        manager = man;

        ballSpeed = 5;
        ballPos = new float[2*ballAmount];
        ballVel = new float[2*ballAmount];
        BlockerMovement = new float[6];
        ballScore = new int[2];
        gameOver = false;
    }
    public void Setup() {
        // Initialise ballPositions.
        for (int i=0;i<ballAmount;i=i+2) {
            ballPos[i] = (PongPanel.gameWidth-PongPanel.ballSize)/2;
            ballPos[i+1] = (PongPanel.gameHeight-PongPanel.ballSize)/2;
        }
        
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
        for (int i=0;i<ballAmount;i=i+2) {
            ang = randAng + 10*(Math.random()-0.5);
            ballVel[i] = (float)Math.cos(ang)*ballSpeed;
            ballVel[i+1] = (float)Math.sin(ang)*ballSpeed;
        }

        // Initialise blockers.
        BlockerMovement[0] = (PongPanel.gameHeight-PongPanel.blockerHeight)/2;
        BlockerMovement[2] = 0;
        BlockerMovement[4] = 0;
        BlockerMovement[1] = (PongPanel.gameHeight-PongPanel.blockerHeight)/2;
        BlockerMovement[3] = 0;
        BlockerMovement[5] = 0;

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
        GameOverUpdate();
    }
    public void RunPhysics() {
        for (int i=0;i<ballAmount*2;i=i+2) {
            moveBall(i);
        }
        moveBlockers();
    }
    private void moveBall(int i) {
        return;
    }
    private void moveBlockers() {
        // For both blockers
        for (int i=0;i<2;i++) {
            BlockerMovement[i+4] += -Math.signum(BlockerMovement[i+2])*Math.pow(BlockerMovement[i+2], 2)*drag;
            BlockerMovement[i+2] += BlockerMovement[i+4]; // vel += acc;
            float newPos = BlockerMovement[i] + BlockerMovement[i+2];
            // Collision with top
            if (newPos < 0) {
                BlockerMovement[i] = -newPos;
                BlockerMovement[i+2] = -BlockerMovement[i+2]*elasticity;
            }
            // Collision with bottom
            else if (newPos > PongPanel.gameHeight) {
                BlockerMovement[i] = 2*PongPanel.gameHeight - newPos;
                BlockerMovement[i+2] = -BlockerMovement[i+2]*elasticity;
            // No collision
            } else {
                BlockerMovement[i] = newPos;
            }
        }

    }
    public void Listen(String actionL,String actionR) {
        return;
    }
    public void sendUpdate() {
        // {LeftBlockerY,RightBlockerY,Ball1X,Ball1Y,Ball2X,ball2Y,...}
        float[] update = new float[ballPos.length+2];
        update[0] = BlockerMovement[0]; update[1] = BlockerMovement[1];
        for(int i=0;i<ballAmount*2;i++) {
            update[i+2] = ballPos[i];
        }
        manager.Update(ID,update);
    }
    public String GameOverUpdate() {
        if (winner == 'L') {
            return "W->L:" + Integer.toString(ballScore[0]) + "-" + Integer.toString(ballScore[1]);
        }
        return "W->R:" + Integer.toString(ballScore[1]) + "-" + Integer.toString(ballScore[0]);
    }
}
