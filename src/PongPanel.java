import javax.swing.*;
import java.awt.*;

public class PongPanel extends JPanel{
    static int GAME_WIDTH = 3000;
    static int GAME_HEIGHT = 1600;
    static double ASPECT_RATIO = ((double)GAME_WIDTH)/GAME_HEIGHT;
    static int BLOCKER_WIDTH = 100;
    static int BLOCKER_HEIGHT = 500;
    static int BALL_SIZE = 100;
    static int L_BLOCKER_X = 200;
    static int R_BLOCKER_X = GAME_WIDTH-(L_BLOCKER_X+BLOCKER_WIDTH);

    private float[] state;
    
    public PongPanel(float[] st) {
        state = st;

        this.setLayout(null);
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Compute scale factors dynamically
        float scaleX = getWidth() / (float) GAME_WIDTH;
        float scaleY = getHeight() / (float) GAME_HEIGHT;

        // Example: draw left blocker
        int x = Math.round(L_BLOCKER_X * scaleX);
        int y = Math.round(state[1] * scaleY);
        int w = Math.round(BLOCKER_WIDTH * scaleX);
        int h = Math.round(BLOCKER_HEIGHT * scaleY);

        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, w, h);

        // Example: draw right blocker
        x = Math.round(R_BLOCKER_X * scaleX);
        y = Math.round(state[2]* scaleY);
        g2.fillRect(x, y, w, h);

        // Example: draw balls
        g2.setColor(Color.RED);
        for (int i=3;i<state.length;i=i+2) { 
            if (!Float.isNaN(state[i]) && !Float.isNaN(state[i+1])) {
                int bx = Math.round(state[i] * scaleX);
                int by = Math.round(state[i+1] * scaleY);
                int bs = Math.round(BALL_SIZE * scaleX); // scale uniformly with X or average X/Y
                g2.fillOval(bx, by, bs, bs);
            }
        }
    }
    public void Update(float[] st) {
        state = st;
    }
}
