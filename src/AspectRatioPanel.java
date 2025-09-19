package src;
import javax.swing.*;
import java.awt.*;

class AspectRatioPanel extends JPanel {
    private final double aspectRatio;
    private final Component child;

    public AspectRatioPanel(double aspectRatio, Component child) {
        this.aspectRatio = aspectRatio;
        this.child = child;
        this.setBackground(Color.DARK_GRAY);
        setLayout(null); // we’ll position child manually
        add(child);
    }

    @Override
    public void doLayout() {
        int w = getWidth();
        int h = getHeight();
        double current = (double) w / h;

        // keep ratio 30:18
        if (current > aspectRatio) {
            w = (int) (h * aspectRatio);
        } else {
            h = (int) (w / aspectRatio);
        }

        // center the child
        child.setBounds((getWidth() - w) / 2, (getHeight() - h) / 2, w, h);
    }
}
