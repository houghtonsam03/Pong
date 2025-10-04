import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GameManager {
    //Settings
    private Settings settings;

    private ArrayList<Integer> hiddenGames; // List of hidden games
    private int[] shownGames; // List of games that are drawn
    private Game[] games; // List of all games
    private Agent[] agents; // A list of AI-agents
    private float[] startUpState;
    private float[][] gameInfo; // The stored game-states
    private int[] score; // How many games have been won {LeftWin,RightWin,Ties}
    private Window window;
    public GameManager() { // Default values
        settings = new Settings(1000,1,true,true,true);

        Game g = new Game(-1,1,1,this);
        g.Setup();
        startUpState = g.getState();

        window = new Window(this,startUpState);
    }
    public void Setup() {
        if (settings.gameAmount > 1 && (!settings.LAI || !settings.RAI)) {
            throw new RuntimeException("Humans cannot play multiple games in parallell.");
        }
        else if (settings.gameAmount < 1 ) {
            throw new RuntimeException("Cannot play less than 1 game");
        }
        // Init.
        hiddenGames = new ArrayList<>();
        games = new Game[settings.gameAmount];
        gameInfo = new float[settings.gameAmount][3+settings.ballAmount*2];
        score = new int[3];
        
        // Setting all games to the same startup state
        for (int i=0;i<settings.gameAmount;i++) {
            for (int j=0;j<startUpState.length;j++) {
                gameInfo[i][j] = startUpState[j];
            }
        }
        // Creating Agents (AI)
        agents = new Agent[2];
        if (settings.LAI)
            agents[0] = new Agent(true,this,settings.gameAmount,settings.ballAmount);
        if (settings.RAI)
            agents[1] = new Agent(false,this,settings.gameAmount,settings.ballAmount);

        // Setup games
        int sp = 1;
        if (settings.gameAmount > 1)
            sp = 4;
        for (int i=0;i<settings.gameAmount;i++) {
            Game g = new Game(i,sp,settings.ballAmount,this);
            games[i] = g;
            hiddenGames.add(i);
            g.Setup();
        }
        // Show Max 8 games. Then, either show (8,6,4,2,1) games.
        // If graphics is off then show 0
        if (settings.graphics) {
            int shown = Math.min(8,settings.gameAmount);
            if (shown % 2 == 1 && shown != 1)
                shown--;
            shownGames = new int[shown];
            for (int i=0;i<shown;i++)  {
                shownGames[i] = i;
                hiddenGames.remove((Integer)i);
            }
        }
        else {
            shownGames = new int[0];
        }
        // Setup Inputs
        SetupKeyInputs();
    }
    public void Start() {
        // Window
        Setup();

        float[][] shownInfo = new float[shownGames.length][3+settings.ballAmount*2];
        for (int i=0;i<shownGames.length;i++) {
            shownInfo[i] = games[i].getState();
        }

        window.StartGame(shownInfo);

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
    public void OpenSettings() {
        window.ToggleSettings();
    }
    public void CloseSettings(Settings s) {
        window.ToggleSettings();
        settings = s;
    }
    public void OpenControls() {
        window.ToggleControls();
    }
    public void CloseControls(Controls c) {
        window.ToggleControls();
    }
    public Settings getSettings() {
        return settings;
    }
    public void Update(int id,float[] info) {
        for (int i=0;i<info.length;i++) {
            gameInfo[id][i] = info[i];
        }
        if (settings.LAI) agents[0].Update(id,info);
        if (settings.RAI) agents[1].Update(id,info);
        int shownID = isShown(id);
        if (shownID >= 0)
            window.Update(shownID,info);
        
    }
    public synchronized void GameOver(int id,String message) {
        char winner = message.toCharArray()[0];
        if (winner == 'L') 
            score[0]++;
        else if (winner == 'R')
            score[1]++;
        else
            score[2]++;
        window.UpdateScore(score);
        hiddenGames.remove((Integer)id);
        int shownID = isShown(id);
        if (shownID >= 0) {
            if (!hiddenGames.isEmpty()) {
                int newID = hiddenGames.remove(0);
                shownGames[shownID] = newID;
                System.out.println();
                System.out.println(id);
                for (int i=0;i<shownGames.length;i++) {
                    System.out.println(Integer.toString(i)+": "+Integer.toString(shownGames[i]));
                }
            }
        }
    }
    private int isShown(int id) {
        for (int i=0;i<shownGames.length;i++) {
            if (shownGames[i] == id) return i;
        }
        return -1;
    }
    private void SetupKeyInputs() {
        JPanel panel = window.getPanel(); // Make sure Window has a getPanel() method
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();


        // Player movements
        String leftUp = null; String leftDown = null;
        String rightUp = null; String rightDown = null;
        if (!settings.LAI && !settings.RAI) {
            leftUp = "W"; leftDown = "S";
            rightUp = "UP"; rightDown = "DOWN";
        } else if (!settings.LAI) {
            leftUp = "W"; leftDown = "S";
        } else if (!settings.RAI) {
            rightUp = "W"; rightDown = "S";
        }
        // Left player
        if (!settings.LAI) {
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
        if (!settings.RAI) {
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
