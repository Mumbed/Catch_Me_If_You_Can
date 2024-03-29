package client;

        import Screen.Icon;
        import Screen.LoginPage;

        import javax.swing.*;

public class Client extends JFrame {
    private static Client instance; // 클라이언트 인스턴스 상태 추적을 위한 정적 변수

    Client() {

        setSize(1100, 700);
        getContentPane().add(new LoginPage()); // LoginPage에 클라이언트 인스턴스를 전달
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }
    // GameClient를 생성하고 GamePage로 전환하는 메서드
    public void loginSuccess(String serverAddress, int port,String username,String role) {
        try {
            GameClient gameClient = new GameClient(serverAddress, port , GameClient.GamePage.getInstance(),username,role);


            // GamePage로 전환합니다.
            getContentPane().removeAll();
            getContentPane().add(GameClient.GamePage.getInstance());
            revalidate();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "서버 연결에 실패했습니다.", "연결 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
        });
    }
}