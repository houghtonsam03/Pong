
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Window extends JFrame{
    private boolean paused;
    private GameWindow gamePanel;
    private MainMenu mainMenu;
    private PauseMenu pauseMenu;
    private SettingsPanel settingsPanel;
    private ControlsPanel controlsPanel;
    private StartupPanel startupPanel;
    private GameManager gm;
    public Window(GameManager manager, float[] startup) {
        
        this.setLayout(new BorderLayout());

        paused = true;
        gm = manager;

        // Window properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        try {
            this.setIconImage(ImageIO.read(new File("src/Images/icon.png")));
        }
        catch(Exception e) {System.out.println(e);}
        mainMenu = new MainMenu(gm,startup);
        pauseMenu = new PauseMenu(gm);
        settingsPanel = new SettingsPanel(gm);
        controlsPanel = new ControlsPanel(gm);
        startupPanel = new StartupPanel(gm);
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
    public void ToggleSettings() {
        JPanel current = (JPanel)this.getContentPane().getComponent(0);
        this.remove(current);

        if (current == settingsPanel) this.add(mainMenu);
        else this.add(settingsPanel);
        
        this.revalidate();
        this.repaint();
    }
    public void ToggleControls() {
        JPanel current = (JPanel)this.getContentPane().getComponent(0);
        this.remove(current);

        if (current == controlsPanel) this.add(mainMenu);
        else this.add(controlsPanel);

        this.revalidate();
        this.repaint();
    }
    public void toggleStartup() {
        JPanel current = (JPanel)this.getContentPane().getComponent(0);
        this.remove(current);

        if (current == startupPanel) this.add(mainMenu);
        else this.add(startupPanel);

        this.revalidate();
        this.repaint();
    }
    public void StartGame(float[][] states) {
        gamePanel = new GameWindow(gm,states);

        paused = false;
        this.remove(startupPanel);
        this.add(gamePanel);

        this.revalidate();
        this.repaint();
    }
    public void Update(int id, float[] state) {
        gamePanel.Update(id,state);
        if (!paused) gamePanel.repaint();
    }

    public void UpdateScore(int[] sc) {
        gamePanel.setScore(sc);
        if (!paused) gamePanel.repaint();
    }

    public JPanel getPanel() {
        return (JPanel)this.getContentPane();
    }
}
