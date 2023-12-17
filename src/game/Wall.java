package game;

import javax.swing.*;
import java.awt.*;

public class Wall {
    private int x, y, width, height;
    private Image image;

    public Wall(int x, int y, int width, int height, String imageName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new ImageIcon(getClass().getResource("/asset/wall/" + imageName)).getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}