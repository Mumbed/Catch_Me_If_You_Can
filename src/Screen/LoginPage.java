package Screen;

import client.Client;
import client.GameClient;
import client.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends ScreenManager {
    public JTextField id;
    public JTextField role;
    public JTextField ipAddress;
    public JTextField port;

    public JLabel idLabel;
    public JLabel roleLabel;
    public JLabel ipAddressLabel;
    public JLabel portLabel;

    public LoginPage() {
        super(Icon.BACKGROUND);

        // 박스 패널 생성하여 레이아웃 설정
        JPanel boxPanel = new JPanel(null);
        boxPanel.setBounds(450, 500, 300, 200);

        // 텍스트 필드 생성
        id = new JTextField();
        role = new JTextField();
        ipAddress = new JTextField();
        port = new JTextField();
        ipAddress.setText("localhost");
        port.setText("12345");
        // 라벨 생성 및 텍스트 설정
        idLabel = new JLabel("id:");
        roleLabel = new JLabel("role:");
        ipAddressLabel = new JLabel("IP Address:");
        portLabel = new JLabel("Port Number:");

        // 텍스트 필드 및 라벨의 위치 및 크기 설정
        idLabel.setBounds(10, 10, 50, 30);
        id.setBounds(100, 10, 100, 30);

        roleLabel.setBounds(10, 50, 100, 30);
        role.setBounds(100, 50, 100, 30);

        ipAddressLabel.setBounds(10, 90, 100, 30);
        ipAddress.setBounds(100, 90, 100, 30);

        portLabel.setBounds(10, 130, 100, 30);
        port.setBounds(100, 130, 100, 30);

        // 박스 패널에 라벨과 텍스트 필드 추가
        boxPanel.add(idLabel);
        boxPanel.add(id);
        boxPanel.add(roleLabel);
        boxPanel.add(role);
        boxPanel.add(ipAddressLabel);
        boxPanel.add(ipAddress);
        boxPanel.add(portLabel);
        boxPanel.add(port);

        // ...

        Button loginBtn = new Button("로그인");
        loginBtn.setBounds(200, 75, 100, 30);
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = id.getText();
                String roles = role.getText();
                String ipAddressText = ipAddress.getText();
                String portNumber = port.getText();
                if (userName.isEmpty() || roles.isEmpty() || ipAddressText.isEmpty() || portNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPage.this, "필수 정보를 모두 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    try {
                        int port = Integer.parseInt(portNumber);
                        // 로그인 성공 시 Client의 loginSuccess 메서드 호출
                        ((Client) SwingUtilities.getWindowAncestor(LoginPage.this)).loginSuccess(ipAddressText, port,userName,roles);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(LoginPage.this, "포트 번호가 유효하지 않습니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LoginPage.this, "게임 서버에 연결할 수 없습니다.", "연결 오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        boxPanel.add(loginBtn);
        // 화면에 박스 패널 추가
        add(boxPanel);

    }
}
