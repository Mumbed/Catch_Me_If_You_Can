package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private ServerSocket serverSocket;
    private List<Socket> clients = new ArrayList<>();
    private List<ObjectOutputStream> outputStreams = new ArrayList<>();

    public GameServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("서버가 " + port + " 포트에서 대기 중입니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStreams.add(out);

                // 클라이언트에게 연결 성공 메시지를 보냅니다.
                out.writeObject("서버에 연결되었습니다.");

                // 클라이언트를 위한 새로운 스레드 시작
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 모든 클라이언트에게 메시지를 브로드캐스트합니다.
    public void broadcastMessage(String message) {
        for (ObjectOutputStream out : outputStreams) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int port = 12345; // 포트 번호를 필요에 따라 변경하세요.

        GameServer server = new GameServer(port);
        server.startServer();
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = (String) in.readObject();
                    System.out.println("클라이언트로부터 수신: " + message);

                    // 받은 메시지를 모든 클라이언트에게 브로드캐스트합니다.
                    broadcastMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    clientSocket.close();
                    clients.remove(clientSocket);
                    outputStreams.remove(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
