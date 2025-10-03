import javax.swing.*;
import java.awt.*;

public class Window extends JFrame{
    private boolean paused;
    private GameWindow gamePanel;
    private MainMenu mainMenu;
    private PauseMenu pauseMenu;
    private GameManager gm;
    public Window(GameManager manager) {
        
        this.setLayout(new BorderLayout());

        paused = true;
        gm = manager;

        // Window properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainMenu = new MainMenu(gm);
        pauseMenu = new PauseMenu(gm);
        this.add(mainMenu);
        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }
    public boolean TogglePause() {
        if (!paused) {
            this.remove(gamePanel);
            this.add(pauseMenu);
        }
        else {
            this.remove(pauseMenu);
            this.add(gamePanel);
        }
        this.revalidate();
        this.repaint();
        paused = !paused;
        return paused;
    }
    public void StartGame(int games, float[][] states) {
        gamePanel = new GameWindow(gm,games,states);

        paused = false;
        this.remove(mainMenu);
        this.add(gamePanel);

        this.revalidate();
        this.repaint();
    }
    public void Update(int id, float[] state) {
        gamePanel.Update(id,state);
        if (!paused) gamePanel.repaint();
        else {
            System.out.println("ERROR: Tried to update while paused");
        }
    }

    public void UpdateScore(int[] sc) {
        gamePanel.setScore(sc);
        if (!paused) gamePanel.repaint();
        else {
            System.out.println("ERROR: Tried to update score while paused");
        }
    }

    public JPanel getPanel() {
        return (JPanel)this.getContentPane();
    }
}
