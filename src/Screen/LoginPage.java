package Screen;

import client.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends ScreenManager {
    public JTextField id;
    public JTextField password;
    public JTextField ipAddress;
    public JTextField port;

    public JLabel idLabel;
    public JLabel passwordLabel;
    public JLabel ipAddressLabel;
    public JLabel portLabel;

    public LoginPage() {
        super(Icon.BACKGROUND);

        // 박스 패널 생성하여 레이아웃 설정
        JPanel boxPanel = new JPanel(null);
        boxPanel.setBounds(450, 500, 300, 200);

        // 텍스트 필드 생성
        id = new JTextField();
        password = new JTextField();
        ipAddress = new JTextField();
        port = new JTextField();

        // 라벨 생성 및 텍스트 설정
        idLabel = new JLabel("id:");
        passwordLabel = new JLabel("password:");
        ipAddressLabel = new JLabel("IP Address:");
        portLabel = new JLabel("Port Number:");

        // 텍스트 필드 및 라벨의 위치 및 크기 설정
        idLabel.setBounds(10, 10, 50, 30);
        id.setBounds(100, 10, 100, 30);

        passwordLabel.setBounds(10, 50, 100, 30);
        password.setBounds(100, 50, 100, 30);

        ipAddressLabel.setBounds(10, 90, 100, 30);
        ipAddress.setBounds(100, 90, 100, 30);

        portLabel.setBounds(10, 130, 100, 30);
        port.setBounds(100, 130, 100, 30);

        // 박스 패널에 라벨과 텍스트 필드 추가
        boxPanel.add(idLabel);
        boxPanel.add(id);
        boxPanel.add(passwordLabel);
        boxPanel.add(password);
        boxPanel.add(ipAddressLabel);
        boxPanel.add(ipAddress);
        boxPanel.add(portLabel);
        boxPanel.add(port);

        Button loginBtn = new Button("로그인");
        loginBtn.setBounds(200, 75, 100, 30);
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = id.getText();
                String passWord = password.getText();
                String ipAddressText = ipAddress.getText();
                String portNumber = port.getText();
                if (userName.isEmpty() || passWord.isEmpty() || ipAddressText.isEmpty() || portNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPage.this, "필수 정보를 모두 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{
                    navigateTo(new GamePage());
                }
                // 여기에서 입력된 값들을 사용하여 로그인 또는 기타 작업을 수행할 수 있습니다.
            }

        });
        boxPanel.add(loginBtn);

        // 화면에 박스 패널 추가
        add(boxPanel);

    }
}
