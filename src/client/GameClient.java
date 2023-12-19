package client;

import Screen.Icon;
import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GameClient {
    private Socket socket;
    static BufferedWriter os;
    BufferedReader is;
    protected GamePage gamePage;
    private static String name;
    static String role;
    private int endNum=0;
    private boolean timerStarted = false; // 타이머 시작 여부를 추적하는 변수

    public GameClient(String host, int port,GamePage gamePage,String username,String role) throws IOException {
        socket = new Socket(host,port);
        name=username;
        this.role=role;
        is =new BufferedReader(new InputStreamReader(socket.getInputStream()));
        os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        os.write(username+" "+role+"\n");
        System.out.println("내역할은  : "+role);
        os.flush();
        this.gamePage=gamePage;
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(username+" 시작");

                while (true) {
                    try {
                        String msg = is.readLine();//서버로부터 받아드리는 문자
                        String[] msgArr = msg.split(" ");//name keycode role
                        if(!msgArr[0].equals(name)&&msgArr[1].equals("시작")) {
                            System.out.println("플레이어 입장 게임시작");
                        }
                        if(msgArr[2].equals("police") && !timerStarted ){
                            gamePage.startTimer(); // 게임 시작 시 타이머 시작
                            timerStarted = true; // 타이머 시작 상태를 true로 설정
                        }
                        if(msgArr[2].equals("rat") && !timerStarted ){
                            gamePage.startTimer(); // 게임 시작 시 타이머 시작
                            timerStarted = true; // 타이머 시작 상태를 true로 설정
                        }
                        System.out.println("서버로부터 수신받음 : "+msgArr[0]);
                        System.out.println("서버로부터 수신받음 : "+msgArr[1]);
                        System.out.println("서버로부터 수신받음 : "+msgArr[2]);

                        if(msgArr[1].equals("서버연결성공")){
                            name=msgArr[0];
                            System.out.println(name+"클라이언트 서버연결성공");

                        }
                        else if(!name.equals(msgArr[0])&&msgArr[2].equals("rat")&&!msgArr[1].equals("end")&&!msgArr[1].equals("reset")&&!msgArr[1].equals("시작")){
                            System.out.println("난"+name+"얘는"+msgArr[0]+msgArr[2]+"상대방움직임"+msgArr[1]);
                            gamePage.moveCharactersPolice(Integer.parseInt(msgArr[1]),"rat");

                        }
                        else if(!name.equals(msgArr[0])&&msgArr[2].equals("police")&&!msgArr[1].equals("end")&&!msgArr[1].equals("reset")&&!msgArr[1].equals("시작")){
                            System.out.println("난"+name+"얘는"+msgArr[0]+msgArr[2]+"상대방움직임"+msgArr[1]);
                            gamePage.moveCharactersPolice(Integer.parseInt(msgArr[1]),"police");

                        }
                        else if(!name.equals(msgArr[0]) && msgArr[1].equals("reset")){
                            gamePage.resetGame();
                        }
                        else if(!name.equals(msgArr[0])&&msgArr[1].equals("end")&&endNum<1){
                            gamePage.endGame();
                            endNum++;
                        }
                        // 받은 패킷을 텍스트 영역에 표시한다.

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread2.start();

    }

    protected void sendMessage(String message) throws IOException {
        os.write(message+ "\n");
        os.flush();
        System.out.println("클라이언트가 서버에게 보냄: "+message);

    }

    public void disconnect() throws IOException {
        socket.close();
    }
    static class GamePage extends ScreenManager{
        private GameCharacter police, rat;
        private ArrayList<Wall> walls;
        private int policex, policey,ratx,raty; // 플레이어 위치 좌표
        private String Role;
        private int gameCount = 0; // 현재 게임 횟수를 추적
        private boolean Catch;

        public static GamePage getInstance() {//싱글톤 적용
            return Holder.INSTANCE;
        }
        private static class Holder {
            private static final GamePage INSTANCE = new GamePage();
        }

        //    private Set<Point> wallPositions; // 벽 위치를 추적하기 위한 Set
        private int[][] levelMap1 = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        public void resetGame() throws IOException {
            // 게임 관련 데이터 초기화
            // 예: 플레이어 위치 초기화, 점수 초기화 등
            //startTimer();
            startTimer(); // 타이머 다시 시작
            policex = 50;
            policey = 80;
            ratx = 930;
            raty = 520;
            police.setPosition(policex, policey);
            rat.setPosition(ratx, raty);
//
//            if(role.equals("police")&&gameCount==0){
//                firstCop=name;
//            }else if(role.equals("police")&&gameCount==1){
//                secondCop=name;
//            }

            // 역할 교체
            if (role.equals("police")&&gameCount==0) {

                role = "rat";
            } else {
                role = "police";
            }



            // 게임 횟수 증가
            gameCount++;

            // 2판이 끝났다면 로그인 화면으로 돌아가기
            if (gameCount == 2) {
                endGame();
            }

        }
        private JLabel timerLabel; // 타이머를 표시할 레이블
        private long startTime;    // 게임 시작 시간
        private Timer timer;       // 스윙 타이머
        private int ratSkill = 0; // 도둑 캐릭터의 스킬 카운터 변수
        private boolean isUsingSkill = false; // 스킬 사용 중인지 여부를 나타내는 변수

        private GamePage() {

            super(Icon.GAME);
            this.setLayout(new BorderLayout()); // 레이아웃을 BorderLayout으로 설정

            this.Role=role;
            // Initialize the gameClient field.
            walls = new ArrayList<>(); // Initialize the list
//        wallPositions = new HashSet<>(); // Set 초기화;
            policex=50;
            policey=80;
            ratx=930;
            raty=520;

            police = new GameCharacter("police", policex, policey); // 격자에 맞
            rat = new GameCharacter("rat", ratx, raty); // 격자에 맞춰 위치 조정

            // 레벨 맵을 기반으로 벽 객체를 생성하고 추가합니다.
            int tileSize = 40; // Change tile size to 20 pixels
            for (int i = 0; i < levelMap1.length; i++) {
                for (int j = 0; j < levelMap1[i].length; j++) {
                    if (levelMap1[i][j] == 1) {
                        // 난수를 생성하여 50%의 확률로 wall1.jpg 또는 wall2.png를 선택
                        String wallImage = Math.random() < 0.5 ? "wall1.jpg" : "wall2.png";
                        Wall wall = new Wall(j * tileSize, i * tileSize, tileSize, tileSize, wallImage);
                        walls.add(wall);
                    }
                }
            }
            // 타이머 레이블 초기화 및 추가
            timerLabel = new JLabel("00:00:00");
            timerLabel.setHorizontalAlignment(JLabel.CENTER); // 레이블 텍스트를 중앙 정렬
            timerLabel.setPreferredSize(new Dimension(100, 30)); // 레이블 크기 설정
            this.revalidate();
            this.repaint();
            this.add(timerLabel, BorderLayout.SOUTH); // 레이블을 패널 하단에 추가

            // 타이머 객체 초기화
            timer = new Timer(1000, e -> updateTimer());
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
                    sendMoveToServer(key);
                    if (role.equals("police")) {
                        moveCharactersKeyPressed(key, police);
                        if (key == KeyEvent.VK_SPACE && isCharactersNear(police, rat, 40)) {
                            // 게임 종료 처리
                            System.out.println("게임 종료: 경찰이 쥐를 잡았습니다.");
                            //gameIsOver();
                            sendCatchToServer("reset");
                            try {
                                resetGame(); // 게임 리셋 및 역할 교체
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            // 게임 종료 로직 추가
                        }
                    } else {
                        moveCharactersKeyPressed(key, rat);
                        if (key == KeyEvent.VK_SPACE && ratSkill < 3) {
                            if (!collisionWithWall(rat.getPos_X(), rat.getPos_Y(), rat.getWidth(), rat.getHeight())) {
                                ratSkill++;
                                isUsingSkill = true; // 스킬 사용 상태로 변경
                                for(int i=0;i<2;i++){
                                    sendMoveToServer(key);
                                }

                                System.out.println("도둑이 스킬을 사용하였습니다.");
                            }
                        }
                    }
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
        public void startTimer() {
            startTime = System.currentTimeMillis();
            timer.start();
        }
        private void updateTimer() {
            long elapsedTime = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsedTime / 1000) % 60;
            int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
            int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);

            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            timerLabel.setText(timeString);
        }



        public void moveCharactersKeyPressed(int key,GameCharacter character) {
            int dx = 0;
            int dy = 0;
            int moveDistance = 20; // 이동 거리를 벽의 너비/높이와 일치시킵니다.
            if (isUsingSkill) {
                System.out.println("야호");
                moveDistance = 100; // 도둑 캐릭터가 스킬을 사용 중이면 이동 거리를 60으로 변경
                isUsingSkill =false;
            }
            switch (key) {
                case KeyEvent.VK_RIGHT:
                    dx = moveDistance;
                    character.setDirection("right");

                    break;
                case KeyEvent.VK_LEFT:
                    dx = -moveDistance;
                    character.setDirection("left");

                    break;
                case KeyEvent.VK_UP:
                    dy = -moveDistance;
                    character.setDirection("up");

                    break;
                case KeyEvent.VK_DOWN:
                    dy = moveDistance;
                    character.setDirection("down");

                    break;
                // 다른 키 처리가 필요한 경우 여기에 추가
            }

            int futureX = character.getPos_X() + dx;
            int futureY = character.getPos_Y() + dy;
            if (!collisionWithWall(futureX, futureY, character.getWidth(), character.getHeight())) {
                character.move(dx, dy);
            }
        }
        public void moveCharactersPolice(int key,String characterName){
            GameCharacter character = null;
            int dx = 0;
            int dy = 0;
            int moveDistance = 20; // 이동 거리를 벽의 너비/높이와 일치시킵니다.
            if(characterName.equals("rat"))character=rat;
            if(characterName.equals("police"))character=police;

            switch (key) {
                case 39:
                    dx = moveDistance;
                    break;
                case 37:
                    dx = -moveDistance;
                    break;
                case 38:
                    dy = -moveDistance;
                    break;
                case 40:
                    dy = moveDistance;
                    break;
                // 다른 키 처리가 필요한 경우 여기에 추가
            }

            int futureX = character.getPos_X() + dx;
            int futureY = character.getPos_Y() + dy;

            // 충돌 감지 로직을 미래 위치에 대해 수행합니다.
            if (!collisionWithWall(futureX, futureY, character.getWidth(), character.getHeight())) {
                character.move(dx, dy);
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

        private boolean isCharactersNear(GameCharacter char1, GameCharacter char2, int distance) {
            int dx = char1.getPos_X() - char2.getPos_X();
            int dy = char1.getPos_Y() - char2.getPos_Y();
            return Math.sqrt(dx * dx + dy * dy) <= distance;
        }
        public void endGame() {
            sendCatchToServer("end"); // 서버에게 게임 종료 신호를 보냅니다.

            // 게임 종료 이미지 표시
            ImageIcon gameOverIcon = new ImageIcon("src/asset/screen/gameover.png"); // gameover 이미지 경로에 맞게 수정
            JLabel gameOverLabel = new JLabel(gameOverIcon);
            gameOverLabel.setBounds(0, 0, getWidth(), getHeight());
            add(gameOverLabel);

            // 타이머 설정
            Timer gameOverTimer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 타이머가 만료되면 현재 게임 페이지를 종료하고 로그인 화면으로 이동
                    SwingUtilities.getWindowAncestor(GamePage.this).dispose();
                    SwingUtilities.invokeLater(() -> {
                        Client.getInstance().setVisible(true);
                    });
                }
            });
            gameOverTimer.setRepeats(false); // 타이머를 한 번만 실행하도록 설정
            gameOverTimer.start();
        }

        private void screenDraw(Graphics g){
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



        private void sendCatchToServer(String check){
            try {
                os.write(check+" "+role+"\n");
                os.flush();
                System.out.println("클라이언트가 서버에게 보냄: "+check);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exceptions, maybe try to reconnect or inform the player.
            }

        }


        // Method to send move to the server
        private void sendMoveToServer(int keycode) {
            try {
                os.write(keycode+" "+role+"\n");
                os.flush();
                System.out.println("클라이언트가 서버에게 보냄: "+keycode);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exceptions, maybe try to reconnect or inform the player.
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            screenDraw(g);
        }

    }

}