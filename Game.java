import java.util.concurrent.TimeUnit;

public class Game {
    //Static values
    static int FPS = 20;

    // Constant attributes
    private int gameSpeed = 1;
    private int ballAmount; // Allow multiple balls in play.
    private int ID;

    // Changing attributes
    // All position coordinates are the bottom left corner of an object.
    private float ballSpeed;
    private float[] ballPos; // {ball1X,ball1Y,ball2X,ball2Y,...}
    private float[] ballVel; // ...
    private float LBlockerPos; // Y-Coordinate
    private float LBlockerVel; // Y-Coordinate
    private float RBlockerPos; // Y-Coordinate
    private float RBlockerVel; // Y-Coordinate
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

        ballSpeed = 10;
        ballPos = new float[2*ballAmount];
        ballVel = new float[2*ballAmount];
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
        LBlockerPos = (PongPanel.gameHeight-PongPanel.blockerHeight)/2;
        LBlockerVel = 0;
        RBlockerPos = LBlockerPos;
        RBlockerVel = 0;

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
            ballPos[i] += ballVel[i];
            ballPos[i+1] += ballVel[i+1];
        }
        LBlockerPos += LBlockerVel;
        RBlockerPos += RBlockerVel;
    }
    public void Listen(String actionL,String actionR) {
        return;
    }
    public void sendUpdate() {
        // {LeftBlockerY,RightBlockerY,Ball1X,Ball1Y,Ball2X,ball2Y,...}
        float[] update = new float[ballPos.length+2];
        update[0] = LBlockerPos; update[1] = RBlockerPos;
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
