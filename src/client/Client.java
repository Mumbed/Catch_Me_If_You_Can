package client;

import Screen.LoginPage;

import javax.swing.*;

public class Client extends JFrame {
    private Client() {
        setSize(1100, 700);
        getContentPane().add(new LoginPage());
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Client();
    }
}
