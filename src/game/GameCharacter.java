package game;

import javax.swing.*;
import java.awt.*;

public class GameCharacter {
    private Image up;
    private Image down;
    private Image left;
    private Image right;

    private Image state;
    public Image getState() {
        return state;
    }

    private String direction;
    private int pos_X, pos_Y;
    public int getPos_X() {
        return pos_X;
    }

    public int getPos_Y() {
        return pos_Y;
    }
    public void setPosition(int x, int y) {
        this.pos_X = x;
        this.pos_Y = y;
    }
    public GameCharacter(String characterType, int initialX, int initialY) {
        loadImage(characterType);
        setPosition(initialX, initialY); // 초기 위치 설정
    }

    private void loadImage(String characterType) {
        if ("police".equals(characterType)) {
            this.up = new ImageIcon("asset/Char/police_back.png").getImage();
            this.down = new ImageIcon("asset/Char/police_front.png").getImage();
            this.right = new ImageIcon("asset/Char/police_right.png").getImage();
            this.left = new ImageIcon("asset/Char/police_left.png").getImage();
        } else if ("rat".equals(characterType)) {
            this.up = new ImageIcon("asset/Char/rat_back.png").getImage();
            this.down = new ImageIcon("asset/Char/rat_front.png").getImage();
            this.right = new ImageIcon("asset/Char/rat_right.png").getImage();
            this.left = new ImageIcon("asset/Char/rat_left.png").getImage();
        }

        this.state = down;
        this.direction = "down"; // 게임 시작 시 front
    }
    public void move(int dx, int dy) {
        int newX = pos_X + dx;
        int newY = pos_Y + dy;

        // 이동 경계 체크 (게임 화면의 크기에 따라 조절)
        if (newX >= 0 && newX <= 1060 && newY >= 0 && newY <= 516) { // 수정된 경계값
            pos_X = newX;
            pos_Y = newY;
        }

        // 이동 방향 설정
        if (dx > 0) {
            direction = "right";
            state = right;
        } else if (dx < 0) {
            direction = "left";
            state = left;
        } else if (dy > 0) {
            direction = "down";
            state = down;
        } else if (dy < 0) {
            direction = "up";
            state = up;
        }
    }


    // 그리기 메서드
    public void draw(Graphics g) {
        // 캐릭터 이미지를 그리는 코드를 여기에 추가
        g.drawImage(this.state, this.pos_X, this.pos_Y, null);
    }
}
