
import java.awt.*;
import javax.swing.*;

public class GameWindow extends JPanel{
    private PongPanel[] pongList;
    private JLabel score;
    GameWindow(GameManager manager,float[][] states) {

        // Create scoreboard top of screen
        JPanel scoreboard = new JPanel(); scoreboard.setBackground(Color.BLACK);
        scoreboard.setLayout(new GridBagLayout()); scoreboard.setPreferredSize(new Dimension(PongPanel.GAME_WIDTH,PongPanel.GAME_HEIGHT/4));

        // Creating the "Scoreboard" Title
        JLabel scoreTitle = new JLabel("Scoreboard");
        scoreTitle.setFont(new Font("SansSerif",Font.BOLD,50)); scoreTitle.setForeground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0.1; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        scoreboard.add(scoreTitle,gbc);

        // Creating the scoreboard
        score = new JLabel(0 + " - " + 0 + "  (" + 0 + ")");
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
        int games = states.length;
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
                gbc.gridx = c;
                gbc.gridy = r;

                pongList[index] = new PongPanel(states[index]);
                AspectRatioPanel wrapper = new AspectRatioPanel(PongPanel.ASPECT_RATIO,pongList[index]);
                pongGrid.add(wrapper, gbc);
                index++;
            }
        }

        // Adding marginals and the PonGrid to the gameboard
        // Wrap gameBoard with padding/margins
        int marginSize = 100; // pixels on each side
        gameboard.setBorder(BorderFactory.createEmptyBorder(marginSize/2, marginSize, marginSize/2, marginSize));

        gameboard.add(pongGrid, BorderLayout.CENTER); // grid in the middle

        // Adding components
        this.setLayout(new BorderLayout());
        this.add(scoreboard, BorderLayout.NORTH); this.add(gameboard, BorderLayout.CENTER);
    }
    public void Update(int id , float[] state) {
        pongList[id].Update(state);
    }
    public void setScore(int[] sc) {
        score.setText(sc[0] + " - " + sc[1] + "  (" + sc[2] + ")");
    }
}
