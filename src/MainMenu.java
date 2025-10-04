
import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(GameManager manager, float[] startup) {
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new GridBagLayout()); // layout for precise positioning

        GridBagConstraints gbc = new GridBagConstraints();
        // Top buffer
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.weighty = 0.1; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);
        // Left buffer
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1; gbc.weighty = 0; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);
        // Middle buffer
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.2; gbc.weighty = 0; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);
        // Right buffer
        gbc.gridx = 4; gbc.gridy = 1; gbc.weightx = 0.2; gbc.weighty = 0; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);
        // Bottom buffer
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.weighty = 0.1; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);


        // Main panel
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.1; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE,5));
        mainPanel.setLayout(new GridBagLayout());
        
        // Top buffer
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0; mainGbc.gridy = 0; mainGbc.weightx = 0; mainGbc.weighty = 0.01; mainGbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(Box.createRigidArea(new Dimension(0,0)),mainGbc);
        // Bottom buffer
        mainGbc.gridx = 0; mainGbc.gridy = 5; mainGbc.weightx = 0; mainGbc.weighty = 0.01; mainGbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(Box.createRigidArea(new Dimension(0,0)),mainGbc);
        // Left buffer
        mainGbc.gridx = 0; mainGbc.gridy = 0; mainGbc.weightx = 0.1; mainGbc.weighty = 0; mainGbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(Box.createRigidArea(new Dimension(0,0)),mainGbc);
        // Right buffer
        mainGbc.gridx = 2; mainGbc.gridy = 0; mainGbc.weightx = 0.1; mainGbc.weighty = 0; mainGbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(Box.createRigidArea(new Dimension(0,0)),mainGbc);
        // Adding the start button
        mainGbc.gridx = 1; mainGbc.gridy = 1; mainGbc.weightx = 0.8; mainGbc.weighty = 0.245; mainGbc.fill = GridBagConstraints.BOTH;
        JButton startButton = new JButton("Start Game");
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setFont(new Font("SansSerif",Font.BOLD,60));
        startButton.addActionListener(e -> {
            manager.Start();
        });
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        mainPanel.add(startButton,mainGbc);
        // Adding the settings button
        mainGbc.gridx = 1; mainGbc.gridy = 2; mainGbc.weightx = 0.8; mainGbc.weighty = 0.245; mainGbc.fill = GridBagConstraints.BOTH;
        JButton settingsButton = new JButton("Settings");
        settingsButton.setBackground(Color.BLACK);
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setFocusPainted(false);
        settingsButton.setFont(new Font("SansSerif",Font.BOLD,60));
        settingsButton.addActionListener(e -> {
            manager.OpenSettings();
        });
        settingsButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        mainPanel.add(settingsButton,mainGbc);
        // Adding the control button
        mainGbc.gridx = 1; mainGbc.gridy = 3; mainGbc.weightx = 0.8; mainGbc.weighty = 0.245; mainGbc.fill = GridBagConstraints.BOTH;
        JButton controlButton = new JButton("Controls");
        controlButton.setBackground(Color.BLACK);
        controlButton.setForeground(Color.WHITE);
        controlButton.setFocusPainted(false);
        controlButton.setFont(new Font("SansSerif",Font.BOLD,60));
        controlButton.addActionListener(e -> {
            manager.OpenControls();
        });
        controlButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        mainPanel.add(controlButton,mainGbc);
        // Adding the exit button
        mainGbc.gridx = 1; mainGbc.gridy = 4; mainGbc.weightx = 0.8; mainGbc.weighty = 0.245; mainGbc.fill = GridBagConstraints.BOTH;
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setFont(new Font("SansSerif",Font.BOLD,60));
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        mainPanel.add(exitButton,mainGbc);

        this.add(mainPanel,gbc);


        // Title
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.DARK_GRAY);
        titlePanel.setLayout(new GridBagLayout());
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridy = 0; titleGbc.gridx = 0; titleGbc.weightx = 1.0; titleGbc.weighty = 0.3; titleGbc.anchor = GridBagConstraints.CENTER; titleGbc.fill = GridBagConstraints.NONE;
        JLabel title = new JLabel("PONG");
        title.setFont(new Font("SansSerif",Font.BOLD,100)); title.setForeground(Color.RED);
        titlePanel.add(title,titleGbc);
        titleGbc.gridy = 1; titleGbc.weighty = 0.7; titleGbc.fill = GridBagConstraints.BOTH;
        PongPanel p = new PongPanel(startup);
        AspectRatioPanel wrapper = new AspectRatioPanel(PongPanel.ASPECT_RATIO,p);
        titlePanel.add(wrapper,titleGbc);
        this.add(titlePanel,gbc);
    }
}