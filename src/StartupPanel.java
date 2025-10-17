import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;

public class StartupPanel extends JPanel{
    private GameManager manager;
    private Settings settings;
    StartupPanel(GameManager gm) {
        settings = gm.getSettings();
        manager = gm;

        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new GridBagLayout()); 

        GridBagConstraints rowGBC = new GridBagConstraints();
        // Row 1 (Buffer row)
        rowGBC.gridx = 0; rowGBC.gridy = 0; rowGBC.weightx = 1; rowGBC.weighty = 0.325; rowGBC.fill = GridBagConstraints.BOTH; rowGBC.anchor = GridBagConstraints.CENTER;
        JPanel row1 = new JPanel();
        row1.setBackground(Color.DARK_GRAY);
        this.add(row1,rowGBC);
        // Row 2 (Settings row)
        rowGBC.gridx = 0; rowGBC.gridy = 1; rowGBC.weightx = 1; rowGBC.weighty = 0.15; rowGBC.fill = GridBagConstraints.BOTH; rowGBC.anchor = GridBagConstraints.CENTER;
        JPanel row2 = new JPanel();
        row2.setBackground(Color.DARK_GRAY); row2.setLayout(new GridBagLayout());
        // Create columns for settings
            GridBagConstraints colGBC = new GridBagConstraints();
            // Left buffer
            colGBC.gridx = 0; colGBC.gridy = 0; colGBC.weightx = 0.27; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JPanel col1 = new JPanel();
            col1.setBackground(Color.DARK_GRAY);
            row2.add(col1,colGBC);
            // Left player toggle
            colGBC.gridx = 1; colGBC.gridy = 0; colGBC.weightx = 0.1; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JCheckBox LPlayerToggle = new JCheckBox("Left Player");
            LPlayerToggle.setBackground(Color.BLACK); LPlayerToggle.setForeground(Color.WHITE);
            LPlayerToggle.setHorizontalTextPosition(SwingConstants.CENTER); LPlayerToggle.setVerticalTextPosition(SwingConstants.TOP); // Moving text above checkbox
            LPlayerToggle.setHorizontalAlignment(SwingConstants.CENTER); LPlayerToggle.setVerticalAlignment(SwingConstants.CENTER); // Centering text and checkbox
            LPlayerToggle.setFont(new Font("SansSerif",Font.BOLD,40));
            LPlayerToggle.setFocusPainted(false);
            LPlayerToggle.setIcon(new ImageIcon("src/Images/checkbox_unchecked.png"));
            LPlayerToggle.setSelectedIcon(new ImageIcon("src/Images/checkbox_checked.png"));
            LPlayerToggle.addActionListener(e -> {
                settings.updateLAI(!LPlayerToggle.isSelected());
            });
            LPlayerToggle.setSelected(settings.getAI()[0]);

            row2.add(LPlayerToggle,colGBC);
            // Buffer
            colGBC.gridx = 2; colGBC.gridy = 0; colGBC.weightx = 0.02; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JPanel col3 = new JPanel();
            col3.setBackground(Color.DARK_GRAY);
            row2.add(col3,colGBC);
            // Game amount field
            colGBC.gridx = 3; colGBC.gridy = 0; colGBC.weightx = 0.1; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JPanel gamePanel = new JPanel();
            gamePanel.setLayout(new GridBagLayout()); gamePanel.setBackground(Color.BLACK);
            GridBagConstraints ballGBC = new GridBagConstraints();

            ballGBC.gridx = 0; ballGBC.gridy = 0; ballGBC.weightx = 1; ballGBC.weighty = 0.3; ballGBC.fill = GridBagConstraints.BOTH; ballGBC.anchor = GridBagConstraints.CENTER;
            JLabel gameLabel = new JLabel("Games");
            gameLabel.setHorizontalAlignment(SwingConstants.CENTER); gameLabel.setVerticalAlignment(SwingConstants.CENTER);
            gameLabel.setBackground(Color.BLACK); gameLabel.setForeground(Color.WHITE);
            gameLabel.setFont(new Font("SansSerif",Font.BOLD,30));
            gamePanel.add(gameLabel,ballGBC);

