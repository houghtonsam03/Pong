import java.util.concurrent.TimeUnit;

public class Game {
    //Static values
    static int gameWidth = 3000;
    static int gameHeight = 1600;
    static int blockerWidth = 100;
    static int blockerHeight = 500;
    static int ballWidth = 100;
    static int ballHeight = 100;
    static int LBlockerX = 200;
    static int RBlockerX = gameWidth-(LBlockerX+blockerWidth);
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
    private float LBlockerPos;
    private float LBlockerVel;
    private float RBlockerPos;
    private float RBlockerVel;
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
    public void Start() {
        // Initialise ballPositions.
        for (int i=0;i<ballAmount;i++) {
            if (i % 2 == 0) 
                ballPos[i] = (gameWidth-ballWidth)/2; // Middle of the ball is in the middle of the screen.
            else
                ballPos[i] = (gameHeight-ballHeight)/2; // Middle of the ball is in the middle of the screen.
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
        LBlockerPos = (gameHeight-blockerHeight)/2;
        LBlockerVel = 0;
        RBlockerPos = (gameHeight-blockerHeight)/2;
        RBlockerVel = 0;

        // Run the game.
        try {
            Run();
        } 
        catch (Exception e) {}
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
        return;
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
