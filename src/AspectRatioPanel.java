import javax.swing.*;

public class AspectRatioPanel extends JPanel {
    private final double aspectRatio;
    private final JPanel child; // the actual content to keep aspect ratio

    public AspectRatioPanel(double aspectRatio, JPanel child) {
        this.aspectRatio = aspectRatio;
        this.child = child;
        this.setLayout(null); // use absolute positioning for resizing
        this.add(child);
        this.setOpaque(false);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeChild();
            }
        });
    }

    private void resizeChild() {
        int w = getWidth();
        int h = getHeight();
        double current = (double) w / h;

        if (current > aspectRatio) {
            // too wide → limit width
            w = (int) (h * aspectRatio);
        } else {
            // too tall → limit height
            h = (int) (w / aspectRatio);
        }

        int x = (getWidth() - w) / 2;
        int y = (getHeight() - h) / 2;
        child.setBounds(x, y, w, h);
        child.revalidate();
        child.repaint();
    }
}