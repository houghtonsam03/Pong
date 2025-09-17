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

    private float scale;
    private float[] state;
    private JPanel LBlocker;
    private JPanel RBlocker;
    private JPanel[] balls;
    
    public PongPanel(int bAmount, int realWidth) {
        scale = realWidth/gameWidth;

        this.setLayout(null);
        this.setSize(Math.round(realWidth*scale), Math.round(realWidth*scale));
        this.setBackground(Color.BLACK);
        
        // Initialisation
        LBlocker = new JPanel(); LBlocker.setBackground(Color.WHITE);
        RBlocker = new JPanel(); RBlocker.setBackground(Color.WHITE);
        balls = new JPanel[bAmount];
        for (int i=0;i<bAmount;i++) {
            balls[i] = new JPanel();
            balls[i].setBackground(Color.RED);
            balls[i].setBounds(new Rectangle(gameWidth, bAmount, i, i));
        }

        this.setVisible(true);

    }
    
}
