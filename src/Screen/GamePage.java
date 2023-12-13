package Screen;

import client.ScreenManager;
import game.GameCharacter;
import game.Wall;
import lombok.NonNull;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GamePage extends ScreenManager {
    @NonNull
    private GameCharacter police, rat;
    private ArrayList<Wall> walls;
//    private Set<Point> wallPositions; // 벽 위치를 추적하기 위한 Set
private int[][] levelMap1 = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
        {1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1},
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
};



    public GamePage() {
        super(Icon.GAME);
        walls = new ArrayList<>(); // Initialize the list
//        wallPositions = new HashSet<>(); // Set 초기화

        police = new GameCharacter("police", 50, 80); // 격자에 맞
        rat = new GameCharacter("rat", 880, 560); // 격자에 맞춰 위치 조정

        // 레벨 맵을 기반으로 벽 객체를 생성하고 추가합니다.
        int tileSize = 40; // Change tile size to 20 pixels
        for (int i = 0; i < levelMap1.length; i++) {
            for (int j = 0; j < levelMap1[i].length; j++) {
                if (levelMap1[i][j] == 1) {
                    Wall wall = new Wall(j * tileSize, i * tileSize, tileSize, tileSize, "wall1.jpg");
                    walls.add(wall);
                }
            }
        }

        setFocusable(true);
        requestFocusInWindow();


        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // 필요한 경우 구현
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                moveCharacters(key);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // 필요한 경우 구현
            }
        });


        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 게임 루프: 화면 업데이트 및 게임 로직 처리
                // 예를 들어, 캐릭터 이동 로직이나 충돌 검사 등을 여기에 추가
                repaint();
            }
        });
        timer.start();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });
    }

    public void moveCharacters(int key) {
        int dx = 0;
        int dy = 0;
        int moveDistance = 40; // 이동 거리를 벽의 너비/높이와 일치시킵니다.

        switch (key) {
            case KeyEvent.VK_RIGHT:
                dx = moveDistance;
                break;
            case KeyEvent.VK_LEFT:
                dx = -moveDistance;
                break;
            case KeyEvent.VK_UP:
                dy = -moveDistance;
                break;
            case KeyEvent.VK_DOWN:
                dy = moveDistance;
                break;
            // 다른 키 처리가 필요한 경우 여기에 추가
        }

        int futureX = police.getPos_X() + dx;
        int futureY = police.getPos_Y() + dy;

        // 충돌 감지 로직을 미래 위치에 대해 수행합니다.
        if (!collisionWithWall(futureX, futureY, police.getWidth(), police.getHeight())) {
            police.move(dx, dy);
        }
    }
    private boolean isNearInitialPosition(int x, int y) {
        int initialX = police.getPos_X();
        int initialY = police.getPos_Y();

        int distance = (int) Math.sqrt((x - initialX) * (x - initialX) + (y - initialY) * (y - initialY));
        return distance < 50; // 초기 위치 주변 100 픽셀 이내라면 true 반환
    }
    private boolean collisionWithWall(int x, int y, int width, int height) {
        Rectangle futureRect = new Rectangle(x, y, width, height);
        for (Wall wall : walls) {
            Rectangle wallRect = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            if (futureRect.intersects(wallRect)) {
                return true; // 충돌이 감지되었습니다.
            }
        }
        return false; // 충돌이 없습니다.
    }
    private void screenDraw(Graphics g) {
        for (Wall wall : walls) {
            Rectangle wallRect = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            if (wallRect.intersects(new Rectangle(police.getPos_X(), police.getPos_Y(), police.getWidth(), police.getHeight()))) {
                // Change the color to red if a collision is detected for debugging
                g.setColor(Color.RED);
                g.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
            } else {
                // Otherwise, draw the wall normally
                wall.draw(g);
            }
        }
        g.drawImage(police.getState(), police.getPos_X(), police.getPos_Y(), null);
        g.drawImage(rat.getState(), rat.getPos_X(), rat.getPos_Y(), null);
        this.repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        screenDraw(g);
    }

}
