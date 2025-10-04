
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel{
    private GameManager manager;
    private Settings settings;
    SettingsPanel(GameManager gm) {
        settings = gm.getSettings();
        manager = gm;

        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new GridBagLayout()); 

        GridBagConstraints gbc = new GridBagConstraints();
        // Top buffer / Title
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.2; gbc.weighty = 0.1; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        JLabel settingsTitle = new JLabel("Settings");
        settingsTitle.setFont(new Font("SansSerif",Font.BOLD,80));
        settingsTitle.setForeground(Color.WHITE);
        this.add(settingsTitle,gbc);
        // Left buffer
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);
        // Right buffer
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.4; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);
        // Bottom buffer
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.4; gbc.weighty = 0.1; gbc.fill = GridBagConstraints.BOTH;
        this.add(Box.createRigidArea(new Dimension(0,0)),gbc);

        // Back button
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4; gbc.weighty = 0.1; gbc.fill = GridBagConstraints.BOTH;
        JPanel topLeft = new JPanel();
        topLeft.setBackground(Color.DARK_GRAY);
        topLeft.setLayout(new GridBagLayout());
        GridBagConstraints buttonGBC = new GridBagConstraints();
        JButton back = new JButton("Main Menu");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);
        back.setFont(new Font("SansSerif",Font.BOLD,20));
        back.addActionListener(e -> {
            manager.CloseSettings(settings);
        });
        back.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        buttonGBC.gridx = 1; buttonGBC.gridy = 1; buttonGBC.weightx = 0.1; buttonGBC.weighty = 0.4; buttonGBC.fill = GridBagConstraints.BOTH;
        topLeft.add(back,buttonGBC);
        // Left Buffer
        buttonGBC.gridx = 0; buttonGBC.gridy = 1; buttonGBC.weightx = 0.1; buttonGBC.weighty = 0.4; buttonGBC.fill = GridBagConstraints.BOTH;
        topLeft.add(Box.createRigidArea(new Dimension(0,0)),buttonGBC);
        // Right Buffer
        buttonGBC.gridx = 2; buttonGBC.gridy = 1; buttonGBC.weightx = 0.8; buttonGBC.weighty = 0.4; buttonGBC.fill = GridBagConstraints.BOTH;
        topLeft.add(Box.createRigidArea(new Dimension(0,0)),buttonGBC);
        // Top Buffer
        buttonGBC.gridx = 1; buttonGBC.gridy = 0; buttonGBC.weightx = 0.1; buttonGBC.weighty = 0.3; buttonGBC.fill = GridBagConstraints.BOTH;
        topLeft.add(Box.createRigidArea(new Dimension(0,0)),buttonGBC);
        // Bottom buffer
        buttonGBC.gridx = 1; buttonGBC.gridy = 2; buttonGBC.weightx = 0.1; buttonGBC.weighty = 0.3; buttonGBC.fill = GridBagConstraints.BOTH;
        topLeft.add(Box.createRigidArea(new Dimension(0,0)),buttonGBC);
        this.add(topLeft,gbc);

        // Settings
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.2; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.BLACK);
        this.add(settingsPanel,gbc);
    }
}
