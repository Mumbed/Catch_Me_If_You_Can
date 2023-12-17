//package game;
//
//import Screen.Icon;
//import client.ScreenManager;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//
//public class Map extends ScreenManager {
//    private GameCharacter police, rat;
//
//    public Map() {
//        super(Icon.BACKGROUND);
//        police = new GameCharacter("police",200,200);
//        rat = new GameCharacter("rat",400,400);
//
//        // 포커스 설정
//        setFocusable(true);
//        requestFocus();
//
//        // 키 리스너 추가
//        addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                // 필요한 경우 구현
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                int key = e.getKeyCode();
//                moveCharacters(key);
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                // 필요한 경우 구현
//            }
//        });
//
//        Timer timer = new Timer(100, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 게임 루프: 화면 업데이트 및 게임 로직 처리
//                // 예를 들어, 캐릭터 이동 로직이나 충돌 검사 등을 여기에 추가
//                repaint();
//            }
//        });
//        timer.start();
//    }
//
//    public void moveCharacters(int key) {
//        // 캐릭터 이동 로직
//        switch (key) {
//            case KeyEvent.VK_RIGHT:
//                police.move(20, 0);
//                break;
//            case KeyEvent.VK_LEFT:
//                police.move(-20, 0);
//                break;
//            case KeyEvent.VK_UP:
//                police.move(0, -20);
//                break;
//            case KeyEvent.VK_DOWN:
//                police.move(0, 20);
//                break;
//            // 다른 키 입력에 대한 처리 추가
//        }
//    }
//
//    private void screenDraw(Graphics g) {
//        // 화면 그리기 코드
//        // 캐릭터 그리기
//        g.drawImage(police.getState(),police.getPos_X(),police.getPos_Y(),null);
//        g.drawImage(rat.getState(),rat.getPos_X(),rat.getPos_Y(),null);
//        this.repaint();
//
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        screenDraw(g);
//    }
//}
