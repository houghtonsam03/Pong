
public class GameManager {
    private Game[] games;
    private Game[] shownGames;
    private Agent[] agents;
    private float[][] gameInfo;
    private int[] score;
    private Window window;

    public GameManager(int gameAmount,int ballAmount,boolean LAI, boolean RAI) {
        if (gameAmount > 1 && (!LAI || !RAI)) {
            throw new RuntimeException("Humans cannot play multiple games in parallell.");
        }
        else if (gameAmount < 1 ) {
            throw new RuntimeException("Cannot play less than 1 game");
        }
        games = new Game[gameAmount];
        gameInfo = new float[gameAmount][2+ballAmount*2];
        score = new int[2];

        // Show Max 8 games. Then, either show (8,6,4,2,1) games.
        int shown = Math.min(8,gameAmount);
        if (shown % 2 == 1 && shown != 1)
            shown--;
        shownGames = new Game[shown];
        for (int i=0;i<shown;i++) 
            shownGames[i] = games[i];

        window = new Window(shownGames);
        
    }
    public void Update(int id,float[] info) {

    }
}
