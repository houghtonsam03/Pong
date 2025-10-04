
public class Settings {
    int gameAmount;
    int ballAmount;
    boolean LAI; // Left player is AI
    boolean RAI; // Right player is AI
    boolean graphics; // Disables the Pong graphics and lets all games run entirely without visuals. Good for optimization.
    Settings(int games,int balls,boolean L,boolean R,boolean graph) {
        gameAmount = games;
        ballAmount = balls;
        LAI = L;
        RAI = R;
        graphics = graph;
    }
}
