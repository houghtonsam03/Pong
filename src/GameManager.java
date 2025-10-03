import java.awt.event.*;
import javax.swing.*;

public class GameManager {
    //Settings
    private int gameAmount;
    private int ballAmount;
    private boolean L; // If Left is an AI
    private boolean R; // If Right is an AI
    private boolean graphics; // Disables the Pong graphics and lets all games run entirely without visuals. Good for optimization.

    private Game[] games; // List of all games
    private Game[] shownGames; // List of games that are drawn
    private Agent[] agents; // A list of AI-agents
    private float[] startUpState;
    private float[][] gameInfo; // The stored game-states
    private int[] score; // How many games have been won {LeftWin,RightWin,Ties}
    private Window window;

    public GameManager() { // Default values
        gameAmount = 8;
        ballAmount = 1;
        L = true;
        R = true;
        graphics = true;

        Game g = new Game(-1,1,1,this);
        g.Setup();
        startUpState = g.getState();

        window = new Window(this);
    }
    private void updateSettings(int g,int b,boolean LAI,boolean RAI,boolean gra) {
        gameAmount = g;
        ballAmount = b;
        L = LAI;
        R = RAI;
        graphics = gra;
    }
    public void Setup() {
        if (gameAmount > 1 && (!L || !R)) {
            throw new RuntimeException("Humans cannot play multiple games in parallell.");
        }
        else if (gameAmount < 1 ) {
            throw new RuntimeException("Cannot play less than 1 game");
        }
        // Init.
        games = new Game[gameAmount];
        gameInfo = new float[gameAmount][2+ballAmount*2];
        score = new int[3];

        // Creating Agents (AI)
        agents = new Agent[2];
        if (L)
            agents[0] = new Agent(true,this,gameAmount,ballAmount);
        if (R)
            agents[1] = new Agent(false,this,gameAmount,ballAmount);

        // Show Max 8 games. Then, either show (8,6,4,2,1) games.
        int shown = Math.min(8,gameAmount);
        if (shown % 2 == 1 && shown != 1)
            shown--;
        shownGames = new Game[shown];
        for (int i=0;i<shown;i++) 
            shownGames[i] = games[i];

        // Setup games
        int sp = 1;
        if (gameAmount > 1)
            sp = 4;
        for (int i=0;i<gameAmount;i++) {
            Game g = new Game(i,sp,ballAmount,this);
            games[i] = g;
            g.Setup();
        }
        // Setup Inputs
        SetupKeyInputs();
    }
    public void Start() {
        // Window
        Setup();
        window.StartGame(gameAmount,gameInfo);

        for (Game g : games) {
            Thread t = new Thread(() -> {
                try {
                    g.Run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }
    public void Pause() {
        for (Game g : games) {
            g.TogglePause();
        }
        window.TogglePause();
    }

    public void Update(int id,float[] info) {
        for (int i=0;i<info.length;i++) {
            gameInfo[id][i] = info[i];
        }
        if (L) agents[0].Update(id,info);
        if (R) agents[1].Update(id,info);
        if (id <= shownGames.length - 1)
            window.Update(id,info);
        
    }
    public void GameOver(int id,String message) {
        char winner = message.toCharArray()[0];
        if (winner == 'L') 
            score[0]++;
        else if (winner == 'R')
            score[1]++;
        else
            score[2]++;
        window.UpdateScore(score);
    }
    private void SetupKeyInputs() {
        JPanel panel = window.getPanel(); // Make sure Window has a getPanel() method
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();


        // Player movements
        String leftUp = null; String leftDown = null;
        String rightUp = null; String rightDown = null;
        if (!L && !R) {
            leftUp = "W"; leftDown = "S";
            rightUp = "UP"; rightDown = "DOWN";
        } else if (!L) {
            leftUp = "W"; leftDown = "S";
        } else if (!R) {
            rightUp = "W"; rightDown = "S";
        }
        // Left player
        if (!L) {
            im.put(KeyStroke.getKeyStroke(("pressed "+leftUp)), "leftUpPressed");
            am.put("leftUpPressed", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(true, true, true); }
            });

            im.put(KeyStroke.getKeyStroke(("released "+leftUp)), "leftUpReleased");
            am.put("leftUpReleased", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(true, true, false); }
            });
            im.put(KeyStroke.getKeyStroke(("pressed "+leftDown)), "leftDownPressed");
            am.put("leftDownPressed", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(true, false, true); }
            });

            im.put(KeyStroke.getKeyStroke(("released "+leftDown)), "leftDownReleased");
            am.put("leftDownReleased", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(true, false, false); }
            });
            
        }

        // Right player
        if (!R) {
            im.put(KeyStroke.getKeyStroke(("pressed "+rightUp)), "rightUpPressed");
            am.put("rightUpPressed", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(false, true, true); }
            });

            im.put(KeyStroke.getKeyStroke(("released "+rightUp)), "rightUpReleased");
            am.put("rightUpReleased", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(false, true, false); }
            });
            im.put(KeyStroke.getKeyStroke(("pressed "+rightDown)), "rightDownPressed");
            am.put("rightDownPressed", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(false, false, true); }
            });

            im.put(KeyStroke.getKeyStroke(("released "+rightDown)), "rightDownReleased");
            am.put("rightDownReleased", new AbstractAction() {
                public void actionPerformed(ActionEvent e) { games[0].ListenHuman(false, false, false); }
            });
        }
        // Pause game
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "pause");
        am.put("pause", new AbstractAction() { public void actionPerformed(ActionEvent e) { Pause(); }
        });

    }
    public void Listen(int id, boolean left, boolean actionUp) {
        games[id].ListenAI(left, actionUp);
    }
    public void Debug() {
        for (int i=0;i<games.length;i++) {
            System.out.println("LeftBlocker: " + Float.valueOf(gameInfo[i][0]));
            System.out.println("RightBlocker: " + Float.valueOf(gameInfo[i][1]));
            for (int j=2;j<gameInfo[0].length;j=j+2) {
                System.out.println("Ball " + Integer.toString(j/2));
                System.out.println("X: " + Float.valueOf(gameInfo[i][j]));
                System.out.println("Y: " + Float.valueOf(gameInfo[i][j+1]));
            }
        }
    }
}
