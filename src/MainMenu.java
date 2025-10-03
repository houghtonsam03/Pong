import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(GameManager manager) {
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout()); // layout for precise positioning

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Top vertical spacer (takes 1/6 of space)
        gbc.gridy = 0;
        gbc.weighty = 1; // proportion of space above
        this.add(Box.createVerticalGlue(), gbc);

        // Start button
        gbc.gridy = 1;
        gbc.weighty = 0; // button doesn’t expand vertically
        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(300, 100));
        startButton.setFont(new Font("SansSerif", Font.BOLD, 40));
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(Color.WHITE);
        startButton.setFocusable(false);
        startButton.addActionListener(e -> manager.Start());
        this.add(startButton, gbc);

        // Bottom vertical spacer (takes 5/6 of space)
        gbc.gridy = 2;
        gbc.weighty = 5; // proportion of space below
        this.add(Box.createVerticalGlue(), gbc);
    }
}