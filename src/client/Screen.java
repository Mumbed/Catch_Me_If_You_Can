package client;


import java.awt.*;
import javax.swing.*;

public class Screen extends JPanel {

    private final Image backgroundImageIcon;

    public Screen(ImageIcon backgroundImageIcon) {
        this.backgroundImageIcon = backgroundImageIcon.getImage();
        setLayout(null);
        setFocusable(true);
    }

    protected void onDestroyed() {
    }

    protected void navigateTo(JComponent panel) {
        onDestroyed();
        Navi.navigateTo(this, panel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImageIcon, 0, 0, null);
        setOpaque(false);
    }
    private static class Navi {

        private final JFrame rootFrame;

        private Navi(JFrame rootFrame) {
            this.rootFrame = rootFrame;
        }
        public static Navi of(JComponent contextComponent) {
            return new Navi((JFrame) SwingUtilities.getWindowAncestor(contextComponent));
        }


        public void navigateTo(JComponent component) {
            if (rootFrame == null) {
                return;
            }
            Container c = rootFrame.getContentPane();
            c.removeAll();
            c.add(component);
            c.revalidate();
            c.repaint();
        }
        public static void navigateTo(JComponent contextComponent, JComponent target) {
            Navi.of(contextComponent).navigateTo(target);
        }
    }
}
