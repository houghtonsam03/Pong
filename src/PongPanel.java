package src;
import javax.swing.*;
import java.awt.*;

public class PongPanel extends JPanel{
    static int gameWidth = 3000;
    static int gameHeight = 1600;
    static int blockerWidth = 100;
    static int blockerHeight = 500;
    static int ballSize = 100;
    static int LBlockerX = 200;
    static int RBlockerX = gameWidth-(LBlockerX+blockerWidth);

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
        float scaleX = getWidth() / (float) gameWidth;
        float scaleY = getHeight() / (float) gameHeight;

        // Example: draw left blocker
        int x = Math.round(LBlockerX * scaleX);
        int y = Math.round(state[0] * scaleY);
        int w = Math.round(blockerWidth * scaleX);
        int h = Math.round(blockerHeight * scaleY);

        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, w, h);

        // Example: draw right blocker
        x = Math.round(RBlockerX * scaleX);
        y = Math.round(state[1]* scaleY);
        g2.fillRect(x, y, w, h);

        // Example: draw balls
        g2.setColor(Color.RED);
        for (int i=2;i<state.length;i=i+2) { 
            if (!Float.isNaN(state[i]) && !Float.isNaN(state[i+1])) {
                int bx = Math.round(state[i] * scaleX);
                int by = Math.round(state[i+1] * scaleY);
                int bs = Math.round(ballSize * scaleX); // scale uniformly with X or average X/Y
                g2.fillOval(bx, by, bs, bs);
            }
        }
    }
    public void Update(float[] st) {
        state = st;
    }
    public void GameOver() {
        state = new float[]{(gameHeight-blockerHeight)/2,(gameHeight-blockerHeight)/2};
    }
}
