package Screen;

import client.Client;
import client.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends Screen {
    public JTextField id;
    public JTextField ipAddress;
    public JTextField port;

    public JLabel idLabel;
    public JLabel ipAddressLabel;
    public JLabel portLabel;

    public JRadioButton policeRadio;
    public JRadioButton ratRadio;
    private ButtonGroup roleButtonGroup;

    public LoginPage() {
        super(Icon.BACKGROUND);
//        Font pixelArtFont = loadCustomFont("path_to_your_pixel_art_font.ttf");


        JPanel boxPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image to fill the panel
                ImageIcon backgroundIcon = new ImageIcon("asset/screen/pan.png");
                g.drawImage(backgroundIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        boxPanel.setBounds(350, 350, 400, 300);

        // 텍스트 필드 생성
        id = new JTextField();
        ipAddress = new JTextField();
        port = new JTextField();
        ipAddress.setText("localhost");
        port.setText("12345");

        // 라벨 생성 및 텍스트 설정
        idLabel = new JLabel("");
        ipAddressLabel = new JLabel("");
        portLabel = new JLabel("");

        // 텍스트 필드 및 라벨의 위치 및 크기 설정
        idLabel.setBounds(10, 120, 50, 30);
        id.setBounds(105, 120, 200, 30);

        ipAddressLabel.setBounds(10, 160, 100, 30);
        ipAddress.setBounds(105, 160, 200, 30);

        portLabel.setBounds(10, 200, 100, 30);
        port.setBounds(105, 200, 200, 30);

        // 라디오 버튼 생성 및 그룹화
        policeRadio = new JRadioButton("");
        ratRadio = new JRadioButton("");
        roleButtonGroup = new ButtonGroup();

        roleButtonGroup.add(policeRadio);
        roleButtonGroup.add(ratRadio);

        policeRadio.setBounds(190, 70, 100, 30);
        ratRadio.setBounds(250, 70, 100, 30);

        // 로그인 버튼 생성 및 액션 리스너 설정
        Button loginBtn = new Button("로그인");
        loginBtn.setBounds(116, 250, 200, 40);
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = id.getText();
                String ipAddressText = ipAddress.getText();
                String portNumber = port.getText();
                String selectedRole = policeRadio.isSelected() ? "police" : "rat"; // 선택된 역할 가져오기

                if (userName.isEmpty() || ipAddressText.isEmpty() || portNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPage.this, "필수 정보를 모두 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (!policeRadio.isSelected() && !ratRadio.isSelected()) {
                    JOptionPane.showMessageDialog(LoginPage.this, "역할을 선택하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    try {
                        int port = Integer.parseInt(portNumber);
                        // 로그인 성공 시 Client의 loginSuccess 메서드 호출
                        ((Client) SwingUtilities.getWindowAncestor(LoginPage.this)).loginSuccess(ipAddressText, port, userName, selectedRole);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(LoginPage.this, "포트 번호가 유효하지 않습니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(LoginPage.this, "게임 서버에 연결할 수 없습니다.", "연결 오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 박스 패널에 컴포넌트 추가
        boxPanel.add(idLabel);
        boxPanel.add(id);
        boxPanel.add(ipAddressLabel);
        boxPanel.add(ipAddress);
        boxPanel.add(portLabel);
        boxPanel.add(port);
        boxPanel.add(policeRadio);
        boxPanel.add(ratRadio);
        boxPanel.add(loginBtn);

        // 화면에 박스 패널 추가
        add(boxPanel);
    }
}


