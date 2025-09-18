
import java.awt.event.*;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
public class GameManager {
    private Game[] games; // List of all games
    private Game[] shownGames; // List of games that are drawn
    private Agent[] agents; // A list of AI-agents
    private float[][] gameInfo; // The stored game-states
    private int[] score; // How many games have been won
    private Window window;
    private boolean L; // If Left is an AI
    private boolean R; // If Right is an AI

    public GameManager(int gameAmount,int ballAmount,boolean LAI, boolean RAI) {
        // Checking bad parameters
        if (gameAmount > 1 && (!LAI || !RAI)) {
            throw new RuntimeException("Humans cannot play multiple games in parallell.");
        }
        else if (gameAmount < 1 ) {
            throw new RuntimeException("Cannot play less than 1 game");
        }
        // Init.
        games = new Game[gameAmount];
        gameInfo = new float[gameAmount][2+ballAmount*2];
        score = new int[2];

        // Creating Agents (AI)
        agents = new Agent[2];
        L = LAI;
        R = RAI;
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

        // Window
        window = new Window(shown,gameInfo);

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

    public void Update(int id,float[] info) {
        for (int i=0;i<info.length;i++) {
            gameInfo[id][i] = info[i];
        }
        if (L) agents[0].Update(id,gameInfo[id]);
        if (R) agents[1].Update(id,gameInfo[id]);
        if (id <= shownGames.length - 1)
            window.Update(id,gameInfo[id]);
        
    }
    public void GameOver(int id,String message) {
        if (message.toCharArray()[0] == 'L') 
            score[0]++;
        else
            score[1]++;
        window.UpdateScore(score);
    }
    private void SetupKeyInputs() {
        JPanel panel = window.getPanel(); // Make sure Window has a getPanel() method
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

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
