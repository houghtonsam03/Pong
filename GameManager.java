
public class GameManager {
    private Game[] games;
    private Game[] shownGames;
    private Agent[] agents;
    private float[][] gameInfo;
    private int[] score;
    private Window window;

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
        if (LAI)
            agents[0] = new Agent();
        if (RAI)
            agents[1] = new Agent();

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
            g.Setup();
            games[i] = g;
        }
        
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
        try {
            agents[0].Update(gameInfo[id]);
            agents[1].Update(gameInfo[id]);
        }
        catch (Exception e) {}
        if (id <= shownGames.length - 1)
            window.Update(id,gameInfo[id]);

        Debug();
        
    }
    public void GameOver(int id,String message) {
        if (message.toCharArray()[0] == 'L') 
            score[0]++;
        else
            score[1]++;
        window.UpdateScore(score);
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