            ballGBC.gridx = 0; ballGBC.gridy = 1; ballGBC.weightx = 1; ballGBC.weighty = 0.7; ballGBC.fill = GridBagConstraints.BOTH; ballGBC.anchor = GridBagConstraints.CENTER;
            JTextField gameField = new JTextField(Integer.toString(settings.getGames()));
            gameField.setFont(new Font("SansSerif",Font.BOLD,30));
            gameField.setHorizontalAlignment(SwingConstants.CENTER); gameField.setBackground(Color.BLACK); gameField.setForeground(Color.WHITE);
            gameField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateGame(gameField);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateGame(gameField);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateGame(gameField);
                }
            });
            gamePanel.add(gameField,ballGBC);
            row2.add(gamePanel,colGBC);
            // Buffer
            colGBC.gridx = 4; colGBC.gridy = 0; colGBC.weightx = 0.02; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JPanel col5 = new JPanel();
            col5.setBackground(Color.DARK_GRAY);
            row2.add(col5,colGBC);
            // Ball amount field
            colGBC.gridx = 5; colGBC.gridy = 0; colGBC.weightx = 0.1; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JPanel ballJPanel = new JPanel();
            ballJPanel.setLayout(new GridBagLayout()); ballJPanel.setBackground(Color.BLACK);
            ballGBC = new GridBagConstraints();

            ballGBC.gridx = 0; ballGBC.gridy = 0; ballGBC.weightx = 1; ballGBC.weighty = 0.3; ballGBC.fill = GridBagConstraints.BOTH; ballGBC.anchor = GridBagConstraints.CENTER;
            JLabel ballLabel = new JLabel("Balls");
            ballLabel.setHorizontalAlignment(SwingConstants.CENTER); ballLabel.setVerticalAlignment(SwingConstants.CENTER);
            ballLabel.setBackground(Color.BLACK); ballLabel.setForeground(Color.WHITE);
            ballLabel.setFont(new Font("SansSerif",Font.BOLD,30));
            ballJPanel.add(ballLabel,ballGBC);

            ballGBC.gridx = 0; ballGBC.gridy = 1; ballGBC.weightx = 1; ballGBC.weighty = 0.7; ballGBC.fill = GridBagConstraints.BOTH; ballGBC.anchor = GridBagConstraints.CENTER;
            JTextField ballField = new JTextField(Integer.toString(settings.getBalls()));
            ballField.setFont(new Font("SansSerif",Font.BOLD,30));
            ballField.setHorizontalAlignment(SwingConstants.CENTER); ballField.setBackground(Color.BLACK); ballField.setForeground(Color.WHITE);
            ballField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateBall(ballField);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateBall(ballField);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateBall(ballField);
                }
            });
            ballJPanel.add(ballField,ballGBC);
            row2.add(ballJPanel,colGBC);
            // Buffer
            colGBC.gridx = 6; colGBC.gridy = 0; colGBC.weightx = 0.02; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JPanel col7 = new JPanel();
            col7.setBackground(Color.DARK_GRAY);
            row2.add(col7,colGBC);
            // Right player toggle
            colGBC.gridx = 7; colGBC.gridy = 0; colGBC.weightx = 0.1; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JCheckBox RPLayerToggle = new JCheckBox("Right Player");
            RPLayerToggle.setBackground(Color.BLACK); RPLayerToggle.setForeground(Color.WHITE);
            RPLayerToggle.setHorizontalTextPosition(SwingConstants.CENTER); RPLayerToggle.setVerticalTextPosition(SwingConstants.TOP); // Moving text above checkbox
            RPLayerToggle.setHorizontalAlignment(SwingConstants.CENTER); RPLayerToggle.setVerticalAlignment(SwingConstants.CENTER); // Centering text and checkbox
            RPLayerToggle.setFont(new Font("SansSerif",Font.BOLD,40));
            RPLayerToggle.setFocusPainted(false);
            RPLayerToggle.setIcon(new ImageIcon("src/Images/checkbox_unchecked.png"));
            RPLayerToggle.setSelectedIcon(new ImageIcon("src/Images/checkbox_checked.png"));
            RPLayerToggle.addActionListener(e -> {
                settings.updateRAI(RPLayerToggle.isSelected());
            });
            RPLayerToggle.setSelected(settings.getAI()[1]);
            row2.add(RPLayerToggle,colGBC);
            // Right buffer
            colGBC.gridx = 8; colGBC.gridy = 0; colGBC.weightx = 0.27; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JPanel col9 = new JPanel();
            col9.setBackground(Color.DARK_GRAY);
            row2.add(col9,colGBC);

        this.add(row2,rowGBC);
        // Row 3 (Buffer row)
        rowGBC.gridx = 0; rowGBC.gridy = 2; rowGBC.weightx = 1; rowGBC.weighty = 0.05; rowGBC.fill = GridBagConstraints.BOTH; rowGBC.anchor = GridBagConstraints.CENTER;
        JPanel row3 = new JPanel();
        row3.setBackground(Color.DARK_GRAY);
        this.add(row3,rowGBC);
        // Row 4 (Start row)
        rowGBC.gridx = 0; rowGBC.gridy = 3; rowGBC.weightx = 1; rowGBC.weighty = 0.15; rowGBC.fill = GridBagConstraints.BOTH; rowGBC.anchor = GridBagConstraints.CENTER;
        JPanel row4 = new JPanel();
        row4.setBackground(Color.DARK_GRAY); row4.setLayout(new GridBagLayout());
        // Create columns for Startbutton
            // Left buffer
            colGBC.gridx = 0; colGBC.gridy = 0; colGBC.weightx = 0.45; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            col1 = new JPanel();
            col1.setBackground(Color.DARK_GRAY);
            row4.add(col1,colGBC);
            // Start button
            colGBC.gridx = 1; colGBC.gridy = 0; colGBC.weightx = 0.1; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            JButton startButton = new JButton("START GAME");
            startButton.setBackground(Color.BLACK);
            startButton.setForeground(Color.WHITE);
            startButton.setFocusPainted(false);
            startButton.setFont(new Font("SansSerif",Font.BOLD,40));
            startButton.addActionListener(e -> {
                manager.Start();
            });
            row4.add(startButton,colGBC);
            // Right buffer
            colGBC.gridx = 2; colGBC.gridy = 0; colGBC.weightx = 0.45; colGBC.weighty = 1; colGBC.fill = GridBagConstraints.BOTH; colGBC.anchor = GridBagConstraints.CENTER;
            col3 = new JPanel();
            col3.setBackground(Color.DARK_GRAY);
            row4.add(col3,colGBC);
        this.add(row4,rowGBC);
        // Row 5 (Buffer row)
        rowGBC.gridx = 0; rowGBC.gridy = 4; rowGBC.weightx = 1; rowGBC.weighty = 0.325; rowGBC.fill = GridBagConstraints.BOTH; rowGBC.anchor = GridBagConstraints.CENTER;
        JPanel row5 = new JPanel();
        row5.setBackground(Color.DARK_GRAY);
        this.add(row5,rowGBC);
        
    }
    private void updateGame(JTextField field) {
        String s = field.getText();
        System.out.println(s);
        if (s.matches("[0-9]+")) {settings.updateGameAmount(Integer.parseInt(s));}
        else if (!s.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                field.setText(Integer.toString(settings.getGames()));
            });
        }
    }
    private void updateBall(JTextField field) {
        String s = field.getText();
        System.out.println(s);
        if (s.matches("[0-9]+")) {settings.updateBallAmount(Integer.parseInt(s));}
        else if (!s.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                field.setText(Integer.toString(settings.getBalls()));
            });
        }
    }

}
