package client;


import java.awt.*;
import javax.swing.*;

public class ScreenManager extends JPanel {

    private final Image backgroundImageIcon;

    public ScreenManager(ImageIcon backgroundImageIcon) {
        this.backgroundImageIcon = backgroundImageIcon.getImage();
        setLayout(null);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImageIcon, 0, 0, null);
        setOpaque(false);
    }

}
