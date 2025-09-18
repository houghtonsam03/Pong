import javax.swing.*;
import java.awt.*;

public class Window extends JFrame{
    private int windowWidth = 2500;
    private int windowHeight = 1200;
    private PongPanel[] pongList;
    private int[] gameScore;
    public Window(int games,float[][] states) {

        JPanel scoreboard = new JPanel(); scoreboard.setBackground(Color.BLACK);
        scoreboard.setLayout(new GridBagLayout()); scoreboard.setPreferredSize(new Dimension(windowWidth,windowHeight/4));
        

        // Creating the "Scoreboard" Title
        JLabel scoreTitle = new JLabel("Scoreboard");
        scoreTitle.setFont(new Font("SansSerif",Font.BOLD,50)); scoreTitle.setForeground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0.1; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        scoreboard.add(scoreTitle,gbc);


        // Creating the scoreboard
        gameScore = new int[2];
        JLabel score = new JLabel(gameScore[0] + " - " + gameScore[1]);
        score.setFont(new Font("SansSerif",Font.BOLD,50)); score.setForeground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 0.2; gbc.anchor = GridBagConstraints.PAGE_START; gbc.fill = GridBagConstraints.NONE;
        scoreboard.add(score,gbc);
        
        // Creating the gameboard
        JPanel gameboard = new JPanel(); gameboard.setBackground(Color.DARK_GRAY);
        gameboard.setLayout(new BorderLayout());


        // Creating the PongGrid
        JPanel pongGrid = new JPanel(new GridBagLayout()); pongGrid.setBackground(Color.DARK_GRAY);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        int rows=1; int cols=1;
        switch (games) {
            case 1: rows = 1; cols = 1; break;
            case 2: rows = 1; cols = 2; break;
            case 4: rows = 2; cols = 2; break;
            case 6: rows = 2; cols = 3; break;
            case 8: rows = 2; cols = 4; break;
        }
        // Creating PongPanels
        pongList = new PongPanel[games];
        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (index >= games) break;

                pongList[index] = new PongPanel(states[index]);
                AspectRatioPanel wrapper = new AspectRatioPanel(30.0/18.0, pongList[index]);
                gbc.gridx = c;
                gbc.gridy = r;
                pongGrid.add(wrapper, gbc);
                index++;
            }
        }
        // Adding marginals and the PonGrid to the gameboard
        // Wrap gameBoard with padding/margins
        int marginSize = 200; // pixels on each side
        gameboard.add(Box.createRigidArea(new Dimension(marginSize, 0)), BorderLayout.WEST);
        gameboard.add(Box.createRigidArea(new Dimension(marginSize, 0)), BorderLayout.EAST);
        gameboard.add(Box.createRigidArea(new Dimension(0, marginSize/2)), BorderLayout.NORTH);
        gameboard.add(Box.createRigidArea(new Dimension(0, marginSize/2)), BorderLayout.SOUTH);
        gameboard.add(pongGrid, BorderLayout.CENTER); // grid in the middle

        // Final touches
        this.setLayout(new BorderLayout());
        this.setSize(windowWidth,windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.add(scoreboard, BorderLayout.NORTH); this.add(gameboard, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void Update(int id, float[] state) {
        pongList[id].Update(state);
        pongList[id].repaint();
    }

    public void UpdateScore(int[] score) {
        gameScore = score;
    }

    public JPanel getPanel() {
        return (JPanel)this.getContentPane();
    }
}
